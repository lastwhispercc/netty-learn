package com.maxus.netty.netty.chapter4.server;

import com.maxus.netty.netty.chapter4.protocol.request.HeartBeatRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/19 10:07
 */
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartBeatRequestPacket heartBeatRequestPacket) throws Exception {
        channelHandlerContext.channel().writeAndFlush(new HeartBeatRequestPacket());
    }
}
