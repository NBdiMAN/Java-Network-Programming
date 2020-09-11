package aio.server;

/**
 * @author wangyang
 * @date 2020/9/7 14:46
 * @description:
 */
public class AioServer {
    public  volatile static Integer clientCount = 0;
    private static AioServerHandler serverHandler;

    public static void main(String[] args) {
        serverHandler = new AioServerHandler(666);
        new Thread(serverHandler,"server").start();
    }
}
