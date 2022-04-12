package com.maxus.netty.netty.chapter4.client;

import com.maxus.netty.netty.chapter4.protocol.response.LoginResponsePacket;
import com.maxus.netty.netty.chapter4.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/14 15:56
 */
@Slf4j
@SuppressWarnings("all")
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.isSuccess()) {
            log.debug("{}: 客户端登录成功", new Date());
            LoginUtil.markAsLogin(channelHandlerContext.channel());
        } else {
            log.debug("{}: 客户端登录失败，原因：{}", new Date(), loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("{} oh my god，客户端连接关闭",new Date());
    }
}
