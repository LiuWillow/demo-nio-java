package com.lwl.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lwl
 * @date 2019/3/21 18:03
 * @description
 */
public class ServerThread extends Thread {
    private Selector selector;

    @Override
    public void run() {
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                if (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    serverSocketChannel.configureBlocking(false);
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.register(selector, SelectionKey.OP_READ);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void init() {
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}