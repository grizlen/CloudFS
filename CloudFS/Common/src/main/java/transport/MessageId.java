package transport;

public enum MessageId {
    AUTH("auth"),
    AUTH_NEW("auth_new"),
    AUTH_OK("auth_ok"),
    AUTH_FAIL("auth_fail"),
    AUTH_CLOSE("auth_close"),
    LIST("list"),
    LIST_REQUEST("list_request"),
    SEND("send"),
    GET("get");

    private final String name;

    MessageId(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Message: " + name;
    }
}
