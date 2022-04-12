package com.maxus.netty.netty.chapter3.protocol;

import lombok.Data;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 通信过程中的 Java 对象
 * Time:Create on 2018/10/14 9:37
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     * @return
     */
    public abstract Byte getCommand();
}
