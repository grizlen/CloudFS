package cloudserver.main;

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
import cloudserver.service.AuthService;

@Slf4j
public class Server {

    private static final int SERVER_PORT = 8189;

    private AuthService authService;

    public Server() {
        try {
            authService = new AuthService();
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
        EventLoopGroup bossGroupe = new NioEventLoopGroup(1);
        EventLoopGroup workerGroupe = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroupe, workerGroupe)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new ServerMessageHandler(authService));
                        }
                    });
            ChannelFuture future = bootstrap.bind(SERVER_PORT).sync();
            log.debug("Server started...");
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            bossGroupe.shutdownGracefully();
            workerGroupe.shutdownGracefully();
            try {
                authService.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
