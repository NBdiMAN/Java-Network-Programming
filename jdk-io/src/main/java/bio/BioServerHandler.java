package bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * @author wangyang
 * @date 2020/9/6 20:25
 * @description:
 */
public class BioServerHandler implements Runnable{

    private Socket socket;

    public BioServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            String message;
            String result;
            while ((message = in.readLine()) != null){
                System.out.printf("%s\n",message);
                result = LocalDateTime.now() + ":" + message;
                out.println(result);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(socket != null){
                try {
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
                socket = null;
            }
        }

    }
}
