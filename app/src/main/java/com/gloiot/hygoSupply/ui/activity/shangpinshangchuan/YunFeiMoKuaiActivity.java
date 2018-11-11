//package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;
//
//import android.app.Activity;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//
//import com.gloiot.hygoSupply.R;
//import com.gloiot.hygoSupply.bean.YunFeiMoKuaiBean;
//import com.gloiot.hygoSupply.ui.activity.BaseActivity;
//import com.gloiot.hygoSupply.utlis.CommonUtils;
//import com.zyd.wlwsdk.adapter.ViewHolder;
//import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by hygo03 on 2017/5/4.
// */
//
//public class YunFeiMoKuaiActivity extends BaseActivity {
//
//    private ListView lv_yunfei_liebiao;
//    private CommonAdapter commonAdapter;
//    private List<YunFeiMoKuaiBean> listDatas = new ArrayList<>(5);
//
//    @Override
//    public int initResource() {
//        return R.layout.activity_yunfei_mokuai;
//    }
//
//    @Override
//    public void initData() {
//        initComponent();
//        CommonUtils.setTitleBar((Activity) mContext, true, "运费模块", "");
//        for (int i = 0; i < 4; i++) {
//            YunFeiMoKuaiBean yunFeiMoKuaiBean = new YunFeiMoKuaiBean();
//            yunFeiMoKuaiBean.setMingchen("运费模块" + i);
//            yunFeiMoKuaiBean.setXueliehao(i + "");
//            yunFeiMoKuaiBean.setYanse(R.color.cl_333);
//            listDatas.add(yunFeiMoKuaiBean);
//        }
//    }
//
//    public void initComponent() {
//        lv_yunfei_liebiao = (ListView) findViewById(R.id.lv_yunfei_liebiao);
//        commonAdapter = new CommonAdapter<YunFeiMoKuaiBean>(mContext, R.layout.item_yunfei_liebiao, listDatas) {
//            LinearLayout ll_bianji;
//
//            @Override
//            public void convert(ViewHolder holder, YunFeiMoKuaiBean yunFeiMoKuaiBean) {
//                holder.setBackgroundColor(R.id.view_yansekuan, yunFeiMoKuaiBean.getYanse());
//                holder.setText(R.id.tv_yunfei_shulie, yunFeiMoKuaiBean.getXueliehao());
//                holder.setText(R.id.tv_yunfei_mingchen, yunFeiMoKuaiBean.getMingchen());
//                ll_bianji = holder.getView(R.id.ll_bianji);
//            }
//        };
//        lv_yunfei_liebiao.setAdapter(commonAdapter);
//
//    }
//}
