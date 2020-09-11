package aio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangyang
 * @date 2020/9/7 0:55
 * @description:
 */
public class AioClientWriteHandler implements CompletionHandler<Integer, ByteBuffer>{

    private AsynchronousSocketChannel asynchronousSocketChannel;
    private CountDownLatch countDownLatch;

    public AioClientWriteHandler(AsynchronousSocketChannel asynchronousSocketChannel,CountDownLatch countDownLatch) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        if(byteBuffer.hasRemaining()){
            asynchronousSocketChannel.write(byteBuffer,byteBuffer,this);
        }else{
            //读取服务端传回的数据
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            //异步读
            asynchronousSocketChannel.read(readBuffer,readBuffer,new AioClientReadHandler(asynchronousSocketChannel,countDownLatch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        System.out.println("读取数据失败。。。");
        try {
            asynchronousSocketChannel.close();
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
