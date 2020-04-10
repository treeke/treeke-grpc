package com.treeke.grpc.observer;

/**
 * @Description 业务回调对象容器
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public class ObserverNode {
    /**
     * 安装智能合约服务对象
     */
    private final Node<InstallChaincodeObserver> installChaincodeObserverNode = new Node<>();
    /**
     * 安装智能合约返回服务对象
     */
    private final Node<InstallReportObserver> installReportObserverNode = new Node<>();
    /**
     * 加入通道对象
     */
    private final Node<JoinChannelObserver> joinChannelObserverNode = new Node<>();
    /**
     * 加入通道返回对象
     */
    private final Node<JoinReportObserver> joinReportObserverNode = new Node<>();

    public InstallChaincodeObserver getInstallChaincode() {
        return installChaincodeObserverNode.get();
    }

    public void setInstallChaincode(InstallChaincodeObserver installChaincodeObserver) {
        installChaincodeObserverNode.set(installChaincodeObserver);
    }

    public InstallReportObserver getInstallReport() {
        return installReportObserverNode.get();
    }

    public void setInstallReport(InstallReportObserver installReportObserver) {
        installReportObserverNode.set(installReportObserver);
    }

    public JoinChannelObserver getJoinChannel() {
        return joinChannelObserverNode.get();
    }

    public void setJoinChannel(JoinChannelObserver joinChannelObserver) {
        joinChannelObserverNode.set(joinChannelObserver);
    }

    public JoinReportObserver getJoinReport() {
        return joinReportObserverNode.get();
    }

    public void setJoinReport(JoinReportObserver joinReportObserver) {
        joinReportObserverNode.set(joinReportObserver);
    }

    class Node<T extends Observer> {
        private T v;

        public void set(T value) {
            this.v = value;
        }

        public T get() {
            return this.v;
        }

    }
}
