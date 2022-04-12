package com.maxus.netty.netty.chapter4.protocol;

import com.maxus.netty.netty.chapter4.protocol.command.Command;
import com.maxus.netty.netty.chapter4.protocol.request.HeartBeatRequestPacket;
import com.maxus.netty.netty.chapter4.protocol.request.LoginRequestPacket;
import com.maxus.netty.netty.chapter4.protocol.request.MessageRequestPacket;
import com.maxus.netty.netty.chapter4.protocol.response.HeartBeatResponsePacket;
import com.maxus.netty.netty.chapter4.protocol.response.LoginResponsePacket;
import com.maxus.netty.netty.chapter4.protocol.response.MessageResponsePacket;
import com.maxus.netty.netty.chapter4.serialize.Serializer;
import com.maxus.netty.netty.chapter4.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * Created with IDEA
 * Author:catHome
 * Description: 编码与解码
 * Time:Create on 2018/10/14 10:06
 */
@SuppressWarnings("all")
@Slf4j
public class PacketCodeC {

    /**
     * 通信协议的设计：魔术（4个字节）+版本号(1个字节)+ 序列化算法（1个字节）+指令+数据长度【即长度域】（4个字节）+数据（N个字节）
     */

    /**
     * 魔数：设计魔数为了尽早屏蔽非本协议的客户端,十六进制0x12345678转化为10进制数为305419896
     */
    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;


    static {
        //指令对应的数据包对象
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(Command.HEART_BEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(Command.HEART_BEAT_RESPONSE, HeartBeatResponsePacket.class);

        //序列化方式
        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {

        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 2. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }
}
