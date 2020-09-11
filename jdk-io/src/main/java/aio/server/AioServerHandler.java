package aio.server;

import aio.client.AioClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangyang
 * @date 2020/9/7 14:06
 * @description:
 */
public class AioServerHandler implements Runnable{


    private CountDownLatch countDownLatch;

    private AsynchronousServerSocketChannel serverSocketChannel;

    /**
     * 进行异步通信的通道
     */
    public AioServerHandler(int port){
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("Server is start, port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        //用于接收客户端连接
        serverSocketChannel.accept(this,new AioAcceptHandler(countDownLatch,serverSocketChannel));
        try {
            countDownLatch.await();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

    }
}
