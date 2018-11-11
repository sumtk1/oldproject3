package com.gloiot.hygoSupply.ui.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.Life;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.ui.activity.SaoMiaoActivity;
import com.gloiot.hygoSupply.ui.activity.payment.SettlementActivity;
import com.gloiot.hygoSupply.ui.activity.web.WebActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemCommonAdapter;
import com.zyd.wlwsdk.adapter.abslistview.MultiItemTypeSupport;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.widge.EmptyLayout;
import com.zyd.wlwsdk.widge.pulltorefresh.PullToRefreshLayout;
import com.zyd.wlwsdk.zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LifeFragment extends BaseFragment implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {


    @Bind(R.id.iv_life_scan)
    ImageView ivLifeScan;
    @Bind(R.id.lv_life)
    ListView lvLife;
    @Bind(R.id.ptrl_life)
    PullToRefreshLayout ptrlLife;
    @Bind(R.id.life_emptylayout)
    EmptyLayout life_emptylayout;
    @Bind(R.id.gv_life)
    GridView gvLife;
    @Bind(R.id.rl_life)
    RelativeLayout rlLife;

    RelativeLayout fakeStatusBar;

    private List<Life> list = new ArrayList<>();
    private List<String[]> lists = new ArrayList<>();
    private LifeAdapter lifeAdapter;
    private CommonAdapter gridAdapter;
    private boolean isRefreshing;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_life, container, false);
        ButterKnife.bind(this, view);
        ptrlLife.setOnRefreshListener(this);

        initView();

        if (null != preferences.getString(ConstantUtils.CACHE_LIFE, null)) {
            try {
                JSONObject jsonObject = new JSONObject(preferences.getString(ConstantUtils.CACHE_LIFE, null));
                ShuJuChuLi(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                life_emptylayout.showError();
            }
        }
        lvLife.setFocusable(false); // 去掉listview的焦点, 解决scrollview嵌套listview运行后最先显示出来的位置不在顶部而是中间问题
        lvLife.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (checkIsSetPwd()) {

                    if (list.get(position).getShowType() == 0) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra("url", mUrl(list.get(position).getBtnLists().get(0)[1]));
                        startActivity(intent);
                    }
                }
            }
        });
        fakeStatusBar = (RelativeLayout) view.findViewById(R.id.fake_status_bar);
        setStatusBar();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.getLifeData(LifeFragment.this));
    }

    private void initView() {
        life_emptylayout.setErrorDrawable(R.mipmap.img_error_layout);
        life_emptylayout.setErrorMessage("网络出错了");
        life_emptylayout.setErrorViewButtonId(R.id.buttonError);
        life_emptylayout.setShowErrorButton(true);
        life_emptylayout.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestHandleArrayList.add(requestAction.getLifeData(LifeFragment.this));
                life_emptylayout.hide();
            }
        });
        lifeAdapter = new LifeAdapter(mContext, list);
        lvLife.setAdapter(lifeAdapter);
        gridAdapter = new CommonAdapter<String[]>(mContext, R.layout.item_life_top, lists) {
            @Override
            public void convert(ViewHolder holder, final String[] strings) {
                holder.setText(R.id.tv_life_name, strings[1]);
                CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_life_img), strings[0], 0);
                holder.setOnClickListener(R.id.rl_life_top, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkIsSetPwd()) {
                            if (lists == null || lists.isEmpty()) {
                                return;
                            }
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(strings[2]));
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        gvLife.setAdapter(gridAdapter);

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_GETLIFEDATA:
                if (isRefreshing) {
                    isRefreshing = false;
                    ptrlLife.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
                lists.clear();
                list.clear();
                ShuJuChuLi(response);
                break;
        }
    }

    @Override
    public void statusUnusual(JSONObject response) throws JSONException {
        super.statusUnusual(response);
        if (isRefreshing) {
            isRefreshing = false;
            ptrlLife.refreshFinish(PullToRefreshLayout.FAIL);
        }
    }

    private void ShuJuChuLi(JSONObject response) throws JSONException {
        editor.putString(ConstantUtils.CACHE_LIFE, response.toString());
        editor.commit();
        /**
         * 存储顶部数据
         */
        JSONArray jsonArray = response.getJSONArray("top");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
            String[] details = new String[3];
            details[0] = jsonObj.getString("ico");
            details[1] = jsonObj.getString("title");
            details[2] = jsonObj.getString("url");
            lists.add(details);
        }
        setData();


        /**
         * 存储模板1、2,3数据
         */
        JSONArray jAMB = response.getJSONArray("list");
        for (int i = 0; i < jAMB.length(); i++) {
            JSONObject jOMB = (JSONObject) jAMB.get(i);
            Log.e("json数据" + i, jOMB.toString());
            Life life = new Life();
            life.setIcon(jOMB.getString("ico"));
            life.setTitle(jOMB.getString("title"));
            life.setExplain(jOMB.getString("slogan"));

            JSONArray ivLists = jOMB.getJSONArray("images");
            List<String[]> btnList = new ArrayList<>();
            for (int j = 0; j < ivLists.length(); j++) {
                JSONObject jOs = (JSONObject) ivLists.get(j);
                String[] s = new String[3];
                s[0] = jOs.getString("picture");
                s[1] = jOs.getString("url");
                if (jOMB.getString("event").equals("5")) {
                    s[2] = jOs.getString("标题");
                }
                btnList.add(s);
            }
            life.setBtnLists(btnList);
            if (jOMB.getString("event").equals("1")) {
                life.setShowType(Life.SHOWTYPE_0);
                list.add(life);
            } else if (jOMB.getString("event").equals("3")) {
                life.setShowType(Life.SHOWTYPE_1);
                list.add(life);
            } else if (jOMB.getString("event").equals("5")) {
                life.setShowType(Life.SHOWTYPE_2);
                list.add(life);
            }
        }
        lifeAdapter.notifyDataSetChanged();
        CommonUtils.setListViewHeightBasedOnChildren(lvLife);
    }

    /**
     * 设置顶部模块数据
     */
    private void setData() {
        if (lists.size() > 3) {
            gvLife.setNumColumns(4);
        } else {
            gvLife.setNumColumns(3);
        }
        gridAdapter.notifyDataSetChanged();
        CommonUtils.reMesureGridViewHeight(gvLife);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.iv_life_scan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_life_scan:
                    if (checkIsSetPwd()) {
                        if (lists == null || lists.isEmpty()) {
                            return;
                        }
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                checkPermission(new CheckPermListener() {
                                    @Override
                                    public void superPermission() {
                                        Intent intent = new Intent(mContext, SaoMiaoActivity.class);
                                        startActivityForResult(intent, 100);

                                    }
                                }, R.string.perm_camera, Manifest.permission.VIBRATE);

                            }
                        }, R.string.perm_camera, Manifest.permission.CAMERA);
                    }

                break;
        }
    }

    /**
     * 刷新
     *
     * @param pullToRefreshLayout
     */
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        requestHandleArrayList.add(requestAction.getLifeData(LifeFragment.this));
        isRefreshing = true;
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        super.onFailure(requestTag, errorResponse, showLoad);
//      life_emptylayout.showError();
    }


    public class LifeAdapter extends MultiItemCommonAdapter<Life> {
        public LifeAdapter(Context context, List<Life> datas) {
            super(context, datas, new MultiItemTypeSupport<Life>() {
                @Override
                public int getLayoutId(int position, Life life) {
                    switch (life.getShowType()) {
                        case Life.SHOWTYPE_0:
                            return R.layout.item_life01;
                        case Life.SHOWTYPE_1:
                            return R.layout.item_life02;
                        case Life.SHOWTYPE_2:
                            return R.layout.item_life03;
                        default:
                            return 0;
                    }
                }

                @Override
                public int getViewTypeCount() {
                    return 3;
                }

                @Override
                public int getItemViewType(int position, Life life) {
                    return life.getShowType();
                }
            });
        }

        @Override
        public void convert(ViewHolder holder, final Life life) {
            switch (holder.getLayoutId()) {
                case R.layout.item_life01:
                    CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_icon), life.getIcon(), 10);
                    holder.setText(R.id.tv_title, life.getTitle());
                    holder.setText(R.id.tv_explain, life.getExplain());
                    CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_guanggao), life.getBtnLists().get(0)[0], 0);
                    break;
                case R.layout.item_life02:
                    CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_icon), life.getIcon(), 10);
                    holder.setText(R.id.tv_title, life.getTitle());
                    holder.setText(R.id.tv_explain, life.getExplain());
                    L.e("-lists-", life.getTitle() + "==" + life.getBtnLists().size());
                    if (life.getBtnLists().size() > 0) {
                        String[] guanggaos = life.getBtnLists().get(0);
                        ImageView iv_guanggao = holder.getView(R.id.iv_guanggao);
                        CommonUtils.setDisplayImageOptions(iv_guanggao, guanggaos[0], 0);
                        setClick(iv_guanggao, guanggaos[1]);
                    }
                    if (life.getBtnLists().size() > 1) {
                        String[] firsts = life.getBtnLists().get(1);
                        ImageView iv_first = holder.getView(R.id.iv_first);
                        CommonUtils.setDisplayImageOptions(iv_first, firsts[0], 0);
                        setClick(iv_first, firsts[1]);
                    }
                    if (life.getBtnLists().size() > 2) {
                        String[] seconds = life.getBtnLists().get(2);
                        ImageView iv_second = holder.getView(R.id.iv_second);
                        CommonUtils.setDisplayImageOptions(iv_second, seconds[0], 0);
                        setClick(iv_second, seconds[1]);
                    }
                    break;
                case R.layout.item_life03:

                    if (life.getBtnLists().size() > 0) {
                        String[] titles = life.getBtnLists().get(0);
                        RelativeLayout rl_title = holder.getView(R.id.rl_title);
                        CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_guanggao), life.getIcon(), 0);
                        holder.setText(R.id.tv_title, titles[2]);
                        setClick(rl_title, titles[1]);
                    }
                    if (life.getBtnLists().size() > 1) {
                        String[] firsts = life.getBtnLists().get(1);
                        RelativeLayout rl_son1 = holder.getView(R.id.rl_son1);
                        CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_son_icon1), firsts[0], 10);
                        holder.setText(R.id.tv_son_title1, firsts[2]);
                        setClick(rl_son1, firsts[1]);
                    }
                    if (life.getBtnLists().size() > 2) {
                        String[] seconds = life.getBtnLists().get(2);
                        RelativeLayout rl_son2 = holder.getView(R.id.rl_son2);
                        CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.iv_son_icon2), seconds[0], 10);
                        holder.setText(R.id.tv_son_title2, seconds[2]);
                        setClick(rl_son2, seconds[1]);
                    }

                    break;
            }
        }
    }

    /**
     * 设置item里面的控件的事件
     */
    private void setClick(ImageView iv, final String url) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIsSetPwd()) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("url", mUrl(url));
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 设置item里面的控件的事件
     */
    private void setClick(RelativeLayout rl, final String url) {
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIsSetPwd()) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("url", mUrl(url));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e("扫描结果", result);
                    if (!result.startsWith("http")) {
                        // 判断是否ip地址开头
                        if (result.startsWith("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$")) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(result));
                            startActivity(intent);
                        } else if (result.startsWith("www.")) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(result));
                            startActivity(intent);
                        } else {
                            DialogUtlis.oneBtnNormal(mContext, result);
                        }
                    } else {
                        if (result.contains("&AppType=Self")) {
                            String onlyid = "";
                            try {
                                onlyid = result.split("onlyID=")[1].split("&AppType")[0];
                                Log.e("扫描结果333", onlyid);
                            } catch (Exception e) {

                            }
                            //收款的二维码扫描回调，暂时写加在末尾的AppType=Self标记，等接口一出再重新拼接所需参数写过完整的标识
                            Intent it = new Intent(getActivity(), SettlementActivity.class);
                            it.putExtra("onlyID", onlyid);
                            startActivity(it);
                        } else {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra("url", mUrl(result));
                            startActivity(intent);
                        }
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    MToast.showToast("解析二维码失败");
                }
            }
        }
    }

    // 判断地址是否有问号
    private String mUrl(String url) {
        if (url.contains("?")) {
            return url + "&onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" + "&version=" + ConstantUtils.VERSION;
//            return url + "&onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" ;
        } else {
            return url + "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" + "&version=" + ConstantUtils.VERSION;
//            return url + "?onlyID=" + preferences.getString(ConstantUtils.SP_ONLYID, "") + "&business=business" ;
        }
    }

    //设置颜色
    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarHeight(fakeStatusBar);
            fakeStatusBar.setBackgroundColor(Color.parseColor("#ff7f29"));
        }
    }
}
