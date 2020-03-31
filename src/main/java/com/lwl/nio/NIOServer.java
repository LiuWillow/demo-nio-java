package com.lwl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lwl
 * @date 2019/2/1 14:36
 * @description 非阻塞IO
 */
public class NIOServer {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws IOException {
        int port = 9384;
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (selector.selectNow() == 0) {
                continue;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("接收链接：" + client.getRemoteAddress());

                        client.configureBlocking(false);
                        final ByteBuffer wrap = ByteBuffer.wrap("连接成功".getBytes());
                        client.write(wrap);
                        client.register(selector, SelectionKey.OP_READ);
                        continue;
                    }
                    //表示服务端读客户端的内容
                    if (key.isReadable()) {
                        System.out.println("读事件就绪");
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(20);
                        client.read(buffer);
                        System.out.println("接收到客户端消息：" + new String(buffer.array(), StandardCharsets.UTF_8));
                        key.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}