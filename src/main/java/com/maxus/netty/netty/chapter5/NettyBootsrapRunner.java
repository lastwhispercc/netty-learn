package com.maxus.netty.netty.chapter5;

import com.maxus.netty.netty.chapter4.codec.PacketDecoder;
import com.maxus.netty.netty.chapter4.codec.PacketEncoder;
import com.maxus.netty.netty.chapter4.codec.Spliter;
import com.maxus.netty.netty.chapter4.handler.IMIdleStateHandler;
import com.maxus.netty.netty.chapter4.server.AuthHandler;
import com.maxus.netty.netty.chapter4.server.HeartBeatRequestHandler;
import com.maxus.netty.netty.chapter4.server.LoginRequestHandler;
import com.maxus.netty.netty.chapter4.server.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * netty spirngboot版本
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class NettyBootsrapRunner implements ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {

    @org.springframework.beans.factory.annotation.Value("${netty.server.port:8000}")
    private Integer port;

    private ApplicationContext applicationContext;

    private Channel serverChannel;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //NioEventLoopGroup的本质就是死循环，不停地检测IO事件，处理IO事件，执行任务
        //监听端口、accept新连接的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理每一条连接的数据读写的线程组
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //ServerBootstrap是引导类，引导服务器端的启动工作
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap
                    //配置两大线程组
                    .group(bossGroup, workGroup)
                    //指定服务器端的IO模型，NIO模型： BIO模型：NioServerSocketChannel ; OioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //调用childHandler()方法，给这个ServerBootstrap引导类创建一个ChannelInitializer，这里主要就是定义后续每条连接的数据读写，业务处理逻辑
                    //ChannelInitializer这个类中，泛型参数NioSocketChannel是对NIO类型连接的抽象
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            log.info("deal with netty connection....... ");
                            //连接数据读写逻辑
                            ch.pipeline()
                                    //空闲检查处理器，这里空闲检查处理器置顶？如果不置顶，在连接读到数据后，可能会由于inbound传播出错而不能向后传递，最终IMIdleStateHandler就不能读到数据，就导致误判
                                    //特别注意：inBoundHandler的事件传播顺序与addLast() 方法添加的顺序一致，而outBoundHandler的事件传播顺序与addLast() 方法添加的顺序是相反的
                                    .addLast(new IMIdleStateHandler())
                                    //拆包处理器
                                    .addLast(new Spliter())
                                    //解码处理器
                                    .addLast(new PacketDecoder())
                                    //登录处理器
                                    .addLast(new LoginRequestHandler())
                                    //接收客户端心跳包并响应的处理器
                                    .addLast(new HeartBeatRequestHandler())
                                    //身份认证处理器
                                    .addLast(new AuthHandler())
                                    //消息处理器
                                    .addLast(new MessageRequestHandler())
                                    //编码处理器
                                    .addLast(new PacketEncoder())
                            ;


                        }
                    })
                    //handler()用于指定在服务端启动过程中的逻辑，通常情况下使用不到
                    .handler(new ChannelInitializer<NioServerSocketChannel>() {
                        @Override
                        protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                            log.info("serverChannel initializer");
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
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("netty server bind port success");
                    } else {
                        log.error("netty server bind port failure");
                    }
                }
            });
            //临时变量存储ServerChannel对象
            serverChannel = channelFuture.channel();
            //阻塞至channel关闭
            serverChannel.closeFuture().sync();
        } catch (Exception e) {
            log.error("Netty server error", e);
        } finally {
            //释放资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        if (serverChannel != null) {
            log.info("close serverChannel");
            serverChannel.close();
            serverChannel = null;
        }
    }
}
