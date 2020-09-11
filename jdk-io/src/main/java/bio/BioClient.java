package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author wangyang
 * @date 2020/9/6 16:45
 * @description:
 */
public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",12345);
        System.out.println("请输入消息");
        ReadMsg readMsg = new ReadMsg(socket);
        readMsg.start();
        PrintWriter pw = null;
        while (true){
            pw = new PrintWriter(socket.getOutputStream());
            pw.println(new Scanner(System.in).next());
            pw.flush();
        }


    }

    private static class ReadMsg extends Thread{
        private Socket socket = null;

        public ReadMsg(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                String line = null;
                while(true){
                    while((line = bufferedReader.readLine()) != null){
                        System.out.printf("%s\n", line);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
