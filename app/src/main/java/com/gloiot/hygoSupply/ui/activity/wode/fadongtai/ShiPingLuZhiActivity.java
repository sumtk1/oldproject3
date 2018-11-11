package com.gloiot.hygoSupply.ui.activity.wode.fadongtai;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.StringUtil;
import com.gloiot.hygoSupply.utlis.VideoUtils;
import com.zyd.wlwsdk.server.AliOss.AliOss;
import com.zyd.wlwsdk.utlis.BitmapUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ShiPingLuZhiActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback,
        MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener, Camera.AutoFocusCallback {

    @Bind(R.id.im_fazhuangtai_guanbi)
    ImageView imFazhuangtaiGuanbi;
    @Bind(R.id.im_fazhuangtai_qiehuan)
    ImageView imFazhuangtaiQiehuan; //切换摄像头
    @Bind(R.id.tv_fazhuangtai_time)
    TextView tvFazhuangtaiTime;  //时间
    @Bind(R.id.im_fazhuangtai_kaishi)
    ImageView imFazhuangtaiKaishi; //开始和停止
    @Bind(R.id.im_fazhuangtai_queding)
    ImageView imFazhuangtaiQueding; //保存按钮

    SurfaceView surfaceview;
    private SurfaceHolder mSurfaceHolder;
    private ProgressDialog progressDialog;

    String video_url;// 录制的视频路径
    String video_name;


    private Camera mCamera = null;//相机
    //    public PowerManager.WakeLock mWakeLock;
    private int cameraPosition = 0;//0代表前置摄像头，1代表后置摄像头
    private boolean isRecording = false;//标记是否已经在录制
    private MediaRecorder mRecorder;//音视频录制类
    private final static String CLASS_LABEL_ONE = "ShiPingLuZhiActivity";
    int defaultVideoFrameRate = -1;
    private int recordDuration = 60 * 1000;
    // 预览的宽高
    private int previewWidth = 480;
    private int previewHeight = 480;
    private String aliuyunfiles = "video_file";

    @Override
    public int initResource() {
        return R.layout.activity_shipingluzhi;
    }

    @Override
    public void initData() {
        surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        imFazhuangtaiQueding.setVisibility(View.INVISIBLE);
        SurfaceHolder holder = surfaceview.getHolder();// 取得holder
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.setKeepScreenOn(true);
        holder.addCallback(this); // holder加入回调接口
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
//                CLASS_LABEL_ONE);
//        mWakeLock.acquire();
    }

    @OnClick({R.id.im_fazhuangtai_guanbi, R.id.im_fazhuangtai_qiehuan, R.id.im_fazhuangtai_kaishi, R.id.im_fazhuangtai_queding})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_fazhuangtai_guanbi://关闭界面，返回上一层界面
                if (!isRecording) {
                    surfaceview = null;
                    mSurfaceHolder = null;
                    finish();
                }
                break;
            case R.id.im_fazhuangtai_qiehuan://切换前后摄像头
                //切换前后摄像头
                if (isRecording) {
                    return;
                }
                switchCamera();
                break;
            case R.id.im_fazhuangtai_kaishi://开始录制
                if (!isRecording) {
//                    startRecord();
//                    startRecording();
                    getPerMission();
                } else {
                    imFazhuangtaiQueding.setVisibility(View.VISIBLE);
                    imFazhuangtaiQueding.setImageResource(R.mipmap.ic_fazhuangtai_sure_clickable);
                    imFazhuangtaiKaishi.setImageResource(R.mipmap.ic_fazhuangtai_kaishi);
                    mThread.interrupt();
                    Log.e("mThread", "interrupt()");
                    stopRecording();
                }
                break;
            case R.id.im_fazhuangtai_queding://确定保存
                if (!isRecording) {
                    Bitmap bitmapSource = ThumbnailUtils.createVideoThumbnail(video_url, MediaStore.Images.Thumbnails.MINI_KIND);
                    bitmapSource = ThumbnailUtils.extractThumbnail(bitmapSource, previewWidth, previewWidth / 2);
                    final Bitmap bitmap = bitmapSource;
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle("缩略图上传中，请稍候...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new AliOss(mContext, AliOss.setPicName(aliuyunfiles, bitmap.toString()), BitmapUtils.compressImage(bitmap)) {//这里设置上传的地址
                                @Override
                                protected void uploadProgress(long currentSize, long totalSize) {
                                    progressDialog.setProgress((int) currentSize);
                                    progressDialog.setMax((int) totalSize);
//                                if (currentSize == totalSize) {
//                                }
                                }

                                @Override   //阿里云上传成功
                                public void uploadSuccess(String myPicUrl) {
                                    Log.e("阿里云单张上传成功", "  myPicUrl" + myPicUrl);
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(ShiPingLuZhiActivity.this, FabuVideoActivity.class);
                                    intent.putExtra("video_url", video_url);
                                    intent.putExtra("video_name", video_name);
                                    intent.putExtra("video_image", myPicUrl);
                                    startActivityForResult(intent, 1);
                                }

                                @Override   //阿里云上传失败
                                protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                    Log.e("阿里云单张上传失败", "request" + request + "  clientExcepion" + clientExcepion + "  serviceException" + serviceException);
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(ShiPingLuZhiActivity.this, FabuVideoActivity.class);
                                    intent.putExtra("video_url", video_url);
                                    intent.putExtra("video_name", video_name);
                                    intent.putExtra("video_image", "");
                                    startActivityForResult(intent, 1);
                                }
                            }.start();
                        }
                    }).start();

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!isRecording) {
            super.onBackPressed();
            surfaceview = null;
            mSurfaceHolder = null;
        }
    }

    @SuppressLint("NewApi")
    public void switchCamera() {
        if (mCamera == null) {
            return;
        }
        if (Camera.getNumberOfCameras() >= 2) {
            imFazhuangtaiQiehuan.setEnabled(false);
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            switch (cameraPosition) {
                case 0:
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    cameraPosition = 1;
                    break;
                case 1:
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    cameraPosition = 0;
                    break;
            }
            try {
                mCamera.lock();
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(surfaceview.getHolder());
                mCamera.startPreview();
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
            }
            imFazhuangtaiQiehuan.setEnabled(true);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        imFazhuangtaiKaishi.setImageResource(R.mipmap.ic_fazhuangtai_kaishi);
//        imFazhuangtaiQueding.setVisibility(View.INVISIBLE);
        if (mCamera == null) {
            Log.e("onResume", "mCamera is null ", null);
            if (!initCamera()) {
                return;
            }
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                handleSurfaceChanged();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            mCamera.startPreview();
        }
        if (mRecorder == null) {
            Log.e("onResume", "mRecorder is null ", null);
        }
//        if (mWakeLock == null) {
//            // 获取唤醒锁,保持屏幕常亮
//            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
//                    CLASS_LABEL_ONE);
//            mWakeLock.acquire();
//        }
    }

    @Override
    public void onPause() {
        Log.e("onPause", "onPause()", null);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        stopRecording();
        if (mThread != null) {
            mThread.interrupt();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.e("surfaceChanged", "surfaceChanged()", null);
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;
        if (mCamera == null) {
            return;
        }
        try {
            //设置显示
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
            finish();
        }

    }

    @SuppressLint("NewApi")
    private boolean initCamera() {
        try {
            if (cameraPosition == 0) {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            } else {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }

//            camParams.setFocusMode("continuous-video");
//            mCamera.setParameters(camParams);
            mCamera.lock();
            mSurfaceHolder = surfaceview.getHolder();
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            Camera.Parameters parameters = mCamera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();

            //设置对焦模式
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            try {
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);
//            mCamera.autoFocus(this);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @SuppressLint("NewApi")
    private boolean initRecorder() {
//        if(!CommonUtils.isExitsSdcard()){
//            showNoSDCardDialog();
//            return false;
//        }
        if (mCamera == null) {
            if (!initCamera()) {
                return false;
            }
        }
//        mVideoView.setVisibility(View.VISIBLE);
        // TODO init button
        mCamera.stopPreview();
        mRecorder = new MediaRecorder();
        mCamera.unlock();
        mRecorder.setCamera(mCamera);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        // 设置录制视频源为Camera（相机）
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (cameraPosition == 1) {
            mRecorder.setOrientationHint(270);
        } else {
            mRecorder.setOrientationHint(90);
        }
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 设置录制的视频编码h263 h264
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mRecorder.setVideoSize(previewWidth, previewHeight);
        // 设置视频的比特率
        mRecorder.setVideoEncodingBitRate(384 * 1024);
        // // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        if (defaultVideoFrameRate != -1) {
            mRecorder.setVideoFrameRate(defaultVideoFrameRate);
        }

        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "AVideo/");
        if (!file.exists()) {
            boolean ismk = file.mkdirs();
            Log.e("mkdirs", "             mkdirs  ==== " + ismk);
        }
        String path = file.getAbsolutePath();
        Log.e("path", "             path  ==== " + path);
        video_url = path + File.separator + System.currentTimeMillis() + ".mp4"; //
        video_name = System.currentTimeMillis() + "";
        mRecorder.setOutputFile(video_url);
        mRecorder.setMaxDuration(recordDuration);
        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        try {
            mRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean startRecording() {
        if (mRecorder == null) {
            if (!initRecorder())
                return false;
        }
        mRecorder.setOnInfoListener(this);
        mRecorder.setOnErrorListener(this);
        mRecorder.start();
        isRecording = true;
        imFazhuangtaiKaishi.setImageResource(R.mipmap.ic_fazhuangtai_stop);
        imFazhuangtaiQueding.setVisibility(View.GONE);
        imFazhuangtaiKaishi.setClickable(false);
        daojishiRun();
        return true;
    }

    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.setOnErrorListener(null);
            mRecorder.setOnInfoListener(null);
            try {
                mRecorder.stop();
            } catch (IllegalStateException e) {
            }
        }
        releaseRecorder();

        if (mCamera != null) {
            mCamera.stopPreview();
            releaseCamera();
        }
        mThread = null;
        isRecording = false;
    }

    private void releaseRecorder() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    protected void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
        }
    }

    private void handleSurfaceChanged() {
        if (mCamera == null) {
            finish();
            return;
        }
        boolean hasSupportRate = false;
        List<Integer> supportedPreviewFrameRates = mCamera.getParameters()
                .getSupportedPreviewFrameRates();
        if (supportedPreviewFrameRates != null
                && supportedPreviewFrameRates.size() > 0) {
            Collections.sort(supportedPreviewFrameRates);
            for (int i = 0; i < supportedPreviewFrameRates.size(); i++) {
                int supportRate = supportedPreviewFrameRates.get(i);

                if (supportRate == 15) {
                    hasSupportRate = true;
                }

            }
            if (hasSupportRate) {
                defaultVideoFrameRate = 15;
            } else {
                defaultVideoFrameRate = supportedPreviewFrameRates.get(0);
            }

        }
        // 获取摄像头的所有支持的分辨率
        List<Camera.Size> resolutionList = VideoUtils.getResolutionList(mCamera);
        if (resolutionList != null && resolutionList.size() > 0) {
            Collections.sort(resolutionList, new VideoUtils.ResolutionComparator());
            Camera.Size previewSize = null;
            boolean hasSize = false;
            // 如果摄像头支持640*480，那么强制设为640*480
            for (int i = 0; i < resolutionList.size(); i++) {
                Camera.Size size = resolutionList.get(i);
                Log.e(TAG, "handleSurfaceChanged: " + size.width + "------" + size.height);
                if (size != null && size.width == 640 && size.height == 480) {
                    previewSize = size;
                    previewWidth = previewSize.width;
                    previewHeight = previewSize.height;
                    hasSize = true;
                    break;
                }
            }
            // 如果不支持设为中间的那个
            if (!hasSize) {
                int mediumResolution = resolutionList.size() / 2;
                if (mediumResolution >= resolutionList.size())
                    mediumResolution = resolutionList.size() - 1;
                previewSize = resolutionList.get(mediumResolution);
                previewWidth = previewSize.width;
                previewHeight = previewSize.height;

            }
            Log.e(TAG, "handleSurfaceChanged: " + previewWidth + "====" + previewHeight);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        Log.e("surfaceCreated", "surfaceCreated()", null);
        mSurfaceHolder = holder;
        if (mCamera == null) {
            if (!initCamera()) {
                return;
            }
        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            handleSurfaceChanged();
        } catch (Exception e1) {
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("surfaceDestroyed", "surfaceDestroyed()", null);
        // surfaceDestroyed的时候同时对象设置为null
        if (isRecording && mCamera != null) {
            mCamera.lock();
        }
        releaseRecorder();
        releaseCamera();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        stopRecording();
        Toast.makeText(this,
                "Recording error has occurred. Stopping the recording",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            stopRecording();
            finish();
//            if (video_url == null) {
//                return;
//            }
        }
    }


    Thread mThread;
    int i = 60;

    public void daojishiRun() {
        try {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (i = 60; i > 0; i--) {
                        try {
                            Thread.sleep(1000);
                            ShiPingLuZhiActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvFazhuangtaiTime.setText(StringUtil.getInstance().toTime(i));
//                                    Log.e("iiiiiiiiiiiiiiiiii", i + "     top           ");
                                    if (i == 50) {
                                        imFazhuangtaiKaishi.setClickable(true);
//                                        Log.e("iiiiiiiiiiiiiiiiii", i + "     botton           ");
                                        imFazhuangtaiQueding.setVisibility(View.VISIBLE);
                                        imFazhuangtaiQueding.setImageResource(R.mipmap.ic_fazhuangtai_queding);
                                    } else if (i == 0) {
                                        imFazhuangtaiQueding.setVisibility(View.VISIBLE);
                                        imFazhuangtaiQueding.setImageResource(R.mipmap.ic_fazhuangtai_sure_clickable);
                                        imFazhuangtaiKaishi.setImageResource(R.mipmap.ic_fazhuangtai_kaishi);
                                        stopRecording();
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });
            mThread.start();
            Log.e("mThread", "start()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 权限获取
     * 成功回调superPermission（）
     * startRecording（）开始录制视频
     */
    private void getPerMission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(ShiPingLuZhiActivity.this,
                    Manifest.permission.CAMERA);
            int checkCallPhonePermission2 = ContextCompat.checkSelfPermission(ShiPingLuZhiActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int checkCallPhonePermission3 = ContextCompat.checkSelfPermission(ShiPingLuZhiActivity.this,
                    Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission == PackageManager.PERMISSION_GRANTED
                    && checkCallPhonePermission2 == PackageManager.PERMISSION_GRANTED
                    && checkCallPhonePermission3 == PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(ShiPingLuZhiActivity.this,
//                        new String[]{Manifest.permission.CAMERA,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.RECORD_AUDIO},
//                        REQUEST_CODE_ASK_CALL_PHONE);
                startRecording();
                return;
            } else {
                Log.e("权限获取", "getPerMission: 权限获取失败", null);
            }
        }
        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                Log.e("permission", "permission   superPermission()");
                                startRecording();
                            }
                        }, R.string.perm_camera
                , Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
    }


    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            initCamera();//实现相机的参数初始化
            mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
        }
    }
}
