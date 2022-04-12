package com.maxus.netty.netty.chapter4.codec;

import com.maxus.netty.netty.chapter4.protocol.Packet;
import com.maxus.netty.netty.chapter4.protocol.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created with IDEA
 * Author:catHome
 * Description: netty提供了特殊channelHandler用于专门处理编码逻辑
 * Time:Create on 2018/10/15 11:35
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        PacketCodeC.INSTANCE.encode(byteBuf,packet);
    }
}
