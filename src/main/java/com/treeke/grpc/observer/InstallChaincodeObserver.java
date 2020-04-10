package com.treeke.grpc.observer;


import com.treeke.grpc.model.InstallChaincodeRep;
import com.treeke.grpc.model.Transport;

/**
 * @Description 安装智能合约业务回调接口定义
 * @Author treeke
 * @Date 2020/3/27
 * @Version 1.0
 **/
public interface InstallChaincodeObserver extends Observer {

    boolean callBack(Transport<InstallChaincodeRep> transport);
}
