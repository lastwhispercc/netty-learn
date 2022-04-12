package com.maxus.netty.netty.chapter3.protocol.command;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/14 9:42
 */
public interface Command {

    /**
     * 登录请求指令
     */
    Byte LOGIN_REQUEST = 1;

    /**
     * 登录请求响应指令
     */
    Byte LOGIN_RESPONSE = 2;

    /**
     * 消息发送指令
     */
    Byte MESSAGE_REQUEST = 3;

    /**
     * 消息响应指令
     */
    Byte MESSAGE_RESPONSE = 4;

}
