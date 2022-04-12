package com.maxus.netty.netty.chapter4.codec;

import com.maxus.netty.netty.chapter4.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created with IDEA
 * Author:catHome
 * Description: netty提供了特殊channelHandler用于专门处理解码逻辑
 * Time:Create on 2018/10/15 11:31
 */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(PacketCodeC.INSTANCE.decode(byteBuf));
    }
}
