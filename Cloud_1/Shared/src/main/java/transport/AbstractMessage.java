package transport;

public class AbstractMessage implements Message{
    private String msg;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AbstractMessage(String msg) {
        this.msg = msg;
    }
}
