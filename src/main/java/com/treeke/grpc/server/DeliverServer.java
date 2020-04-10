package com.treeke.grpc.server;

import com.cetcxl.luna.protos.thrid.DeliverGrpc;
import com.cetcxl.luna.protos.thrid.Xledger;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.treeke.grpc.cert.CertAlgorithm;
import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.grpc.DeliverGrpcServer;
import com.treeke.grpc.model.InstallChaincodeRep;
import com.treeke.grpc.model.JoinChannelRep;
import com.treeke.grpc.model.Transport;
import io.grpc.stub.StreamObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * @Description 命令下发接口实现
 * @Author treeke
 * @Date 2020/2/20
 * @Version 1.0
 **/
public class DeliverServer extends DeliverGrpc.DeliverImplBase {

    private static final Log log = LogFactory.getLog(DeliverServer.class);

    private DeliverGrpcServer deliverGrpcServer;

    public DeliverServer(DeliverGrpcServer deliverGrpcServer) {
        this.deliverGrpcServer = deliverGrpcServer;
    }

    /**
     * 服务器响应客户端消息
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void joinChannel(Xledger.SignedEnvelope request, StreamObserver<Xledger.CommonResponse> responseObserver) {
        ByteString envelopeBytes = request.getEnvelope();
        ByteString signature = request.getSignature();
        byte[] rawData = envelopeBytes.toByteArray();
        byte[] signResult = signature.toByteArray();
        Xledger.Envelope envelope = null;
        try {
            //数据反序列化
            envelope = Xledger.Envelope.parseFrom(envelopeBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
            Xledger.DeliverJoinReq deliverJoinReq = envelope.getDeliverJoinEnv();
            JoinChannelRep joinChannelReq = JoinChannelRep.builder()
                    .id(deliverJoinReq.getId())
                    .destCertSerialNumber(envelope.getDesPki())
                    .srcCertSerialNumber(envelope.getSrcPki())
                    .block(deliverJoinReq.getBlock().toByteArray()).build();
            //向下调用杨家兴的加入通道
            Transport<JoinChannelRep> transport = new Transport<>();
            transport.setRawData(rawData);
            transport.setSignResult(signResult);
            transport.setT(joinChannelReq);

            boolean result = deliverGrpcServer.callBackJoinChannel(transport);
            Xledger.ResponseEnv responseEnv = Xledger.ResponseEnv.newBuilder()
                .setHash(envelopeBytes)
                .setType(result ? Xledger.ResponseEnv.Type.SUCCESS : Xledger.ResponseEnv.Type.Fail).build();
            retrunMsg(responseObserver, responseEnv);
    }

    @Override
    public void installChaincode(Xledger.SignedEnvelope request, StreamObserver<Xledger.CommonResponse> responseObserver) {
        ByteString envelopeBytes = request.getEnvelope();
        ByteString signature = request.getSignature();
        //数据反序列化
        byte[] rawData = envelopeBytes.toByteArray();
        byte[] signResult = signature.toByteArray();
        Xledger.Envelope envelope = null;
        try {
            envelope = Xledger.Envelope.parseFrom(envelopeBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
            Xledger.DeliverInstallReq deliverInstallReq = envelope.getDeliverInstallEnv();
            //反序列化数据
            InstallChaincodeRep installChaincodeReq = InstallChaincodeRep.builder()
                    .id(deliverInstallReq.getId())
                    .destCertSerialNumber(envelope.getDesPki())
                    .srcCertSerialNumber(envelope.getSrcPki())
                    .chaincodeName(deliverInstallReq.getChaincodeName())
                    .language(deliverInstallReq.getLanguageValue())
                    .chaincodeVersion(deliverInstallReq.getChaincodeVersion())
                    .chaincodePackage(deliverInstallReq.getChaincodePackage().toByteArray())
                    .chaincodePath(deliverInstallReq.getChaincodePath()).build();
            // 调用下层方法
            Transport<InstallChaincodeRep> transport = new Transport<>();
            transport.setRawData(rawData);
            transport.setSignResult(signResult);
            transport.setT(installChaincodeReq);
            boolean result = deliverGrpcServer.callBackInstallChaincode(transport);
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
