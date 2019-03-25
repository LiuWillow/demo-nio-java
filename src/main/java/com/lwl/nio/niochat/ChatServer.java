package com.lwl.nio.niochat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lwl
 * @date 2019/3/21 18:03
 * @description
 */
public class ChatServer {
    private Selector selector;


    private void doRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        socketChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        buffer.flip();
        System.out.println("服务器读到客户端传来的内容：" + new String(buffer.array()));
        socketChannel.write(ByteBuffer.wrap("服务端已收到！".getBytes()));
        System.out.println("服务端回复给客户端：" + "服务端已收到!");
    }

    private void doAccept(SelectionKey selectionKey) throws IOException {
        System.out.println("accept事件就绪");
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        socketChannel.write(ByteBuffer.wrap("----------已成功连接---------".getBytes()));
    }

    public void init() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //与selector一起用的时候必须是非阻塞的
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(9999));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true) {
            try {
                if (selector.select() == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                if (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isAcceptable()) {
                        doAccept(selectionKey);
                    }
                    if (selectionKey.isReadable()) {
                        doRead(selectionKey);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ChatServer serverThread = new ChatServer();
        serverThread.init();
        serverThread.listen();
    }
}