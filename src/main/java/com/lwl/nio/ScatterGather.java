package com.lwl.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author lwl
 * @date 2019/3/21 15:14
 * @description
 */
public class ScatterGather {
    public static void main(String[] args) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        ByteBuffer[] buffers = new ByteBuffer[]{header, body};
        SocketChannel socketChannel = SocketChannel.open();
        //用于把channel中的数据读取到多个buffer中，一个buffer满了才会移动到下一个，适用于定长内容
        socketChannel.read(buffers);

        //同样也可以按顺序写入多个buffer
        socketChannel.write(buffers);
    }
}