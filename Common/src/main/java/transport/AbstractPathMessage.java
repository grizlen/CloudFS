package transport;

public abstract class AbstractPathMessage extends AbstractMessage{

    private String pathName;

    public AbstractPathMessage(MessageId msg, String  pathName) {
        super(msg);
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }

    @Override
    public String toString() {
        return String.format("%s [Path = %s]", getMsg().toString(), pathName);
    }
}
