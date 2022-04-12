package com.maxus.netty.netty.chapter3.util;

import com.maxus.netty.netty.chapter3.attributes.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * Created with IDEA
 * Author:catHome
 * Description:
 * Time:Create on 2018/10/14 19:20
 */
public class LoginUtil {

    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null;
    }

}
