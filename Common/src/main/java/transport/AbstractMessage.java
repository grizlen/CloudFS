package transport;

public abstract class AbstractMessage implements Message{

    private final MessageId msg;

    public AbstractMessage(MessageId msg) {
        this.msg = msg;
    }

    @Override
    public MessageId getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return msg.toString();
    }
}
