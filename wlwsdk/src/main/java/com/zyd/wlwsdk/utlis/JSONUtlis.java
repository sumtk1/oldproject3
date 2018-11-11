package com.zyd.wlwsdk.utlis;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hygo00 on 17/3/23.
 * JSON解析工具类
 */

public class JSONUtlis {


    public static String getString(JSONObject jsonObject, String key){
        return getString(jsonObject, key, "");
    }

    public static String getString(JSONObject jsonObject, String key, String sDefault){
        try{
            return jsonObject.getString(key);
        }catch (JSONException e){
            return sDefault;
        }
    }

    public static int getInt(JSONObject jsonObject, String key){
        return getInt(jsonObject, key, 0);
    }

    public static int getInt(JSONObject jsonObject, String key, int iDefault){
        try{
            return jsonObject.getInt(key);
        }catch (JSONException e){
            return iDefault;
        }
    }

    public static JSONObject putString(JSONObject jsonObject, String key, String value){
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
