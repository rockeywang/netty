package com.kkb.server;

public class RpcServerStarter {
    public static void main(String[] args) throws Exception {
        RpcServer server = new RpcServer();
        server.publish("com.kkb.service");
        server.start();
    }
}
