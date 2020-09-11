package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangyang
 * @date 2020/9/7 23:58
 * @description: Nio客户端处理器
 */
public class NioServerHandler implements Runnable{
    private int port;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean started;

    public NioServerHandler(int port) {
        this.port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            started = true;
            System.out.println("服务器已启动、、、");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (started){
            try {
                //阻塞方法，一直到获取到事件
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    if(key.isValid()){
                        ServerSocketChannel channel ;
                        if(key.isAcceptable()){
                            channel = (ServerSocketChannel) key.channel();
                            SocketChannel accept = channel.accept();
                            System.out.println("Socket channel 建立连接");
                            accept.configureBlocking(false);
                            accept.register(selector,SelectionKey.OP_READ);

                        }
                        if (key.isReadable()){
                            System.out.println("数据准备完成，可以读取。。。");
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            SocketChannel sc = (SocketChannel) key.channel();
                            int readBytes = 0;
                            try {
                                readBytes = sc.read(byteBuffer);
                            } catch (IOException e) {
                                e.printStackTrace();
                                sc.close();
                            }
                            if (readBytes > 0){
                                byteBuffer.flip();
                                byte[] bytes = new byte[byteBuffer.remaining()];
                                byteBuffer.get(bytes);
                                String receive =  new String(bytes);
                                System.out.println("从客户端接收的数据：" + receive);
                                String response = "Server Data: " +receive;
                                byte[] responseBytes = response.getBytes();
                                ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                writeBuffer.put(responseBytes);
                                writeBuffer.flip();
                                sc.write(writeBuffer);
                            }else{
                                key.cancel();
                                sc.close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(selector != null){
            try {
                System.out.println("关闭selector");
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        started = false;
    }
}
