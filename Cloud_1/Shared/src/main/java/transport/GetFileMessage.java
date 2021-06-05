package transport;

public class GetFileMessage extends AbstractMessage{
    private String path;

    public GetFileMessage() {
        super("get-file");
        path = "";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
