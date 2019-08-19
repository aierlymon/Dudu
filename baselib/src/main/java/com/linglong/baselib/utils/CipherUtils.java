package com.linglong.baselib.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by huanghao on 2017-07-31.
 */

public class CipherUtils {

    //和md5校验一样是不可逆的
    public static String sha256(String src) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(src.getBytes());
            byte[] byteBuffer = messageDigest.digest();

            return Utils.byteArrayToHexString(byteBuffer);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sha1(String src) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(src.getBytes());
            byte[] byteBuffer = messageDigest.digest();

            return Utils.byteArrayToHexString(byteBuffer);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    public String setEncryption(String oldWord) {
        return Base64.encodeToString(oldWord.getBytes(), Base64.DEFAULT);
    }


    //AES解码，获取扫码时间（对称揭秘）
    public static String aesDecrypt(byte[] srcData, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(key, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(key, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            byte[] original = cipher.doFinal(srcData);
            byte[] bytes = PKCS7UnPadding(original);

            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] PKCS7UnPadding(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    /**
     * RSA算法
     */
    public static final String RSA = "RSA";
/**加密方式,android的*/
// public static final String TRANSFORMATION = "RSA/None/NoPadding";
    /**
     * 加密方式,标准jdk的
     */
    public static final String TRANSFORMATION = "RSA/None/PKCS1Padding";

    //RSA加密
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return cp.doFinal(data);
    }

    //获取文件的md5
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    //判断md5的值是否相等
    public static boolean judgeMd5(String md5, File file) {
        if (md5.equals(getFileMD5(file))) {
            return true;
        }
        return false;
    }
}


