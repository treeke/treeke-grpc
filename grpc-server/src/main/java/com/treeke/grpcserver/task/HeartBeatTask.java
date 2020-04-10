package com.treeke.grpcserver.task;


import com.cetcxl.luna.protos.thrid.HeartbeatGrpc;
import com.cetcxl.luna.protos.thrid.HeartbeatOuterClass;
import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.grpc.ClientManager;
import com.treeke.grpc.grpc.DeliverGrpcClient;
import io.grpc.ConnectivityState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author treeke
 * @Date 2020/2/21
 * @Version 1.0
 **/
@Component
@Slf4j
public class HeartBeatTask {

    /**
     * 心跳检测
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void heartBeat() {
        ConcurrentHashMap<String, DeliverGrpcClient> clients = ClientManager.getInstance().getClients();
        clients.forEach((serverId, grpcClient) -> {
            HeartbeatGrpc.HeartbeatBlockingStub heartbeatBlockingStub = grpcClient.getHeartbeatBlockingStub();
            HeartbeatOuterClass.HeartBeatRes heartBeatRes = null;
            try {
                heartBeatRes = heartbeatBlockingStub.heartbeat(HeartbeatOuterClass.HeartBeatReq.newBuilder().setId(CertManager.getCertSerialNumber()).build());
            } catch (Throwable e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
            ConnectivityState state = grpcClient.getState(false);
            log.info("Grpc state is " + state);
            if (!state.equals(ConnectivityState.READY)) {
                log.warn("Grpc-HeartBeat:Server[" + heartBeatRes.getCode() + "] state is " + state);
            }
        });
    }
}
