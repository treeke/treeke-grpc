package com.treeke.grpc.grpc;

import com.treeke.grpc.base.AbstractGrpcServer;
import com.treeke.grpc.bindserver.ReportBindServer;
import com.treeke.grpc.callback.ReportCallBack;
import com.treeke.grpc.model.ReportRep;
import com.treeke.grpc.model.Transport;
import com.treeke.grpc.observer.InstallReportObserver;
import com.treeke.grpc.observer.JoinReportObserver;
import com.treeke.grpc.observer.ObserverNode;
import com.treeke.grpc.server.HeartBeatServer;
import com.treeke.grpc.server.ReportServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

/**
 * @Description 事件上报处理server
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public class ReportGrpcServer extends AbstractGrpcServer implements ReportCallBack,ReportBindServer {

    private static final Log log = LogFactory.getLog(ReportGrpcServer.class);

    /**
     * 回调服务对象集合
     */
    private final ObserverNode obs = new ObserverNode();

    public ReportGrpcServer(int port, File cert, File key) throws IOException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException {
        super(port, cert, key);
        this.bindReportServer();
        this.bindHeartbeatServer();
    }

    /**
     * 创建TLS加密通道的Server
     * @param port 端口号
     * @param certPath 证书路径
     * @param keyPath 私钥路径
     * @return
     */
    public static ReportGrpcServer createTLSServer(int port, String certPath, String keyPath){
        return createTLSServer(port, new File(certPath), new File(keyPath));
    }

    /**
     * 创建TLS加密通道的Server
     * @param port 端口号
     * @param cert 证书文件
     * @param key 私钥文件
     * @return
     */
    public static ReportGrpcServer createTLSServer(int port, File cert, File key){
        try {
            return new ReportGrpcServer(port, cert, key);
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
    public void bindReportServer() {
        this.addServer(new ReportServer(this).bindService());
    }

    @Override
    public void bindHeartbeatServer() {
        this.addServer(new HeartBeatServer().bindService());
    }

    @Override
    public void registerJoinReport(JoinReportObserver observer) {
        this.obs.setJoinReport(observer);
    }

    @Override
    public void registerInstallReport(InstallReportObserver observer) {
        this.obs.setInstallReport(observer);
    }

    @Override
    public boolean callBackJoinReport(Transport<ReportRep> transport) {
        return this.obs.getJoinReport().callBack(transport);
    }

    @Override
    public boolean callBackInstallReport(Transport<ReportRep> transport) {
        return this.obs.getInstallReport().callBack(transport);
    }
}
