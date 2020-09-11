package buffer;

import java.nio.ByteBuffer;

/**
 * @author wangyang
 * @date 2020/9/7 22:11
 * @description:
 */
public class BufferMethod {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        System.out.println("init buffer:" + byteBuffer);
        byteBuffer.put("aaa".getBytes());
        System.out.println("after put buffer:" + byteBuffer);
        byteBuffer.flip();
        System.out.println("after flip buffer:" + byteBuffer);
        System.out.println(byteBuffer.array());
        System.out.println("after array buffer:" + byteBuffer);
        byte[] bytes = new byte[1];
        byteBuffer.get(bytes);
        System.out.println(new String(bytes));
        System.out.println("after get buffer:" + byteBuffer);
        byte[] bytes2 = new byte[1];
        byteBuffer.get(bytes2);
        System.out.println(new String(bytes2));
        System.out.println("after get buffer:" + byteBuffer);
        byte[] bytes3 = new byte[10];
        byteBuffer.get(bytes3,0,1);
        System.out.println(new String(bytes3));
        System.out.println("after get buffer:" + byteBuffer);

    }
}
