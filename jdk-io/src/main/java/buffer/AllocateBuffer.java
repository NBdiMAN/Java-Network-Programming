package buffer;

import java.io.BufferedReader;
import java.nio.ByteBuffer;

/**
 * @author wangyang
 * @date 2020/9/7 19:03
 * @description:
 */
public class AllocateBuffer {
    public static void main(String[] args) {
        //JVM堆上分配内存
        System.out.println("before Memory Size：" + Runtime.getRuntime().freeMemory());
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024000);
        System.out.println("buffer:" + byteBuffer);
        System.out.println("after Memory Size：" + Runtime.getRuntime().freeMemory());
        System.out.println("=====================================================");
        //直接使用机器内存
//        System.out.println("before Memory Size：" + Runtime.getRuntime().freeMemory());
//        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024000);
//        System.out.println("buffer:" + directBuffer);
//        System.out.println("after Memory Size：" + Runtime.getRuntime().freeMemory());
//        System.out.println("=====================================================");
    }

}
