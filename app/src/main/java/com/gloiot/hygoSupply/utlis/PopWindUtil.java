package com.gloiot.hygoSupply.utlis;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.BianJiNeiRongActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.FaShangPingZhuangTaiActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.ShiPingLuZhiActivity;
import com.gloiot.hygoSupply.ui.adapter.SharePopWindAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能：popwind的工具类
 * Created by Ad on 2017/5/8.
 */

public class PopWindUtil {

    private static PopWindUtil mPopWindUtil;

    public static PopWindUtil getInstance() {
        if (mPopWindUtil == null) {
            mPopWindUtil = new PopWindUtil();
        }
        return mPopWindUtil;
    }


    /***
     * 发布动态
     */
    public void faBuDongTai(final Activity mContext) {
        this.mContext = mContext;
//        if (sharePopWind == null) {
        view1 = mContext.getLayoutInflater().inflate(
                R.layout.popwind_fabudongtai, null);
        view1.findViewById(R.id.tv_ppwd_fbdt_kongbai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharePopWind != null && sharePopWind.isShowing()) {
                    sharePopWind.dismiss();
                }
            }
        });
        view1.findViewById(R.id.rl_ppwd_fbdt_shipin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharePopWind != null && sharePopWind.isShowing()) {
                    Intent intent = new Intent(mContext, ShiPingLuZhiActivity.class);
                    intent.putExtra("type", "视频");
                    mContext.startActivity(intent);
                    sharePopWind.dismiss();
                }
            }
        });
        view1.findViewById(R.id.rl_ppwd_fbdt_tuwen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharePopWind != null && sharePopWind.isShowing()) {
                    Intent intent = new Intent(mContext, BianJiNeiRongActivity.class);
                    intent.putExtra("type", "图文");
                    mContext.startActivity(intent);
                    sharePopWind.dismiss();
                }
            }
        });
        view1.findViewById(R.id.rl_ppwd_fbdt_shangpin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (sharePopWind != null && sharePopWind.isShowing()) {
                    Intent intent = new Intent(mContext, FaShangPingZhuangTaiActivity.class);
                    intent.putExtra("type", "商品");
                    mContext.startActivity(intent);
                    sharePopWind.dismiss();
                }
//                MToastUtils.showToast("该功能正在优化中");
            }
        });
        sharePopWind = new PopupWindow(
                view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
//            sharePopWind.setBackgroundDrawable(new BitmapDrawable());

        sharePopWind.setOutsideTouchable(true);
        sharePopWind.setFocusable(true);
        sharePopWind.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                ScreenUtil.getInstance().backgroundAlpha(1f, mContext);
            }
        });
//        }
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        sharePopWind.setBackgroundDrawable(dw);
        sharePopWind.showAtLocation(view1, Gravity.BOTTOM, 0, 0);

        view1.startAnimation(ScreenUtil.getInstance().inDownFromRightAnimation());
    }


    /**
     * 分享popWind
     */
    private Activity mContext;
    View view1 = null;
    PopupWindow sharePopWind = null;
    GridView branch_store_share_gridview;
    private List<Map<String, Object>> data_list;

    // 图片封装为一个数组
    private int[] icon = {R.mipmap.ic_app_icon, R.mipmap.ic_app_icon, R.mipmap.ic_app_icon, R.mipmap.ic_app_icon
            , R.mipmap.ic_app_icon, R.mipmap.ic_app_icon, R.mipmap.ic_app_icon};
    List<String> nameList = null;
    List<Drawable> drawableList = null;
    SharePopWindAdapter shareAdapter = null;


    public void showSharePopWind(final Activity mContext) {
        this.mContext = mContext;
        if (sharePopWind == null) {
            view1 = mContext.getLayoutInflater().inflate(
                    R.layout.share_popwind, null);
            sharePopWind = new PopupWindow(
                    view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            view1.findViewById(R.id.popwind_cancel_txt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePopWind.dismiss();
                }
            });
            nameList = new ArrayList<String>();
            drawableList = new ArrayList<Drawable>();
            data_list = getGridViewData();

            branch_store_share_gridview = (GridView) view1.findViewById(R.id.share_gridview);
            branch_store_share_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));//点击时候以免出现系统默认的黄色边框，显示透明
            shareAdapter = new SharePopWindAdapter(mContext, data_list);
            branch_store_share_gridview.setAdapter(shareAdapter);
            branch_store_share_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    switch (position) {
                        /**微信*/
                        case 0:
                            sharePopWind.dismiss();
                            MToastUtils.showToast("微信");
                            break;
                        /**朋友圈*/
                        case 1:
                            sharePopWind.dismiss();
                            MToastUtils.showToast("朋友圈");
                            break;
                        /**qq好友*/
                        case 2:
                            sharePopWind.dismiss();
                            MToastUtils.showToast("qq好友");
                            break;
                        /** qq空间*/
                        case 3:
                            sharePopWind.dismiss();
                            MToastUtils.showToast("qq空间");
                            break;
                        /**新浪微博*/
                        case 4:
                            sharePopWind.dismiss();
                            MToastUtils.showToast("新浪微博");
                            break;
                        /**复制链接*/
                        case 5:
                            sharePopWind.dismiss();
                            MToastUtils.showToast("复制链接");
                            break;
                        case 6:
                            sharePopWind.dismiss();
                            MToastUtils.showToast("更多");
                            break;
                        default:
                            break;
                    }
                }
            });
            sharePopWind.setBackgroundDrawable(new BitmapDrawable());
            sharePopWind.setOutsideTouchable(true);
            sharePopWind.setFocusable(true);
            sharePopWind.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    ScreenUtil.getInstance().backgroundAlpha(1f, mContext);
                }
            });
        }
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        sharePopWind.setBackgroundDrawable(dw);
        sharePopWind.showAtLocation(view1, Gravity.BOTTOM, 0, 0);
        sharePopWind.update();
        view1.startAnimation(ScreenUtil.getInstance().inDownFromRightAnimation());
    }

    /**
     * 0是微信好友，1是微信朋友圈，2是qq好友，3是qq空间，4是新浪微博，5是复制链接，
     */
    public List<Map<String, Object>> getGridViewData() {

        String[] iconName = {"微信好友", "朋友圈"
                , "qq好友", "qq空间", "新浪微博"
                , "复制链接", "更多"};
        data_list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("name", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }

    public void closePopWind() {
        sharePopWind = null;
    }

}
