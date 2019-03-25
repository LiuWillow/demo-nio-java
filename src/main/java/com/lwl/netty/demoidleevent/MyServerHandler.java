package com.lwl.netty.demoidleevent;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author lwl
 * @date 2019/3/25 10:57
 * @description 该类是ChannelInbound啥的父类
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 触发事件时会调用该方法
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //如果是空闲事件的话
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
                default:
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "超时事件: " + eventType);
            ctx.channel().close();
        }
    }
}