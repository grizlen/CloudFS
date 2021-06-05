package transport;

public class ListMessage extends AbstractMessage{
    private String path;

    public ListMessage() {
        super("list");
        path = "";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
