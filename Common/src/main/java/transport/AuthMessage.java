package transport;

public class AuthMessage extends AbstractMessage{

    private String userName;
    private String userPassword;

    public AuthMessage() {
        super(MessageId.AUTH);
        this.userName = "";
        this.userPassword = "";
    }

    public AuthMessage(String userName, String userPassword) {
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
