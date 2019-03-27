package com.lwl.nio.simplereview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lwl
 * @date 2019/3/27 15:26
 * @description
 */
public class NioMapped {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("path", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        //把文件映射到内存中
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        //直接在堆外内存修改，不需要通过channel去写，就能将结果反映到文件上
        mappedByteBuffer.put(0, (byte) 'a');
        mappedByteBuffer.put(2, (byte) 'b');
        randomAccessFile.close();
    }
}