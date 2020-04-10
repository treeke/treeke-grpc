package com.treeke.grpc.grpc;


import com.treeke.grpc.base.AbstractGrpcClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description  客户端管理器
 * @Author treeke
 * @Date 2020/2/21
 * @Version 1.0
 **/
public class ClientManager<T extends AbstractGrpcClient> {

    private final ConcurrentHashMap<String, T> clients = new ConcurrentHashMap();

    private ClientManager(){}

    private static class Manager{
        private static final ClientManager ClIENT = new ClientManager();
    }

    public static ClientManager getInstance(){
        return Manager.ClIENT;
    }

    /**
     * 获取客户端列表
     * @return
     */
    public ConcurrentHashMap<String, T> getClients() {
        return this.clients;
    }

    /**
     * 添加客户端
     * @return
     */
    public void addClient(String serverId, T grpcClient) {
        clients.put(serverId, grpcClient);
    }

    public boolean hasKey(String serverId){
        return clients.containsKey(serverId);
    }

    public T getClient(String serverId){
        return clients.get(serverId);
    }

    /**
     * 删除客户端
     * @return
     */
    public void removeClient(String serverId) {
        T remove = clients.remove(serverId);
        remove.stop();
    }

}
