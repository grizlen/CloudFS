package transport;

public class DeleteFileMessage extends AbstractMessage{

    private String path;

    public DeleteFileMessage() {
        super(MessageId.DELETE_FILE);
        path = "";
    }

    public DeleteFileMessage(String path) {
        this();
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
