package com.treeke.grpc.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName ReportReq
 * @Description TODO
 * @Author liuanyicun
 * @Date 2020/2/26 17:05
 * @Version 1.0
 **/
@Getter
@Setter
public class ReportReq extends RequestEntity {

    /**
     * 执行成功的ID
     */
    private String id;

    @Builder
    public ReportReq(String host, int port, byte[] nonce, String srcCertSerialNumber, String destCertSerialNumber, byte[] tlsCert, String privateKey, String id) {
        super(host, port, nonce, srcCertSerialNumber, destCertSerialNumber, tlsCert, privateKey);
        this.id = id;
    }
}
