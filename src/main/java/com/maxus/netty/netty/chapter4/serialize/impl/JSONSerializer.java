package com.maxus.netty.netty.chapter4.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.maxus.netty.netty.chapter4.serialize.Serializer;
import com.maxus.netty.netty.chapter4.serialize.SerializerAlgorithm;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 使用阿里的fastjson序列化
 * Time:Create on 2018/10/14 9:51
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
