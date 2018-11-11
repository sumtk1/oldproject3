package com.gloiot.hygoSupply.ui.activity.evaluation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.xiaopan.sketch.SketchImageView;

/**
 * 作者：Ljy on 2017/9/2.
 * 功能：我的——我的资料
 */


public class DisplayPictureActivity extends BaseActivity {
    @Bind(R.id.sketch_iv_dislay)
    SketchImageView sketchIvDislay;

    @Override
    public int initResource() {
        return R.layout.activity_display_picture;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "查看图片", "");
        sketchIvDislay.displayImage(getIntent().getStringExtra("url"));
        sketchIvDislay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
