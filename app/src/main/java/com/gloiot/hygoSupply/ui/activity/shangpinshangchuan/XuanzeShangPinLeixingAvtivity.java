package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

/**
 * 作者：Ljy on 2016/11/25.
 * 邮箱：enjoy_azad@sina.com
 */

public class XuanzeShangPinLeixingAvtivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_shangpinleixing_shiwu;
    private ImageView iv_shangpinleixing_dianzi;
    //FrameLayout多此一举,待删
    private FrameLayout fl_shangpinleixing_shiwu;
    private FrameLayout fl_shangpinleixing_dianzi;
    //判断是否是实物商品
    private boolean isShiwu = true;
    private String shiwu = "*实物商品：买家下单后卖家需要选择物流公司并安排发货。";
    private String kajuan = "*电子卡卷：买家下单后获取订单的电子凭证，卖家无需发货。";
    private TextView tv_shangpinleixing_shiwumiaoshu;
    private TextView tv_shangpinleixing_dianzimiaoshu;

    @Override
    public int initResource() {
        return R.layout.activity_shangpin_leixing;
    }

    @Override
    public void initData() {
        initComponent();
        CommonUtils.setTitleBar((Activity) mContext, true, "商品类型", "保存");
        CommonUtils.getTitleMore((Activity) mContext).setOnClickListener(this);

        fl_shangpinleixing_shiwu.setOnClickListener(this);
        fl_shangpinleixing_dianzi.setOnClickListener(this);
        SpannableStringBuilder sp1 = new SpannableStringBuilder(shiwu);
        sp1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_red)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_shangpinleixing_shiwumiaoshu.setText(sp1);
        SpannableStringBuilder sp2 = new SpannableStringBuilder(kajuan);
        sp2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cl_red)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_shangpinleixing_dianzimiaoshu.setText(sp2);
        if (getIntent().getStringExtra("类型").equals("电子卡卷")) {
            isShiwu = false;
            iv_shangpinleixing_shiwu.setVisibility(View.INVISIBLE);
            iv_shangpinleixing_dianzi.setVisibility(View.VISIBLE);
        } else {
            iv_shangpinleixing_shiwu.setVisibility(View.VISIBLE);
            iv_shangpinleixing_dianzi.setVisibility(View.INVISIBLE);
            isShiwu = true;
        }
    }

    public void initComponent() {
        iv_shangpinleixing_dianzi = (ImageView) findViewById(R.id.iv_shangpinleixing_dianzi);
        iv_shangpinleixing_shiwu = (ImageView) findViewById(R.id.iv_shangpinleixing_shiwu);
        fl_shangpinleixing_dianzi = (FrameLayout) findViewById(R.id.fl_shangpinleixing_dianzi);
        fl_shangpinleixing_shiwu = (FrameLayout) findViewById(R.id.fl_shangpinleixing_shiwu);
        tv_shangpinleixing_shiwumiaoshu = (TextView) findViewById(R.id.tv_shangpinleixing_shiwumiaoshu);
        tv_shangpinleixing_dianzimiaoshu = (TextView) findViewById(R.id.tv_shangpinleixing_dianzimiaoshu);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_shangpinleixing_shiwu:
            case R.id.iv_shangpinleixing_shiwu:
                isShiwu = true;
                iv_shangpinleixing_shiwu.setVisibility(View.VISIBLE);
                iv_shangpinleixing_dianzi.setVisibility(View.INVISIBLE);
                break;
            case R.id.fl_shangpinleixing_dianzi:
            case R.id.iv_shangpinleixing_dianzi:
                isShiwu = false;
                iv_shangpinleixing_shiwu.setVisibility(View.INVISIBLE);
                iv_shangpinleixing_dianzi.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_toptitle_right:
                Intent intent = new Intent();
                intent.putExtra("isShiwu", isShiwu);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
