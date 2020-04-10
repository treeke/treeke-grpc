package com.treeke.grpc.cert;


import java.io.ByteArrayInputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Description 通信证书的签名验签算法
 * @Author treeke
 * @Date 2020/3/17
 * @Version 1.0
 **/
public class CertAlgorithm {

    /**
     * 通信证书数据签名
     *
     * @param data       签名数据
     * @param privateKey 私钥
     * @return byte[]
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] sign(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 通信证书数据验签
     *
     * @param data      原数据
     * @param sign      签名值
     * @param publicKey 公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public static boolean verifySig(byte[] data, byte[] sign, PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature rsa = Signature.getInstance("MD5withRSA");
        rsa.initVerify(publicKey);
        rsa.update(data);
        return rsa.verify(sign);
    }

    /**
     * 通信证书数据验签
     *
     * @param data      原数据
     * @param sign      签名值
     * @param publicKey 公钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateException
     */
    public static boolean verifySig(byte[] data, byte[] sign, String publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(publicKey.getBytes());
        X509Certificate x509Certificate = CertManager.initPublicKey(byteArrayInputStream);
        Signature rsa = Signature.getInstance("MD5withRSA");
        rsa.initVerify(x509Certificate.getPublicKey());
        rsa.update(data);
        return rsa.verify(sign);
    }


}
