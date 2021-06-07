package cloudclient.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import transport.Message;

@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {

    private final MessageReceiver receiver;

    public ClientMessageHandler(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Connected to server.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Disconnected.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if (receiver != null) {
            receiver.receiveMessage(message);
        }
    }
}
