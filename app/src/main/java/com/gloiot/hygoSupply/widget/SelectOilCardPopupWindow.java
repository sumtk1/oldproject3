package com.gloiot.hygoSupply.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择油卡弹出框
 * Created by hygo04 on 2016/6/2.
 */
public class SelectOilCardPopupWindow extends PopupWindow {

    private Context mContext;
    private RelativeLayout rl_select, rl_add;
    private FrameLayout mydialog_custompanel;
    private LinearLayout iv_cancel;        
    private TextView tv_confirm, tv_select,tv_explain;
    private View mMenuView,customView;
    private List<String[]> list = new ArrayList<String[]>();
    private int wHeight;//屏幕高度
    private View listviewSingle,mCustomView;

    @SuppressLint("InflateParams")
    public SelectOilCardPopupWindow(Context context, View.OnClickListener itemsOnClick, List<String[]> datas) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_dialog_oilcard, null);
        list = datas;

        wHeight = getScreenHeight();//屏幕高度

        rl_select = (RelativeLayout) mMenuView.findViewById(R.id.rl_01);
        iv_cancel = (LinearLayout) mMenuView.findViewById(R.id.iv_cancel);
        tv_select = (TextView) mMenuView.findViewById(R.id.tv_select);
        tv_confirm = (TextView) mMenuView.findViewById(R.id.tv_confirm);
        tv_explain = (TextView) mMenuView.findViewById(R.id.tv_explain);
        mydialog_custompanel = (FrameLayout) mMenuView.findViewById(R.id.mydialog_custompanel);
        rl_add = (RelativeLayout) mMenuView.findViewById(R.id.rl_02);

        // 设置监听
//        rl_select.setOnClickListener(itemsOnClick);
        iv_cancel.setOnClickListener(itemsOnClick);//取消
        tv_confirm.setOnClickListener(itemsOnClick);//确定
        rl_add.setOnClickListener(itemsOnClick);//添加更多加油卡

        // 设置SelectOilCardPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectOilCardPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectOilCardPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectOilCardPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectOilCardPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.anim_popup_dir);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectOilCardPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }


    // 获得屏幕高度
    public int getScreenHeight() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 标题
     */
    public SelectOilCardPopupWindow setTitle (String text){
        tv_select.setText(text);
        return  this;
    }

    /**
     * 油费账户的文本说明
     */
    public SelectOilCardPopupWindow addExplain (String text){
        tv_explain.setVisibility(View.VISIBLE);
        tv_explain.setText(text);
        return  this;
    }

    /**
     * 添加油卡
     */
    public SelectOilCardPopupWindow addOilCard (){
        rl_add.setVisibility(View.VISIBLE);
        return  this;
    }

    /**
     * 定制layout
     * @param resId 定制layout的ID
     * @return
     */
    public SelectOilCardPopupWindow setCustomView(int resId) {
        customView = View.inflate(mContext, resId, null);
        mydialog_custompanel.addView(customView);
        return this;
    }

    /**
     * 可以在调用时设置适配器的listview
     * @param context
     * @return
     */
    public ListView setListViewSingle2(Context context) {
        listviewSingle = View.inflate(context, R.layout.dialog_son_listview, null);
        mydialog_custompanel.addView(listviewSingle);
        final ListView listView = (ListView) listviewSingle.findViewById(R.id.dialog_listview);
        return listView;
    }

    /**
     * 设置最大高度
     *
     * @return
     */
    public SelectOilCardPopupWindow setMaxHeight(final View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int cHeight = view.getHeight();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (cHeight >= wHeight * 2 / 5) {
                    lp.height = wHeight * 2 / 5;
                }
                view.setLayoutParams(lp);
            }
        });
        return this;
    }

}
