package com.gloiot.hygoSupply.utlis;

import android.content.Context;
import android.content.SharedPreferences;

import com.gloiot.hygoSupply.ui.App;


/**
 * Created by hygo00 on 2016/7/6.
 */
public class SharedPreferencesUtils {

    private static SharedPreferencesUtils mSharedPreferencesUtils;
    public static Context mContext;
    private SharedPreferences mPreferences;

    public static void init(Context context) {
        mContext = context;
        mSharedPreferencesUtils = new SharedPreferencesUtils();
    }

    public static SharedPreferencesUtils getInstance() {
        if (mSharedPreferencesUtils == null) {
            mSharedPreferencesUtils = new SharedPreferencesUtils();
        }
        return mSharedPreferencesUtils;
    }

    private SharedPreferencesUtils() {
        mSharedPreferencesUtils = this;
        if (mContext == null) {
            mContext = App.mContext;
        }
        mPreferences = mContext.getSharedPreferences(ConstantUtils.MYSP, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

}
