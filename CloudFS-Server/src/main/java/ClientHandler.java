import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable, Closeable {

    private final Socket socket;
    private final Path serverPath;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        serverPath = Paths.get("CloudFS-Server", "server-files");
    }

    private String getFiles() throws IOException {
        return Files.list(serverPath)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.joining(","));
    }

    private void sendFile(DataInputStream in, DataOutputStream out) throws Exception {
        File f = new File("CloudFS-Server/server-files/" + in.readUTF());
        if (!f.exists()){
            throw new Exception("File not exists.");
        }
        FileInputStream fs = new FileInputStream(f.getPath());
        long sz = f.length();
        out.writeUTF("/sendFile");
        out.writeUTF(f.getName());
        out.writeUTF(String.valueOf(sz));
        byte[] buf = new byte[1024];
        while (fs.read(buf) > -1){
            out.write(buf);
        }
    }

    @Override
    public void run() {
        System.out.println("Start listening...");
        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())){
            while (true){
                String msg = in.readUTF();
                System.out.println("Received: " + msg);
                if (msg.equals("/getListFiles")) {
                    out.writeUTF("/takeFiles");
                    out.writeUTF(getFiles());
                    out.flush();
                } else if (msg.equals("/getFile")){
                    sendFile(in, out);
                } else {
                    out.writeUTF(msg);
                    out.flush();
                }
            }
        } catch (Exception e) {
            System.err.println("Connection was broken.");
            try {
                close();
            } catch (IOException ioException) {
                System.err.println("Exception while socket close.");
            }
        } finally {
            try {
                close();
                System.out.println("Finish listening.");
            } catch (IOException e) {
                System.err.println("Exception while socket close.");
            }
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
