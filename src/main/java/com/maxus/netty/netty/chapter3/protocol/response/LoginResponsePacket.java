package com.maxus.netty.netty.chapter3.protocol.response;

import com.maxus.netty.netty.chapter3.protocol.Packet;
import com.maxus.netty.netty.chapter3.protocol.command.Command;
import lombok.Data;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/14 16:16
 */
@Data
public class LoginResponsePacket extends Packet {

    /**
     * 登录成功的标识
     */
    private boolean success;

    /**
     * 登录失败原因
     */
    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
