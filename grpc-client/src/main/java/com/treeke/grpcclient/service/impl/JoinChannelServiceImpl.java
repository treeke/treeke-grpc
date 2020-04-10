package com.treeke.grpcclient.service.impl;


import com.treeke.grpc.model.JoinChannelRep;
import com.treeke.grpc.model.Transport;
import com.treeke.grpcclient.service.JoinChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yjx
 * @date 2020/3/20
 **/
@Service
@Slf4j
public class JoinChannelServiceImpl implements JoinChannelService {


    @Override
    public boolean callBack(Transport<JoinChannelRep> transport) {
        log.info(transport.toString());
        return true;
    }
}
