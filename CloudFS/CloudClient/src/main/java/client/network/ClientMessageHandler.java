package client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import transport.Message;

public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {

    private final MessageReceiver receiver;

    public ClientMessageHandler(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if (receiver != null) {
            receiver.receveMessage(message);
        }
    }
}
