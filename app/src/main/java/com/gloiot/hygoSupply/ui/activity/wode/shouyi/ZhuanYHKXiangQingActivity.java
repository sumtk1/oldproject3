package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.JSONUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hygo03 on 2017/11/15.
 */

public class ZhuanYHKXiangQingActivity extends BaseActivity {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.lv_content)
    ListView lvContent;
    @Bind(R.id.tv_jiaoyi_type)
    TextView tvJiaoyiType;

    @Override
    public int initResource() {
        return R.layout.activity_zdxiangqing;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, "账单详情");
        String id = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        if ("商城收益提现银行卡".equals(getIntent().getStringExtra("type"))) {
            requestHandleArrayList.add(requestAction.shop_billNotice_goBank_xq(ZhuanYHKXiangQingActivity.this, phone, id, "提现到银行卡"));
        } else if ("商城收益提现支付宝".equals(getIntent().getStringExtra("type"))) {
            requestHandleArrayList.add(requestAction.shop_wl_billDetail(ZhuanYHKXiangQingActivity.this, phone, id, title));
        } else {
            requestHandleArrayList.add(requestAction.shop_gain_recording_bank_xq(ZhuanYHKXiangQingActivity.this, phone, id, "提现到银行卡"));
        }


    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        ArrayList<String[]> list = new ArrayList<>();

        switch (requestTag) {
            case ConstantUtils.TAG_shop_gain_recording_bank_xq:
            case ConstantUtils.TAG_shop_billNotice_goBank_xq:
                tvTitle.setText("提现到银行卡");
                tvMoney.setText(JSONUtlis.getString(response, "money"));
                switch (JSONUtlis.getString(response, "审批状态")) {
                    case "已成功":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_sucess);
                        break;
                    case "审核中":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_shenghe);
                        break;
                    case "提现失败已退回":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_fail);
                        break;
                    default:
                        tvJiaoyiType.setVisibility(View.GONE);
                        break;
                }

                list.add(new String[]{"银行卡名称", JSONUtlis.getString(response, "bank")});
                list.add(new String[]{"银行账户", JSONUtlis.getString(response, "cardNumber")});
                list.add(new String[]{"时间", JSONUtlis.getString(response, "time")});
                lvContent.setDividerHeight(0);
                lvContent.setAdapter(new CommonAdapter<String[]>(this, R.layout.item_zdxiangqing, list){
                    @Override
                    public void convert(ViewHolder holder, String[] strings) {
                        holder.setText(R.id.tv_item_title, strings[0]);
                        holder.setText(R.id.tv_item_content, strings[1]);
                    }
                });
                break;
            case ConstantUtils.TAG_shop_wl_billDetail:
                tvTitle.setText(JSONUtlis.getString(response, "title"));
                tvMoney.setText(JSONUtlis.getString(response, "金额"));
                switch (JSONUtlis.getString(response, "审批状态")) {
                    case "已成功":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_sucess);
                        break;
                    case "审核中":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_shenghe);
                        break;
                    case "提现失败已退回":
                        tvJiaoyiType.setBackgroundResource(R.mipmap.shouyi_fail);
                        break;
                    default:
                        tvJiaoyiType.setVisibility(View.GONE);
                        break;
                }

                JSONObject data = new JSONObject(JSONUtlis.getString(response, "数据"));
                list.add(new String[]{"说明", JSONUtlis.getString(data, "说明")});
                list.add(new String[]{"支付方式", JSONUtlis.getString(data, "支付方式")});
                list.add(new String[]{"手续费", JSONUtlis.getString(data, "手续费")});
                list.add(new String[]{"备注", JSONUtlis.getString(data, "备注")});
                list.add(new String[]{"时间", JSONUtlis.getString(data, "时间")});
                lvContent.setDividerHeight(0);
                lvContent.setAdapter(new CommonAdapter<String[]>(this, R.layout.item_zdxiangqing, list){
                    @Override
                    public void convert(ViewHolder holder, String[] strings) {
                        holder.setText(R.id.tv_item_title, strings[0]);
                        holder.setText(R.id.tv_item_content, strings[1]);
                    }
                });
                break;
            default:
        }

    }
}
