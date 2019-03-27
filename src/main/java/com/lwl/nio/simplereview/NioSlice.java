package com.lwl.nio.simplereview;

import java.nio.ByteBuffer;

/**
 * @author lwl
 * @date 2019/3/27 14:49
 * @description
 */
public class NioSlice {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        buffer.position(2);
        buffer.limit(6);
        //sliceBuffer和原buffer共享一个数组地址
        ByteBuffer sliceBuffer = buffer.slice();
        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            sliceBuffer.put((byte) (sliceBuffer.get(i) * 2));
        }

        buffer.limit(buffer.capacity());
        buffer.position(0);
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.get());
        }

    }
}