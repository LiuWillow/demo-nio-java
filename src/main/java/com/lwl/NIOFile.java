package main.java.com.lwl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author lwl
 * @date 2019/2/26 20:46
 * @description
 */
public class NIOFile {
    public static void main(String[] args) throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\dell\\Desktop\\nio\\in.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\dell\\Desktop\\nio\\out.txt"), StandardOpenOption.WRITE);

        //堆内存分配   用allocateDirect是在系统内存分配，并且读取的时候会从内存中拷贝到堆内存
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //读到缓冲区
        //TODO 什么时候返回0 什么时候-1
        inChannel.read(buffer);
        //切换到写模式（就是把limit position改了）
        buffer.flip();
        //把缓冲区里的数据写到out path
        //TODO 为啥一次写不完
        outChannel.write(buffer);
    }
}