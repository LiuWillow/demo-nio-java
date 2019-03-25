package com.lwl.netty.demoSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * date  2019/3/24
 * author liuwillow
 **/
public class MyClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println(channelHandlerContext.channel().remoteAddress());
        System.out.println("msg is " + msg);
        channelHandlerContext.channel().writeAndFlush("from client：" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

    }
}
