package com.maxus.netty.netty.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * Created with IDEA
 * Author:catHome
 * Description:服务端读取客户端数据
 * Time:Create on 2018/10/13 15:45
 */
@Slf4j
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 与客户端不同的是，这里覆盖的方法是 channelRead()，这个方法在接收到客户端发来的数据之后被回调
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String data = byteBuf.toString(Charset.forName("utf-8"));
        log.debug("netty server receive data {}",data);
        ByteBuf out = getByteBuf(ctx);
        ctx.channel().writeAndFlush(out);
    }

    public ByteBuf getByteBuf(ChannelHandlerContext ctx){
        String message = "芸芸众生";
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes(message.getBytes(Charset.forName("utf-8")));
        log.debug("netty server write into client data {}",message);
        return byteBuf;
    }
}
