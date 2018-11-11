package com.zyd.wlwsdk.utlis;

import java.io.UnsupportedEncodingException;

/**
 * 创建者 zengming.
 * 功能：Url编码、解码
 */
public class EncodeUtli {
    /**
     * encode编译URL
     *
     * @param kv
     * @return
     */
    public static String encodeUtli(String kv) {
        try {
            kv = java.net.URLEncoder.encode(kv, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return kv;
    }

    /**
     * decode编译URL
     *
     * @param kv
     * @return
     */
    public static String decodeUtli(String kv) {
        try {
            kv = java.net.URLDecoder.decode(kv, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return kv;
    }
}
