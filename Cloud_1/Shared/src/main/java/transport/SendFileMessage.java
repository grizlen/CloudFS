package transport;

public class SendFileMessage extends AbstractMessage{

    private String path;
    byte[] data;

    public SendFileMessage() {
        super(MessageId.SEND_FILE);
        path = "";
        data = null;
    }

    public SendFileMessage(String path, byte[] data) {
        this();
        this.path = path;
        this.data = data;
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
