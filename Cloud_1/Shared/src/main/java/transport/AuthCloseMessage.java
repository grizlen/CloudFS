package transport;

public class AuthCloseMessage extends AbstractMessage{

    public AuthCloseMessage() {
        super(MessageId.AUTH_CLOSE);
    }

}
