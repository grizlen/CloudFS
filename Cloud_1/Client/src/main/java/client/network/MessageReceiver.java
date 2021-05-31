package client.network;

import transport.Message;

public interface MessageReceiver {
    void receiveMessage(Message message);
}
