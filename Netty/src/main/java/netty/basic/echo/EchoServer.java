package netty.basic.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author wangyang
 * @date 2020/9/8 14:19
 * @description:
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9999;
        EchoServer echoServer = new EchoServer(port);
        System.out.println("服务器即将启动");
        echoServer.start();
        System.out.println("服务器关闭");
    }

    private void start() throws InterruptedException {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        /* 线程组 */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    //收到连接请求,新启动一个socket通信,也就是channel,每个channel有自己的时间handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoServerHandler);
                        }
                    });
            /**
             * 阻塞的绑定到指定端口
             */
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }


}
