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
public class ReportRep extends ResponseEntity {



    @Builder
    public ReportRep(String id, String srcCertSerialNumber, String destCertSerialNumber) {
        super(id, srcCertSerialNumber, destCertSerialNumber);
    }
}
