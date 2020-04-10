package com.treeke.grpcclient.service;


import com.treeke.grpc.model.ReportReq;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/4
 * @Version 1.0
 **/
public interface GrpcService {
    /**
     * 第三方节点加入通道执行成功后 返回消息
     *
     * @return
     */
    byte[] joinChannel(ReportReq reportReq);

    byte[] installChaincode(ReportReq reportReq);
}
