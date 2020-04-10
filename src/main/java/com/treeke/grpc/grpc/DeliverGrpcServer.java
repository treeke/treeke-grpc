package com.treeke.grpc.grpc;

import com.treeke.grpc.base.AbstractGrpcServer;
import com.treeke.grpc.bindserver.DeliverBindServer;
import com.treeke.grpc.callback.DeliverCallBack;
import com.treeke.grpc.model.InstallChaincodeRep;
import com.treeke.grpc.model.JoinChannelRep;
import com.treeke.grpc.model.Transport;
import com.treeke.grpc.observer.InstallChaincodeObserver;
import com.treeke.grpc.observer.JoinChannelObserver;
import com.treeke.grpc.observer.ObserverNode;
import com.treeke.grpc.server.DeliverServer;
import com.treeke.grpc.server.HeartBeatServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

/**
 * @Description 事件下发处理server
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public class DeliverGrpcServer extends AbstractGrpcServer implements DeliverBindServer,DeliverCallBack {

    private static final Log log = LogFactory.getLog(DeliverGrpcServer.class);

    /**
     * 回调服务对象集合
     */
    private final ObserverNode obs = new ObserverNode();


    public DeliverGrpcServer(int port, File cert, File key) throws IOException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException {
        super(port, cert, key);
        this.bindDeliverServer();
        this.bindHeartbeatServer();
    }

    /**
     * 创建TLS加密通道的Server
     * @param port 端口号
     * @param certPath 证书路径
     * @param keyPath 私钥路径
     * @return
     */
    public static DeliverGrpcServer createTLSServer(int port, String certPath, String keyPath){
        return createTLSServer(port, new File(certPath), new File(keyPath));
    }

    /**
     * 创建TLS加密通道的Server
     * @param port 端口号
     * @param cert 证书文件
     * @param key 私钥文件
     * @return
     */
    public static DeliverGrpcServer createTLSServer(int port, File cert, File key){
        try {
            return new DeliverGrpcServer(port, cert, key);
        } catch (SSLException e) {
            log.error("initialize Grpc Server false" + e.getMessage());
            e.printStackTrace();
        } catch (CertificateException e) {
            log.error("initialize Grpc Server false" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            log.error("initialize Grpc Server false" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("initialize Grpc Server false" + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            log.error("initialize Grpc Server false" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void bindDeliverServer() {
        this.addServer(new DeliverServer(this).bindService());
    }

    @Override
    public void bindHeartbeatServer() {
        this.addServer(new HeartBeatServer().bindService());
    }

    @Override
    public void registerJoinChannel(JoinChannelObserver observer) {
        this.obs.setJoinChannel(observer);
    }

    @Override
    public void registerInstallChaincode(InstallChaincodeObserver observer) {
        this.obs.setInstallChaincode(observer);
    }

    @Override
    public boolean callBackJoinChannel(Transport<JoinChannelRep> transport) {
        return this.obs.getJoinChannel().callBack(transport);
    }

    @Override
    public boolean callBackInstallChaincode(Transport<InstallChaincodeRep> transport) {
        return this.obs.getInstallChaincode().callBack(transport);
    }
}
