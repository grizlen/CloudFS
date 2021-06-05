package server.main;

import com.sun.jmx.snmp.SnmpUnknownAccContrModelException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import server.service.AuthService;
import server.service.UserEntry;
import transport.*;

public class ServerMessageHandler extends SimpleChannelInboundHandler<Message> {

    private final AuthService authService;

    public ServerMessageHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client connected: " + ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected: " +ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        processMessages(channelHandlerContext, message);
    }

    private void processMessages(ChannelHandlerContext channelHandlerContext, Message message) {
        System.out.println("Received: " + message.getMsg());
        if (message instanceof AuthMessage) {
            UserEntry userEntry = authService.newEntry(channelHandlerContext, ((AuthMessage) message).getUserName(), ((AuthMessage) message).getUserPassword());
            if (userEntry != null) {
                channelHandlerContext.writeAndFlush(new AuthOkMessage());
            } else {
                channelHandlerContext.writeAndFlush(new AuthFailMessage());
            }
        } else if (message instanceof ListMessage) {
            UserEntry userEntry = authService.getEntry(channelHandlerContext);
            if (userEntry == null) {
                System.err.println("User not found.");
                return;
            }
            channelHandlerContext.writeAndFlush(userEntry.listFiles(((ListMessage) message).getPath()));
        } else if (message instanceof GetFileMessage) {
            UserEntry userEntry = authService.getEntry(channelHandlerContext);
            if (userEntry == null) {
                System.err.println("User not found.");
                return;
            }
            channelHandlerContext.writeAndFlush(userEntry.sendFile(((GetFileMessage) message).getPath()));
        }
    }
}
