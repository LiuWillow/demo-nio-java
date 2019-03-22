package com.lwl.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author lwl
 * @date 2019/3/22 11:10
 * @description
 */
public class ChatRoomClient {
    private Selector selector = null;
    static final int port = 9999;
    private Charset charset = Charset.forName("UTF-8");
    private String name = "";
    private static String USER_EXIST = "system message: user exist, please change a name";
    private static String USER_CONTENT_SPILIT = "#@#";

    public void init() throws IOException
    {
        selector = Selector.open();
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1",port));
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
        new Thread(new ClientThread()).start();
        //在主线程中 从键盘读取数据输入到服务器端
        Scanner scan = new Scanner(System.in);
        while(scan.hasNextLine())
        {
            String line = scan.nextLine();
            //不允许发空消息
            if("".equals(line)) {
                continue;
            }
            if("".equals(name)) {
                name = line;
                line = name+USER_CONTENT_SPILIT;
            } else {
                line = name+USER_CONTENT_SPILIT+line;
            }
            //sc既能写也能读，这边是写
            sc.write(charset.encode(line));
        }

    }
    private class ClientThread implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                while(true) {
                    int readyChannels = selector.select();
                    if(readyChannels == 0) {
                        continue;
                    }
                    //可以通过这个方法，知道可用通道的集合
                    Set selectedKeys = selector.selectedKeys();
                    Iterator keyIterator = selectedKeys.iterator();
                    while(keyIterator.hasNext()) {
                        SelectionKey sk = (SelectionKey) keyIterator.next();
                        keyIterator.remove();
                        dealWithSelectionKey(sk);
                    }
                }
            }
            catch (IOException ignored)
            {}
        }

        private void dealWithSelectionKey(SelectionKey sk) throws IOException {
            if(sk.isReadable())
            {
                //使用 NIO 读取 Channel中的数据，这个和全局变量sc是一样的，因为只注册了一个SocketChannel
                //sc既能写也能读，这边是读
                SocketChannel sc = (SocketChannel)sk.channel();

                ByteBuffer buff = ByteBuffer.allocate(1024);
                StringBuilder content = new StringBuilder();
                while(sc.read(buff) > 0)
                {
                    buff.flip();
                    content.append(charset.decode(buff));
                }
                //若系统发送通知名字已经存在，则需要换个昵称
                if(USER_EXIST.equals(content.toString())) {
                    name = "";
                }
                System.out.println(content);
            }
        }
    }



    public static void main(String[] args) throws IOException
    {
        new ChatRoomClient().init();
    }
}