package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import server.handlers.SeverMessageHandler;
import server.services.AuthService;

@Slf4j
public class Server {
    private static final int SERVER_PORT = 8189;
    private AuthService authService;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try {
            authService = new AuthService();
            start();
        } catch (Exception e) {
            log.error("Server error: ", e);
        } finally {
            if (authService != null) {
                authService.close();
            }
        }
    }

    private void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new SeverMessageHandler(authService)
                            );
                        }
                    });
            ChannelFuture future = bootstrap.bind(SERVER_PORT).sync();
            log.debug("Server started...");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Server error: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
