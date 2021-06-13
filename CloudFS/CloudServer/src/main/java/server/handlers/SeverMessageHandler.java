package server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import server.services.AuthService;
import transport.*;

@Slf4j
public class SeverMessageHandler extends SimpleChannelInboundHandler<Message> {

    private final AuthService authService;

    public SeverMessageHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        log.debug("Receive: " + message.toString());
        UserHandler userHandler;
        switch (message.getMsg()){
            case AUTH:
                userHandler = authService.subscribe(
                        channelHandlerContext,
                        ((AuthMessage) message).getUserName(),
                        ((AuthMessage) message).getUserPassword());
                if (userHandler != null) {
                    channelHandlerContext.writeAndFlush(new AuthOkMessage());
                } else {
                    channelHandlerContext.writeAndFlush(new AuthFailMessage());
                }
                break;
            case AUTH_NEW:
                userHandler = authService.newUser(
                        channelHandlerContext,
                        ((AuthNewMessage) message).getUserName(),
                        ((AuthNewMessage) message).getUserPassword()
                );
                if (userHandler != null) {
                    channelHandlerContext.writeAndFlush(new AuthOkMessage());
                } else {
                    channelHandlerContext.writeAndFlush(new AuthFailMessage());
                }
                break;
            case AUTH_CLOSE:
                authService.unSubscribe(channelHandlerContext);
                break;
            default:
                userHandler = authService.getUser(channelHandlerContext);
                if (userHandler != null) {
                    userHandler.processMessage(channelHandlerContext, message);
                } else {
                    log.error("Unsubscribed client: ", channelHandlerContext);
                }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client connected [{}]", ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        authService.unSubscribe(ctx);
        log.debug("Client disconnected [{}]", ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception: ", cause);
    }
}
