package com.kkb.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.*;

public class RpcServer {

    // 注册表
    private Map<String, Object> registerMap = new HashMap<>();
    // 用于缓存指定包下的实现类的类名
    private List<String> classCache = new ArrayList<>();
    // 线程安全的list
    // private List<String> classCache = Collections.synchronizedList(new ArrayList<>());

    // 将接口名（服务名称）与指定包中的实现类的实例进行对应，即写入到一个map
    public void publish(String basePackage) throws Exception {
        // 将指定包中的实现类的实例写入到classCache中
        cacheProviderClass(basePackage);
        // 真正的注册
        doRegister();
    }

    private void cacheProviderClass(String basePackage) {

        URL resource = this.getClass().getClassLoader()
                // com.kkb.service  -> com/kkb/service
                .getResource(basePackage.replaceAll("\\.", "/"));

        // 若目录中没有任何资源，则直接结束
        if(resource == null) return;

        // 将URL资源转换为File
        File dir = new File(resource.getFile());
        // 遍历指定包及其子孙包中所有文件，查找.class文件
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                cacheProviderClass(basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "").trim();
                classCache.add(basePackage + "." + fileName);
            }
        }

        // System.out.println(classCache);
    }

    private void doRegister() throws Exception {

        if (classCache.size() == 0) {
            return;
        }

        // 遍历缓存中的所有类
        for(String className : classCache) {
            Class<?> clazz = Class.forName(className);
            // 获取接口名
            String interfaceName = clazz.getInterfaces()[0].getName();
            registerMap.put(interfaceName, clazz.newInstance());
        }
    }

    // 启动Netty Server
    public void start() throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    // 用于指定，当服务端的请求处理线程全部用完时，
                    // 临时存放已经完成了三次握手的请求的队列的最大长度。
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 指定是否启用心跳机制来维护长连接的不被清除
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 指定要创建Channel的类型
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加编码器
                            pipeline.addLast(new ObjectEncoder());
                            // 添加解码器
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            // 添加自定义处理器
                            pipeline.addLast(new RpcServerHandler(registerMap));
                        }
                    });
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("微服务已经注册完成。。。");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
