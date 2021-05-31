package transport;

import java.io.Serializable;

public interface Message extends Serializable {
    String getMsg();
    void setMsg(String msg);
}
