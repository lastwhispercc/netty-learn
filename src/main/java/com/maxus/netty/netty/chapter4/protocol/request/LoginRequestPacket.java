package com.maxus.netty.netty.chapter4.protocol.request;

import com.maxus.netty.netty.chapter4.protocol.Packet;
import com.maxus.netty.netty.chapter4.protocol.command.Command;
import lombok.Data;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 登录请求数据包
 * Time:Create on 2018/10/14 9:44
 */
@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
