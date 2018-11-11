package com.gloiot.chatsdk.imagepicker;

import android.app.Activity;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gloiot.chatsdk.R;
import com.gloiot.chatsdk.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ip_default_image)     //设置占位图片
                .error(R.mipmap.ip_default_image)           //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.ALL);  //缓存全尺寸

        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ip_default_image)     //设置占位图片
                .error(R.mipmap.ip_default_image)           //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.ALL);  //缓存全尺寸
//
//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .apply(options)
//                .into(imageView);

        Log.e("111111111", "000   " + path);
        if (new File(path).exists()) {
            Log.e("111111111", "111");
            Glide.with(activity)
                    .asBitmap() // bitmap转化，如果是gif，则会显示第一帧
                    .load(Uri.fromFile(new File(path)))
                    .apply(options)
                    .into(imageView); // 显示图片
        } else if (path.startsWith("http")){
            Log.e("111111111", "222");
            Glide.with(activity)                             //配置上下文
                    .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .apply(options)
                    .into(imageView);
        }else {
            Log.e("111111111", "333");
            byte[] decode = Base64.decode(path, Base64.DEFAULT);
            Glide.with(activity)
                    .asBitmap() // bitmap转化，如果是gif，则会显示第一帧
                    .load(decode)
                    .apply(options)
                    .into(imageView); // 显示图片
        }
    }

    @Override
    public void clearMemoryCache() {
    }
}
