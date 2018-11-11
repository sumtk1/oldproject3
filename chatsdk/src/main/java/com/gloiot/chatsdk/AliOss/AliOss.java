package com.gloiot.chatsdk.AliOss;

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
import java.util.Random;


/**
 * 阿里云上传图片
 * aliyun-oss-sdk-android-2.0.1
 * okhttp-2.6.0
 * okio-1.6.0
 */
public abstract class AliOss {

    private final String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    private final String accessKeyId = "LTAIfqTWsP5HAPoj";
    private final String accessKeySecret = "i99ENWsdXFjcVgXZsEtWnxIn0f8Ibd";
    private final String BucketName = "tj-hygo-shop";
    private final String aliyunPath1 = "http://oss.zhenxuanzhuangyuan.com/";
    private final String pathName = "chatImagetest"; // 测试
//    private final String pathName = "chatImage";
    private Context mContext;
    private String picName;
    private String picurlpath;


    public AliOss(Context mContext, String picurlpath) {
        this.mContext = mContext;
        this.picurlpath = picurlpath;
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

    public void start() {
        // 上传文件目录名字
        picName = setPicName();
        // 构造上传请求
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
                Log.d("AliOss", "---上传图片成功---" + aliyunPath1 + picName);
                uploadSuccess(aliyunPath1 + picName);
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
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                uploadFailure(request, clientExcepion, serviceException);
            }
        });

        task.waitUntilFinished(); // 等待直到任务完成
    }


    /**
     * 设置上传路径
     *
     * @return
     */
    private String setPicName() {
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String picName = pathName + "/hygoChat" + dateFolder + new Random().nextInt(100) + ".jpg";
        return picName;
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
}
