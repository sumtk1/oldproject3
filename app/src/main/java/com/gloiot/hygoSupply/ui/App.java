package com.gloiot.hygoSupply.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.gloiot.chatsdk.SocketEvent;
import com.gloiot.chatsdk.socket.LinkServer;
import com.gloiot.chatsdk.utlis.ChatNotification;
import com.gloiot.hygoSupply.ui.activity.MainActivity;
import com.gloiot.hygoSupply.ui.activity.message.ChatEvent;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.zyd.wlwsdk.autolayout.config.AutoLayoutConifg;
import com.zyd.wlwsdk.server.network.HttpManager;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.NoDoubleClickUtils;
import com.zyd.wlwsdk.utlis.SharedPreferencesUtlis;
import com.zyd.wlwsdk.utlis.xmlanalsis.ProvinceAreaHelper;

import java.io.File;
import java.util.Map;


/**
 * App
 */
public class App extends MultiDexApplication {


    public static Context mContext;
    public ActivityStack mActivityStack = null; // Activity 栈
    private static App instance;
    int mCount = 0;
    public static Map<String, Long> timeMap;

    /**
     * 分割 Dex 支持
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 日记调试打印
         * 测试 true，正式 false。
         */
        L.isDebug = true;

//        /**
//         * Bugly初始化
//         * 测试 true，正式 false。
//         */
//        CrashReport.initCrashReport(getApplicationContext(),ConstantUtils.BUGLY_KEY, true);
        Bugly.init(getApplicationContext(), "d74b10c5dd", false);
        instance = this;
        mContext = getApplicationContext();
        mActivityStack = new ActivityStack();   // 初始化Activity 栈
        NoDoubleClickUtils.initLastClickTime();
        initImageLoader(getApplicationContext());
        SharedPreferencesUtlis.init(this, ConstantUtils.MYSP);
        // 网络请求
        HttpManager.init(ConstantUtils.URL);
        // 自动适配
        AutoLayoutConifg.getInstance().useDeviceSize();
        // 初始化toast
        MToast.init(this);
        // 读取本地省市文件，存储
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProvinceAreaHelper.getInstance(getApplicationContext());
            }
        }).start();

        // 连接消息服务器
        LinkServer.init();
        SocketEvent.init(this);
        ChatEvent.init(this);

        ChatNotification.init(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chatSelectFlag", 4);
        ChatNotification.getInstance().setIntent(intent);

    }


    public synchronized static App getInstance() {
        return instance;
    }


    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningAppProcesses() != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return null;
    }


    /**
     * 缓存图片
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "qqwlw/ImageLoader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }
}
