package com.maxus.netty.netty.chapter4.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 空闲检查handler
 * Time:Create on 2018/10/18 16:24
 */
@Data
@Slf4j
public class IMIdleStateHandler  extends IdleStateHandler{

    /**
     * 空闲检测时间
     */
    private static final int READER_IDLE_TIME = 30;

    public IMIdleStateHandler() {
        //参数：读空闲时间、写空闲时间、读写空闲时间、时间单位
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.warn("在{}秒时间内未读取到对方{}的数据，连接关闭",READER_IDLE_TIME,ctx.channel().remoteAddress());
        //粗暴的直接关闭连接
       ctx.channel().close();
    }


}
