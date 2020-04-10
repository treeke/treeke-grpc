package com.treeke.grpc.callback;


import com.treeke.grpc.model.ReportRep;
import com.treeke.grpc.model.Transport;
import com.treeke.grpc.observer.InstallReportObserver;
import com.treeke.grpc.observer.JoinReportObserver;

/**
 * @Description
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public interface ReportCallBack {

    /**
     * 注册加入通道返回服务
     * @param Observer
     */
    void registerJoinReport(JoinReportObserver Observer);

    /**
     * 注册安装智能合约返回服务
     * @param Observer
     */
    void registerInstallReport(InstallReportObserver Observer);

    /**
     * 加入通道返回回调函数
     * @param transport
     * @return
     */
    boolean callBackJoinReport(Transport<ReportRep> transport);

    /**
     * 安装智能合约返回回调函数
     * @param transport
     * @return
     */
    boolean callBackInstallReport(Transport<ReportRep> transport);
}
