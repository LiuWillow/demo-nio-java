package com.lwl.nio.simplereview;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author lwl
 * @date 2019/3/27 15:29
 * @description
 */
public class NioFileLock {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("path", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        //用的不多
        FileLock lock = channel.lock();
        lock.release();
    }
}