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
public class JoinChannelRep extends ResponseEntity {

    /**
     * 创世块
     */
    private byte[] block;

    @Builder
    public JoinChannelRep(String id, String srcCertSerialNumber, String destCertSerialNumber, byte[] block) {
        super(id, srcCertSerialNumber, destCertSerialNumber);
        this.block = block;
    }
}
