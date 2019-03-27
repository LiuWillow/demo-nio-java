package com.lwl.nio.simplereview;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

/**
 * @author lwl
 * @date 2019/3/27 14:08
 * @description
 */
public class NioFile {
    public static void main(String[] args) throws IOException {
//        FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\dell\\Desktop\\shuru.txt"));
//        FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\dell\\Desktop\\shuchu.txt"));
        FileChannel inChannel = new FileInputStream("C:\\Users\\dell\\Desktop\\shuru.txt").getChannel();
        FileChannel outChannel = new FileOutputStream("C:\\Users\\dell\\Desktop\\shuchu.txt").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);

        while (true) {
            buffer.clear();
            int read = inChannel.read(buffer);
            if (read == -1){
                break;
            }
            buffer.flip();
            outChannel.write(buffer);
        }
        inChannel.close();
        outChannel.close();
    }
}