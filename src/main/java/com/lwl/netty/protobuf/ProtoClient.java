package com.lwl.netty.protobuf;

import com.lwl.netty.demoSocket.MyClientInitializer;
import com.lwl.netty.demoidleevent.MyServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lwl
 * @date 2019/3/25 15:23
 * @description
 */
public class ProtoClient {
    public static void main(String[] args) {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture channelFuture = bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new ProtoClientInitializer())
                    .connect("localhost", ProtoServer.PORT)
                    .sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}