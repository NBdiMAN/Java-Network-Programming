package bio;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangyang
 * @date 2020/9/6 20:20
 * @description:
 */
public class BioService {
    private static ServerSocket serverSocket;

    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void start(){
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("服务器启动。。。。");
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("有新的客户端连接。。。");
                pool.submit(new BioServerHandler(socket));
            }
        }catch (Exception e){

        }
    }
    public static void main(String[] args) {
        start();
    }
}
