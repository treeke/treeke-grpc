package com.treeke.grpcclient.service.impl;


import com.treeke.grpc.model.InstallChaincodeRep;
import com.treeke.grpc.model.Transport;
import com.treeke.grpcclient.service.InstallChaincodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yjx
 * @date 2020/3/20
 **/
@Service
@Slf4j
public class InstallChaincodeServiceImpl implements InstallChaincodeService {


    @Override
    public boolean callBack(Transport<InstallChaincodeRep> transport) {
        log.info(transport.toString());
        return true;
    }
}
