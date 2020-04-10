package com.treeke.grpc.model;

import lombok.Data;

/**
 * @ClassName Transport
 * @Description TODO
 * @Author liuanyicun
 * @Date 2020/2/27 10:01
 * @Version 1.0
 **/
@Data
public class Transport<T> {

    /**
     * 签名原数据
     */
    byte[] rawData;

    /**
     * 签名结果
     */
    byte[] signResult;

    /**
     * 需要传输的参数类
     */
    T t;

}
