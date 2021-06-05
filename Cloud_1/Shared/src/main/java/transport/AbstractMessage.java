package transport;

public class AbstractMessage implements Message{

    private final MessageId msg;

    @Override
    public MessageId getMsg() {
        return msg;
    }

    public AbstractMessage(MessageId msg) {
        this.msg = msg;
    }
}
