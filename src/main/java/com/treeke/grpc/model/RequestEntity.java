package com.treeke.grpc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yjx
 * @date 2020/3/4
 **/
@AllArgsConstructor
@Getter
@Setter
class RequestEntity {
    private String host;
    private int port;
    private byte[] nonce;

    /**
     * 发送方证书ID
     */
    private String srcCertSerialNumber;
    /**
     * 接受方证书ID
     */
    private String destCertSerialNumber;
    /**
     * tls证书
     */
    private byte[] tlsCert;
    /**
     * 发送方私钥
     */
    private String privateKey;

}
