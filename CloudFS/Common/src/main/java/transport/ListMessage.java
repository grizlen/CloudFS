package transport;

public class ListMessage extends AbstractPathMessage {
    public ListMessage(String pathName) {
        super(MessageId.LIST, pathName);
    }
}
