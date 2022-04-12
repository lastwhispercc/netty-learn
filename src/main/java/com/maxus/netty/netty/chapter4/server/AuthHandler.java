package com.maxus.netty.netty.chapter4.server;

import com.maxus.netty.netty.chapter4.util.ChannelHandlerGroup;
import com.maxus.netty.netty.chapter4.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 身份认证
 * Time:Create on 2018/10/15 20:48
 */
@Slf4j
public class AuthHandler extends ChannelInboundHandlerAdapter{





    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //如果客户端没有登录，简单粗暴关闭客户端连接
        if (!LoginUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            //动态移除校验逻辑(性能优化)
            ctx.pipeline().remove(this);

            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (LoginUtil.hasLogin(ctx.channel())) {
            log.debug("当前连接登录验证完毕，无需再次验证, AuthHandler被移除");
        } else {
            log.debug("无登录验证，强制关闭连接!");
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }
}
