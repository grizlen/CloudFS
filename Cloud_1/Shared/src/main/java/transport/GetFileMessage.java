package transport;

public class GetFileMessage extends AbstractMessage{
    private String path;

    public GetFileMessage() {
        super(MessageId.GET_FILE);
        path = "";
    }

    public GetFileMessage(String path) {
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
