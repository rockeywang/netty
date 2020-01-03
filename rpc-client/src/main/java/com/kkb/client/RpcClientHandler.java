package com.kkb.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangchao
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {


    private  Object result;


    public Object getResult() {
        return this.result;
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    // msg是服务端传递来的远程调用的结果
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
