package cloudserver.main;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import cloudserver.service.AuthService;
import cloudserver.service.UserEntry;
import transport.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {

    private final AuthService authService;

    public ServerMessageHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("client connected: " + ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        authService.unSubscribe(ctx);
        log.debug("Client disconnected: " + ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        processMessages(channelHandlerContext, message);
    }

    private void processMessages(ChannelHandlerContext channelHandlerContext, Message message) {
        log.debug("Received: " + message.getMsg());
        UserEntry userEntry;
        switch (message.getMsg()){
            case AUTH:
                userEntry = authService.subscribe(
                        channelHandlerContext,
                        ((AuthMessage) message).getUserName(),
                        ((AuthMessage) message).getUserPassword());
                if (userEntry != null) {
                    channelHandlerContext.writeAndFlush(new AuthOkMessage());
                } else {
                    channelHandlerContext.writeAndFlush(new AuthFailMessage());
                }
                break;
            case AUTH_NEW:
                userEntry = authService.newEntry(
                        channelHandlerContext,
                        ((AuthNewMessage) message).getUserName(),
                        ((AuthNewMessage) message).getUserPassword());
                if (userEntry != null) {
                    channelHandlerContext.writeAndFlush(new AuthOkMessage());
                } else {
                    channelHandlerContext.writeAndFlush(new AuthFailMessage());
                }
                break;
            case AUTH_CLOSE:
                authService.unSubscribe(channelHandlerContext);
                break;
            case LIST:
                userEntry = authService.getEntry(channelHandlerContext);
                if (userEntry == null) {
                    log.error("User not found.");
                    return;
                }
                userEntry.setUserCurrentPath(((ListMessage) message).getPath());
                channelHandlerContext.writeAndFlush(userEntry.listFiles());
                break;
            case GET_FILE:
                userEntry = authService.getEntry(channelHandlerContext);
                if (userEntry == null) {
                    log.error("User not found.");
                    return;
                }
                channelHandlerContext.writeAndFlush(userEntry.sendFile(((GetFileMessage) message).getPath()));
                break;
            case SEND_FILE:
                userEntry = authService.getEntry(channelHandlerContext);
                if (userEntry == null) {
                    log.error("User not found.");
                    return;
                }
                userEntry.saveFile(((SendFileMessage) message).getPath(), ((SendFileMessage) message).getData());
                channelHandlerContext.writeAndFlush(userEntry.listFiles());
                break;
            case DELETE_FILE:
                userEntry = authService.getEntry(channelHandlerContext);
                if (userEntry == null) {
                    log.error("User not found.");
                    return;
                }
                userEntry.deleteFile(((DeleteFileMessage) message).getPath());
                channelHandlerContext.writeAndFlush(userEntry.listFiles());
                break;
            default:
                log.error("Incorrect message.");
        }
    }
}
