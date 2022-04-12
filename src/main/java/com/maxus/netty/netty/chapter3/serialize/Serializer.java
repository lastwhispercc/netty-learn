package com.maxus.netty.netty.chapter3.serialize;

import com.maxus.netty.netty.chapter3.serialize.impl.JSONSerializer;

/**
 * Created with IDEA
 * Author:catHome
 * Description:序列化接口
 * Time:Create on 2018/10/14 9:47
 */
public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);


}
