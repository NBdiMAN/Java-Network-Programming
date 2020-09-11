package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangyang
 * @date 2020/9/7 23:58
 * @description: Nio客户端处理器
 */
public class NioClientHandler implements Runnable{
    private String host;
    private int port;

    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean started;

    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            if(socketChannel.connect(new InetSocketAddress(host,port))){

            }else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        while (started){
            try {
                int select = selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    if(key.isValid()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        if(key.isConnectable()){
                            System.out.println("连接服务器成功");
                            if(channel.finishConnect()){

                            }else {
                                System.exit(1);
                            }
                            socketChannel.register(selector,SelectionKey.OP_READ);
                        }
                        if (key.isReadable()){
                            System.out.println("数据准备完成，可以读取。。。");
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int readBytes = channel.read(byteBuffer);
                            if (readBytes > 0){
                                byteBuffer.flip();
                                byte[] bytes = new byte[byteBuffer.remaining()];
                                byteBuffer.get(bytes);
                                System.out.println("从服务端接收的数据：" + new String(bytes));
                            }else{
                                key.cancel();
                                channel.close();
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
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean sendMsg(String msg) throws IOException {
        if("q".equals(msg)){
            return false;
        }else {
            byte[] bytes = msg.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
            return true;
        }
    }

    public void stop(){
        started = false;
    }
}
