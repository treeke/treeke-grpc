package com.treeke.grpcserver.service;


import com.treeke.grpc.model.InstallChaincodeReq;
import com.treeke.grpc.model.JoinChannelReq;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/4
 * @Version 1.0
 **/
public interface GrpcService {
    /**
     * 加入通道
     *
     * @param joinChannelReq
     * @return
     */
    byte[] joinChannel(JoinChannelReq joinChannelReq);

    /**
     * 安装智能合约
     *
     * @param installChaincodeReq
     */
    byte[] installChaincode(InstallChaincodeReq installChaincodeReq);
}
