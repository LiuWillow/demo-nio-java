package com.lwl.netty.demoSocket;

import com.lwl.netty.demoidleevent.MyServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * date  2019/3/24
 * author liuwillow
 **/
public class MyClient {
    public static void main(String[] args) {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture channelFuture = bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer())
                    .connect("localhost", MyServer.PORT)
                    .sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
