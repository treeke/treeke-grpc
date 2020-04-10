package com.treeke.grpc.callback;


import com.treeke.grpc.model.InstallChaincodeRep;
import com.treeke.grpc.model.JoinChannelRep;
import com.treeke.grpc.model.Transport;
import com.treeke.grpc.observer.InstallChaincodeObserver;
import com.treeke.grpc.observer.JoinChannelObserver;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public interface DeliverCallBack {
    /**
     * 注册加入通道服务
     * @param Observer
     */
    void registerJoinChannel(JoinChannelObserver Observer);

    /**
     * 注册安装智能合约服务
     * @param Observer
     */
    void registerInstallChaincode(InstallChaincodeObserver Observer);


    /**
     * 加入通道回调函数
     * @param transport
     * @return
     */
    boolean callBackJoinChannel(Transport<JoinChannelRep> transport);

    /**
     * 安装智能合约回调函数
     * @param transport
     * @return
     */
    boolean callBackInstallChaincode(Transport<InstallChaincodeRep> transport);

}
