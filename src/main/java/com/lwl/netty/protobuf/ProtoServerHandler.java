package com.lwl.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lwl
 * @date 2019/3/25 15:21
 * @description
 */
public class ProtoServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.MyMessage myMessage) throws Exception {
        if (myMessage.getDataType() == MyDataInfo.MyMessage.DataType.STUDENT_TYPE){
            System.out.println("收到学生类型数据");
            MyDataInfo.Student student = myMessage.getStudent();
            System.out.println(student.getName());
            System.out.println(student.getAge());
            System.out.println(student.getAddress());
        }else if (myMessage.getDataType() == MyDataInfo.MyMessage.DataType.DOG_TYPE){
            System.out.println("收到dog类型数据");
            MyDataInfo.Dog dog = myMessage.getDog();
            System.out.println(dog.getName());
            System.out.println(dog.getAge());
        }else {
            System.out.println("收到cat类型");
            MyDataInfo.Cat cat = myMessage.getCat();
            System.out.println(cat.getCity());
            System.out.println(cat.getName());
        }
    }
}