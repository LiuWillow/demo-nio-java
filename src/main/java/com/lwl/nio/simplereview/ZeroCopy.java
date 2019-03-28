package com.lwl.nio.simplereview;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author lwl
 * @date 2019/3/28 15:49
 * @description
 */
public class ZeroCopy {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress(8899));
        //阻塞状态会一次读完
        socketChannel.configureBlocking(true);
        FileChannel fileChannel = FileChannel.open(Paths.get("charsetIn.txt"), StandardOpenOption.READ);
        //将0到fileChannel.size()的文件都写入到socketChannel中，比循环读取效率更高，在linux之类的环境中实现零拷贝
        fileChannel.transferTo(0, fileChannel.size(), socketChannel);
    }
}