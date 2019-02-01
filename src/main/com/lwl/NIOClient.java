package main.com.lwl;


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
        System.out.println("服务端说：" + new String(buffer.array(), "utf-8"));
    }
}