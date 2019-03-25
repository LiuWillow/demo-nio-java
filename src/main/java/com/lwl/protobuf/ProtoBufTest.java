package com.lwl.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author lwl
 * @date 2019/3/25 15:03
 * @description
 */
public class ProtoBufTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student student = DataInfo.Student.newBuilder().setName("柳威龙").setAge(23)
                .setAddress("成都").build();

        //字节就可以在网络上传输
        byte[] student2ByteArray = student.toByteArray();

        DataInfo.Student studentDecode = DataInfo.Student.parseFrom(student2ByteArray);
        System.out.println(studentDecode.getName());
        System.out.println(studentDecode.getAddress());
        System.out.println(studentDecode.getAge());
    }
}