package com.gloiot.chatsdk.chatui.keyboard.emoji;


import com.gloiot.chatsdk.chatui.keyboard.data.EmoticonEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ParseDataUtils {

    public static ArrayList<EmoticonEntity> ParseQqData(HashMap<String, Integer> data) {
        Iterator iter = data.entrySet().iterator();
        if(!iter.hasNext()){
            return null;
        }
        ArrayList<EmoticonEntity> emojis = new ArrayList<>();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            EmoticonEntity entity = new EmoticonEntity();
            entity.setContent((String) key);
            entity.setIconUri("" + val);
            emojis.add(entity);
        }
        return emojis;
    }

}
