package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import butterknife.Bind;

/**
 * 缴纳记录账单详情
 *
 * @author ljz
 */
public class JiaoNaJiLuXiangQingActivity extends BaseActivity {

    @Bind(R.id.tv_jnxq_title)
    TextView tvJnxqTitle;       // 标题
    @Bind(R.id.tv_jnxq_money)
    TextView tvJnxqMoney;       // 金额
    @Bind(R.id.tv_jnxq_shijian)
    TextView tvJnxqShijian;     // 时间
    @Bind(R.id.tv_jnxq_zhanghu)
    TextView tvJnxqZhangHu;     // 缴纳账户

    @Override
    public int initResource() {
        return R.layout.activity_jiaonajiluxiangqing;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "账单详情", "");
        tvJnxqTitle.setText(getIntent().getStringExtra("title"));
        tvJnxqMoney.setText(getIntent().getStringExtra("money"));
        tvJnxqShijian.setText(getIntent().getStringExtra("time"));
        tvJnxqZhangHu.setText(getIntent().getStringExtra("zhanghu"));
        // 根据正负改变字体颜色
        if (getIntent().getStringExtra("money").contains("-")) {
            tvJnxqMoney.setTextColor(getResources().getColor(R.color.cl_29b69d));
        } else {
            tvJnxqMoney.setTextColor(getResources().getColor(R.color.cl_ff6666));
        }
    }

}
