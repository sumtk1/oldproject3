package com.gloiot.hygoSupply.ui.activity.wode.zhifubao;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.wode.shimingrenzheng.ShiMingRenZhengActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.widge.swipe.MySwipe;
import com.zyd.wlwsdk.widge.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WoDeZhiFuBaoActivity extends BaseActivity {
    @Bind(R.id.lv_my_zhifubao)
    ListView lvMyzhifubao;

    private View view;
    private List<ZhiFuBaoBean> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private int deletePosition = -1;//记录删除的位置，初始化为-1

    @Override
    public int initResource() {
        return R.layout.activity_wode_zhifubao;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, "关联支付宝");
        view = View.inflate(mContext, R.layout.item_tianjiayinhangka, null);
        ((TextView) view.findViewById(R.id.tv_tianjia)).setText("添加支付宝账户");
        lvMyzhifubao.addFooterView(view);
        //添加支付宝
        view.findViewById(R.id.item_listcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preferences.getString(ConstantUtils.SP_SHIMINGYANZHENG, "").equals("已认证")) {
                    DialogUtlis.twoBtnNormal(mContext, "请先进行实名认证,一经认证通过，以后只能添加该认证人支付宝", "提示", true, "取消", "去认证",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtlis.dismissDialog();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtlis.dismissDialog();
                                    Intent intent = new Intent(mContext, ShiMingRenZhengActivity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                } else {
                    Intent intent = new Intent(mContext, AddAlipayActivity.class);
                    startActivity(intent);
                }
            }
        });

        commonAdapter = new CommonAdapter<ZhiFuBaoBean>(mContext, R.layout.item_my_zhifubao, list) {
            @Override
            public void convert(ViewHolder holder, final ZhiFuBaoBean zhiFuBaoBean) {
                holder.setText(R.id.tv_alipay_zhanghao, zhiFuBaoBean.zhifubao_zhanghao);
                holder.setText(R.id.tv_alipay_xingming, zhiFuBaoBean.zhifubao_xingming);
//                if (zhiFuBaoBean.zhifubao_zhuangtai) {
//                    holder.setBackgroundRes(R.id.img_alipay, R.mipmap.ic_wode_zhifubao_youxiao);
//                } else {
//                    holder.setBackgroundRes(R.id.img_alipay, R.mipmap.ic_wode_zhifubao_wuxiao);
//                }

                SwipeLayout sl = (SwipeLayout) holder.getConvertView();
                sl.close(false, false);
                sl.getFrontView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                int p = holder.getmPosition();//选中的位置
                Button b2 = holder.getView(R.id.bt_alipay_delete);
                sl.setSwipeListener(MySwipe.mSwipeListener);
                b2.setTag(p);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DialogUtlis.oneBtnPwd(mContext, null, new DialogUtlis.PasswordCallback() {
                            @Override
                            public void callback(String data) {
                                requestHandleArrayList.add(requestAction.shop_aliacc_edit(WoDeZhiFuBaoActivity.this, phone, "del",
                                        zhiFuBaoBean.zhifubao_id, zhiFuBaoBean.zhifubao_zhanghao, zhiFuBaoBean.zhifubao_xingming, data
                                        , ""));
                            }
                        });
                    }
                });
            }
        };
        lvMyzhifubao.setAdapter(commonAdapter);
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        L.e("requestSuccess: " + requestTag, response.toString());
        switch (requestTag) {
            case ConstantUtils.TAG_shop_aliacc_list:
                list.clear();
                commonAdapter.notifyDataSetChanged();

                JSONArray jsonArray = response.getJSONArray("list");
                ZhiFuBaoBean zhiFuBaoBean;
                JSONObject jsonObject;
                for (int i = 0; i < jsonArray.length(); i++) {
                    zhiFuBaoBean = new ZhiFuBaoBean();
                    jsonObject = jsonArray.getJSONObject(i);

                    zhiFuBaoBean.zhifubao_id = JSONUtlis.getString(jsonObject, "id");
                    zhiFuBaoBean.zhifubao_xingming = JSONUtlis.getString(jsonObject, "name");
                    zhiFuBaoBean.zhifubao_zhanghao = JSONUtlis.getString(jsonObject, "account");
                    list.add(zhiFuBaoBean);
                }
                commonAdapter.notifyDataSetChanged();
                break;

            case ConstantUtils.TAG_shop_aliacc_edit:
                //"状态": "成功"
                list.clear();
                commonAdapter.notifyDataSetChanged();
                requestHandleArrayList.add(requestAction.shop_aliacc_list(this, phone));
                break;
            default:
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_aliacc_list(this, phone));
    }

    public class ZhiFuBaoBean {
        private String zhifubao_zhanghao;   //账号
        private String zhifubao_xingming;   //姓名
        private String zhifubao_id;         //id
    }
}
