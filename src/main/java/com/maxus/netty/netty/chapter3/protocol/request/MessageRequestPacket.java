package com.maxus.netty.netty.chapter3.protocol.request;

import com.maxus.netty.netty.chapter3.protocol.Packet;
import com.maxus.netty.netty.chapter3.protocol.command.Command;
import lombok.Data;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/14 19:33
 */
@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
