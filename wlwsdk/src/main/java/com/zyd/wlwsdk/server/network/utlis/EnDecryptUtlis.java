package com.zyd.wlwsdk.server.network.utlis;

import android.util.Base64;
import android.util.Log;

/**
 * Created by hygo00 on 2016/8/9.
 * 加解密封装类
 */
public class EnDecryptUtlis {

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String aesEncrypt(String data, String key) {
        try {
            // base64加密
            String base64 = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
            // aes加密
            return AES256.encrypt(base64, key);
        } catch (Exception e) {
            Log.e("base64加密错误", "：" + e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static String aesDecrypt(String data, String key) {
        try {
            // aes解密
            String aes = AES256.decrypt(data, key);
            // base64解密
            return new String(Base64.decode(aes.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            Log.e("base64解密错误", "：" + e);
        }
        return null;
    }
}
