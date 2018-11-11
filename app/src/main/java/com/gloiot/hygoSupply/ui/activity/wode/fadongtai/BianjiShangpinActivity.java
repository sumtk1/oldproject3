package com.gloiot.hygoSupply.ui.activity.wode.fadongtai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.ProductModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Ljy on 2017/8/16.
 * 功能：我的——我的资料
 */


public class BianjiShangpinActivity extends BaseActivity {
    @Bind(R.id.tv_dynamic_product_link)
    TextView tvDynamicProductLink;
    @Bind(R.id.et_dynamic_product_title)
    EditText etDynamicProductTitle;
    @Bind(R.id.tv_dynamic_product_titlenum)
    TextView tvDynamicProductTitlenum;
    private String link;
    private String id;
    private String name;
    private String detail;
    private String num;
    private String price;
    private int position = -1;


    @Override
    public int initResource() {
        return R.layout.activity_bianji_shangpin;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "编辑商品", "");
        Intent intent = getIntent();
        if (intent.getIntExtra("position", -1) > -1) {
            detail = intent.getStringExtra("detail");
            link = intent.getStringExtra("url");
            name = intent.getStringExtra("name");
            id = intent.getStringExtra("id");
            position = intent.getIntExtra("position", 0);
            price=intent.getStringExtra("price");
            num=intent.getStringExtra("num");
            tvDynamicProductLink.setText(link);
            etDynamicProductTitle.setText(detail);
            Log.e(TAG, "initData: "+position );
        }
        etDynamicProductTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvDynamicProductTitlenum.setText(charSequence.toString().length() + "/399");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @OnClick({R.id.rl_dynamic_product_link, R.id.but_bianji_baocun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_dynamic_product_link:
                startActivityForResult(new Intent(BianjiShangpinActivity.this, XuanZeiShanagPingLianJIeActivity.class), 0);
                break;
            case R.id.but_bianji_baocun:
                if (TextUtils.isEmpty(link)) {
                    MToastUtils.showToast("请选择商品链接");
                } else if (TextUtils.isEmpty(etDynamicProductTitle.getText().toString())) {
                    MToastUtils.showToast("请添加商品描述");
                } else {
                    detail = etDynamicProductTitle.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("detail", detail);
                    intent.putExtra("url", link);
                    intent.putExtra("name", name);
                    intent.putExtra("id", id);
                    intent.putExtra("position", position);
                    intent.putExtra("num", num);
                    intent.putExtra("price", price);
                    setResult(RESULT_OK, intent);
                    Log.e(TAG, "onViewClicked: "+position );
                    finish();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + resultCode);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    link = data.getStringExtra("url");
                    id = data.getStringExtra("id");
                    name = data.getStringExtra("name");
                    num = data.getStringExtra("num");
                    price = data.getStringExtra("price");
                    tvDynamicProductLink.setText(link);
                }
                break;
        }
    }
}
