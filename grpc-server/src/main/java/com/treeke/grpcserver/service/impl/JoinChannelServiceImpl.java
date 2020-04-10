package com.treeke.grpcserver.service.impl;

import com.treeke.grpc.model.ReportRep;
import com.treeke.grpc.model.Transport;
import com.treeke.grpcserver.service.JoinChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yjx
 * @date 2020/3/18
 **/
@Service
@Slf4j
public class JoinChannelServiceImpl implements JoinChannelService {



    @Override
    public boolean callBack(Transport<ReportRep> transport) {
        log.info(transport.toString());
        return true;
    }
}
