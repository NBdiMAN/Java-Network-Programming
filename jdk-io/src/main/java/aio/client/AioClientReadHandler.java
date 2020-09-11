package aio.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangyang
 * @date 2020/9/7 2:05
 * @description:
 */
public class AioClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel asynchronousSocketChannel;
    private CountDownLatch countDownLatch;

    public AioClientReadHandler(AsynchronousSocketChannel asynchronousSocketChannel, CountDownLatch countDownLatch) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        try {
            String ret = new String(bytes,"UTF-8");
            System.out.println(ret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        System.out.println("读取数据失败。。。");
        try {
            countDownLatch.countDown();
            asynchronousSocketChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
