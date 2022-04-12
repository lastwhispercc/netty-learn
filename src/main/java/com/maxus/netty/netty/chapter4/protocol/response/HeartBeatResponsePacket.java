package com.maxus.netty.netty.chapter4.protocol.response;

import com.maxus.netty.netty.chapter4.protocol.command.Command;
import com.maxus.netty.netty.chapter4.protocol.Packet;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/18 16:39
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.HEART_BEAT_RESPONSE;
    }
}
