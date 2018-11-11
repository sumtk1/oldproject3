package com.zyd.wlwsdk.utlis;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by hygo00 on 2016/7/6.
 */
public class SharedPreferencesUtlis {

    private static SharedPreferencesUtlis mSharedPreferencesUtlis;
    public static Context mContext;
    private SharedPreferences mPreferences;
    public static String mSPname;

    public static void init(Context context, String SpName) {
        mSPname = SpName;
        mContext = context;
        mSharedPreferencesUtlis = new SharedPreferencesUtlis();
    }

    public static SharedPreferencesUtlis getInstance() {

        if (mSharedPreferencesUtlis == null) {
            mSharedPreferencesUtlis = new SharedPreferencesUtlis();
        }
        return mSharedPreferencesUtlis;
    }

    private SharedPreferencesUtlis() {
        mSharedPreferencesUtlis = this;
        mPreferences = mContext.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }


    public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }


    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(mSPname, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

}
