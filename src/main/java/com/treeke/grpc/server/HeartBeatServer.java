package com.treeke.grpc.server;

import com.cetcxl.luna.protos.thrid.HeartbeatGrpc;
import com.cetcxl.luna.protos.thrid.HeartbeatOuterClass;
import com.treeke.grpc.cert.CertManager;
import io.grpc.stub.StreamObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Description 心跳检测接口实现
 * @Author treeke
 * @Date 2020/2/20
 * @Version 1.0
 **/
public class HeartBeatServer extends HeartbeatGrpc.HeartbeatImplBase {
    private static final Log log = LogFactory.getLog(HeartBeatServer.class);

    @Override
    public void heartbeat(HeartbeatOuterClass.HeartBeatReq request, StreamObserver<HeartbeatOuterClass.HeartBeatRes> responseObserver) {
        String id = request.getId();
        log.info("client[" + id + "] heartBeating");
        responseObserver.onNext(HeartbeatOuterClass.HeartBeatRes.newBuilder().setCode(CertManager.getCertSerialNumber()).setMessage("success").build());
        responseObserver.onCompleted();
    }
}
