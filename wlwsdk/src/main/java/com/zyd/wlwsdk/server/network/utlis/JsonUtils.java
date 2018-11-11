package com.zyd.wlwsdk.server.network.utlis;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;


/**
 * Created by hygo00 on 2016/3/3.
 */
public class JsonUtils {

    /**
     * hashMap转JSON
     *
     * @param hashMap
     * @return
     */
    public static JSONStringer createJSON(HashMap<String, Object> hashMap) {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            for (HashMap.Entry<String, Object> entry : hashMap.entrySet()) {
                if (null != entry && null != entry.getKey()) {
                    jsonText.key(entry.getKey());   // 获取key
                    String value;
                    if (null != entry.getValue()){
                        value = entry.getValue().toString();
                        jsonText.value(value.replaceAll("'", "’")); // value不为null 英文引号替换成中文引号
                    } else {
                        jsonText.value("");
                    }
                }
            }
            jsonText.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    /**
     * hashMap转JSONObject
     *
     * @param hashMap
     * @return
     */
    public static JSONObject createJSONObject(HashMap<String, Object> hashMap) {
        JSONObject jsonObject = new JSONObject();
        try {
            for (HashMap.Entry<String, Object> entry : hashMap.entrySet()) {
                if (null != entry) {
                    jsonObject.put(entry.getKey(), entry.getValue().toString().replaceAll("'", "’"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
