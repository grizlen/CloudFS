package transport;

public enum MessageId {
    AUTH("auth"),
    AUTH_NEW("auth_new"),
    AUTH_OK("auth_ok"),
    AUTH_FAIL("auth_fail");

    private final String name;

    MessageId(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Message: " + name;
    }
}
