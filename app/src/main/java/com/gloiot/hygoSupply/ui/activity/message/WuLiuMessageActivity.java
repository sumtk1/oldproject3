package com.gloiot.hygoSupply.ui.activity.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.ChakanWuliuActicity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hygo03 on 2017/5/26.
 */

public class WuLiuMessageActivity extends BaseActivity {

    @Bind(R.id.lv_wuliu_message)
    ListView lvWuLiuMessage;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;

    private List<ImMsgBean> list = new ArrayList<>();
    private CommonAdapter commonAdapter;
    int page = 1;

    @Override
    public int initResource() {
        return R.layout.activity_wuliu_message;
    }

    @Override
    public void initData() {

        String type = getIntent().getStringExtra("data");
        CommonUtils.setTitleBar(this, "物流消息");
        if (type.equals("no")) {
            lvWuLiuMessage.setVisibility(View.GONE);
            llNodata.setVisibility(View.VISIBLE);
            tvNodata.setText("暂无物流消息");
        } else {
            lvWuLiuMessage.setVisibility(View.VISIBLE);
            llNodata.setVisibility(View.GONE);
            getData(true);
            BroadcastManager.getInstance(mContext).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // TODO 收到系统通知回调
                    ImMsgBean imMsgBean = (ImMsgBean) intent.getExtras().getSerializable("data");
                    if (imMsgBean.getSendid().equals("002")) {
                        getData(true);
                    }
                }
            });

            lvWuLiuMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String tradeNum = "";
                    try {
                        tradeNum = new JSONObject(list.get(position).getMessage()).getString("tradeNum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(mContext, ChakanWuliuActicity.class);
                    intent.putExtra("id", tradeNum);
                    startActivity(intent);
                }
            });
            lvWuLiuMessage.setOnScrollListener(mScrollFirstItem);
        }
    }

    /**
     * listview的滑动事件监听
     */
    private AbsListView.OnScrollListener mScrollFirstItem = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                //当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    //判断滚动到底部
                    if (lvWuLiuMessage.getLastVisiblePosition() == (list.size() - 1)) {
                        Log.e("----mDatas", list.size() + "--" + (list.size() % 20));
                        if (list.size() != 0 && list.size() % 20 == 0)
                            getData(false);
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

    /**
     * 获取物流消息数据
     *
     * @param isNewest 判断是加载还是刷新
     */
    private void getData(boolean isNewest) {
        try {
            List<ImMsgBean> onceList = new ArrayList<>();
            IMDBManager imdbManager = IMDBManager.getInstance(mContext);
            if (isNewest) {
                list.clear();
                onceList = imdbManager.queryChatMsg("002", 0);
            } else {
                onceList = imdbManager.queryChatMsg("002", page);
            }

            if (onceList.size() == 20) {
                page++;
            }
            list.addAll(onceList);

            if (isNewest) {
                setAdapter();
            } else {
                commonAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        commonAdapter = new CommonAdapter<ImMsgBean>(mContext, R.layout.item_message_wuliu, list) {
            @Override
            public void convert(ViewHolder holder, ImMsgBean systemMessageBean) {
                if (systemMessageBean.getMsgtype().equals("图文_0")) {
                    if (!TextUtils.isEmpty(systemMessageBean.getSendTime())) {
                        holder.setVisible(R.id.tv_message_time, true);
                        holder.setText(R.id.tv_message_time, systemMessageBean.getSendTime());
                    } else {
                        holder.setVisible(R.id.tv_message_time, false);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(systemMessageBean.getMessage());

                        Log.e("jsonObject=", jsonObject.toString());
                        CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_message_icon), jsonObject.getString("img"), 0);

                        TextView tv_message_title = holder.getView(R.id.tv_message_title);
                        switch (jsonObject.getString("title")) {
                            case "订单已签收":
                                tv_message_title.setTextColor(Color.parseColor("#01B175"));
                                break;
                            case "订单已付款":
                                tv_message_title.setTextColor(Color.parseColor("#F34758"));
                                break;
                            case "订单已发货":
                                tv_message_title.setTextColor(Color.parseColor("#4292E4"));
                                break;
                        }
                        holder.setText(R.id.tv_message_title, jsonObject.getString("title"));
                        holder.setText(R.id.tv_message_content, jsonObject.getString("content"));
                        holder.setText(R.id.tv_message_num, "订单编号：\n" + jsonObject.getString("tradeNum"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        lvWuLiuMessage.setAdapter(commonAdapter);
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(mContext).destroy(MessageManager.NEW_MESSAGE);
        super.onDestroy();
    }

}
