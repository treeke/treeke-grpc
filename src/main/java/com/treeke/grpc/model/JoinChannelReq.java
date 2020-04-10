package com.treeke.grpc.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yjx
 * @date 2020/2/26
 **/
@Getter
@Setter
public class JoinChannelReq extends RequestEntity {
    /**
     * 执行命令id
     */
    private String id;
    /**
     * 创世块
     */
    private byte[] block;

    @Builder
    public JoinChannelReq(String host, int port, byte[] nonce, String srcCertSerialNumber, String destCertSerialNumber, byte[] tlsCert, String privateKey, String id, byte[] block) {
        super(host, port, nonce, srcCertSerialNumber, destCertSerialNumber, tlsCert, privateKey);
        this.id = id;
        this.block = block;
    }
}
