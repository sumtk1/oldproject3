package com.gloiot.hygoSupply.ui.activity.dianpuguanli;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hygo03 on 2016/12/2.
 */

public class RenZhengBiaoZhunXiZeActivity extends BaseActivity {

    @Bind(R.id.lv_renzheng_biaozhun)
    ListView lv_renzheng_biaozhun;
    @Bind(R.id.rl_fg_wushuju)
    RelativeLayout rl_fg_wushuju;
    private String phone;
    private List<String> biaoti_list;
    private CommonAdapter adapter;

    @Override
    public int initResource() {
        return R.layout.activity_renzheng_biaozhun;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "认证标准", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        requestHandleArrayList.add(requestAction.renZhengBiaoZhun(this, phone, ""));

        lv_renzheng_biaozhun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, RenZhengBiaoZhunXiZe_SonActivity.class);
                intent.putExtra("标题", biaoti_list.get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_dianpu_renzheng_xize:
                if (response.getString("状态").equals("成功")) {
                    int num = Integer.parseInt(response.getString("认证标准条数"));
                    biaoti_list = new ArrayList<>();
                    if (num != 0) {
                        lv_renzheng_biaozhun.setVisibility(View.VISIBLE);
                        rl_fg_wushuju.setVisibility(View.GONE);
                        JSONArray jsonArray = response.getJSONArray("认证标准列表");
                        for (int i = 0; i < num; i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Log.e("0909", jsonObject.getString("标题"));
                            biaoti_list.add(jsonObject.getString("标题"));
                        }
                    } else {
                        rl_fg_wushuju.setVisibility(View.VISIBLE);
                        lv_renzheng_biaozhun.setVisibility(View.GONE);
                    }

                    adapter = new CommonAdapter<String>(mContext, R.layout.item_renzheng_biaozhun, biaoti_list) {
                        @Override
                        public void convert(ViewHolder holder, String s) {
                            holder.setText(R.id.tv_item_biaozhun, s);
                        }
                    };
                    lv_renzheng_biaozhun.setAdapter(adapter);


                }
                break;
        }
    }

}
