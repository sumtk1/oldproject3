package com.gloiot.hygoSupply.ui.activity.wode.kefuzhongxin;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.message.ConversationActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.widget.LineGridView;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 创建者 zengming.
 * 功能：选择客服
 */
public class SelectKeFuActivity extends BaseActivity {

    @Bind(R.id.gv_kefu_select)
    LineGridView gvKefuSelect;

    private CommonAdapter gvAdapter;
    private List<KeFuBean> mData = new ArrayList<>();
    private String cityID, cityName, systemKeFu;

    @Override
    public int initResource() {
        return R.layout.activity_kefu_select;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "请选择客服", "");

        Intent intent = getIntent();
        try {
            cityID = intent.getStringExtra("cityID");
//            cityName = intent.getStringExtra("cityName");
            systemKeFu = intent.getStringExtra("system");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setAdapter();
        requestHandleArrayList.add(requestAction.SelectKeFu(this, preferences.getString(ConstantUtils.SP_MYID, ""), cityID, cityName, systemKeFu));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("SelectKeFuActivity：" + requestTag, response.toString());
        switch (requestTag) {
            case ConstantUtils.TAG_KEHU_SELECT:
                mData.clear();
                JSONArray jsonArray = response.getJSONArray("数据");
                JSONObject jsonObject;
                KeFuBean keFuBean;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    keFuBean = new KeFuBean();
                    keFuBean.setNickName(jsonObject.getString("昵称"));
                    keFuBean.setZhangHao(jsonObject.getString("账号"));
                    keFuBean.setXingming(jsonObject.getString("姓名"));
                    mData.add(keFuBean);
                }
                gvAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void setAdapter() {
        gvAdapter = new CommonAdapter<KeFuBean>(this, R.layout.item_kefu_select, mData) {
            @Override
            public void convert(ViewHolder holder, KeFuBean keFuBean) {
                holder.setText(R.id.tv_kefu_select, keFuBean.getXingming());
            }
        };
        gvKefuSelect.setNumColumns(2);
        gvKefuSelect.setAdapter(gvAdapter);
        gvKefuSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.e(TAG, "position " + position + "--- " + mData.get(position) + "---id " + id);
                Intent intent = new Intent(new Intent(mContext, ConversationActivity.class));
                intent.putExtra("receiveId", mData.get(position).getZhangHao());
                intent.putExtra("name", mData.get(position).getNickName());
                startActivity(intent);
            }
        });
    }

    class KeFuBean {
        private String zhangHao;
        private String nickName;
        private String xingming;

        public String getZhangHao() {
            return zhangHao;
        }

        public void setZhangHao(String zhangHao) {
            this.zhangHao = zhangHao;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getXingming() {
            return xingming;
        }

        public void setXingming(String xingming) {
            this.xingming = xingming;
        }
    }
}
