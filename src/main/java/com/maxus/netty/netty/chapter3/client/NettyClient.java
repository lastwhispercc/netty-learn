package com.maxus.netty.netty.chapter3.client;

import com.maxus.netty.netty.chapter3.protocol.PacketCodeC;
import com.maxus.netty.netty.chapter3.protocol.request.MessageRequestPacket;
import com.maxus.netty.netty.chapter3.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * Author:catHome
 * Description: netty client
 * Time:Create on 2018/10/13 11:15
 */
@Slf4j
@SuppressWarnings("all")
public class NettyClient {

    /**
     * 最大重连次数
     */
    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //客户端引导类Bootstrap，客户端引导类是ServerBootStrap
        Bootstrap bootstrap = new Bootstrap();

        //1.指定线程模型
        bootstrap.group(workerGroup)
                //2.指定io模型
                .channel(NioSocketChannel.class)
                //3.IO处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //连接数据逻辑处理
                        //ch.pipeline() 返回的是和这条连接相关的逻辑处理链，采用了责任链模式
                        ch.pipeline()
                                //调用addLast()方法添加逻辑处理器，这个逻辑处理器为的就是在客户端建立连接成功之后，向服务端写数据
                                .addLast(new ClientHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);

        //建立连接
        connect(bootstrap, "127.0.0.1", 8000, MAX_RETRY);
    }

    /**
     * 建立连接，连接失败自动重连（在网络环境较差的情况下，客户端第一次连接可能会连接失败，所以需要尝试重新连接逻辑处理）
     * 代码逻辑：
     * 1、如果连接成功则打印连接成功的消息
     * 2、如果连接失败但是重试次数已经用完，放弃连接
     * 3、如果连接失败但是重试次数仍然没有用完，则计算下一次重连间隔 delay，然后定期重连
     *
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("connect netty server success");
                Channel channel = ((ChannelFuture) future).channel();
                // 连接成功之后，启动控制台线程
                startConsoleThread(channel);
            } else if (retry == 0) {
                log.debug("maxRetries have been reached,give up connect");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔：通常情况下，连接建立失败不会立即重连，而是会以指数退避的方式，比如每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，然后到达一定次数之后就放弃连接
                int delay = 1 << order;
                log.error(new Date() + ": 连接失败，第" + order + "次重连……");
                //定时任务逻辑
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                // channel 是登录状态时允许控制台输入消息
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
                    ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channel.alloc(), packet);
                    channel.writeAndFlush(byteBuf);
                }
            }
        }).start();
    }
}
