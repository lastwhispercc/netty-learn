package com.maxus.netty.netty.chapter4.client;

import com.maxus.netty.netty.chapter4.protocol.request.HeartBeatRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 定时发送心跳包的handler
 * Time:Create on 2018/10/18 16:44
 */
@Slf4j
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 心跳的时间间隔
     */
    private static final int HEART_BEAT_INTERVAL = 5;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.debug("心跳handler加入责任链");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**
         * 定时发送心跳请求.备注:心跳数据包的发送时间间隔一般比空闲检测时间的一半要短一些，可以设置为空闲检测时间的三分之一，主要是为了排除公网偶发的秒级抖动
         */
        ctx.executor().scheduleAtFixedRate(() -> {
            log.debug("心跳包发送 {}",new Date());
            ctx.writeAndFlush(new HeartBeatRequestPacket());
        }, HEART_BEAT_INTERVAL, HEART_BEAT_INTERVAL, TimeUnit.SECONDS);
        super.channelActive(ctx);
    }
}
