package client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import transport.AuthCloseMessage;
import transport.Message;

@Slf4j
public class Network {

    private SocketChannel channel;

    public Network(String serverHost, int serverPort, MessageReceiver receiver) {
        new Thread(() ->{
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new ClientMessageHandler(receiver)
                                );
                            }
                        });
                ChannelFuture future = bootstrap.connect(serverHost, serverPort).sync();
                log.debug("Connected to: [{}:{}]", serverHost, serverPort);
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("Client exception: ", e);
            } finally {
                group.shutdownGracefully();
            }
        }).start();
    }

    public void send(Message message) {
        log.debug("Send: " + message.toString());
        channel.writeAndFlush(message);
    }

    public void close() {
        channel.writeAndFlush(new AuthCloseMessage());
        channel.close();
    }
}
