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
public class InstallChaincodeRep extends ResponseEntity {

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
    public InstallChaincodeRep(String id, String srcCertSerialNumber, String destCertSerialNumber, byte[] chaincodePackage, int language, String chaincodeName, String chaincodeVersion, String chaincodePath) {
        super(id, srcCertSerialNumber, destCertSerialNumber);
        this.chaincodePackage = chaincodePackage;
        this.language = language;
        this.chaincodeName = chaincodeName;
        this.chaincodeVersion = chaincodeVersion;
        this.chaincodePath = chaincodePath;
    }
}
