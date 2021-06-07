package transport;

public class AuthMessage extends AbstractMessage{

    private String userName;
    private String userPassword;

    public AuthMessage() {
        super(MessageId.AUTH);
        userName = "";
        userPassword = "";
    }

    public AuthMessage(String userName, String userPassword) {
        super(MessageId.AUTH);
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
