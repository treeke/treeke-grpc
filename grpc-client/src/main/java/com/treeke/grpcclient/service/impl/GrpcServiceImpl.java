package com.treeke.grpcclient.service.impl;

import com.cetcxl.luna.protos.thrid.ReportGrpc;
import com.cetcxl.luna.protos.thrid.Xledger;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.treeke.grpc.cert.CertAlgorithm;
import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.grpc.ClientManager;
import com.treeke.grpc.grpc.ReportGrpcClient;
import com.treeke.grpc.model.ReportReq;
import com.treeke.grpcclient.service.GrpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Service
@Slf4j
public class GrpcServiceImpl implements GrpcService {

    private static final ClientManager<ReportGrpcClient> CLIENT_MANAGER = ClientManager.getInstance();

    @Override
    public byte[] joinChannel(ReportReq reportReq) {
        //接受方证书编号
        String destCertId = reportReq.getDestCertSerialNumber();
        //发送方证书编号
        String srcCertId = reportReq.getSrcCertSerialNumber();
        ReportGrpcClient grpcClient;
        //根据证书ID 判断是否存在client
        if (CLIENT_MANAGER.hasKey(destCertId)) {
            grpcClient = CLIENT_MANAGER.getClient(destCertId);
        } else {
            //不存在client 创建新的client
            grpcClient = ReportGrpcClient.createTLSClient(destCertId, reportReq.getHost(), reportReq.getPort(), reportReq.getTlsCert());
        }
        //序列化数据
        Xledger.ReportReq reportReq1 = Xledger.ReportReq.newBuilder()
                .setId(reportReq.getId()).build();
        Xledger.Envelope envelope = Xledger.Envelope.newBuilder()
                .setSrcPki(srcCertId)
                .setDesPki(destCertId)
                .setReportEnv(reportReq1).build();

        byte[] sign = null;
        try {
            sign = CertAlgorithm.sign(envelope.toByteArray(), CertManager.getPrivateKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        ByteString envelopeByte = envelope.toByteString();
        //组装请求数据
        Xledger.SignedEnvelope signedEnvelope = Xledger.SignedEnvelope.newBuilder()
                .setEnvelope(envelopeByte)
                .setSignature(ByteString.copyFrom(sign)).build();
        //获取服务组件
        ReportGrpc.ReportBlockingStub reportBlockingStub = grpcClient.getReportBlockingStub();
        Xledger.CommonResponse commonResponse = reportBlockingStub.joinChannel(signedEnvelope);
        ByteString signature = commonResponse.getSignature();
        //返回值判断
        Xledger.ResponseEnv responseEnv;
        try {
            responseEnv = Xledger.ResponseEnv.parseFrom(commonResponse.getResposeEnv().toByteArray());
            //hash值判断
            if (!envelopeByte.equals(responseEnv.getHash())) {
                log.error("返回原数据错误 ");
                return null;
            }
            Xledger.ResponseEnv.Type type = responseEnv.getType();
            if (type == Xledger.ResponseEnv.Type.SUCCESS) {
                return signature.toByteArray();
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] installChaincode(ReportReq reportReq) {
        //接受方证书编号
        String destCertId = reportReq.getDestCertSerialNumber();
        //发送方证书编号
        String srcCertId = reportReq.getSrcCertSerialNumber();
        ReportGrpcClient grpcClient;
        //根据证书ID 判断是否存在client
        if (CLIENT_MANAGER.hasKey(destCertId)) {
            grpcClient = CLIENT_MANAGER.getClient(destCertId);
        } else {
            //不存在client 创建新的client
            grpcClient = ReportGrpcClient.createTLSClient(destCertId, reportReq.getHost(), reportReq.getPort(), reportReq.getTlsCert());
        }
        String privateKey = reportReq.getPrivateKey();
        //序列化数据
        Xledger.ReportReq reportReq1 = Xledger.ReportReq.newBuilder()
                .setId(reportReq.getId()).build();
        Xledger.Envelope envelope = Xledger.Envelope.newBuilder()
                .setSrcPki(srcCertId)
                .setDesPki(destCertId)
                .setReportEnv(reportReq1).build();

        byte[] sign = null;
        try {
            sign = CertAlgorithm.sign(envelope.toByteArray(), CertManager.getPrivateKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        ByteString envelopeByte = envelope.toByteString();
        //组装请求数据
        Xledger.SignedEnvelope signedEnvelope = Xledger.SignedEnvelope.newBuilder()
                .setEnvelope(envelopeByte)
                .setSignature(ByteString.copyFrom(sign)).build();
        //获取服务组件
        ReportGrpc.ReportBlockingStub reportBlockingStub = grpcClient.getReportBlockingStub();
        Xledger.CommonResponse commonResponse = reportBlockingStub.installChaincode(signedEnvelope);
        ByteString signature = commonResponse.getSignature();
        //返回值判断
        Xledger.ResponseEnv responseEnv;
        try {
            responseEnv = Xledger.ResponseEnv.parseFrom(commonResponse.getResposeEnv().toByteArray());
            //hash值判断
            if (!envelopeByte.equals(responseEnv.getHash())) {
                log.error("返回原数据错误 ");
                return null;
            }
            Xledger.ResponseEnv.Type type = responseEnv.getType();
            if (type == Xledger.ResponseEnv.Type.SUCCESS) {
                return signature.toByteArray();
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}
