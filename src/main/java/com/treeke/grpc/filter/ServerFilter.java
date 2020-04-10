package com.treeke.grpc.filter;

import io.grpc.Attributes;
import io.grpc.ServerTransportFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

/**
 * @Description grpc服务端过滤器
 * @Author treeke
 * @Date 2020/3/31
 * @Version 1.0
 **/
public class ServerFilter extends ServerTransportFilter {

    private static final Log log = LogFactory.getLog(ServerFilter.class);

    @Override
    public Attributes transportReady(Attributes transportAttrs) {
        String remote = null;
        Set<Attributes.Key<?>> keys = transportAttrs.keys();
        for(Attributes.Key<?> key : keys){
            if(key.toString().equals("remote-addr")){
                Object entry =
                        transportAttrs.get(key);
                remote = entry.toString();
            }
        }
        log.info("Grpc:节点[" + remote + "]已连接");
        return super.transportReady(transportAttrs);
    }

    @Override
    public void transportTerminated(Attributes transportAttrs) {
        String remote = null;
        Set<Attributes.Key<?>> keys = transportAttrs.keys();
        for(Attributes.Key<?> key : keys){
            if(key.toString().equals("remote-addr")){
                Object entry =
                        transportAttrs.get(key);
                remote = entry.toString();
            }
        }
        log.info("Grpc:节点[" + remote + "]已断开连接");
        super.transportTerminated(transportAttrs);
    }
}
