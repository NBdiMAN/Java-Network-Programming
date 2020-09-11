package nio;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author wangyang
 * @date 2020/9/8 1:15
 * @description:
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        NioClientHandler nioClientHandler = new NioClientHandler("127.0.0.1", 66);
        new Thread(nioClientHandler).start();
        Scanner scanner = new Scanner(System.in);
        while(nioClientHandler.sendMsg(scanner.next())){

        }
    }
}
