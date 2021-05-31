package transport;

public class AuthMessage extends AbstractMessage{

    private String userName;
    private String userPassword;

    public AuthMessage(String msg) {
        super(msg);
        userName = "";
        userPassword = "";
    }

    public AuthMessage() {
        this("auth");
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
