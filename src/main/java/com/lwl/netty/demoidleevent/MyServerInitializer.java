package com.lwl.netty.demoidleevent;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author lwl
 * @date 2019/3/25 10:52
 * @description
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //空闲检测器，空闲超过多久就会触发对应事件，该事件指的是服务端的读写状态而非客户端
        pipeline.addLast(new IdleStateHandler(5, 7, 10, TimeUnit.SECONDS));
        pipeline.addLast(new MyServerHandler());
    }
}