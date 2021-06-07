package transport;

public class ListMessage extends AbstractMessage{
    private String path;

    public ListMessage() {
        super(MessageId.LIST);
        path = "";
    }

    public ListMessage(String path) {
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
