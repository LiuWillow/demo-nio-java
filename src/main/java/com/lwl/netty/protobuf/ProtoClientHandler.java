package com.lwl.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 * @author lwl
 * @date 2019/3/25 15:26
 * @description
 */
public class ProtoClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.MyMessage myMessage) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage;
        if (0 == random) {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.STUDENT_TYPE)
                    .setStudent(MyDataInfo.Student.newBuilder()
                            .setName("lwl").setAge(123).setAddress("上海").build()
                    ).build();
        } else if (1 == random) {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.DOG_TYPE)
                    .setDog(MyDataInfo.Dog.newBuilder()
                            .setAge(123).setName("旺财").build()
                    ).build();
        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.CAT_TYPE)
                    .setCat(MyDataInfo.Cat.newBuilder()
                            .setName("降龙").setCity("猫猫城市").build()
                    ).build();
        }
        ctx.channel().writeAndFlush(myMessage);
    }
}