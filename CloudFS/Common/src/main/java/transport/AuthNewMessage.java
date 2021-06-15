package transport;

public class AuthNewMessage extends AbstractMessage{

    private String userName;
    private String userPassword;

    public AuthNewMessage() {
        super(MessageId.AUTH_NEW);
        this.userName = "";
        this.userPassword = "";
    }

    public AuthNewMessage(String userName, String userPassword) {
        this();
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public String toString() {
        return String.format("%s [User = %s]", getMsg().toString(), userName);
    }
}
