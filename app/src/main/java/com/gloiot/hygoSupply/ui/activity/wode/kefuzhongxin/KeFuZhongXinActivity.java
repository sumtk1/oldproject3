package com.gloiot.hygoSupply.ui.activity.wode.kefuzhongxin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.web.WebActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.LinesGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 创建者 zengming.
 * 功能：客服中心
 */
public class KeFuZhongXinActivity extends BaseActivity {

    @Bind(R.id.kefu_gridView)
    LinesGridView kefuGridView;
    @Bind(R.id.kefu_wenti)
    ListView kefuWenti;
    @Bind(R.id.kefu_banner)
    Banner kefuBanner;

    private CommonAdapter gridViewAdapter;
    private CommonAdapter wenTiAdapter;

    private List<KeFuFuWuBean> keHuDatas = new ArrayList<>();
    private List<WenTiBean> wentiDatas = new ArrayList<>();
    private List<String> lunBoTuDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_kefu_zhongxin;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "客服中心", "");

        //客服服务  adapter
        gridViewAdapter = new CommonAdapter<KeFuFuWuBean>(this, R.layout.item_kefu_kehufuwu, keHuDatas) {
            @Override
            public void convert(ViewHolder holder, KeFuFuWuBean keHuFuWuBean) {
                ImageView ivKefu = holder.getView(R.id.iv_kefu);
                Glide.with(mContext).load(keHuFuWuBean.getImg_Url())
                        .apply(new RequestOptions().centerCrop()
                                .placeholder(R.drawable.default_image)
                                .error(R.drawable.default_image)
                        ).into(ivKefu);
                holder.setText(R.id.tv_kefu, keHuFuWuBean.getTitle());
            }
        };
        kefuGridView.setNumColumns(3);
        kefuGridView.setAdapter(gridViewAdapter);
        kefuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("在线".equals(keHuDatas.get(position).getZhuangtai())) {
                    if (keHuDatas.get(position).getRegionId() == 0) {
                        Intent intent = new Intent(mContext, SelectKeFuActivity.class);
                        intent.putExtra("system", "系统客服");
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, SelectKeFuRegionActivity.class);
                        intent.putExtra("regionID", keHuDatas.get(position).getRegionId());
                        startActivity(intent);
                    }
                } else {
                    DialogUtlis.oneBtnNormal(mContext, "客服专线暂未开通，请耐心等待");
                }
            }
        });

        //常见问题 Adapter
        wenTiAdapter = new CommonAdapter<WenTiBean>(this, R.layout.item_kefu_changjianwenti, wentiDatas) {
            @Override
            public void convert(ViewHolder holder, WenTiBean wenTiBean) {
                holder.setText(R.id.tv_kefu_changjianti, wenTiBean.getTitle());
            }
        };
        kefuWenti.setAdapter(wenTiAdapter);
        CommonUtils.setListViewHeightBasedOnChildren(kefuWenti);

        kefuWenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", wentiDatas.get(position).getUrl());
                startActivity(intent);
            }
        });

        requestHandleArrayList.add(requestAction.KeHuZhongXin(this, preferences.getString(ConstantUtils.SP_MYID, "")));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("KeFuZhongXinActivity : " + requestTag, response.toString());
        switch (requestTag) {
            case ConstantUtils.TAG_KEHU_ZHONGXIN:
                JSONObject jsonObject = response.getJSONObject("列表");

                //常见问题
                JSONArray wenTiArray = jsonObject.getJSONArray("常见问题");
                wentiDatas.clear();
                JSONObject wentiObj;
                WenTiBean wenTiBean;
                for (int i = 0; i < wenTiArray.length(); i++) {
                    wentiObj = wenTiArray.getJSONObject(i);
                    wenTiBean = new WenTiBean();
                    wenTiBean.setTitle(wentiObj.getString("标题"));
                    wenTiBean.setUrl(wentiObj.getString("跳转网址"));
                    wentiDatas.add(wenTiBean);
                }
                wenTiAdapter.notifyDataSetChanged();
                CommonUtils.setListViewHeightBasedOnChildren(kefuWenti);

                //轮播图
                JSONArray lunBoTuArray = jsonObject.getJSONArray("轮播图");
                JSONObject lunBoTuObj;
                lunBoTuDatas.clear();
                for (int i = 0; i < lunBoTuArray.length(); i++) {
                    lunBoTuObj = lunBoTuArray.getJSONObject(i);
                    lunBoTuDatas.add(lunBoTuObj.getString("图片"));
                }
                initLunbo(lunBoTuDatas);

                //客服
                JSONArray keHuArray = jsonObject.getJSONArray("客服");
                keHuDatas.clear();
                JSONObject keHuObj;
                KeFuFuWuBean keFuFuWuBean;
                for (int i = 0; i < keHuArray.length(); i++) {
                    keHuObj = keHuArray.getJSONObject(i);
                    keFuFuWuBean = new KeFuFuWuBean();
                    keFuFuWuBean.setTitle(keHuObj.getString("客服组"));
                    keFuFuWuBean.setImg_Url(keHuObj.getString("图标"));
                    keFuFuWuBean.setZhuangtai(keHuObj.getString("状态"));
                    keFuFuWuBean.setRegionId(keHuObj.getInt("区域id"));
                    keHuDatas.add(keFuFuWuBean);
                }
                gridViewAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 轮播图
     *
     * @param list
     */
    public void initLunbo(final List<String> list) {
        List<String> imgList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            imgList.add(list.get(i));
        }
        //初始化顶部轮播
        kefuBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置轮播样式
        kefuBanner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置
        kefuBanner.setDelayTime(3000);//设置间隔时间
        kefuBanner.setImages(imgList);//设置图片
        kefuBanner.setImageLoader(new GlideImageLoader()).start();
    }

    //客户服务Bean
    class KeFuFuWuBean {
        private String title;
        private String Img_Url;
        private String zhuangtai;
        private int regionId;

        public String getZhuangtai() {
            return zhuangtai;
        }

        public void setZhuangtai(String zhuangtai) {
            this.zhuangtai = zhuangtai;
        }

        public String getImg_Url() {
            return Img_Url;
        }

        public void setImg_Url(String img_Url) {
            Img_Url = img_Url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRegionId() {
            return regionId;
        }

        public void setRegionId(int regionId) {
            this.regionId = regionId;
        }
    }

    //常见问题Bean
    class WenTiBean {
        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
