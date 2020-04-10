package com.treeke.grpc.bindserver;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public interface DeliverBindServer {
    /**
     * 绑定Deliver服务
     */
    void bindDeliverServer();

    /**
     * 绑定Heartbeat服务
     */
    void bindHeartbeatServer();


}
