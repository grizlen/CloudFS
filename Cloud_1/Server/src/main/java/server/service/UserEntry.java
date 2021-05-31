package server.service;

import transport.ListRequestMessage;
import transport.SendFileMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class UserEntry {

    private final String name;
    private Path userRootPath;

    public String getName() {
        return name;
    }

    public Path getUserRootPath() {
        return userRootPath;
    }

    public void setUserRootPath(String userRootPathName) {
        userRootPath = Paths.get(userRootPathName);
    }

    public UserEntry(String name) {
        this.name = name;
    }

    public ListRequestMessage listFiles(String pathName) {
        try {
            Path path = userRootPath.resolve(pathName);
            System.out.println("list of: " + path.toString());
            List<String> list = Files.list(path)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
            ListRequestMessage message = new ListRequestMessage();
            message.setPath(pathName);
            message.getFiles().addAll(list);
            return message;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public SendFileMessage sendFile(String pathName) {
        Path path = userRootPath.resolve(pathName);
        File file = path.toFile();
        if (!file.exists() || file.isDirectory()) {
            System.err.println("file not exists: " + path.toString());
            return null;
        }
        System.out.println("send: " + path.toString());
        try (FileInputStream fs = new FileInputStream(file)) {
            int sz = (int) file.length();
            byte[] data = new byte[sz];
            fs.read(data);
            SendFileMessage message = new SendFileMessage();
            message.setPath(pathName);
            message.setData(data);
            return message;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
