package com.maxus.netty.netty.chapter4.server;

import com.maxus.netty.netty.chapter4.protocol.response.MessageResponsePacket;
import com.maxus.netty.netty.chapter4.protocol.request.MessageRequestPacket;
import com.maxus.netty.netty.chapter4.util.ChannelHandlerGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/15 13:05
 */
@Slf4j
@SuppressWarnings("all")
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当有客户端链接后，添加到channelGroup通信组
        ChannelHandlerGroup.channelGroup.add(ctx.channel());
        //日志信息
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");
        //通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(str);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();  //其实相当于一个connection

        /**
         * 调用channelGroup的writeAndFlush其实就相当于channelGroup中的每个channel都writeAndFlush
         *
         * 先去广播，再将自己加入到channelGroup中
         */
        ChannelHandlerGroup.channelGroup.writeAndFlush(" 【服务器】 -" +channel.remoteAddress() +" 加入\n");
        ChannelHandlerGroup.channelGroup.add(channel);


    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequestPacket messageRequestPacket) throws Exception {
        String message = messageRequestPacket.getMessage();
        log.debug("接收到客户端发来的消息：{} {}", message, new Date());

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端回复【" + message + "】");
        // channelHandlerContext.channel().writeAndFlush(messageResponsePacket);

        Channel channel = channelHandlerContext.channel();
        ChannelHandlerGroup. channelGroup.forEach(ch -> {
            if(channel !=ch){
                messageResponsePacket.setMessage("服务端回复【" + message + "】");
                ch.writeAndFlush(messageResponsePacket);
            }else{
                messageResponsePacket.setMessage("you【" + message + "】");
                ch.writeAndFlush(messageResponsePacket);
            }
        });


    }
}
