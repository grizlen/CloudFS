package transport;

public class AuthNewMessage extends AbstractMessage {

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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
