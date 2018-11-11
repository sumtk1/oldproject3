package com.gloiot.hygoSupply.utlis;

/**
 * Created by ZXL on 2017/6/22.
 */

public class StringUtil {
    private static StringUtil mStringUtil = null;

    public static StringUtil getInstance () {
        if (mStringUtil == null) {
            mStringUtil = new StringUtil();
        }
        return mStringUtil;
    }

    public String toTime (int old_time) {
        String new_time = "01:00:00";
        if (old_time == 60) {
            new_time = "01:00:00";
        } else if (old_time <10) {
            new_time = "00:0" + old_time +":00" ;
        } else {
            new_time = "00:" + old_time +":00" ;
        }
        return new_time;
    }

}
