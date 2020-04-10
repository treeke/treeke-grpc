package com.treeke.grpc.base;

/**
 * @Description grpc顶层接口
 * @Author treeke
 * @Date 2020/3/30
 * @Version 1.0
 **/
public interface Grpc {

    /**
     * 停止服务/连接
     */
    void stop();

    /**
     * 创建服务/连接
     */
    void build();
}
