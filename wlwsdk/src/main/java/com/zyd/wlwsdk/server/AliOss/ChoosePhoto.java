package com.zyd.wlwsdk.server.AliOss;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.zyd.wlwsdk.utlis.BitmapHelper;
import com.zyd.wlwsdk.utlis.BitmapUtils;
import com.zyd.wlwsdk.widge.ActionSheet.ActionSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.PhotoEditActivity;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import top.zibin.luban.Luban;


/**
 * Created by hygo00 on 2016/7/28.
 */
public abstract class ChoosePhoto {

    private Context mContext;
    private ChoosePhotoUtils choosePhotoUtlis;

    private int pics_upload_num, pics_upload_success_num = 0, muti_num; //可上传的图片数量
    private List<Boolean> pics_upload_sf = new ArrayList<>(); //保存当前这张图片有没有上传成功
    private boolean muti;
    private ProgressDialog progressDialog;

    private String aliuyunfiles; //阿里云路径
    // TODO: 2017/7/18    上线修改
//    public static final String filePathPortrait="sj-portrait";   //正式
//    public static final String filePathGoods="sj-goods";
    public static final String filePathPortrait = "sj-test";        //测试
    public static final String filePathGoods = "sj-test";
    private int aspectX;//裁剪比例x
    private int aspectY;//裁剪比例y

    public ChoosePhoto(Context mContext) {
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("图片上传中，请稍候...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    public ChoosePhoto setAspectXY(int aspectX, int aspectY) {
        this.aspectY = aspectY;
        this.aspectX = aspectX;
        return this;
    }

    /**
     * 选择上传单张图片
     * is_touxiang  阿里云上传要求把头像和其他图片分开
     * is_zhengfangxing 上传图片时是否有强制剪裁为正方形
     *
     * @return
     */
    public ChoosePhoto setPic(final boolean is_touxiang, final boolean is_zhnegfangxing) {
        muti = false;
        choosePhotoUtlis = new ChoosePhotoUtils(mContext) {
            @Override
            public void onSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
                setBitmapDegree(resultList.get(0).getPhotoPath());
                Bitmap image1;
                try {
                    long current = System.currentTimeMillis();
                    File file;
                    File picture = new File(resultList.get(0).getPhotoPath());
                    if (picture.length() > 150 * 1024) {
                        file = Luban.with(mContext).load(picture).get();
                    } else {
                        file = picture;
                    }
                    FileInputStream fis = new FileInputStream(file);
                    image1 = BitmapFactory.decodeStream(fis);
                    long current1 = System.currentTimeMillis();
                    Log.e("TAG", "onSuccess: 耗时" + (current1 - current));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "onSuccess压缩出错: " + e.getMessage());
                    image1 = BitmapHelper.compressBitmap(resultList.get(0).getPhotoPath(), 720, 1080);
                }

                final Bitmap image = image1;
                if (image == null) {
                    return;
                }
//                if (null == image) {
//                    setPicFailure();
//                } else {
                final String imgSrc = "file://" + resultList.get(0).getPhotoPath();  //imgsrc 本地路径
//              final String imgSrc = resultList.get(0).getPhotoPath();  //imgsrc 本地路径
                Log.e("单张图片本地路径", imgSrc);
                progressDialog.show();
                //判断是不是头像 是的话传输到头像路径 否则传输到商品路径

                if (is_touxiang) {
                    aliuyunfiles = filePathPortrait;

                } else {
                    aliuyunfiles = filePathGoods;

                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AliOss(mContext, AliOss.setPicName(aliuyunfiles, image.toString()), BitmapUtils.compressImage(image)) {//这里设置上传的地址
                            @Override
                            protected void uploadProgress(long currentSize, long totalSize) {
                                progressDialog.setProgress((int) currentSize);
                                progressDialog.setMax((int) totalSize);
//                                if (currentSize == totalSize) {
//                                }
                            }

                            @Override   //阿里云上传成功
                            public void uploadSuccess(String myPicUrl) {
                                Log.e("阿里云单张上传成功", "imgSrc" + imgSrc + "  myPicUrl" + myPicUrl);
                                progressDialog.dismiss();
                                setPicSuccess(imgSrc, myPicUrl);
                            }

                            @Override   //阿里云上传失败
                            protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                Log.e("阿里云单张上传失败", "request" + request + "  clientExcepion" + clientExcepion + "  serviceException" + serviceException);
                                progressDialog.dismiss();
                                setPicFailure();
                            }
                        }.start();
                    }
                }).start();
            }

            //            }
            @Override  //选择图片失败
            public void onFailure(int requestCode, String errorMsg) {
                Log.e("选择单张图片失败", "requestCode" + requestCode + "  errorMsg" + errorMsg);
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        if (is_zhnegfangxing) {//上传是否要求是正方形，强制裁剪成正方形
            choosePhotoUtlis.setEnableCrop().setForceCrop().setCropSquare(); //强制剪裁正方形
        } else {
            choosePhotoUtlis.setEnableCrop().setForceCrop();//强制剪裁自由图形
        }
        Log.e("222", "setPic: " + aspectX);
        if (aspectX > 0) {
            choosePhotoUtlis.setAspectXY(aspectX, aspectY);
        }

        showActionSheet();
        return this;
    }

    /**
     * 选择上传多张图片
     * is_zhnegfangxing 设置是否都是正方形
     * num 设置最大选择的图片数量
     */
    public ChoosePhoto setPics(final boolean is_zhnegfangxing, int num) {
        final List<String> imgSrcList = new ArrayList<>();  //多张图片本地路径集合
        final List<String> imgUrlList = new ArrayList<>();  //多张图片网络路径集合
        muti = true; //多选
        this.muti_num = num;
        choosePhotoUtlis = new ChoosePhotoUtils(mContext) {
            @Override
            public void onSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
                final List<Bitmap> images = new ArrayList<>();

                for (int i = 0; i < resultList.size(); i++) {
                    setBitmapDegree(resultList.get(i).getPhotoPath());
                    Bitmap image = BitmapHelper.compressBitmap(resultList.get(i).getPhotoPath(), 720, 1080);
                    images.add(image);
                    if (!TextUtils.isEmpty(resultList.get(i).getPhotoPath())) {
                        imgSrcList.add("file:/" + resultList.get(i).getPhotoPath());
                    }
                }

                Log.e("多张图片本地路径", imgSrcList.toString());
                progressDialog.show();
                aliuyunfiles = filePathGoods;


                for (final Bitmap image : images) {
                    if (null == image) {
                        progressDialog.setProgress(0);
                        progressDialog.setMax(0);
                        progressDialog.dismiss();
                        setPicsFailure(imgSrcList, imgUrlList, pics_upload_sf);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new AliOss(mContext, AliOss.setPicName(aliuyunfiles, image.toString()), BitmapUtils.compressImage(image)) {//这里设置上传的地址
                                    @Override
                                    protected void uploadProgress(long currentSize, long totalSize) {
                                        progressDialog.setProgress((int) currentSize);
                                        progressDialog.setMax((int) totalSize);
                                    }

                                    @Override
                                    public void uploadSuccess(String myPicUrl) {
                                        imgUrlList.add(myPicUrl);
                                        pics_upload_num++;
                                        pics_upload_success_num++;
                                        pics_upload_sf.add(true);
                                        Log.e("", myPicUrl);
                                        if (pics_upload_num == images.size()) {  //图片全部执行上传 不一定都成功
                                            progressDialog.dismiss();
                                            if (pics_upload_success_num == images.size()) {  //全部上传成功
                                                Log.e("多张图片全部添加成功 本地路径", imgSrcList.toString());
                                                Log.e("多张图片全部添加成功 网络路径", imgUrlList.toString());
                                                setPicsSuccess(imgSrcList, imgUrlList);
                                            } else {
                                                Log.e("多张图片添加部分失败 本地路径", imgSrcList.toString());
                                                Log.e("多张图片添加部分失败 网络路径", imgUrlList.toString());
                                                Log.e("上传成功失败标识", pics_upload_sf.toString());
                                                setPicsFailure(imgSrcList, imgUrlList, pics_upload_sf);
                                            }
                                        }
                                    }

                                    @Override
                                    protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {

//                                    imgUrlList.add("第" + (imgUrlList.size() - 1) + "上传失败");
                                        pics_upload_num++;
                                        pics_upload_sf.add(false);
                                        if (pics_upload_num == images.size()) {  //图片全部执行长传 不一定都失败
                                            progressDialog.dismiss();
                                            Log.e("多张图片添加部分失败 本地路径", imgSrcList.toString());
                                            Log.e("多张图片添加部分失败 网络路径", imgUrlList.toString());
                                            Log.e("上传成功失败标识", pics_upload_sf.toString());
                                            setPicsFailure(imgSrcList, imgUrlList, pics_upload_sf);
                                        }
                                    }
                                }.start();

                            }

                        }).start();
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorMsg) {
                //TODO
//                imgSrcList.add("第" + (imgSrcList.size() - 1) + "选择失败");
                Log.e("选择多张图片失败", "requestCode" + requestCode + "  errorMsg" + errorMsg);
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
//        if (is_zhnegfangxing) {//上传是否要求是正方形，强制裁剪成正方形
//            choosePhotoUtlis.setEnableEdit().setEnableCrop().setCropSquare(); //强制剪裁正方形
//        }
////        else {
////            choosePhotoUtlis.setEnableCrop().setForceCrop();//强制剪裁自由图形
////        }
        showActionSheet();
        return this;
    }


    /**
     * 选择单张图片上传成功
     *
     * @param myImgSrc 本地地址
     * @param myPicUrl 网络地址
     */
    protected void setPicSuccess(String myImgSrc, String myPicUrl) {
    }

    /**
     * 选择单张图片上传失败
     */
    protected void setPicFailure() {
    }

    /**
     * 选择多张图片上传失败
     *
     * @param picsSrc 本地地址集合
     * @param picsUrl 网络地址集合
     */
    protected void setPicsSuccess(List<String> picsSrc, List<String> picsUrl) {

    }

    /**
     * 选择多张图片部分上传失败
     *
     * @param picsSrc        本地地址集合
     * @param picsUrl        网络地址集合
     * @param pics_upload_sf 标识那些图片上传失败
     */
    protected void setPicsFailure(List<String> picsSrc, List<String> picsUrl, List<Boolean> pics_upload_sf) {

    }

    /**
     * 弹出选择框
     */
    private void showActionSheet() {
        choosePhotoUtlis.setEnableEdit().setEnableCrop();

        ActionSheet.createBuilder(mContext, ((FragmentActivity) mContext).getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0://相册
                                if (muti) {//多选
                                    choosePhotoUtlis.setMutiSelectMax(muti_num).build();
                                    choosePhotoUtlis.setMutiSelect();
                                } else {
                                    choosePhotoUtlis.build();
                                    choosePhotoUtlis.setSingleSelect();
                                }
                                break;
                            case 1://拍照
                                choosePhotoUtlis.build();
                                choosePhotoUtlis.setOpenCamera();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();

    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private void setBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息

            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            Log.e("TAG", "getBitmapDegree: " + degree);
            if (degree != 0) {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
                exifInterface.saveAttributes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择上传多张图片
     * is_zhnegfangxing 设置是否都是正方形
     * num 设置最大选择的图片数量
     */
    public ChoosePhoto setPicsNew(final boolean is_zhnegfangxing, int num) {
        final List<String> imgSrcList = new ArrayList<>();  //多张图片本地路径集合
        final List<String> imgUrlList = new ArrayList<>();  //多张图片网络路径集合
        muti = true; //多选
        this.muti_num = num;
        choosePhotoUtlis = new ChoosePhotoUtils(mContext) {
            @Override
            public void onSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
                final List<Bitmap> images = new ArrayList<>();

                for (int i = 0; i < resultList.size(); i++) {
                    setBitmapDegree(resultList.get(i).getPhotoPath());
                    Bitmap image = BitmapHelper.compressBitmap(resultList.get(i).getPhotoPath(), 720, 1080);
                    images.add(image);
                    if (!TextUtils.isEmpty(resultList.get(i).getPhotoPath())) {
                        imgSrcList.add("file:/" + resultList.get(i).getPhotoPath());
                    }
                }

                Log.e("多张图片本地路径", imgSrcList.toString());
                progressDialog.show();
                aliuyunfiles = "shopTest";
                AliOss_new ossNew = new AliOss_new(mContext);

                for (final Bitmap image : images) {
                    if (null == image) {
                        progressDialog.setProgress(0);
                        progressDialog.setMax(0);
                        progressDialog.dismiss();
                        setPicsFailure(imgSrcList, imgUrlList, pics_upload_sf);
                    } else {
                        String picName = AliOss_new.setPicName(aliuyunfiles, image.toString());
                        final String myPicUrl = "http://zykshop.qqjlb.cn/" + picName;
                        ossNew.start(picName, BitmapUtils.compressImage(image), new OSSProgressCallback<PutObjectRequest>() {
                            @Override
                            public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
                                progressDialog.setProgress((int) l);
                                progressDialog.setMax((int) l1);
                            }
                        }, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                                imgUrlList.add(myPicUrl);
                                pics_upload_num++;
                                pics_upload_success_num++;
                                pics_upload_sf.add(true);
                                Log.e("", myPicUrl);
                                if (pics_upload_num == images.size()) {  //图片全部执行上传 不一定都成功
                                    progressDialog.dismiss();
                                    if (pics_upload_success_num == images.size()) {  //全部上传成功
                                        Log.e("多张图片全部添加成功 本地路径", imgSrcList.toString());
                                        Log.e("多张图片全部添加成功 网络路径", imgUrlList.toString());
                                        setPicsSuccess(imgSrcList, imgUrlList);
                                    } else {
                                        Log.e("多张图片添加部分失败 本地路径", imgSrcList.toString());
                                        Log.e("多张图片添加部分失败 网络路径", imgUrlList.toString());
                                        Log.e("上传成功失败标识", pics_upload_sf.toString());
                                        setPicsFailure(imgSrcList, imgUrlList, pics_upload_sf);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                                pics_upload_num++;
                                pics_upload_sf.add(false);
                                if (pics_upload_num == images.size()) {  //图片全部执行长传 不一定都失败
                                    progressDialog.dismiss();
                                    Log.e("多张图片添加部分失败 本地路径", imgSrcList.toString());
                                    Log.e("多张图片添加部分失败 网络路径", imgUrlList.toString());
                                    Log.e("上传成功失败标识", pics_upload_sf.toString());
                                    setPicsFailure(imgSrcList, imgUrlList, pics_upload_sf);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorMsg) {
                //TODO
                Log.e("选择多张图片失败", "requestCode" + requestCode + "  errorMsg" + errorMsg);
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        };
        showActionSheet();
        return this;
    }
}
