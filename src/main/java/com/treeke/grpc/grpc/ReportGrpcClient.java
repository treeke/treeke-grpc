package com.treeke.grpc.grpc;

import com.cetcxl.luna.protos.thrid.HeartbeatGrpc;
import com.cetcxl.luna.protos.thrid.ReportGrpc;
import com.treeke.grpc.base.AbstractGrpcClient;
import com.treeke.grpc.stub.HeartBeatStub;
import com.treeke.grpc.stub.ReportStub;
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
public class ReportGrpcClient  extends AbstractGrpcClient implements ReportStub,HeartBeatStub {

    private static final Log log = LogFactory.getLog(ReportGrpcClient.class);

    public ReportGrpcClient(String host, int port, InputStream certInput) throws SSLException {
        super(host, port, certInput);
    }

    public static ReportGrpcClient createTLSClient(String serverId, String host, int port, InputStream certInput) {
        try {
            ReportGrpcClient grpcClient = new ReportGrpcClient(host, port, certInput);
            ClientManager.getInstance().addClient(serverId, grpcClient);
            return grpcClient;
        } catch (SSLException e) {
            log.error("initialize ReportGrpcClient false" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static ReportGrpcClient createTLSClient(String serverId, String host, int port, String certPath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(certPath);
            return createTLSClient(serverId, host, port, fileInputStream);
        } catch (FileNotFoundException e) {
            log.error("initialize ReportGrpcClient false" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static ReportGrpcClient createTLSClient(String serverId, String host, int port, byte[] certBytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(certBytes);
        return createTLSClient(serverId, host, port, byteArrayInputStream);
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

    @Override
    public ReportGrpc.ReportBlockingStub getReportBlockingStub(){
        return ReportGrpc.newBlockingStub(managedChannel);
    }

    @Override
    public ReportGrpc.ReportStub getReportStub(){
        return ReportGrpc.newStub(managedChannel);
    }

    @Override
    public ReportGrpc.ReportFutureStub getReportFutureStub(){
        return ReportGrpc.newFutureStub(managedChannel);
    }
}
