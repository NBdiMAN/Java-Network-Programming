package aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangyang
 * @date 2020/9/7 0:11
 * @description:
 */
public class AioClientHandler implements CompletionHandler<Void,AioClientHandler>,Runnable {

    private AsynchronousSocketChannel asynchronousSocketChannel;
    private String host;
    private int port;
    private CountDownLatch latch;

    public AioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            //创建一个实际的通道
            asynchronousSocketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AioClientHandler attachment) {
        System.out.println("已连接到了客户端！");

    }

    @Override
    public void failed(Throwable exc, AioClientHandler attachment) {
        System.out.println("连接客户端失败：" + exc.getMessage());
        latch.countDown();
        try {
            asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        asynchronousSocketChannel.connect(new InetSocketAddress(host, port),this,this);
        try {
            latch.wait();
            asynchronousSocketChannel.close();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        byte[] bytes = msg.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        //异步写
        asynchronousSocketChannel.write(writeBuffer,writeBuffer,new AioClientWriteHandler(asynchronousSocketChannel,latch));
    }
}
