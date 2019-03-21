package main.java.com.lwl;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author lwl
 * @date 2019/2/1 15:43
 * @description
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9384));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        System.out.println("客户端接收到服务端的数据： " + new String(buffer.array(), "utf-8"));
        buffer.clear();
        System.out.println("客户端开始写数据：haha");
        buffer.put("haha".getBytes());
        socketChannel.write(buffer);
        System.out.println("客户端数据写完了");
        socketChannel.close();
    }
}