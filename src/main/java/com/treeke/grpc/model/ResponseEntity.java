package com.treeke.grpc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yjx
 * @date 2020/3/5
 **/
@Getter
@Setter
@AllArgsConstructor
public class ResponseEntity {

    /**
     * 执行命令id
     */
    private String id;

    /**
     * 发送方证书ID
     */
    private String srcCertSerialNumber;
    /**
     * 接受方证书ID
     */
    private String destCertSerialNumber;
}
