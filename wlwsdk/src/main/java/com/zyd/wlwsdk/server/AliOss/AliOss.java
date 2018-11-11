package com.zyd.wlwsdk.server.AliOss;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * +
 * aliyun-oss-sdk-android-2.0.1
 * okhttp-2.6.0
 * okio-1.6.0
 */
public abstract class AliOss {

    String myPicUrl;
    private Context mContext;
    private String picName;
    private byte[] picurlpath;


    //    public static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
//    public static final String accessKeyId = "ynjAwXSJsm6tHvbW";
//    public static final String accessKeySecret = "lnrN4oWq90GPEXZnmarHj8HOgfQFVe";
//    public static final String BucketName = "zykshop";
//    //class首的aliyunBucketName更改
//    public static final String aliyunPath1 = "http://zykshop.qqjlb.cn/";
    public static final String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    public static final String accessKeyId = "LTAIfqTWsP5HAPoj";
    public static final String accessKeySecret = "i99ENWsdXFjcVgXZsEtWnxIn0f8Ibd";
    public static final String BucketName = "tj-hygo-shop";
    //class首的aliyunBucketName更改
    public static final String aliyunPath1 = "http://oss.zhenxuanzhuangyuan.com/";

    public AliOss(Context mContext, String picName, byte[] picurlpath) {
        this.mContext = mContext;
        this.picName = picName;
        this.picurlpath = picurlpath;
    }

    private String filepath;

    public AliOss(Context mContext, String picName, String filepath) {
        this.mContext = mContext;
        this.picName = picName;
        this.filepath = filepath;
    }

    public OSS getOss() {
        // 阿里云基础配置
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        return new OSSClient(this.mContext, endpoint, credentialProvider, conf);
    }

    public String start() {
        myPicUrl = aliyunPath1 + picName;

        // 上传文件目录
        PutObjectRequest put = new PutObjectRequest(BucketName, picName, picurlpath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                uploadProgress(currentSize, totalSize);
            }
        });

        OSSAsyncTask task = getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("AliOss", "---上传图片成功");
                uploadSuccess(myPicUrl);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                }
                uploadFailure(request, clientExcepion, serviceException);
            }
        });

        task.waitUntilFinished(); // 等待直到任务完成
        return myPicUrl;
    }

    public String uploadFile() {
        myPicUrl = aliyunPath1 + picName;

        // 上传文件目录
        PutObjectRequest put = new PutObjectRequest(BucketName, picName, filepath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                uploadProgress(currentSize, totalSize);
            }
        });

        OSSAsyncTask task = getOss().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("AliOss", "---上传图片成功");
                uploadSuccess(myPicUrl);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                }
                uploadFailure(request, clientExcepion, serviceException);
            }
        });

        task.waitUntilFinished(); // 等待直到任务完成
        return myPicUrl;
    }

    /**
     * 上传进度
     *
     * @param currentSize
     * @param totalSize
     */
    protected abstract void uploadProgress(long currentSize, long totalSize);

    /**
     * 上传成功
     *
     * @param myPicUrl
     */
    protected abstract void uploadSuccess(String myPicUrl);

    /**
     * 上传失败
     *
     * @param request
     * @param clientExcepion
     * @param serviceException
     */
    protected abstract void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException);

    /**
     * 设置上传路径
     *
     * @param pathName 文件夹名
     * @return
     */
    public static String setPicName(String pathName, String pic) {
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        Log.e("pathName", pathName);
        Log.e("dateFolder", dateFolder);
        String picName = pathName + "/zykshop" + dateFolder + pic + ".jpg";
        return picName;
    }

    /**
     * 设置上传路径
     *
     * @param pathName 文件夹名
     * @return
     */
    public static String setViedoName(String pathName, String pic) {
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        Log.e("pathName", pathName);
        Log.e("dateFolder", dateFolder);
        String picName = pathName + "/zykshop" + dateFolder + pic + ".mp4";
        return picName;
    }

}
