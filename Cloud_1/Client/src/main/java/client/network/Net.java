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
import transport.Message;

public class Net {

    private SocketChannel channel;

    public Net(String serverHost, int serverPort, MessageReceiver receiver) {
        new Thread(() -> {
            EventLoopGroup loopGroup = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(loopGroup)
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
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                loopGroup.shutdownGracefully();
            }
        }).start();
    }

    public void send(Message message) {
        System.out.println("Send: " + message.getMsg());
        channel.writeAndFlush(message);
    }

    public void colse() {
        channel.close();
    }
}
