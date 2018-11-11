package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.message.ConversationActivity;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.widget.LineGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能：请选择客服
 * Created by ZXL on 2017/7/26.
 */

public class QingxuanzeKefuActivity extends BaseActivity {
    @Bind(R.id.gv_qingxuanzekefu_kefu_team)
    LineGridView gvKefuTeam;
    @Bind(R.id.ll_kefu_unavailable)
    LinearLayout llKefuUnavailable;
    private String phone;
    private List<UserInfo> kefuBeanList = new ArrayList<>();
    private CommonAdapter adapter;

    @Override
    public int initResource() {
        return R.layout.activity_qingxuanze_kefu;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "请选择客服", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        if (!getIntent().getBooleanExtra("isAvailable", false)) {
            llKefuUnavailable.setVisibility(View.VISIBLE);
        }
        adapter = new CommonAdapter<UserInfo>(this, R.layout.item_kefu, kefuBeanList) {
            @Override
            public void convert(ViewHolder holder, UserInfo kefuBean, final int position) {
                holder.setText(R.id.tv_item_kefu_team_nickname, kefuBean.getRealName());
                holder.setOnClickListener(R.id.iv_item_kefu_team_headimg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(mContext, ConversationActivity.class));
                        intent.putExtra("receiveId", kefuBeanList.get(position).getId());
                        intent.putExtra("name", kefuBeanList.get(position).getName());
                        startActivity(intent);
                    }
                });
            }
        };
        gvKefuTeam.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.sel_shop_kefu(QingxuanzeKefuActivity.this, phone));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        kefuBeanList.clear();
        JSONArray array = response.getJSONArray("数据");
        if (array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject kefu = array.getJSONObject(i);
                UserInfo bean = new UserInfo();
                bean.setId(kefu.getString("账号"));
                bean.setName(kefu.getString("昵称"));
                bean.setRealName(kefu.getString("姓名"));
                Uri uri = Uri.parse("R.mipmap.ic_kefu");
                bean.setUrl("客服");
                kefuBeanList.add(bean);
                UserInfoCache.getInstance(mContext).putData(bean.getId(), bean);//存入缓存
                IMDBManager.getInstance(mContext, preferences.getString(ConstantUtils.SP_MYID, "")).insertUserInfo(bean);


            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
