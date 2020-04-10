package com.treeke.grpc.base;

import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.filter.ServerFilter;
import io.grpc.Server;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServerTransportFilter;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @Description server抽象类
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public abstract class AbstractGrpcServer implements com.treeke.grpc.base.Server {

    private static final Log log = LogFactory.getLog(AbstractGrpcServer.class);

    protected Server server;

    protected NettyServerBuilder nettyServerBuilder;

    protected SslContext sslContext;

    protected AbstractGrpcServer(int port, File cert, File key) throws IOException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException {
        SslContextBuilder scb = SslContextBuilder.forServer(cert, key);
        SslProvider sp = SslProvider.OPENSSL;
        GrpcSslContexts.configure(scb, sp);
        sslContext = scb.build();
        nettyServerBuilder = NettyServerBuilder.forPort(port)
                .sslContext(sslContext);
        this.addTransportFilter(new ServerFilter());
        CertManager.initCert(key, cert);
    }

    protected void addTransportFilter(ServerTransportFilter filter){
        checkServer();
        nettyServerBuilder.addTransportFilter(filter);
    }

    protected void keepAliveTime(long keepAliveTime, TimeUnit timeUnit){
        checkServer();
        nettyServerBuilder.keepAliveTime(keepAliveTime, timeUnit);
    }

    protected void keepAliveTimeout(long keepAliveTime, TimeUnit timeUnit){
        checkServer();
        nettyServerBuilder.keepAliveTimeout(keepAliveTime, timeUnit);
    }

    protected void workerEventLoopGroup(EventLoopGroup group){
        checkServer();
        nettyServerBuilder.workerEventLoopGroup(group);
    }

    protected void executor(Executor executor){
        checkServer();
        nettyServerBuilder.executor(executor);
    }

    protected void intercept(ServerInterceptor interceptor){
        checkServer();
        nettyServerBuilder.intercept(interceptor);
    }

    protected void addServer(ServerServiceDefinition... services){
        checkServer();
        for (ServerServiceDefinition serverServiceDefinition : services) {
            nettyServerBuilder = nettyServerBuilder.addService(serverServiceDefinition);
        }
    }

    @Override
    public List<ServerServiceDefinition> getServices(){
        return server.getServices();
    }

    @Override
    public boolean isShutdown(){
        return server.isShutdown();
    }

    @Override
    public List<? extends SocketAddress> getListenSockets(){
        return server.getListenSockets();
    }

    /**
     * 启动服务
     *
     * @throws IOException
     */
    @Override
    public void start() throws IOException {
        this.build();
        server.start();
        log.info("Grpc is ready");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("shutting down gRPC server since JVM is shutting down");
                AbstractGrpcServer.this.stop();
                log.info("Grpc server shut down");
            }
        });
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        try {
            server.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.error("Grpc Server has been shutdown");
    }

    @Override
    public void build(){
        server = nettyServerBuilder.build();
    }

    private void checkServer() {
        if(server != null){
            throw new RuntimeException("Grpc Server has been build");
        }
    }

}
