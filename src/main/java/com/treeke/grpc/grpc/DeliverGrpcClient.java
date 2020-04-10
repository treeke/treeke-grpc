package com.treeke.grpc.grpc;

import com.cetcxl.luna.protos.thrid.DeliverGrpc;
import com.cetcxl.luna.protos.thrid.HeartbeatGrpc;
import com.treeke.grpc.base.AbstractGrpcClient;
import com.treeke.grpc.stub.DeliverStub;
import com.treeke.grpc.stub.HeartBeatStub;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @Description
 * @Author treeke
 * @Date 2020/4/2
 * @Version 1.0
 **/
public class DeliverGrpcClient extends AbstractGrpcClient implements HeartBeatStub,DeliverStub {
    private static final Log log = LogFactory.getLog(DeliverGrpcClient.class);

    public DeliverGrpcClient(String host, int port, InputStream certInput) throws SSLException {
        super(host, port, certInput);
    }

    public static DeliverGrpcClient createTLSClient(String serverId, String host, int port, InputStream certInput) {
        try {
            DeliverGrpcClient grpcClient = new DeliverGrpcClient(host, port, certInput);
            ClientManager.getInstance().addClient(serverId, grpcClient);
            return grpcClient;
        } catch (SSLException e) {
            log.error("initialize DeliverGrpcClient Client false" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static DeliverGrpcClient createTLSClient(String serverId, String host, int port, String certPath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(certPath);
            return createTLSClient(serverId, host, port, fileInputStream);
        } catch (FileNotFoundException e) {
            log.error("initialize DeliverGrpcClient false" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static DeliverGrpcClient createTLSClient(String serverId, String host, int port, byte[] certBytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(certBytes);
        return createTLSClient(serverId, host, port, byteArrayInputStream);
    }

    @Override
    public DeliverGrpc.DeliverBlockingStub getDeliverBlockingStub(){
        return DeliverGrpc.newBlockingStub(managedChannel);
    }

    @Override
    public DeliverGrpc.DeliverStub getDeliverStub(){
        return DeliverGrpc.newStub(managedChannel);
    }

    @Override
    public DeliverGrpc.DeliverFutureStub getDeliverFutureStub(){
        return DeliverGrpc.newFutureStub(managedChannel);
    }

    @Override
    public HeartbeatGrpc.HeartbeatBlockingStub getHeartbeatBlockingStub(){
        return HeartbeatGrpc.newBlockingStub(managedChannel);
    }

    @Override
    public HeartbeatGrpc.HeartbeatStub getHeartbeatStub(){
        return HeartbeatGrpc.newStub(managedChannel);
    }

    @Override
    public HeartbeatGrpc.HeartbeatFutureStub getHeartbeatFutureStub(){
        return HeartbeatGrpc.newFutureStub(managedChannel);
    }

}
