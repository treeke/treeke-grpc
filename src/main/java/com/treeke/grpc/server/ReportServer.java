package com.treeke.grpc.server;

import com.cetcxl.luna.protos.thrid.ReportGrpc;
import com.cetcxl.luna.protos.thrid.Xledger;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.treeke.grpc.cert.CertAlgorithm;
import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.grpc.ReportGrpcServer;
import com.treeke.grpc.model.ReportRep;
import com.treeke.grpc.model.Transport;
import io.grpc.stub.StreamObserver;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * @ClassName 命令上报接口实现
 * @Description TODO
 * @Author liuanyicun
 * @Date 2020/2/25 16:35
 * @Version 1.0
 **/
public class ReportServer extends ReportGrpc.ReportImplBase {


    private ReportGrpcServer reportGrpcServer;

    public ReportServer(ReportGrpcServer reportGrpcServer) {
        this.reportGrpcServer = reportGrpcServer;
    }

    @Override
    public void joinChannel(Xledger.SignedEnvelope request, StreamObserver<Xledger.CommonResponse> responseObserver) {
        //接收到的请求（第三方处理成功回调）
        ByteString envelopeBytes = request.getEnvelope();
        ByteString signature = request.getSignature();
        byte[] rawData = envelopeBytes.toByteArray();
        byte[] signResult = signature.toByteArray();
        //数据反序列化
        Xledger.Envelope envelope = null;
        try {
            envelope = Xledger.Envelope.parseFrom(envelopeBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        Xledger.ReportReq reportReqParam = envelope.getReportEnv();
        ReportRep reportReq = ReportRep.builder()
                .id(reportReqParam.getId())
                .destCertSerialNumber(envelope.getDesPki())
                .srcCertSerialNumber(envelope.getSrcPki()).build();
        //调用下层方法
        Transport<ReportRep> transport = new Transport<>();
        transport.setRawData(rawData);
        transport.setSignResult(signResult);
        transport.setT(reportReq);
        boolean result = reportGrpcServer.callBackJoinReport(transport);

        //处理结束返回
        Xledger.ResponseEnv responseEnv = Xledger.ResponseEnv.newBuilder()
                .setHash(envelopeBytes)
                .setType(result ? Xledger.ResponseEnv.Type.SUCCESS : Xledger.ResponseEnv.Type.Fail).build();
        retrunMsg(responseObserver, responseEnv);
    }

    @Override
    public void installChaincode(Xledger.SignedEnvelope request, StreamObserver<Xledger.CommonResponse> responseObserver) {
        //接收到的请求（第三方处理成功回调）
        ByteString envelopeBytes = request.getEnvelope();
        ByteString signature = request.getSignature();
        byte[] rawData = envelopeBytes.toByteArray();
        byte[] signResult = signature.toByteArray();

        //数据反序列化
        Xledger.Envelope envelope = null;
        try {
            envelope = Xledger.Envelope.parseFrom(envelopeBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        Xledger.ReportReq reportReqParam = envelope.getReportEnv();
        ReportRep reportReq = ReportRep.builder()
                .id(reportReqParam.getId())
                .destCertSerialNumber(envelope.getDesPki())
                .srcCertSerialNumber(envelope.getSrcPki()).build();
        Transport<ReportRep> transport = new Transport<>();
        transport.setRawData(rawData);
        transport.setSignResult(signResult);
        transport.setT(reportReq);
        boolean result = reportGrpcServer.callBackInstallReport(transport);

        //处理结束返回
        Xledger.ResponseEnv responseEnv = Xledger.ResponseEnv.newBuilder()
                .setHash(envelopeBytes)
                .setType(result ? Xledger.ResponseEnv.Type.SUCCESS : Xledger.ResponseEnv.Type.Fail).build();
        retrunMsg(responseObserver, responseEnv);
    }

    private void retrunMsg(StreamObserver<Xledger.CommonResponse> responseObserver, Xledger.ResponseEnv responseEnv) {
        byte[] sign = null;
        try {
            sign = CertAlgorithm.sign(responseEnv.toByteArray(), CertManager.getPrivateKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        //返回数据
        responseObserver.onNext(Xledger.CommonResponse.newBuilder()
                .setResposeEnv(responseEnv.toByteString())
                .setSignature(ByteString.copyFrom(sign)).build());
        responseObserver.onCompleted();
    }


}
