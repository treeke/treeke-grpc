package com.treeke.grpc.observer;

import com.treeke.grpc.model.ReportRep;
import com.treeke.grpc.model.Transport;

/**
 * @Description 加入通道返回业务回调接口定义
 * @Author treeke
 * @Date 2020/3/27
 * @Version 1.0
 **/
public interface JoinReportObserver extends Observer {

    boolean callBack(Transport<ReportRep> transport);

}
