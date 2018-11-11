package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;

public class ChangeShangpinMiaoshuActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_item_shangpin_miaoshu_bianji_miaoshu;
    private ImageView iv_item_shangpin_miaoshu__bianji_pic, iv_toptitle_back;
    private String imageUrl = "";
    private TextView tv_toptitle_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_change_shangpin_miaoshu;
    }

    @Override
    public void initData() {

        et_item_shangpin_miaoshu_bianji_miaoshu = (EditText) findViewById(R.id.et_item_shangpin_miaoshu_bianji_miaoshu);
        iv_item_shangpin_miaoshu__bianji_pic = (ImageView) findViewById(R.id.iv_item_shangpin_miaoshu__bianji_pic);
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().get("type").toString().equals("bianji")) {//编辑
                CommonUtils.setTitleBar(this, true, "编辑", "保存");
                Bundle bundle = getIntent().getExtras();
                String miaoshu = bundle.getString("miaoshu");
                String tupian = bundle.getString("tupian");
                Log.e("bundle", "miaoshu:" + miaoshu + "--" + "tupian:" + tupian);
                et_item_shangpin_miaoshu_bianji_miaoshu.setText(miaoshu);
                if (!"".equals(tupian)) {
                    CommonUtils.setDisplayImageOptions(iv_item_shangpin_miaoshu__bianji_pic, tupian, 4);
                }
                imageUrl = tupian;
            } else if (getIntent().getExtras().get("type").toString().equals("add")) {//添加
                CommonUtils.setTitleBar(this, true, "添加新描述", "保存");
            }
        }
        iv_item_shangpin_miaoshu__bianji_pic.setOnClickListener(this);
        tv_toptitle_right.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_item_shangpin_miaoshu__bianji_pic:
                set_miaoshu_tupian();
                break;
            case R.id.tv_toptitle_right:
                Log.e("保存", "baocun");
                if (checkmiaoshu()) {
                    if (getIntent().getExtras().get("type").toString().equals("bianji")) {
                        Intent intent = new Intent();
                        intent.putExtra("result", "编辑完成");
                        intent.putExtra("newmiaoshu", et_item_shangpin_miaoshu_bianji_miaoshu.getText().toString());
                        intent.putExtra("newimagUrl", imageUrl);
                        setResult(0, intent);
                        finish();
                    } else if (getIntent().getExtras().get("type").toString().equals("add")) {
                        Intent intent = new Intent();
                        intent.putExtra("result", "添加完成");
                        intent.putExtra("newmiaoshu", et_item_shangpin_miaoshu_bianji_miaoshu.getText().toString());
                        intent.putExtra("newimagUrl", imageUrl);
                        setResult(1, intent);
                        finish();
                    }
                    break;
                }
        }
    }

    private void set_miaoshu_tupian() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                new ChoosePhoto(mContext) {
                    @Override
                    protected void setPicSuccess(final String picSrc, final String picUrl) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageUrl = picUrl;
                                CommonUtils.setDisplayImageOptions(iv_item_shangpin_miaoshu__bianji_pic, picUrl, 4);

                            }
                        });
                    }

                    @Override
                    protected void setPicFailure() {
                        //设置头像失败
                        MToast.showToast("图片上传失败,请重新设置");
                    }
                }.setPic(false, false);
            }
        }, R.string.perm_camera, Manifest.permission.CAMERA);
    }


    //判断描述信息
    private boolean checkmiaoshu() {
        if (!imageUrl.equals("")) {
            return true;
        } else {
            DialogUtlis.oneBtnNormal(mContext, "请设置商品上传相关信息");
            return false;
        }
    }
}
