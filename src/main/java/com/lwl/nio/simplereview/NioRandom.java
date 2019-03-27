package com.lwl.nio.simplereview;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * @author lwl
 * @date 2019/3/27 9:15
 * @description
 */
public class NioRandom {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(100);
        System.out.println("limit为" + buffer.limit());
        System.out.println("capacity为" + buffer.capacity());
        System.out.println("position为" + buffer.position());
        buffer.put("sdfgsdfgsdfgsdfgsdfgsdfgsfgs".getBytes());
        System.out.println("--------写数据到buffer中-------");
        System.out.println("limit为" + buffer.limit());
        System.out.println("capacity为" + buffer.capacity());
        System.out.println("position为" + buffer.position());
        System.out.println("-----------调用flip-----------");
        buffer.flip();
        System.out.println("limit为" + buffer.limit());
        System.out.println("capacity为" + buffer.capacity());
        System.out.println("position为" + buffer.position());

        System.out.println(buffer.get());
        System.out.println("-------------读取-----------------");
        System.out.println("limit为" + buffer.limit());
        System.out.println("capacity为" + buffer.capacity());
        System.out.println("position为" + buffer.position());
        System.out.println(buffer.rewind());
        System.out.println("limit为" + buffer.limit());
        System.out.println("capacity为" + buffer.capacity());
        System.out.println("position为" + buffer.position());
    }
}