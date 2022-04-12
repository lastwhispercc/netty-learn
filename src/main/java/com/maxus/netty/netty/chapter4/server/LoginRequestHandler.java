package com.maxus.netty.netty.chapter4.server;

import com.maxus.netty.netty.chapter4.protocol.request.LoginRequestPacket;
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
 * Time:Create on 2018/10/14 16:40
 */
@Slf4j
@SuppressWarnings("all")
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        if (valid(loginRequestPacket)) {
            log.debug("登录成功 {}", new Date());
            loginResponsePacket.setSuccess(true);
            //身份认证通过时给channel
            LoginUtil.markAsLogin(channelHandlerContext.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            log.warn(new Date() + ": 登录失败!");
        }
        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
    }

    /**
     * 验证身份
     * @param loginRequestPacket
     * @return
     */
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

}
