package main.com.lwl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lwl
 * @date 2019/2/1 14:31
 * @description 阻塞io
 */
public class BlockIO {
    public static Executor executor = new ThreadPoolExecutor(10, 20, 5000, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadPoolExecutor.DiscardPolicy());
    public static void main(String[] args) throws IOException {
        int port = 8099;
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            //该过程阻塞
            Socket clientSocket = serverSocket.accept();
            executor.execute(() -> {
                OutputStream outputStream;
                try {
                    outputStream = clientSocket.getOutputStream();
                    outputStream.write("hello".getBytes());
                    outputStream.flush();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}