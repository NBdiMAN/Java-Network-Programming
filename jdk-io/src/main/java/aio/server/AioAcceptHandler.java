package aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangyang
 * @date 2020/9/7 14:15
 * @description:
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AioServerHandler> {

    private CountDownLatch countDownLatch;

    private AsynchronousServerSocketChannel serverSocketChannel;

    public AioAcceptHandler(CountDownLatch countDownLatch, AsynchronousServerSocketChannel serverSocketChannel) {
        this.countDownLatch = countDownLatch;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, AioServerHandler serverHandler) {
        AioServer.clientCount++;
        System.out.println("客户端连接数：" + AioServer.clientCount);
        //重新注册监听，以保证监听持续受到soket请求
        serverSocketChannel.accept(serverHandler,this);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        //异步读
        socketChannel.read(readBuffer,readBuffer,new AioReadHandler(countDownLatch,socketChannel));

    }

    @Override
    public void failed(Throwable exc, AioServerHandler serverHandler) {
        exc.printStackTrace();
        countDownLatch.countDown();
    }
}
