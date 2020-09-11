package nio;

/**
 * @author wangyang
 * @date 2020/9/8 1:23
 * @description:
 */
public class NioSever {
    public static void main(String[] args) {
        new Thread(new NioServerHandler(66)).start();
    }
}
