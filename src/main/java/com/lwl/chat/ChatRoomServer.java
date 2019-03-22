package com.lwl.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lwl
 * @date 2019/3/22 11:08
 * @description
 */
public class ChatRoomServer {
    private Selector selector = null;
    static final int port = 9999;
    private Charset charset = Charset.forName("UTF-8");
    //用来记录在线人数，以及昵称
    private static Set<String> users = new HashSet<>();

    private static String USER_EXIST = "system message: user exist, please change a name";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SPILIT = "#@#";

    public void init() throws IOException {
        selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server is listening now...");

        while (true) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }
            //可以通过这个方法，知道可用通道的集合
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey sk = (SelectionKey) keyIterator.next();
                keyIterator.remove();
                dealWithSelectionKey(server, sk);
            }
        }
    }

    public void dealWithSelectionKey(ServerSocketChannel server, SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            SocketChannel socketChannel = server.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("Server is listening from client :" + socketChannel.getRemoteAddress());
            socketChannel.write(charset.encode("Please input your name."));
        }
        //处理来自客户端的数据读取请求
        if (selectionKey.isReadable()) {
            //返回该SelectionKey对应的 Channel，其中有数据需要读取
            SocketChannel sc = (SocketChannel) selectionKey.channel();
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try {
                while (sc.read(buff) > 0) {
                    buff.flip();
                    content.append(charset.decode(buff));
                }
                System.out.println("Server is listening from client " + sc.getRemoteAddress() + " data rev is: " + content);
            } catch (IOException io) {
                selectionKey.cancel();
                if (selectionKey.channel() != null) {
                    selectionKey.channel().close();
                }
            }
            if (content.length() > 0) {
                String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
                //注册用户
                if (arrayContent.length == 1) {
                    String name = arrayContent[0];
                    if (users.contains(name)) {
                        sc.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(name);
                        int num = onlineNum(selector);
                        String message = "welcome " + name + " to chat room! Online numbers:" + num;
                        broadCast(selector, null, message);
                    }
                }
                //注册完了，发送消息
                else if (arrayContent.length > 1) {
                    String name = arrayContent[0];
                    String message = content.substring(name.length() + USER_CONTENT_SPILIT.length());
                    message = name + " say " + message;
                    if (users.contains(name)) {
                        //不回发给发送此内容的客户端
                        broadCast(selector, sc, message);
                    }
                }
            }
        }
    }

    //TODO 要是能检测下线，就不用这么统计了
    public static int onlineNum(Selector selector) {
        int res = 0;
        for (SelectionKey key : selector.keys()) {
            Channel targetchannel = key.channel();

            if (targetchannel instanceof SocketChannel) {
                res++;
            }
        }
        return res;
    }

    public void broadCast(Selector selector, SocketChannel except, String content) throws IOException {
        //广播数据到所有的SocketChannel中
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            //如果except不为空，不回发给发送此内容的客户端
            if (targetChannel instanceof SocketChannel && targetChannel != except) {
                SocketChannel dest = (SocketChannel) targetChannel;
                dest.write(charset.encode(content));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatRoomServer().init();
    }
}