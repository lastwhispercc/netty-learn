package com.maxus.netty.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IDEA
 * Author:catHome
 * Description: io编程实现通信（摒弃）
 * Time:Create on 2018/10/8 15:02
 */
@SuppressWarnings("all")
@Slf4j
public class IOServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8000);
        // (1) 接收新连接线程
        new Thread(() -> {
            while (true) {
                try {
                    // (1) 阻塞方法获取新的连接
                    Socket socket = serverSocket.accept();
                    InetAddress addr = socket.getInetAddress();
                    String hostName = addr.getHostName();
                    String hostAddress = addr.getHostAddress().toString();
                    log.debug("receive client connect {},{}", hostName, hostAddress);

                    // (2) 每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        System.out.println("线程名：" + Thread.currentThread().getName());
                        try {
                            int len;
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            // (3) 按字节流方式读取数据
                            while ((len = inputStream.read(data)) != -1) {
                                System.out.println(new String(data, 0, len));
                            }
                        } catch (IOException e) {
                        }
                    }).start();

                } catch (IOException e) {
                }

            }
        }).start();
    }
}
