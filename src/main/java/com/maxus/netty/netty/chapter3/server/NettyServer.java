package com.maxus.netty.netty.chapter3.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IDEA
 * Author:catHome
 * Description: netty server服务器端启动流程,最少参数化配置由线程模型，IO模型，连接读写处理逻辑三部分组成
 * Time:Create on 2018/10/9 9:35
 */
@Slf4j
@SuppressWarnings("all")
public class NettyServer {

    public static void main(String[] args) {
        //监听端口、accept新连接的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理每一条连接的数据读写的线程组
        NioEventLoopGroup workgroup =  new NioEventLoopGroup();

        //ServerBootstrap是引导类，引导服务器端的启动工作
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                //配置两大线程组
                .group(bossGroup,workgroup)
                //指定服务器端的IO模型，NIO模型： BIO模型：NioServerSocketChannel ; OioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                //调用childHandler()方法，给这个ServerBootstrap引导类创建一个ChannelInitializer，这里主要就是定义后续每条连接的数据读写，业务处理逻辑
                //ChannelInitializer这个类中，泛型参数NioSocketChannel是对NIO类型连接的抽象
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        log.info("deal with netty connection....... ");
                        //连接数据读写逻辑
                        ch.pipeline().addLast(new ServerHandler());

                    }
                })
                //handler()用于指定在服务端启动过程中的逻辑，通常情况下使用不到
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                        log.info("netty server start.......");
                    }
                })
                //给服务器端设置属性，最常见属性so_backlog，
                //SO_BACKLOG表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                //给每条连接设置一些TCP底层相关的属性
                //ChannelOption.SO_KEEPALIVE表示是否开启TCP底层心跳机制，true为开启
                //ChannelOption.TCP_NODELAY表示是否开启Nagle算法，true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭Nagle，如果需要减少发送次数减少网络交互，就开启Nagle
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
        ;
        //绑定端口，该方法是异步方法，返回ChannelFuture
        //ChannelFuture可以添加一个监听器GenericFutureListener，然后我们在GenericFutureListener的operationComplete方法里面，我们可以监听端口是否绑定成功
        serverBootstrap.bind(8000).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    log.info("netty server bind port success");
                }else{
                    log.error("netty server bind port failure");
                }
            }
        });
    }
}
