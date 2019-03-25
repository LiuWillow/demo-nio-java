package com.lwl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
            selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                executor.execute(() -> {
                    try {
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_WRITE);
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            client.read(buffer);
                            System.out.println("接收链接：" + new String(buffer.array(), "utf-8"));
                            key.cancel();
                            return;
                        }
                        //表示服务端写东西到客户端
                        if (key.isWritable()) {
                            System.out.println("写事件就绪，服务端正在写数据给客户端");
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.wrap("写给客户端的内容".getBytes());
                            while (buffer.hasRemaining()) {
                                if (client.write(buffer) == 0) {
                                    break;
                                }
                            }
                            System.out.println("服务端数据写完了");
                            client.register(selector, SelectionKey.OP_READ);
                            client.close();
                            key.cancel();
                            return;
                        }

                        //表示服务端读客户端的内容
                        if (key.isReadable()) {
                            System.out.println("读事件就绪");
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            client.read(buffer);
                            System.out.println(new String(buffer.array()));
                            key.cancel();
                            client.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });

            }
        }
    }
}