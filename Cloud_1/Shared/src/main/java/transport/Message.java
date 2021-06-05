package transport;

import java.io.Serializable;

public interface Message extends Serializable {
    MessageId getMsg();
}
