package com.treeke.grpcserver.service.impl;


import com.cetcxl.luna.protos.thrid.DeliverGrpc;
import com.cetcxl.luna.protos.thrid.Xledger;
import com.google.protobuf.ByteString;
import com.treeke.grpc.cert.CertAlgorithm;
import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.grpc.ClientManager;
import com.treeke.grpc.grpc.DeliverGrpcClient;
import com.treeke.grpc.model.InstallChaincodeReq;
import com.treeke.grpc.model.JoinChannelReq;
import com.treeke.grpcserver.service.GrpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
@Service
public class GrpcServiceImpl implements GrpcService {


    private static final ClientManager<DeliverGrpcClient> CLIENT_MANAGER = ClientManager.getInstance();

    @Override
    public byte[] joinChannel(JoinChannelReq joinChannelReq) {
        try {
            //接受方证书编号
            String destCertSerialNumber = joinChannelReq.getDestCertSerialNumber();
            //发送方证书编号
            String srcCertSerialNumber = joinChannelReq.getSrcCertSerialNumber();
            DeliverGrpcClient grpcClient;
            //根据证书ID 判断是否存在client
            if(CLIENT_MANAGER.hasKey(destCertSerialNumber)){
                grpcClient = CLIENT_MANAGER.getClient(destCertSerialNumber);
            }else {
                //不存在client 创建新的client
                grpcClient = DeliverGrpcClient.createTLSClient(destCertSerialNumber, joinChannelReq.getHost(), joinChannelReq.getPort(), joinChannelReq.getTlsCert());
            }
            //序列化数据
            Xledger.DeliverJoinReq deliverJoinReq = Xledger.DeliverJoinReq.newBuilder()
                    .setId(joinChannelReq.getId()).setBlock(ByteString.copyFrom(joinChannelReq.getBlock())).build();
            log.info(deliverJoinReq.toString());
            Xledger.Envelope envelope = Xledger.Envelope.newBuilder()
                    .setSrcPki(srcCertSerialNumber)
                    .setDesPki(destCertSerialNumber)
                    .setDeliverJoinEnv(deliverJoinReq).build();
            byte[] sign = CertAlgorithm.sign(envelope.toByteArray(), CertManager.getPrivateKey());
            ByteString envelopeByte = envelope.toByteString();
            //拼装请求数据
            Xledger.SignedEnvelope signedEnvelope = Xledger.SignedEnvelope
                    .newBuilder().setEnvelope(envelopeByte).setSignature(ByteString.copyFrom(sign)).build();
            //获取服务
            DeliverGrpc.DeliverBlockingStub deliverBlockingStub = grpcClient.getDeliverBlockingStub();
            Xledger.CommonResponse commonResponse = deliverBlockingStub.joinChannel(signedEnvelope);
            ByteString signature = commonResponse.getSignature();
            //返回值判断
            Xledger.ResponseEnv responseEnv = Xledger.ResponseEnv.parseFrom(commonResponse.getResposeEnv().toByteArray());
            //hash值判断
            if (!envelopeByte.equals(responseEnv.getHash())) {
                log.error("返回原数据错误");
                return null;
            }
            Xledger.ResponseEnv.Type type = responseEnv.getType();
            if (type == Xledger.ResponseEnv.Type.SUCCESS) {
                return signature.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public byte[] installChaincode(InstallChaincodeReq installChaincodeReq) {
        try {
            //接受方证书编号
            String destCertSerialNumber = installChaincodeReq.getDestCertSerialNumber();
            //发送方证书编号
            String srcCertSerialNumber = installChaincodeReq.getSrcCertSerialNumber();
            DeliverGrpcClient grpcClient;
            //根据证书ID 判断是否存在client
            if(CLIENT_MANAGER.hasKey(destCertSerialNumber)){
                grpcClient = CLIENT_MANAGER.getClient(destCertSerialNumber);
            }else {
                //不存在client 创建新的client
                grpcClient = DeliverGrpcClient.createTLSClient(destCertSerialNumber, installChaincodeReq.getHost(), installChaincodeReq.getPort(), installChaincodeReq.getTlsCert());
            }
            //序列化数据
            Xledger.DeliverInstallReq deliverInstallReq = Xledger.DeliverInstallReq.newBuilder()
                    .setId(installChaincodeReq.getId())
                    .setChaincodeName(installChaincodeReq.getChaincodeName())
                    .setLanguage(Xledger.DeliverInstallReq.Type.forNumber(installChaincodeReq.getLanguage()))
                    .setChaincodeVersion(installChaincodeReq.getChaincodeVersion())
                    .setChaincodePackage(ByteString.copyFrom(installChaincodeReq.getChaincodePackage()))
                    .setChaincodePath(installChaincodeReq.getChaincodePath()).build();
            Xledger.Envelope envelope = Xledger.Envelope.newBuilder()
                    .setSrcPki(srcCertSerialNumber)
                    .setDesPki(destCertSerialNumber)
                    .setDeliverInstallEnv(deliverInstallReq).build();
            byte[] sign = CertAlgorithm.sign(envelope.toByteArray(), CertManager.getPrivateKey());
            ByteString envelopeByte = envelope.toByteString();
            //组装请求数据
            Xledger.SignedEnvelope signedEnvelope = Xledger.SignedEnvelope.newBuilder()
                    .setEnvelope(envelope.toByteString())
                    .setSignature(ByteString.copyFrom(sign)).build();
            //获取服务组件
            DeliverGrpc.DeliverBlockingStub deliverBlockingStub = grpcClient.getDeliverBlockingStub();
            Xledger.CommonResponse commonResponse = deliverBlockingStub.installChaincode(signedEnvelope);
            ByteString signature = commonResponse.getSignature();
            //返回值判断
            Xledger.ResponseEnv responseEnv;

            responseEnv = Xledger.ResponseEnv.parseFrom(commonResponse.getResposeEnv().toByteArray());
            //hash值判断
            if (!envelopeByte.equals(responseEnv.getHash())) {
                log.error(" 返回原数据错误");
                return null;
            }
            Xledger.ResponseEnv.Type type = responseEnv.getType();
            if (type == Xledger.ResponseEnv.Type.SUCCESS) {
                return signature.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
