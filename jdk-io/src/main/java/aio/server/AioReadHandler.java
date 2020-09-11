package aio.server;

import aio.client.AioClientReadHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author wangyang
 * @date 2020/9/7 14:24
 * @description:
 */
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private CountDownLatch countDownLatch;

    private AsynchronousSocketChannel socketChannel;

    public AioReadHandler(CountDownLatch countDownLatch, AsynchronousSocketChannel socketChannel) {
        this.countDownLatch = countDownLatch;
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        //如果条件成立，说明客户端主动终止了TCP套接字，这是服务端socketChannel就可以终止了
        if (result == -1){

            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;

        }
        byteBuffer.flip();
        byte[] message = new byte[byteBuffer.remaining()];
        byteBuffer.get(message);
        try{
            //打印客户端传过来的字节数
            System.out.println(result);
            String response = new String(message, "UTF-8");
            System.out.println("accept message:" + response);
            String responseStr = response(response);
            doWrite(responseStr);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private String response(String response) {
        return response;
    }

    //给客户端响应信息
    private void doWrite(String response){
        byte[] bytes = response.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();

        socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer byteBuffer) {
                if(byteBuffer.hasRemaining()){
                    socketChannel.write(byteBuffer,byteBuffer,this);
                }else{
                    //读取服务端传回的数据
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    //异步读
                    socketChannel.read(readBuffer,readBuffer,new AioReadHandler(countDownLatch,socketChannel));
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer byteBuffer) {
                System.out.println("写数据失败。。。");
                try {
                    socketChannel.close();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
