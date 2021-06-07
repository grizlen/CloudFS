package transport;

public class MakeDirMessage extends AbstractMessage{

    private String path;

    public MakeDirMessage() {
        super(MessageId.MAKE_DIR);
        path = "";
    }

    public MakeDirMessage(String path) {
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
