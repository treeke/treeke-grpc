package com.treeke.grpc.base;

import io.grpc.ClientInterceptor;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @Description Client抽象类
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public abstract class AbstractGrpcClient implements Client {

    private static final Log log = LogFactory.getLog(AbstractGrpcClient.class);

    protected ManagedChannel managedChannel;

    protected NettyChannelBuilder nettyChannelBuilder;

    protected SslContext sslContext;

    protected AbstractGrpcClient(String host, int port, InputStream certInput) throws SSLException {
        sslContext = GrpcSslContexts.forClient()
                .sslProvider(SslProvider.OPENSSL)
                .trustManager(certInput)
                .build();
        nettyChannelBuilder = NettyChannelBuilder.forAddress(host, port)
                .negotiationType(NegotiationType.TLS)
                .sslContext(sslContext);
        this.build();
    }

    protected void keepAliveTime(long keepAliveTime, TimeUnit timeUnit){
        nettyChannelBuilder.keepAliveTime(keepAliveTime, timeUnit);
    }

    protected void keepAliveTimeout(long keepAliveTime, TimeUnit timeUnit){
        nettyChannelBuilder.keepAliveTimeout(keepAliveTime, timeUnit);
    }

    protected void eventLoopGroup(EventLoopGroup eventLoopGroup){
        nettyChannelBuilder.eventLoopGroup(eventLoopGroup);
    }

    protected void intercept(ClientInterceptor... interceptors){
        nettyChannelBuilder.intercept(interceptors);
    }

    protected void executor(Executor executor){
        nettyChannelBuilder.executor(executor);
    }

    protected void disableRetry(){
        nettyChannelBuilder.disableRetry();
    }

    protected void enableRetry(){
        nettyChannelBuilder.enableRetry();
    }

    @Override
    public void stop() {
        if (managedChannel != null) {
            try {
                managedChannel.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.error("Grpc Client has been shutdown");
        }
    }

    @Override
    public void build() {
        if(managedChannel == null){
            managedChannel = nettyChannelBuilder.build();
        }
    }

    @Override
    public void resetConnectBackoff() {
        managedChannel.resetConnectBackoff();
    }

    @Override
    public ConnectivityState getState(boolean requestConnection) {
        return managedChannel.getState(requestConnection);
    }

    @Override
    public boolean isShutdown() {
        return managedChannel.isShutdown();
    }

    @Override
    public void notifyWhenStateChanged(ConnectivityState source, Runnable callback) {
        managedChannel.notifyWhenStateChanged(source, callback);
    }

}
