package netty.basic.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author wangyang
 * @date 2020/9/8 12:09
 * @description:
 */
public class EchoClient {
    private final int port;

    private final String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {

        //可以理解为一个线程组
        EventLoopGroup group = new NioEventLoopGroup();
        //客户端启动必备
        Bootstrap bootstrap = new Bootstrap();
        /* 指明使用nio进行网络通讯*/
        /*配置远程服务器地址*/
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new EchoClientHandler());
        /*sync()阻塞直到连接完成*/
        ChannelFuture channelFuture = bootstrap.connect().sync();
        /* 阻塞,直到channel关闭 */
        channelFuture.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(9999, "127.0.0.1").start();
    }
}
