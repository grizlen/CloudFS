package transport;

public class SendFileMessage extends AbstractMessage{

    private String path;
    byte[] data;

    public SendFileMessage() {
        super("send-file");
        path = "";
        data = null;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
