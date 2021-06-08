package transport;

public enum MessageId {
//    NULL(""),
    AUTH("auth"),
    AUTH_NEW("auth_new"),
    AUTH_CLOSE("auth_close"),
    AUTH_OK("auth_ok"),
    AUTH_FAIL("auth_fail"),
    LIST("list"),
    LIST_REQUEST("list_request"),
    GET_FILE("get_file"),
    SEND_FILE("send_file"),
    DELETE_FILE("delete_file"),
    MAKE_DIR("make_dir");

    private final String name;

    MessageId(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
