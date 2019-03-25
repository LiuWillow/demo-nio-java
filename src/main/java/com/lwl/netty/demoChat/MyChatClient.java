package com.lwl.netty.demoChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author lwl
 * @date 2019/3/25 9:54
 * @description
 */
public class MyChatClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class)
                    .handler(new MyChatClientInitializer());
            Channel channel = bootstrap.connect("localhost", MyChatServer.PORT).sync().channel();

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true){
                channel.writeAndFlush(br.readLine() + "\r\n");
            }
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}