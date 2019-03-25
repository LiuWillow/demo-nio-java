package com.lwl.netty.demowebsocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author lwl
 * @date 2019/3/25 11:29
 * @description
 */
public class MyWebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        //以块的方式写的处理器
        pipeline.addLast(new ChunkedWriteHandler());
        //消息聚合处理器，把各个段聚合成完整的数据
        pipeline.addLast(new HttpObjectAggregator(8192));
        //帮助完成websocket工作，websocket以frame为单位，地址表示websocket的地址，即ws/localhost/chat，ws表示协议名
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));
        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}