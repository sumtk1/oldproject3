package com.gloiot.hygoSupply.utlis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.utlis.Constant;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliBean;
import com.gloiot.hygoSupply.bean.DingdanGuanliShangpinBean;
import com.gloiot.hygoSupply.ui.activity.login.ZhifumimaActivity;
import com.gloiot.hygoSupply.ui.activity.message.ConversationActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zyd.wlwsdk.server.network.utlis.AES256;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.SharedPreferencesUtlis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hygo00 on 2016/7/15.
 */
public class CommonUtils {

    private static SharedPreferences preferences = SharedPreferencesUtlis.getInstance().getSharedPreferences();
    private static SharedPreferences.Editor editor = preferences.edit();

    /**
     * 设置标题栏
     *
     * @param context
     * @param titleString
     * @param tvmore
     */
    public static void setTitleBar(final Activity context, boolean isBack, String titleString, String tvmore) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    View view = context.getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    context.finish();
                }
            });
        } else {
            back.setVisibility(View.GONE);
        }

    }

    /**
     * 设置标题栏，可自定义返回事件
     *
     * @param context
     * @param isBack      是否带返回按钮
     * @param titleString 标题
     * @param tvmore
     * @param listener    返回按钮监听
     */

    public static void setTitleBar(final Activity context, boolean isBack, String titleString, String tvmore, View.OnClickListener listener) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(listener);
        } else {
            back.setVisibility(View.GONE);
        }

    }

    /**
     * 设置标题栏，可自定义右侧点击事件
     *
     * @param context
     * @param isBack      是否带返回按钮
     * @param titleString 标题
     * @param tvmore
     * @param listener    返回按钮监听
     */

    public static void setTitleBarWithRightClick(final Activity context, boolean isBack, String titleString, String tvmore, View.OnClickListener listener) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        title.setText(titleString);
        more.setText(tvmore);
        more.setTextSize(14);
        more.setPadding(0,0,15,0);
        more.setOnClickListener(listener);
        if (isBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    View view = context.getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    context.finish();
                }
            });
        } else {
            back.setVisibility(View.GONE);
        }

    }

    /**
     * 设置标题栏，可自定义返回事件,无分割线
     *
     * @param context
     * @param isBack         是否带返回按钮
     * @param titleString    标题
     * @param tvmore
     * @param listener       返回按钮监听
     * @param title_bg_color 标题背景颜色 默认设置 -1
     * @param title_bg_color 标题字体颜色 默认设置 -1
     */
    public static void setTitleBarWuFengeXian(final Activity context
            , boolean isBack, String titleString, String tvmore, int title_bg_color
            , int title_txt_color, View.OnClickListener listener) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        RelativeLayout rl_bg = (RelativeLayout) context.findViewById(R.id.rl_head_wuxian);
        rl_bg.setBackgroundColor(title_bg_color);
        title.setTextColor(title_txt_color);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(listener);
        } else {
            back.setVisibility(View.GONE);
        }
    }

    public static boolean isWXAppInstalledAndSupported(Context context) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(ConstantUtils.WXAPPID);

        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        if (!sIsWXAppInstalledAndSupported) {
            MToast.showToast("未安装微信");
        }

        return sIsWXAppInstalledAndSupported;
    }

    public static void setTitleBarWuFengeXian(final Activity context
            , boolean isBack, String titleString, String tvmore, int title_bg_color
            , View.OnClickListener listener) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        RelativeLayout rl_bg = (RelativeLayout) context.findViewById(R.id.rl_head_wuxian);
        rl_bg.setBackgroundColor(title_bg_color);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(listener);
        } else {
            back.setVisibility(View.GONE);
        }
    }

    public static void setTitleBarWuFengeXian(final Activity context
            , boolean isBack, String titleString, String tvmore
            , View.OnClickListener listener, int title_txt_color) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        RelativeLayout rl_bg = (RelativeLayout) context.findViewById(R.id.rl_head_wuxian);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(listener);
        } else {
            back.setVisibility(View.GONE);
        }
    }

    public static void setTitleBarWuFengeXian(final Activity context
            , boolean isBack, String titleString, String tvmore
            , View.OnClickListener listener) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        RelativeLayout rl_bg = (RelativeLayout) context.findViewById(R.id.rl_head_wuxian);
        title.setText(titleString);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(listener);
        } else {
            back.setVisibility(View.GONE);
        }
    }

    /**
     * 功能 ：设置标题栏右边是图片
     *
     * @param context         页面的context
     * @param titleString     标题名称
     * @param tvmore          右边的文字
     * @param right_img_resid 右边的图片的res资源文件
     */
    public static void setTitleBarAndRightImg(final Activity context, boolean isBack, String titleString, String tvmore, int right_img_resid) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        ImageView right_img = (ImageView) context.findViewById(R.id.tv_toptitle_right_img);
        right_img.setVisibility(View.VISIBLE);
        title.setText(titleString);
        right_img.setImageResource(right_img_resid);
        more.setText(tvmore);
        if (isBack) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    View view = context.getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    context.finish();
                }
            });
            right_img.setOnClickListener((View.OnClickListener) context);
        } else {
            back.setVisibility(View.GONE);
        }
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

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取Top更多
     *
     * @param context
     * @return
     */
    public static TextView getTitleMore(final Activity context) {
        TextView more = (TextView) context.findViewById(R.id.tv_toptitle_right);
        return more;
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

    public static String Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            System.out.println("result: " + buf.toString());//32位的加密
            System.out.println("result: " + buf.toString().substring(8, 24));//16位的加密
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return baos.toByteArray();
    }

    /**
     * Bitmap → byte[]
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[] → Bitmap
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    /**
     * 保存HashMap
     *
     * @param status
     * @param hashMap
     * @return
     */
    public static boolean saveMap(String status, HashMap<String, Object> hashMap) {
        editor.putInt(status, hashMap.size());
        int i = 0;
        for (HashMap.Entry<String, Object> entry : hashMap.entrySet()) {
            editor.remove(status + "_key_" + i);
            editor.putString(status + "_key_" + i, entry.getKey());
            editor.remove(status + "_value_" + i);
            editor.putString(status + "_value_" + i, (String) entry.getValue());
//            Log.e("save", entry.getKey()+"="+entry.getValue());
            i++;
        }
        return editor.commit();
    }

    /**
     * 取出HashMap
     *
     * @param status
     * @return
     */
    public static HashMap<String, Object> loadMap(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int size = preferences.getInt(status, 0);
        for (int i = 0; i < size; i++) {
            String key = preferences.getString(status + "_key_" + i, null);
            String value = preferences.getString(status + "_value_" + i, null);
            hashMap.put(key, value);
        }
        return hashMap;
    }


    /**
     * 设置图片
     *
     * @param imageView
     * @param imgUrl
     */
    public static void setDisplayImageOptions(ImageView imageView, String imgUrl, int round) {

        //使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(Color.argb(0, 0, 0, 0)) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(Color.argb(0, 0, 0, 0)) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(Color.argb(0, 0, 0, 0)) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片
                .build(); // 构建完成
//      ImageLoader.getInstance().displayImage(imgUrl, new ImageViewAware(imageView), options);
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
    }

    /**
     * 设置图片
     *
     * @param imageView
     * @param imgUrl
     */
    public static void setDisplayImageOptions(ImageView imageView, int imgUrl, int round) {

        //使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(imgUrl) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(imgUrl) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(imgUrl) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片
                .build(); // 构建完成
//      ImageLoader.getInstance().displayImage(imgUrl, new ImageViewAware(imageView), options);
        ImageLoader.getInstance().displayImage("", imageView, options);
    }


    /**
     * 设置图片(自定义获取失败显示头像)
     *
     * @param imageView
     * @param imgUrl
     */
    public static void setDisplayImage(ImageView imageView, String imgUrl, int round, int res_load, int res_null, int res_err) {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(res_load) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(res_null) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(res_err) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片
                .build(); // 构建完成

        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
    }

    /**
     * 设置图片(自定义获取失败显示头像)
     *
     * @param imageView
     * @param imgUrl
     */
    public static void setDisplayImage(ImageView imageView, String imgUrl, int round, int resid) {

        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resid) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(resid) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(resid) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片
                .build(); // 构建完成

        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
    }


//    /**
//     * 设置图片(自定义图片加载期间显示)
//     *
//     * @param imageView
//     * @param imgUrl
//     */
//    public static void setDisplayImageOptions(ImageView imageView, String imgUrl, int round, int type) {
//
//        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_launcher) // 设置图片下载期间显示的图片
//                .showImageForEmptyUri(R.mipmap.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.mipmap.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                .displayer(new RoundedBitmapDisplayer(round)) // 设置成圆角图片
//                .build(); // 构建完成
//
//        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
//    }
//


    /**
     * 检测String是否没有中文
     *
     * @param name
     * @return
     */
    public static boolean checkNoChinese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (isChinese(cTemp[i])) {
                res = false;
                return res;
            }

        }
        return res;
    }

//    /**
//     * 检测List<String>是否重复
//     *
//     * @param
//     * @return
//     */
//    public static boolean isDuplication(List<String> data) {
//        boolean res = true;
//        char[] cTemp = name.toCharArray();
//        for (int i = 0; i < name.length(); i++) {
//            if (isChinese(cTemp[i])) {
//                res = false;
//                return res;
//            }
//
//        }
//        return res;
//    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 获得状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 未设置支付密码
     */
    public static void toSetPwd(final Context mContext) {
        DialogUtlis.twoBtnNormal(mContext, "请设置支付密码保障您的账户安全！", "提示", true, "取消", "立即设置",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialog();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialog();
                        Intent intent = new Intent(mContext, ZhifumimaActivity.class);
                        intent.putExtra("forgetpwd", "设置支付密码");
                        mContext.startActivity(intent);
                    }
                });
    }

    /**
     * 根据当前的ListView的列表项计算列表的尺寸
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    /**
     * 被ScrollView包含的GridView高度设置为wrap_content时只显示一行
     * 此方法用于动态计算GridView的高度(根据item的个数)
     */
    public static void reMesureGridViewHeight(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            // 利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);

            // 利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (columns == 0) return;

        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    /**
     * 设置标题栏
     *
     * @param context
     * @param titleString
     */
    public static void setTitleBar(final Activity context, String titleString) {
        ImageView back = (ImageView) context.findViewById(R.id.iv_toptitle_back);
        TextView title = (TextView) context.findViewById(R.id.tv_toptitle_title);
        title.setText(titleString);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View view = context.getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                context.finish();
            }
        });
    }

    public static void setNumPoint(EditText editText, int length) {
        final int DECIMAL_DIGITS = length;
        InputFilter lengthFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // source:当前输入的字符
                // start:输入字符的开始位置
                // end:输入字符的结束位置
                // dest：当前已显示的内容
                // dstart:当前光标开始位置
                // dent:当前光标结束位置
                Log.i("", "source=" + source + ",start=" + start + ",end=" + end
                        + ",dest=" + dest.toString() + ",dstart=" + dstart
                        + ",dend=" + dend);
                if (dest.length() == 0 && source.equals(".")) {
                    return "0.";
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    if (dotValue.length() == DECIMAL_DIGITS) {
                        return "";
                    }
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{lengthFilter});
    }

    /**
     * 跳转发送商品消息界面
     *
     * @param context
     * @param dingdanGuanliBean 某一笔订单的数据
     */
    public static void intentIMActivity(final Activity context, DingdanGuanliBean dingdanGuanliBean) {
        L.e("待发货", "--" + dingdanGuanliBean.get退款人帐号() + "--" + dingdanGuanliBean.get昵称());

        Bundle bundle = new Bundle();

        ArrayList<DingdanGuanliShangpinBean> datas = dingdanGuanliBean.get订单管理商品集合();
        if (datas.size() > 0) { // 订单里的商品集合的长度大于0
            bundle.putString("topType", Constant.CHAT_TOP_TYPE);// 是否有悬浮，""代表没有悬浮，悬浮类别:shangpin = 商品
            bundle.putString("id", datas.get(0).get商品id());
            bundle.putString("icon", datas.get(0).get缩略图());
            bundle.putString("title", datas.get(0).get商品名称());
            bundle.putString("money", datas.get(0).get价格());
            bundle.putBoolean("single", datas.size() > 1 ? false : true); // 订单包含两种商品及以上值为false
            bundle.putString("extra", "");

            bundle.putString("orderId", dingdanGuanliBean.get订单id());
            bundle.putString("orderState", dingdanGuanliBean.get状态());
        }

        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("receiveId", dingdanGuanliBean.get退款人帐号());
        intent.putExtra("name", dingdanGuanliBean.get昵称());
        intent.putExtra("data", bundle);
        context.startActivity(intent);
    }

    // 判断地址是否有问号
    public static String jointUrl(String url) {
        if (url.contains("?")) {
            return url + "&onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" + "&version=" + ConstantUtils.VERSION;
//            return url + "&onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" ;
        } else {
            return url + "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" + "&version=" + ConstantUtils.VERSION;
//            return url + "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" ;
        }
    }
}
