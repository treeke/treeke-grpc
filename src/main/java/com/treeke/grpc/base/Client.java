package com.treeke.grpc.base;

import io.grpc.ConnectivityState;

/**
 * @Description grpc客户端接口定义
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public interface Client extends Grpc {

    /**
     * 重置当前连接
     */
    void resetConnectBackoff();

    /**
     * 获取当前连接状态
     * @param requestConnection 是否发起请求
     * @return
     */
    ConnectivityState getState(boolean requestConnection);

    /**
     * 当前连接是否被停止
     * @return
     */
    boolean isShutdown();

    /**
     * 当连接期望状态被改变时执行回调
     * @param source 期望连接状态
     * @param callback 回调函数
     */
    void notifyWhenStateChanged(ConnectivityState source, Runnable callback);
}
