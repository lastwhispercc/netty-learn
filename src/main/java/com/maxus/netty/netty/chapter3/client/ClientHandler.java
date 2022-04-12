package com.maxus.netty.netty.chapter3.client;

import com.maxus.netty.netty.chapter3.protocol.Packet;
import com.maxus.netty.netty.chapter3.protocol.PacketCodeC;
import com.maxus.netty.netty.chapter3.protocol.request.LoginRequestPacket;
import com.maxus.netty.netty.chapter3.protocol.response.LoginResponsePacket;
import com.maxus.netty.netty.chapter3.protocol.response.MessageResponsePacket;
import com.maxus.netty.netty.chapter3.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/14 15:56
 */
@Slf4j
@SuppressWarnings("all")
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("{} : 客户端登录",new Date());

        //创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("cat");
        loginRequestPacket.setPassword("cathome");

        //编码,ctx.alloc() 获取的就是与当前连接相关的 ByteBuf 分配器
        ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);

        //写数据
        ctx.channel().writeAndFlush(encode);


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                log.debug("{}: 客户端登录成功",new Date());
                //客户端登录成功后，给客户端绑定登录成功的标志位
                LoginUtil.markAsLogin(ctx.channel());
            } else {
                log.debug("{}: 客户端登录失败，原因：{}",new Date(), loginResponsePacket.getReason());
            }
        }else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            log.debug( "{}: 收到服务端的消息: {}",new Date(),messageResponsePacket.getMessage());
        }
    }
}
