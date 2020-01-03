package com.kkb.consumer;

import com.kkb.client.RpcProxy;
import com.kkb.service.SomeService;

public class RpcConsumer {

    public static void main(String[] args) {
        SomeService service = RpcProxy.create(SomeService.class);
        System.out.println(service.hello("开课吧"));
        System.out.println(service.hashCode());
    }
}
