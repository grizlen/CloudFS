package transport;

public class SendMessage extends AbstractPathMessage {
    private final byte[] data;
    private int id;
    private int index;
    private int count;

    public SendMessage(String pathName, byte[] data) {
        super(MessageId.SEND, pathName);
        this.data = data;
        id = 0;
        index = 1;
        count = 1;
    }

    public byte[] getData() {
        return data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
