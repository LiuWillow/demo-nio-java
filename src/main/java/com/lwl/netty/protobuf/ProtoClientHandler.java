package com.lwl.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lwl
 * @date 2019/3/25 15:26
 * @description
 */
public class ProtoClientHandler extends SimpleChannelInboundHandler<MyDataInfo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.Student student) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MyDataInfo.Student student = MyDataInfo.Student.newBuilder()
                .setName("lwl")
                .setAge(33)
                .setAddress("杭州")
                .build();
        ctx.channel().writeAndFlush(student);
    }
}