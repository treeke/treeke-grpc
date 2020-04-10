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
public class InstallChaincodeReq extends RequestEntity {

    /**
     * 执行命令id
     */
    private String id;
    /**
     * 合约文件
     */
    private byte[] chaincodePackage;
    /**
     * 合约语言 默认golang
     */
    private int language;
    /**
     * 合约名
     */
    private String chaincodeName;
    /**
     * 合约版本号
     */
    private String chaincodeVersion;
    /**
     * 合约源文件路径
     */
    private String chaincodePath;

    @Builder
    public InstallChaincodeReq(String host, int port, byte[] nonce, String srcCertSerialNumber, String destCertSerialNumber, byte[] tlsCert, String privateKey, String id, byte[] chaincodePackage, int language, String chaincodeName, String chaincodeVersion, String chaincodePath) {
        super(host, port, nonce, srcCertSerialNumber, destCertSerialNumber, tlsCert, privateKey);
        this.id = id;
        this.chaincodePackage = chaincodePackage;
        this.language = language;
        this.chaincodeName = chaincodeName;
        this.chaincodeVersion = chaincodeVersion;
        this.chaincodePath = chaincodePath;
    }
}
