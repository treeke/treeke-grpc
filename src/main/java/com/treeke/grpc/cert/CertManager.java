package com.treeke.grpc.cert;

import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;

/**
 * @Description 通信证书管理
 * @Author treeke
 * @Date 2020/3/17
 * @Version 1.0
 **/
public class CertManager {
    /**
     * 证书序列号
     */
    private static String certSerialNumber;
    /**
     * 私钥
     */
    private static PrivateKey privateKey;
    /**
     * 公钥
     */
    private static PublicKey publicKey;

    /**
     * 通信证书私钥初始化方法
     * @param keyPath
     * @param certPath
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws CertificateException
     */
    public static void initCert(String keyPath, String certPath) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, CertificateException {
        PrivateKey privateKey = CertManager.initPrivateKey(keyPath);
        X509Certificate x509Certificate = CertManager.initPublicKey(certPath);
        CertManager.setPrivateKey(privateKey);
        CertManager.setCertSerialNumber(x509Certificate.getSerialNumber().toString());
        CertManager.setPublicKey(x509Certificate.getPublicKey());
    }

    /**
     * 通信证书私钥初始化方法
     * @param keyFile
     * @param certFile
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws CertificateException
     */
    public static void initCert(File keyFile, File certFile) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, CertificateException {
        PrivateKey privateKey = CertManager.initPrivateKey(keyFile);
        X509Certificate x509Certificate = CertManager.initPublicKey(new FileInputStream(certFile));
        CertManager.setPrivateKey(privateKey);
        CertManager.setCertSerialNumber(x509Certificate.getSerialNumber().toString());
        CertManager.setPublicKey(x509Certificate.getPublicKey());
    }

    /**
     * 读取私钥信息
     * @param keyPath 私钥路径
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    protected static PrivateKey initPrivateKey(String keyPath) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        File keyFile = new File(keyPath);
        return initPrivateKey(keyFile);
    }

    /**
     * 读取私钥信息
     * @param keyFile 私钥文件
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    protected static PrivateKey initPrivateKey(File keyFile) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        PrivateKey privKey = null;
        PemReader pemReader = null;
        //try {
        pemReader = new PemReader(new FileReader(keyFile));
        PemObject pemObject = pemReader.readPemObject();
        byte[] pemContent = pemObject.getContent();
        //支持从PKCS#1或PKCS#8 格式的私钥文件中提取私钥
        if (pemObject.getType().endsWith("RSA PRIVATE KEY")) {
            // 取得私钥  for PKCS#1
            RSAPrivateKey asn1PrivKey = RSAPrivateKey.getInstance(pemContent);
            RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privKey = keyFactory.generatePrivate(rsaPrivKeySpec);
        } else if (pemObject.getType().endsWith("PRIVATE KEY")) {
            //取得私钥 for PKCS#8
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(pemContent);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privKey = kf.generatePrivate(privKeySpec);
        }
        pemReader.close();
        return privKey;
    }

    /**
     * 读取证书信息
     * @param certPath 证书路径
     * @return
     * @throws CertificateException
     * @throws FileNotFoundException
     */
    protected static X509Certificate initPublicKey(String certPath) throws CertificateException, FileNotFoundException {
        CertificateFactory
                certificatefactory = CertificateFactory.getInstance("X.509");
        FileInputStream fin = new FileInputStream(certPath);
        X509Certificate
                certificate = (X509Certificate) certificatefactory.generateCertificate(fin);
        return certificate;
    }

    /**
     * 读取证书信息
     * @param cert 证书流
     * @return
     * @throws CertificateException
     */
    protected static X509Certificate initPublicKey(InputStream cert) throws CertificateException {
        CertificateFactory
                certificatefactory = CertificateFactory.getInstance("X.509");
        X509Certificate
                certificate = (X509Certificate) certificatefactory.generateCertificate(cert);
        return certificate;
    }

    private static void setCertSerialNumber(String certSerialNumber) {
        CertManager.certSerialNumber = certSerialNumber;
    }

    private static void setPrivateKey(PrivateKey privateKey) {
        CertManager.privateKey = privateKey;
    }

    private static void setPublicKey(PublicKey publicKey) {
        CertManager.publicKey = publicKey;
    }

    public static String getCertSerialNumber() {
        return certSerialNumber;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }
}
