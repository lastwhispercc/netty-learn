package com.maxus.netty.netty.chapter4.client;

import com.maxus.netty.netty.chapter4.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/15 11:53
 */
@Slf4j
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket messageResponsePacket) throws Exception {
        log.debug("{} 收到服务端消息：{}",new Date(),messageResponsePacket.getMessage());
    }
}
