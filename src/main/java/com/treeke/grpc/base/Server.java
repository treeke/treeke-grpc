package com.treeke.grpc.base;

import io.grpc.ServerServiceDefinition;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;

/**
 * @Description grpc服务端接口定义
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public interface Server extends Grpc {

    /**
     * 启动服务
     * @throws IOException
     */
    void start()throws IOException;

    /**
     * 获取当前server绑定的服务
     * @return
     */
    List<ServerServiceDefinition> getServices();

    /**
     * 判断当前服务是否被停止
     * @return
     */
    boolean isShutdown();

    /**
     * 获取连接当前Server的Client列表
     * @return
     */
    List<? extends SocketAddress> getListenSockets();
}
