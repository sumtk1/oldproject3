package com.gloiot.hygoSupply.ui.activity.wode.fadongtai;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.shangpinshangchuan.XuanzeShangPinLeixingAvtivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;

import butterknife.Bind;
import butterknife.OnClick;

public class TianJiaNeiRongActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_tianjia_neirong)
    EditText etTianjiaNeirong;
    @Bind(R.id.tv_tianjia_num)
    TextView tvTianjiaNum;
    @Bind(R.id.tv_tianjia_sum)
    TextView tvTianjiaSum;
    @Bind(R.id.im_tianjia_zhanshitupian)
    ImageView imTianjiaZhanshitupian;
    @Bind(R.id.im_tianjia_zhanshi)
    ImageView imTianjiaZhanshi;
    @Bind(R.id.tv_tianjianeirongtupian)
    TextView tvTianjianeirongtupian;
    @Bind(R.id.tv_shangping_lianjie)
    TextView tv_shangping_lianjie;
    @Bind(R.id.rl_post_connect)
    RelativeLayout rl_post_connect;
    @Bind(R.id.rl_tianjianeirong_01)
    RelativeLayout rl_tianjianeirong_01;
    private CharSequence tt;
    private int selectionStart;
    private int selectionEnd;
    private int num = 399;
    private String icon = "";
    private String addcontent;
    private int position;

    @Override
    public int initResource() {
        return R.layout.activity_tianjianeirong;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        addcontent = intent.getStringExtra("content");
        position = intent.getIntExtra("position", 0);
        CommonUtils.setTitleBar((Activity) mContext, true, addcontent, "");
        if ("添加内容".equals(addcontent)) {
            etTianjiaNeirong.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    tt = charSequence;
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    tvTianjiaNum.setText(editable.length() + "");
                    selectionStart = etTianjiaNeirong.getSelectionStart();
                    selectionEnd = etTianjiaNeirong.getSelectionEnd();
                    if (tt.length() > num) {
                        editable.delete(selectionStart - 1, selectionEnd);
                        int tempSelection = selectionEnd;
                        etTianjiaNeirong.setText(editable);
                        etTianjiaNeirong.setSelection(tempSelection);//设置光标在最后
                    }
                }
            });
            if (position > 0) {
                CommonUtils.setTitleBar((Activity) mContext, true, "编辑内容", "");
                etTianjiaNeirong.setText(intent.getStringExtra("describe"));
                icon = intent.getStringExtra("image");
                CommonUtils.setDisplayImageOptions(imTianjiaZhanshitupian, icon, 0);
                tvTianjianeirongtupian.setVisibility(View.GONE);
                imTianjiaZhanshi.setVisibility(View.GONE);
            }
        } else {
            rl_post_connect.setVisibility(View.VISIBLE);
            rl_tianjianeirong_01.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.im_tianjia_zhanshi, R.id.but_bianji_baocun, R.id.rl_post_connect,R.id.im_tianjia_zhanshitupian})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_tianjia_zhanshi:
            case R.id.im_tianjia_zhanshitupian:
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                new ChoosePhoto(mContext) {
                                    @Override
                                    protected void setPicSuccess(final String myImgSrc, final String myPicUrl) {
                                        TianJiaNeiRongActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                CommonUtils.setDisplayImageOptions(imTianjiaZhanshitupian, myPicUrl, 0);
                                                icon = myPicUrl;
                                                tvTianjianeirongtupian.setVisibility(View.GONE);
                                                imTianjiaZhanshi.setVisibility(View.GONE);
                                            }
                                        });
                                    }

                                    @Override
                                    protected void setPicFailure() {
                                        //设置图片失败
                                        MToast.showToast("图片上传失败,请重新设置");
                                    }
                                }.setPic(false, false); //是否是头像

                            }
                        }, R.string.perm_camera, Manifest.permission.CAMERA);
                    }
                }, R.string.perm_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.but_bianji_baocun:
                if ("添加内容".equals(addcontent)) {
                    if (!"商品".equals(getIntent().getStringExtra("type"))) {
                        if (TextUtils.isEmpty(etTianjiaNeirong.getText().toString())) {
                            MToast.showToast("描述不能为空");
                        } else if (TextUtils.isEmpty(icon)) {
                            MToast.showToast("图片不能空");
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("neirong", etTianjiaNeirong.getText().toString());
                            intent.putExtra("zhaopian", icon);
                            intent.putExtra("position", position);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        if (!TextUtils.isEmpty(etTianjiaNeirong.getText().toString()) || !TextUtils.isEmpty(icon)) {
                            Intent intent = new Intent();
                            intent.putExtra("neirong", etTianjiaNeirong.getText().toString());
                            intent.putExtra("zhaopian", icon);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            MToastUtils.showToast("请添加内容");
                        }
                    }

                } else {

                }
                break;
            case R.id.rl_post_connect:
                Intent intent = new Intent(TianJiaNeiRongActivity.this, XuanZeiShanagPingLianJIeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
