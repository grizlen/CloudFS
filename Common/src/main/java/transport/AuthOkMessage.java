package transport;

public class AuthOkMessage extends AbstractMessage{
    public AuthOkMessage() {
        super(MessageId.AUTH_OK);
    }
}
