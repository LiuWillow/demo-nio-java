package com.lwl.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author lwl
 * @date 2019/3/21 18:03
 * @description
 */
public class Client {
    private Selector selector;

    private class ReadTask extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    if (selector.select() == 0) {
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    if (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        //很重要
                        iterator.remove();
                        if (selectionKey.isReadable()) {
                            System.out.println("客户端read事件就绪");
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            socketChannel.configureBlocking(false);
                            socketChannel.read(buffer);
                            buffer.flip();
                            System.out.println("客户端读到服务器传来的消息：" + new String(buffer.array()));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init() {
        try {
            selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(9999));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            new ReadTask().start();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String line = scanner.next();
                socketChannel.write(ByteBuffer.wrap(line.getBytes()));
                System.out.println("客户端发送消息 ： " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client serverThread = new Client();
        serverThread.init();
    }
}