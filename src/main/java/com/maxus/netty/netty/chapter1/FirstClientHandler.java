package com.maxus.netty.netty.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * Created with IDEA
 * Author:catHome
 * Description:客户端发数据到服务端
 * Time:Create on 2018/10/13 14:39
 */
@Slf4j
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 这个逻辑处理器继承自 ChannelInboundHandlerAdapter，然后覆盖了 channelActive()方法，这个方法会在客户端连接建立成功之后被调用
     * 客户端连接建立成功之后，调用到 channelActive() 方法，向服务端写数据
     * 写数据的逻辑分为两步：
     * 1、获取netty 对二进制数据的抽象 ByteBuf
     * 1.1、ctx.alloc() 获取到一个 ByteBuf 的内存管理器，这个内存管理器的作用就是分配一个ByteBuf
     * 1.2、将字符串的二进制数据填充到 ByteBuf，这样我们就获取到了 Netty 需要的一个数据格式
     * 2、调用 ctx.channel().writeAndFlush() 把数据写到服务端...
     * 与传统的socket编程不同，netty的数据是以ByteBuf单位的，数据要转化为二进制填充到ByteBuf
     */

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("netty cline start write data into server...");

        long startTime = System.currentTimeMillis();
        //多次向服务端发送数据，服务端接收到的数据会出现粘包与半包现象
        /**
         * 为什么会出现粘包半包现象？
         * 尽管应用层使用到了netty,以ByteBuf作为数据传输的载体，但是底层操作系统只认tcp协议，数据仍以字节流的形式发送，
         * 因此，数据到了服务端，也是按照字节流的方式读入，然后到了 Netty 应用层面，重新拼装成 ByteBuf，
         * 而这里的 ByteBuf 与客户端按顺序发送的 ByteBuf 可能是不对等的，于是乎，出现的粘包半包现象
         */
        for(;;){
            long currentTime = System.currentTimeMillis();
            if (currentTime-startTime>1000){
                break;
            }
            // 1. 获取数据
            ByteBuf buffer = getByteBuf(ctx);

            // 2. 写数据
            ctx.channel().writeAndFlush(buffer);
        }
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        String message = "花花世界";
        // 1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        // 2. 准备数据，指定字符串的字符集为 utf-8
        byte[] bytes = message.getBytes(Charset.forName("utf-8"));

        // 3. 填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        log.debug("netty client write into server data {}", message);

        return buffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String data = in.toString(Charset.forName("utf-8"));
        log.debug("netty server receive server data {}", data);
    }
}
