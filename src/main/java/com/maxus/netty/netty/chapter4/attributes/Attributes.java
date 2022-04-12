package com.maxus.netty.netty.chapter4.attributes;

import io.netty.util.AttributeKey;

/**
 * Created with IDEA
 * Author:catHome
 * Description: NioServerSocketChannel维护的Map
 * Time:Create on 2018/10/14 19:16
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
