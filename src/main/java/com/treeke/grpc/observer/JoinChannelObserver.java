package com.treeke.grpc.observer;


import com.treeke.grpc.model.JoinChannelRep;
import com.treeke.grpc.model.Transport;

/**
 * @Description 加入通道业务回调接口定义
 * @Author treeke
 * @Date 2020/3/27
 * @Version 1.0
 **/
public interface JoinChannelObserver extends Observer {

    boolean callBack(Transport<JoinChannelRep> transport);

}
