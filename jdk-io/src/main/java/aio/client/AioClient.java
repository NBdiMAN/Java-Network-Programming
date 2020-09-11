package aio.client;

import java.util.Scanner;

/**
 * @author wangyang
 * @date 2020/9/7 2:44
 * @description:
 */
public class AioClient {
    private static AioClientHandler aioClientHandler;

    private static void start(){
        if(aioClientHandler != null){
            return;
        }
        aioClientHandler = new AioClientHandler("127.0.0.1",666);
        new Thread(aioClientHandler,"Client").start();
    }
    public static boolean sendMsg(String msg)throws Exception{
        if(msg.equals("q")) return false;
        aioClientHandler.sendMsg(msg);
        return true;
    }

    public static void main(String[] args) throws Exception {
        start();
        System.out.println("请输入请求消息：");
        while(sendMsg(new Scanner(System.in).nextLine())){};
    }
}
