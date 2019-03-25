package com.lwl.netty.demoChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author lwl
 * @date 2019/3/25 9:39
 * @description
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 保存已经注册的channel，并且会自动移除已经断掉的连接
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("服务器接收到" + channel.remoteAddress() + "的消息：" + msg);
        channelGroup.forEach(ch -> {
            if (ch == channel){
                //必须加\n，因为配置了一个特殊的解码器，否则客户端收不到
                channel.writeAndFlush("me: " + msg + "\n");
            }else {
                ch.writeAndFlush(ch.remoteAddress() + ": " + msg + "\n");
            }
        });
    }

    /**
     * 连接建立好了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //调用这个方法会往channelGroup的所有的通道里发送消息
        channelGroup.writeAndFlush("用户" + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);
    }

    /**
     * 连接断掉了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("用户" + channel.remoteAddress() + "离开聊天\n");
        System.out.println("当前在线人数：" + channelGroup.size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}