package com.kkb.server;

import com.kkb.dto.Invocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.PrintWriter;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<Invocation> {

    private Map<String, Object> registerMap;

    public RpcServerHandler(Map<String, Object> registerMap) {
        this.registerMap = registerMap;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Invocation msg) throws Exception {
        Object result = "没有指定的提供者";
        if (registerMap.containsKey(msg.getClassName())) {
            Object provider = registerMap.get(msg.getClassName());
            result = provider.getClass()
                    .getMethod(msg.getMethodName(), msg.getParamTypes())
                    .invoke(provider, msg.getParamValues());
        }
        ctx.writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
