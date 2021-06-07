package transport;

public class AuthFailMessage extends AbstractMessage{
    public AuthFailMessage() {
        super(MessageId.AUTH_FAIL);
    }
}
