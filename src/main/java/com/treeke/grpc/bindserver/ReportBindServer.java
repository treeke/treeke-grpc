package com.treeke.grpc.bindserver;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public interface ReportBindServer {

    /**
     * 绑定Report服务
     */
    void bindReportServer();

    /**
     * 绑定Heartbeat服务
     */
    void bindHeartbeatServer();

}
