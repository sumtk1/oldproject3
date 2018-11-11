package com.zyd.wlwsdk.server.network.utlis;

/**
 * org.bouncycastle.jar
 */

import android.util.Log;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES256 {

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

    private static Key toKey(byte[] key) throws Exception {
        //实例化DES密钥
        //生成密钥
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }


    public static String bytes2String(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    public static byte[] string2Bytes(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }


    public static String encrypt(String data, String key) {
        try {
            //还原密钥
            Key k = toKey(key.getBytes());
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            //初始化，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, k);
            //执行操作
            return bytes2String(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            Log.e("AES256加密错误", "：" + e);
        }
        return null;
    }


    public static String decrypt(String data, String key) {
        try {
            Key k = toKey(key.getBytes());
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            //初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, k);
            //执行操作
            return new String(cipher.doFinal(string2Bytes(data)));
        } catch (Exception e) {
            Log.e("AES256解密错误", "：" + e);
        }
        return null;
    }

}