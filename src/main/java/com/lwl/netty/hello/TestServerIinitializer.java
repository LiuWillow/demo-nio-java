package com.lwl.netty.hello;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * date  2019/3/24
 * author liuwillow
 **/
public class TestServerIinitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 连接创建后立刻调用该方法
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        pipeline.addLast("httpServerHandler", new TestHttpServerHandler());
    }
}
