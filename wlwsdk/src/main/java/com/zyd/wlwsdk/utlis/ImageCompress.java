package com.zyd.wlwsdk.utlis;

import android.graphics.Bitmap;
import android.util.Base64;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hygo00 on 2017/8/1.
 */

public class ImageCompress {

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 文件转base64字符串
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64;
    }

    /**
     * base64字符串转文件
     *
     * @param base64
     * @return
     */
    public static File base64ToFile(String base64, String pathname) {
        File file = new File(pathname);
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            if (!file.exists())
                file.createNewFile();

            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); // 文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }

    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }


    /**
     * 图片压缩（路径）
     *
     * @param path
     * @param tinyCallback
     * @param Options      Options对象设置举例
     *                     File outputFile = new File(imgUrlPath);
     *                     long fileSize = outputFile.length();
     *                     double scale = Math.sqrt((float) fileSize / 10240);    //按照10KB压缩，计算长款压缩比例
     *                     <p>
     *                     BitmapFactory.Options options = new BitmapFactory.Options();
     *                     options.inJustDecodeBounds = true;
     *                     BitmapFactory.decodeFile(outfile, options);
     *                     int height = options.outHeight;     //原图高度
     *                     int width = options.outWidth;       //原图宽度
     *                     <p>
     *                     final Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
     *                     compressOptions.width = (int) (width / scale);        //目标高度大小
     *                     compressOptions.height = (int) (height / scale);     //目标宽度大小
     *                     compressOptions.size = 10;        //目标尺寸大小  10KB
     *                     compressOptions.config = Bitmap.Config.ARGB_8888;
     */
    public static void Tiny(String path, final TinyCallback tinyCallback, Tiny.FileCompressOptions Options) {
        Runtime runtime = Runtime.getRuntime();
        System.gc();
        runtime.runFinalization();
        System.gc();

        final Tiny.FileCompressOptions compressOptions;
        if (Options == null) {
            compressOptions = new Tiny.FileCompressOptions();
        } else {
            compressOptions = Options;
        }

        Tiny.getInstance().source(path).asFile().withOptions(compressOptions).compress(new FileWithBitmapCallback() {

            @Override
            public void callback(boolean isSuccess, Bitmap bitmap, String outfile) {
                tinyCallback.onCallback(isSuccess, bitmap, outfile);
            }
        });
    }

    public interface TinyCallback {
        void onCallback(boolean isSuccess, Bitmap bitmap, String outfile);
    }
}
