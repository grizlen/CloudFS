package client.network;

import transport.Message;

public interface MessageReceiver {
    void receveMessage(Message message);
}
