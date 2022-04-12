package com.maxus.netty.netty.chapter4.protocol.request;

import com.maxus.netty.netty.chapter4.protocol.command.Command;
import com.maxus.netty.netty.chapter4.protocol.Packet;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/18 16:36
 */
public class HeartBeatRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.HEART_BEAT_REQUEST;
    }
}
