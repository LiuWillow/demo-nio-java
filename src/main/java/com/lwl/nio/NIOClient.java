package com.lwl.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lwl
 * @date 2019/2/1 15:43
 * @description
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9384));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        ByteBuffer buffer = ByteBuffer.allocate(20);
        while (true) {
            final int keysNum = selector.select();
            if (keysNum == 0) {
                continue;
            }
            final Set<SelectionKey> selectionKeys = selector.selectedKeys();
            final Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if (iterator.hasNext()) {
                final SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isReadable()) {
                    final SocketChannel channel = (SocketChannel) selectionKey.channel();
                    channel.read(buffer);
                    System.out.println("接受到服务端的消息：" + new String(buffer.array(), StandardCharsets.UTF_8));
                    buffer.clear();
                    buffer.put("收到啦哈哈哈".getBytes());
                    buffer.flip();
                    channel.write(buffer);
                    selectionKey.cancel();
                }
            }
        }
    }
}