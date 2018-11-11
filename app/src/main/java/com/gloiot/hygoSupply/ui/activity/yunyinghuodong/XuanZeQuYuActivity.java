package com.gloiot.hygoSupply.ui.activity.yunyinghuodong;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zxl on 2017/6/20.
 */

public class XuanZeQuYuActivity extends BaseActivity {
    @Bind(R.id.lv_xzqy_shengshi)
    ListView lv_xzqy_shengshi;
    List<XuanZeQuYuBean> all_list = new ArrayList<XuanZeQuYuBean>();
    List<XuanZeQuYuBean> selected_list = new ArrayList<>();
    CommonAdapter adapter = null;
    String phone;
    @Override
    public int initResource() {
        return R.layout.activity_xuanzequyu;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "选择区域", "");
        setAdapter();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        requestHandleArrayList.add(requestAction.getSheng(this, phone));
        findViewById(R.id.btn_xzqy_baocun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selected_list", (Serializable) selected_list);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_sheng:
                all_list.clear();
                JSONArray array = response.getJSONArray("省列表");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject sheng = array.getJSONObject(i);
                    XuanZeQuYuBean mXuanZeQuYuBean = new XuanZeQuYuBean();
                    mXuanZeQuYuBean.setId(sheng.getString("id"));
                    mXuanZeQuYuBean.setDiming(sheng.getString("省份"));
                    all_list.add(mXuanZeQuYuBean);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }


    public void setAdapter () {
        adapter = new CommonAdapter<XuanZeQuYuBean>(this, R.layout.item_xuanze_quyu, all_list) {
            @Override
            public void convert(ViewHolder holder, final XuanZeQuYuBean xuanZeQuYuBean, final int position) {
                holder.setText(R.id.tv_item_xzqy_diming,xuanZeQuYuBean.getDiming());
                if (xuanZeQuYuBean.is_select()) {
                    holder.setImageResource(R.id.iv_item_xzqy_select, R.mipmap.dianji);
                } else {
                    holder.setImageResource(R.id.iv_item_xzqy_select, R.mipmap.ic_baoyou_wendianji);
                }
                holder.setOnClickListener(R.id.iv_item_xzqy_select, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (xuanZeQuYuBean.is_select()) {
                            xuanZeQuYuBean.setIs_select(false);
                            selected_list.remove(xuanZeQuYuBean);
                        } else {
                            xuanZeQuYuBean.setIs_select(true);
                            selected_list.add(xuanZeQuYuBean);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        lv_xzqy_shengshi.setAdapter(adapter);
    }

}

