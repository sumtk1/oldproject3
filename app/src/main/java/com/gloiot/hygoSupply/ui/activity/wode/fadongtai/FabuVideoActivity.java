package com.gloiot.hygoSupply.ui.activity.wode.fadongtai;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.server.AliOss.AliOss;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.Bind;

/***
 * 发布视频（包含文字说明）
 */
public class FabuVideoActivity extends BaseActivity {
    @Bind(R.id.et_fabu_video_neirong)
    EditText et_fabu_video_neirong;
    @Bind(R.id.tv_fabu_video_words)
    TextView tv_fabu_video_words;
    private ProgressDialog progressDialog;

    private String fabu_content; //发布内容
    private String video_url; //视频路径
    private String video_name; //视频名称
    private String video_image; //缩略图
    private String aliuyunfiles = "video_file";


    @Override
    public int initResource() {
        return R.layout.activity_fabu_video;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "发布", "");
        video_url = getIntent().getStringExtra("video_url");
        video_name = getIntent().getStringExtra("video_name");
        video_image = getIntent().getStringExtra("video_image");
        tv_fabu_video_words.setText("0");
        findViewById(R.id.btn_fabu_video_fabu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(fabu_content.trim())) return;
//                requestHandleArrayList.add(requestAction.shop_wl_releasedynamic(FabuVideoActivity.this
//                        , preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), null, "视频", "", "", video_url,fabu_content));
                if (TextUtils.isEmpty(fabu_content)) {
                    MToastUtils.showToast("请填写标题");
                } else {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle("视频上传中，请稍候...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    upload();
                }
            }
        });

        et_fabu_video_neirong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                fabu_content = et_fabu_video_neirong.getText().toString();
                tv_fabu_video_words.setText(fabu_content.length() + "");
            }
        });
    }

    public void upload() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                new AliOss(FabuVideoActivity.this, AliOss.setViedoName(aliuyunfiles, video_name), video_url) {//这里设置上传的地址
                    @Override
                    protected void uploadProgress(long currentSize, long totalSize) {
                        Log.e("视频上传", "currentSize   == " + currentSize);
                        progressDialog.setProgress((int) currentSize);
                        progressDialog.setMax((int) totalSize);

//                                if (currentSize == totalSize) {
//                                }
                    }

                    @Override   //阿里云上传成功
                    public void uploadSuccess(String aliyun_video_url) {
                        progressDialog.dismiss();
                        video_url = aliyun_video_url;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requestHandleArrayList.add(requestAction.shop_wl_releasedynamic(FabuVideoActivity.this
                                        , preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), preferences.getString(ConstantUtils.SP_ONLYID, ""), null, "视频", video_image, fabu_content, video_url, null));
                            }
                        });

//                        Intent intent = new Intent(FabuVideoActivity.this, VideoActivity.class);
//                        intent.putExtra("video_url", video_url);
//                        startActivity(intent);
//                        finish();
                    }

                    @Override   //阿里云上传失败
                    protected void uploadFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        progressDialog.dismiss();
                    }
                }.uploadFile();
            }
        }).start();
    }


    public static byte[] fileTwoBytes(File file) {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1; ) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
//            case ConstantUtils.TAG_shop_wl_releasedynamic:
            case ConstantUtils.TAG_p_add_findTable:
                if ("成功".equals(response.getString("状态"))) {
                    MToast.showToast("发布成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
        }
    }
}
