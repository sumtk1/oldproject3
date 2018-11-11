package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.MiaoShuBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.MToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShangpinMiaoshuActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lv_shangpin_miaoshu_info;
    private List<MiaoShuBean> allmiaoshu;
    private ImageView iv_shangpin_miaoshu_add, iv_shangpin_miaoshu_back;//添加描述 返回
    private Button btn_tianjia_shangpin_miaoshu_tianjia;//确定保存描述按钮
    private CommonAdapter adapter;
    private int itemPosition;
    private Intent intent;
    private TextView tv_toptitle_right;
    private RelativeLayout rl_fg_shangpin_miaosu;
private  boolean  isEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shangpin_miaoshu;
    }

    @Override
    public void initData() {
        if ("edit".equals(getIntent().getStringExtra("type"))) {
            isEdit=true;
            CommonUtils.setTitleBar((Activity) mContext, true, "修改图文详情", "保存");
        } else {
            CommonUtils.setTitleBar((Activity) mContext, true, "图文详情", "确认");
        }
        allmiaoshu = new ArrayList<>();
        initComponent();
        listviewset();

        tv_toptitle_right.setOnClickListener(this);
        btn_tianjia_shangpin_miaoshu_tianjia.setOnClickListener(this);
        if (ConstantUtils.SP_ALLSHANGPINMIAOSHU.size() != 0) {
            allmiaoshu.clear();
            Iterator<MiaoShuBean> iterator=ConstantUtils.SP_ALLSHANGPINMIAOSHU.iterator();
            while (iterator.hasNext()){
                allmiaoshu.add(iterator.next().clone());
            }
        }
//        Log.e("allshangpinGuige", allmiaoshu.size() + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (allmiaoshu.size() == 0) {
            rl_fg_shangpin_miaosu.setVisibility(View.VISIBLE);
            lv_shangpin_miaoshu_info.setVisibility(View.GONE);
            rl_fg_shangpin_miaosu.setOnClickListener(this);
        } else {
            rl_fg_shangpin_miaosu.setVisibility(View.GONE);
            lv_shangpin_miaoshu_info.setVisibility(View.VISIBLE);
        }
    }

    private void listviewset() {
        adapter = new CommonAdapter<MiaoShuBean>(this, R.layout.item_shangpin_miaoshu, allmiaoshu) {
            @Override
            public void convert(final ViewHolder holder, MiaoShuBean miaoshuBean) {
                holder.setText(R.id.tv_item_shangpin_miaoshu_miaoshu, miaoshuBean.getMiaoshu());
                if (!miaoshuBean.getTupian().equals("")) {//图片网址是否为空
                    ImageView view = holder.getView(R.id.iv_item_shangpin_miaoshu_pic);
                    CommonUtils.setDisplayImageOptions(view, miaoshuBean.getTupian(), 4);
                } else {
                    //未传图片的情况
                    ImageView view = holder.getView(R.id.iv_item_shangpin_miaoshu_pic);
                    CommonUtils.setDisplayImageOptions(view, miaoshuBean.getTupian(), 4);
                }
                holder.setOnClickListener(R.id.iv_item_shangpin_miaoshu_del2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        allmiaoshu.remove(holder.getmPosition());
                        if (allmiaoshu.size() == 0) {
                            rl_fg_shangpin_miaosu.setVisibility(View.VISIBLE);
                        } else {
                            rl_fg_shangpin_miaosu.setVisibility(View.GONE);
                        }
//                        ConstantUtils.SP_ALLSHANGPINMIAOSHU.remove(holder.getmPosition());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        lv_shangpin_miaoshu_info.setAdapter(adapter);
        lv_shangpin_miaoshu_info.setOnItemClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String newmiaoshu = null;
        String newimagUrl = null;
        switch (requestCode) {
            //编辑item
            case 0:
                if (data != null) {
                    if (data.getExtras() != null) {
                        if ("编辑完成".equals(data.getExtras().get("result"))) ;
                        {
                            newmiaoshu = data.getExtras().getString("newmiaoshu");
                            newimagUrl = data.getExtras().getString("newimagUrl");
                            MiaoShuBean miaoshuBean = allmiaoshu.get(itemPosition);
                            miaoshuBean.setMiaoshu(newmiaoshu);
                            miaoshuBean.setTupian(newimagUrl);
//                            ConstantUtils.SP_ALLSHANGPINMIAOSHU.clear();
//                            ConstantUtils.SP_ALLSHANGPINMIAOSHU.addAll(allmiaoshu);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            //添加描述
            case 1:
                if (data != null) {
                    if (data.getExtras() != null) {
                        if ("添加完成".equals(data.getExtras().get("result"))) ;
                        {
                            newmiaoshu = data.getExtras().getString("newmiaoshu");
                            newimagUrl = data.getExtras().getString("newimagUrl");
                            allmiaoshu.add(new MiaoShuBean(newmiaoshu, newimagUrl));
//                            ConstantUtils.SP_ALLSHANGPINMIAOSHU.clear();
//                            ConstantUtils.SP_ALLSHANGPINMIAOSHU.addAll(allmiaoshu);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    public void initComponent() {
        lv_shangpin_miaoshu_info = (ListView) findViewById(R.id.lv_shangpin_miaoshu_info);
        btn_tianjia_shangpin_miaoshu_tianjia = (Button) findViewById(R.id.btn_tianjia_shangpin_miaoshu_tianjia);
        rl_fg_shangpin_miaosu = (RelativeLayout) findViewById(R.id.rl_fg_shangpin_miaosu);
        tv_toptitle_right = (TextView) findViewById(R.id.tv_toptitle_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fg_shangpin_miaosu:
            case R.id.btn_tianjia_shangpin_miaoshu_tianjia:
                intent = new Intent(this, ChangeShangpinMiaoshuActivity.class);
                intent.putExtra("type", "add");
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_toptitle_right:
                if (allmiaoshu.size() > 0) {
                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("allmiaoshu", (Serializable) allmiaoshu);
                    intent.putExtras(bundle);
                    setResult(3, intent);
                    ConstantUtils.SP_ALLSHANGPINMIAOSHU.clear();
                    ConstantUtils.SP_ALLSHANGPINMIAOSHU.addAll(allmiaoshu);
                    finish();
                } else {
                    MToast.showToast("请先添加商品描述");
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
//        intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("allmiaoshu", (Serializable) allmiaoshu);
//        intent.putExtras(bundle);
//        setResult(3, intent);
//        ConstantUtils.SP_ALLSHANGPINMIAOSHU.clear();
//        ConstantUtils.SP_ALLSHANGPINMIAOSHU.addAll(allmiaoshu);
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (allmiaoshu.size() >= 1) {
            Intent intent = new Intent(this, ChangeShangpinMiaoshuActivity.class);
            MiaoShuBean bean = allmiaoshu.get(position);
            itemPosition = position;
            intent.putExtra("type", "bianji");
            intent.putExtra("miaoshu", bean.getMiaoshu());
            intent.putExtra("tupian", bean.getTupian());
            startActivityForResult(intent, 0);
        }
    }


}
