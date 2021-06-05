package transport;

public enum MessageId {
//    NULL(""),
    AUTH("auth"),
    AUTH_NEW("auth_new"),
    AUTH_OK("auth_ok"),
    AUTH_FAIL("auth_fail"),
    LIST("list"),
    LIST_REQUEST("list_request"),
    GET_FILE("get_file"),
    SEND_FILE("send_file");

    private final String name;

    MessageId(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
