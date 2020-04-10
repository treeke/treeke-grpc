package com.treeke.grpc.stub;

import io.grpc.stub.AbstractStub;

/**
 * @Description
 * @Author treeke
 * @Date 2020/4/2
 * @Version 1.0
 **/
public interface HeartBeatStub<T extends AbstractStub>{

    T getHeartbeatStub();


    T getHeartbeatBlockingStub();


    T getHeartbeatFutureStub();
}
