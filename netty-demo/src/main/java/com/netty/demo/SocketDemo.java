package com.netty.demo;

import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author wangchao
 */
public class SocketDemo {

    public static void main(String[] args) throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));


        Selector
        while (true){
            //ServerSocketChannel可以设置成非阻塞模式。在非阻塞模式下，accept() 方法会立刻返回，
            //如果还没有新进来的连接,返回的将是null。 因此，需要检查返回的SocketChannel是否是null.
            SocketChannel socketChannel=serverSocketChannel.accept();

            //do something with socketChannel....
        }

    }
}
