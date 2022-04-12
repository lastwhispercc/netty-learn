package com.maxus.netty.netty.chapter2;

/**
 * Created with IDEA
 * Author:catHome
 * Description: 深入理解数据传输载体 ByteBuf
 * Time:Create on 2018/10/13 20:32
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

class ByteBufTest {

    public static void main(String[] args) {

        //第一个参数指定容量（默认是256个字节），第二个参数指定最大容量（默认是2G）
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        boolean direct = buffer.isDirect();
        System.out.println("buffer is direct ? " + (direct ? "Y" : "N"));
        print("allocate ByteBuf(9, 100)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        buffer.writeBytes(new byte[]{1, 2, 3, 4});

        print("writeBytes(1,2,3,4)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写完 int 类型之后，写指针增加4
        buffer.writeInt(12);
        print("writeInt(12)", buffer);

        // write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
        buffer.writeBytes(new byte[]{5});
        print("writeBytes(5)", buffer);

        // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
        buffer.writeBytes(new byte[]{6});
        print("writeBytes(6)", buffer);

        // get 方法不改变读写指针
        System.out.println("getByte(3) return: " + buffer.getByte(3));
        //getshort(3）：从索引3下标处读取两个字节，按照当前字节顺序组成一个short值
        //不理解的话FYI: https://www.freesion.com/article/66351122809/
        System.out.println("getShort(3) return: " + buffer.getShort(3));
        System.out.println("getInt(3) return: " + buffer.getInt(3));
        print("getByte()", buffer);


        // set 方法不改变读写指针
        buffer.setByte(buffer.readableBytes() + 1, 0);
        print("setByte()", buffer);

        // read 方法改变读指针
        byte[] dst = new byte[buffer.readableBytes()];
        buffer.readBytes(dst);
        print("readBytes(" + dst.length + ")", buffer);

    }

    private static void print(String action, ByteBuf buffer) {
        System.out.println("after ===========" + action + "============");
        //capacity表示bytebuf底层占用的字节数
        System.out.println("capacity(): " + buffer.capacity());
        //maxCapacity表示 ByteBuf 底层最大能够占用多少字节的内存，当向 ByteBuf 中写数据的时候，如果发现容量不足，则进行扩容，直到扩容到 maxCapacity，超过这个数，就抛异常
        System.out.println("maxCapacity(): " + buffer.maxCapacity());
        //读指针
        System.out.println("readerIndex(): " + buffer.readerIndex());
        //readableBytes 表示 ByteBuf 当前可读的字节数，它的值等于 writerIndex-readerIndex，如果两者相等，则不可读，isReadable() 方法返回 false
        System.out.println("readableBytes(): " + buffer.readableBytes());
        System.out.println("isReadable(): " + buffer.isReadable());
        //写指针
        System.out.println("writerIndex(): " + buffer.writerIndex());
        //writableBytes() 表示 ByteBuf 当前可写的字节数，它的值等于 capacity-writerIndex，如果两者相等，则表示不可写，isWritable() 返回 false，
        //但是这个时候，并不代表不能往 ByteBuf 中写数据了， 如果发现往 ByteBuf 中写数据写不进去的话，Netty 会自动扩容 ByteBuf，直到扩容到底层的内存大小为 maxCapacity，
        //而 maxWritableBytes() 就表示可写的最大字节数，它的值等于 maxCapacity-writerIndex
        System.out.println("writableBytes(): " + buffer.writableBytes());
        System.out.println("isWritable(): " + buffer.isWritable());
        System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
        System.out.println();
    }


}