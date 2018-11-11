package com.gloiot.hygoSupply.ui.activity.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.web.WebActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemCommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemTypeSupport;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 创建者 zengming.
 * 功能：系统通知展示
 */
public class SystemMessageActivity extends BaseActivity {

    @Bind(R.id.lv_system_message)
    ListView lvSystemMessage;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.ll_nodata)
    LinearLayout llNodata;

    private List<ImMsgBean> list = new ArrayList<>();
    int page = 1;

    private SystemMsgAdapter mSystemMsgAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_system_message;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, "系统消息");
        //判断有无数据
        String type = getIntent().getStringExtra("data");
        if (type.equals("no")) {
            lvSystemMessage.setVisibility(View.GONE);
            llNodata.setVisibility(View.VISIBLE);
            tvNodata.setText("暂无通知消息");
        } else {
            lvSystemMessage.setVisibility(View.VISIBLE);
            llNodata.setVisibility(View.GONE);
            getData(true);
            BroadcastManager.getInstance(mContext).addAction(MessageManager.NEW_MESSAGE, new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // 收到系统通知回调
                    ImMsgBean imMsgBean = (ImMsgBean) intent.getExtras().getSerializable("data");
                    if (imMsgBean.getSendid().equals("001")) {
                        getData(true);
                    }
                }
            });

            lvSystemMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if ("图文_0".equals(list.get(position).getMsgtype())) {
                        String url = "";
                        try {
                            url = new JSONObject(list.get(position).getMessage()).getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    } else {
                        return;
                    }
                }
            });

            lvSystemMessage.setOnScrollListener(mScrollFirstItem);
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
                    if (lvSystemMessage.getLastVisiblePosition() == (list.size() - 1)) {
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
     * 获取系统消息数据
     *
     * @param isNewest 判断是加载还是刷新
     */
    private void getData(boolean isNewest) {
        try {
            List<ImMsgBean> onceList;
            IMDBManager imdbManager = IMDBManager.getInstance(mContext);
            if (isNewest) {
                list.clear();
                onceList = imdbManager.queryChatMsg("001", 0);
            } else {
                onceList = imdbManager.queryChatMsg("001", list.size());
            }
            if (onceList.size() == 20) {
                page++;
            }
            list.addAll(onceList);

            if (isNewest) {
                initAdapter();
            } else {
                mSystemMsgAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("系统消息", "-" + e);
        }
    }

    private void initAdapter() {
        mSystemMsgAdapter = new SystemMsgAdapter(mContext, list);
        lvSystemMessage.setAdapter(mSystemMsgAdapter);
        Log.e("适配器", "用的是新的适配器，马辉");
    }

    private class SystemMsgAdapter extends MultiItemCommonAdapter<ImMsgBean> {

        public SystemMsgAdapter(Context context, List<ImMsgBean> datas) {
            super(context, datas, new MultiItemTypeSupport<ImMsgBean>() {
                @Override
                public int getLayoutId(int position, ImMsgBean imMsgBean) {
                    switch (imMsgBean.getMsgtype()) {
                        case "文本_0":
                            return R.layout.item_message_system_pure_text;
                        case "图文_0":
                            return R.layout.item_message_system;
                        case "购物_0":
                            return R.layout.item_message_shopping;
                        default:
                            return R.layout.layout_no_list_item;
                    }
                }

                @Override
                public int getViewTypeCount() {
                    return 4;
                }

                @Override
                public int getItemViewType(int position, ImMsgBean imMsgBean) {
                    if ("文本_0".equals(imMsgBean.getMsgtype())) {
                        return 0;
                    } else if ("图文_0".equals(imMsgBean.getMsgtype())) {
                        return 1;
                    } else if ("购物_0".equals(imMsgBean.getMsgtype())) {
                        return 2;
                    } else {
                        return 3;
                    }
                }
            });
        }

        @Override
        public void convert(ViewHolder holder, ImMsgBean imMsgBean) {
            switch (holder.getLayoutId()) {
                case R.layout.item_message_system:
                    if (!TextUtils.isEmpty(imMsgBean.getSendTime())) {
                        holder.setVisible(R.id.tv_message_time, true);
                        holder.setText(R.id.tv_message_time, imMsgBean.getSendTime());
                    } else {
                        holder.setVisible(R.id.tv_message_time, false);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(imMsgBean.getMessage());

                        // 在需要重新获取更新的图片时调用
                        String updateTime = String.valueOf(System.currentTimeMillis());
                        RequestOptions options = new RequestOptions()
                                .placeholder(R.drawable.default_image)
                                .centerCrop()
                                .signature(new ObjectKey(updateTime));
                        Glide.with(mContext)
                                .load(jsonObject.getString("img"))
                                .apply(options)
                                .into((ImageView) holder.getView(R.id.iv_message_icon));

                        holder.setText(R.id.tv_message_title, jsonObject.getString("title"));
                        holder.setText(R.id.tv_message_content, jsonObject.getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.layout.item_message_system_pure_text:
                    if (!TextUtils.isEmpty(imMsgBean.getSendTime())) {
                        holder.setVisible(R.id.tv_message_time, true);
                        holder.setText(R.id.tv_message_time, imMsgBean.getSendTime());
                    } else {
                        holder.setVisible(R.id.tv_message_time, false);
                    }
                    try {
                        //因为是纯文本消息通知，所以只有一个text字段!
                        JSONObject jsonObject = new JSONObject(imMsgBean.getMessage());
                        holder.setText(R.id.tv_message_text_content, jsonObject.getString("text"));//text
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.layout.item_message_shopping:
                    if (!TextUtils.isEmpty(imMsgBean.getSendTime())) {
                        holder.setVisible(R.id.tv_message_time, true);
                        holder.setText(R.id.tv_message_time, imMsgBean.getSendTime());
                    } else {
                        holder.setVisible(R.id.tv_message_time, false);
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(imMsgBean.getMessage());

                        // 在需要重新获取更新的图片时调用
                        String updateTime = String.valueOf(System.currentTimeMillis());
                        RequestOptions options = new RequestOptions()
                                .placeholder(R.drawable.default_image)
                                .centerCrop()
                                .signature(new ObjectKey(updateTime));
                        Glide.with(mContext)
                                .load(jsonObject.getString("img"))
                                .apply(options)
                                .into((ImageView) holder.getView(R.id.iv_message_icon));

                        holder.setText(R.id.tv_message_title, jsonObject.getString("title"));
                        holder.setText(R.id.tv_message_state, jsonObject.getString("explain"));
                        holder.setText(R.id.tv_message_content, jsonObject.getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(mContext).destroy(MessageManager.NEW_MESSAGE);
        super.onDestroy();
    }

}
