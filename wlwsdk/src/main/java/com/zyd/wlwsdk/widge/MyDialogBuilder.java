package com.zyd.wlwsdk.widge;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nineoldandroids.view.ViewHelper;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.zyd.wlwsdk.R;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.DatePrickerUtli;
import com.zyd.wlwsdk.utlis.xmlanalsis.ProvinceAreaHelper;
import com.zyd.wlwsdk.widge.loopview.LoopView;
import com.zyd.wlwsdk.widge.loopview.OnItemSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zyd.wlwsdk.utlis.SharedPreferencesUtlis.mContext;


/**
 * Created by hygo00 on 2016/8/10.
 * 自定义Dialog
 */

public class MyDialogBuilder extends Dialog implements DialogInterface {
    public static final int SlideTop = 0;
    public static final int SlideTopDismiss = 1;

    public static final int BtnCancel = 0;
    public static final int BtnNormal = 1;

    public static final int EtText = 0;
    public static final int EtPwd = 1;
    public static final int EtNum = 2;
    public static final int EtDecimal_Tow = 3;

    public volatile static MyDialogBuilder instance;
    private Context context;
    private int wHeight, width; // 屏幕高度宽度
    private int startEffects, dismissEffects; // 开始动画效果，消失动画效果
    private View mDialogView, customView;
    private RelativeLayout mydialog_main, mydialog;
    private TextView mydialog_title, mydialog_content;
    private Button dialog_btn_left, dialog_btn_right, dialog_btn_all;
    private FrameLayout mydialog_custompanel;
    private View listviewSingle, mydialog_flview;

    public void setDismissClickEveryWhere(boolean dismissClickEveryWhere) {
        isDismissClickEveryWhere = dismissClickEveryWhere;
    }

    private boolean isDismissClickEveryWhere;

    public MyDialogBuilder(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialogBuilder(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    public static MyDialogBuilder getInstance(Context context) {
//        if (instance != null && instance.isShowing()) {
//            instance.dismissNoAnimator();
//        }
//        if (instance == null) {
//            synchronized (MyDialogBuilder.class) {
//                if (instance == null) {
        instance = new MyDialogBuilder(context, R.style.dialog_untran);
//                }
//            }
//        }
        return instance;
    }

    public void init() {
        wHeight = getScreenHeight();
        width = getScreenWidth();
        mDialogView = View.inflate(context, R.layout.dialog_main, null);
        mydialog_main = (RelativeLayout) mDialogView.findViewById(R.id.mydialog_main);
        mydialog = (RelativeLayout) mDialogView.findViewById(R.id.mydialog);
        mydialog_title = (TextView) mDialogView.findViewById(R.id.mydialog_title);
        mydialog_content = (TextView) mDialogView.findViewById(R.id.mydialog_content);
        mydialog_custompanel = (FrameLayout) mDialogView.findViewById(R.id.mydialog_custompanel);
        mydialog_flview = mDialogView.findViewById(R.id.mydialog_flview);
        dialog_btn_left = (Button) mDialogView.findViewById(R.id.dialog_btn_left);
        dialog_btn_right = (Button) mDialogView.findViewById(R.id.dialog_btn_right);
        dialog_btn_all = (Button) mDialogView.findViewById(R.id.dialog_btn_all);

        setContentView(mDialogView);
        mydialog_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDismissClickEveryWhere) {
                    dismiss();
                }
            }
        });
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                startAnimation();
            }
        });

        setMydialogWidth();
    }

    /**
     * 设置myDialog宽度
     */
    public void setMydialogWidth() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mydialog.getLayoutParams();
        lp.width = (int) (width * 0.8);
        mydialog.setLayoutParams(lp);
    }

    /**
     * 获得dialogview
     *
     * @return
     */
    public View getDialogView() {
        return mDialogView;
    }


    /**
     * 设置标题
     *
     * @param dialogTitle
     * @return
     */
    public MyDialogBuilder withTitie(String dialogTitle) {
        mydialog_title.setVisibility(View.VISIBLE);
        mydialog_title.setText(dialogTitle);
        return this;
    }

    /**
     * 设置内容
     *
     * @param dialogContent
     * @return
     */
    public MyDialogBuilder withContene(String dialogContent) {
        mydialog_content.setVisibility(View.VISIBLE);
        mydialog_content.setText(dialogContent);
        return this;
    }

    /**
     * 设置内容
     *
     * @param dialogContent
     * @param gravity       文字gravity
     * @return
     */
    public MyDialogBuilder withContene(String dialogContent, int gravity) {
        mydialog_content.setVisibility(View.VISIBLE);
        mydialog_content.setText(dialogContent);
        mydialog_content.setGravity(gravity);
        return this;
    }


    /**
     * 定制layout
     *
     * @param resId 定制layout的ID
     * @return
     */
    public MyDialogBuilder setCustomView(int resId) {
        customView = View.inflate(context, resId, null);
        mydialog_custompanel.addView(customView);
        return this;
    }

    /**
     * 定制layout
     *
     * @param view 定制layout的view
     * @return
     */
    public MyDialogBuilder setCustomView(View view) {

        mydialog_custompanel.removeAllViews();
        mydialog_custompanel.addView(view);
        return this;
    }

    /**
     * 获得定制layout
     *
     * @return
     */
    public View getCustomView() {
        return customView;
    }

    /**
     * 设置动画效果
     *
     * @param startEffects   开始动画
     * @param dismissEffects 结束动画
     * @return
     */
    public MyDialogBuilder withEffects(int startEffects, int dismissEffects) {
        this.startEffects = startEffects;
        this.dismissEffects = dismissEffects;
        return this;
    }


    public MyDialogBuilder setBtn1(View.OnClickListener click) {
        dialog_btn_all.setVisibility(View.VISIBLE);
        dialog_btn_all.setOnClickListener(click);
        return this;
    }

    // 按钮字体
    public MyDialogBuilder setBtn1Text(String btn1) {
        dialog_btn_all.setText(btn1);
        return this;
    }

    // 按钮背景
    public MyDialogBuilder setBtn1Bg(int drawable) {
        dialog_btn_all.setBackgroundResource(drawable);
        dialog_btn_all.setTextColor(0xFF000000);
        return this;
    }

    public MyDialogBuilder setBtn2(View.OnClickListener clickLeft, View.OnClickListener clickRight) {
        dialog_btn_all.setVisibility(View.GONE);
        dialog_btn_left.setVisibility(View.VISIBLE);
        dialog_btn_right.setVisibility(View.VISIBLE);
        dialog_btn_left.setOnClickListener(clickLeft);
        dialog_btn_right.setOnClickListener(clickRight);
        return this;
    }

    // 按钮字体
    public MyDialogBuilder setBtn2Text(String btn1, String btn2) {
        dialog_btn_left.setText(btn1);
        dialog_btn_right.setText(btn2);
        return this;
    }

    /**
     * ===========================================================================================================
     * 特殊性：只有日期使用
     * 定制日期弹出框
     *
     * @param resId 定制layout的ID
     * @return
     */
    public MyDialogBuilder setCustomDateView(int resId) {
        customView = View.inflate(context, resId, null);
        DatePicker datePicker = (DatePicker) customView.findViewById(R.id.datePicker);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        DatePrickerUtli.findNumberPicker(datePicker);
        try {
//            datePicker.setMinDate(new SimpleDateFormat("yyyy-MM-dd").parse("1898-01-01").getTime());
            datePicker.setMinDate(new SimpleDateFormat("yyyy-MM-dd").parse(DatePrickerUtli.getDate()).getTime());
//            Calendar c=Calendar.getInstance();
//            c.add(Calendar.YEAR,1);
//            datePicker.setMaxDate(c.getTime().getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePrickerUtli.setDatePickerDividerColor(datePicker);
        mydialog_custompanel.addView(customView);
        return this;
    }

    /**
     * ===========================================================================================================
     */

    public MyDialogBuilder setCustomDateView(int resId, int month) {
        customView = View.inflate(context, resId, null);
        DatePicker datePicker = (DatePicker) customView.findViewById(R.id.datePicker);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        DatePrickerUtli.findNumberPicker(datePicker);
        try {
            datePicker.setMinDate(new SimpleDateFormat("yyyy-MM-dd").parse(DatePrickerUtli.getDate()).getTime());
            datePicker.setMaxDate(new SimpleDateFormat("yyyy-MM-dd").parse(DatePrickerUtli.getMaxDate(month)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePrickerUtli.setDatePickerDividerColor(datePicker);
        mydialog_custompanel.addView(customView);
        return this;
    }

    /**
     * ===========================================================================================================
     * 单选listview
     *
     * @param context
     * @param position
     * @return
     */
    private int listviewSingleItems;

    public MyDialogBuilder setListViewSingle(Context context, final String[] datas, final LoopViewCallBack loopViewCallBack, int currentPostition) {
        listviewSingle = View.inflate(context, R.layout.dialog_son_loopview, null);
        mydialog_custompanel.addView(listviewSingle);
        LoopView loopView = (LoopView) listviewSingle.findViewById(R.id.lp_loopView);

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                listviewSingleItems = index;
                loopViewCallBack.callBack(index);
            }
        });
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            list.add(datas[i]);
        }
        loopView.setItemsVisibleCount(6);
        loopView.setNotLoop();
        loopView.setItems(list);
        loopView.setCurrentPosition(currentPostition);
        return this;
    }

    // listView item点击
    public interface LoopViewCallBack {
        void callBack(int data);
    }

    // 返回单选listview 选择位置
    public int getlistviewSingleItems() {
        return listviewSingleItems;
    }
    /**===========================================================================================================*/


    /**
     * ===========================================================================================================
     * 联动选择省市（WheelView）
     *
     * @param context
     * @param position
     * @return
     */

    // 省集合
    private List<String> mProvinceList = new ArrayList<>();
    // 市集合
    private List<String> mCityList = new ArrayList<>();
    // 省市集合
    private Map<String, List<String>> mCitisListMap = new HashMap<>();
    private WheelView mViewProvince, mViewCity;
    // 选中的省市
    private String selectP, selectC;
    /**
     * 市默认显示位置
     */
    int cPosition = 0;

    public MyDialogBuilder setPCWheelView(final Context context, String provinceStr, String cityStr) {
        View wheelview = View.inflate(context, R.layout.dialog_son_wheelview, null);
        mViewProvince = (WheelView) wheelview.findViewById(R.id.wv_province);
        mViewCity = (WheelView) wheelview.findViewById(R.id.wv_city);
        /** 获取省市数据 */
        ProvinceAreaHelper pa = ProvinceAreaHelper.getInstance(context);
        mProvinceList = pa.getmProvinceDatas();
        mCitisListMap = pa.getmCitisDatasMap();
        /** 初始化省适配器 */
        mViewProvince.setWheelAdapter(new ArrayWheelAdapter(context)); // 文本数据源
        mViewProvince.setSkin(WheelView.Skin.None); // common皮肤
        mViewProvince.setWheelData(mProvinceList);  // 数据集合

        /** 自定义style */
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = 0xff2b9ced;
        style.textColor = 0xff000000;
        style.selectedTextZoom = 1.2f;
        /** 设置省的字体颜色 */
        mViewProvince.setStyle(style);
        /** 设置省默认显示 */
        int position = 0;
        if (!TextUtils.isEmpty(provinceStr)) {
            for (int i = 0; i < mProvinceList.size(); i++) {
                if (mProvinceList.get(i) == provinceStr) {
                    position = i;
                    break;
                }
            }
        }
        mViewProvince.setSelection(position);
        mViewProvince.setWheelSize(5);
        /** 初始化选中的省 */
        selectP = mProvinceList.get(0);
        /** 添加省滚动的监听器 */
        mViewProvince.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                setCityData();
            }
        });

        /** 初始化市的适配器 */
        mViewCity.setWheelAdapter(new ArrayWheelAdapter(context)); // 文本数据源
        mViewCity.setSkin(WheelView.Skin.None); // common皮肤
        /** 设置市的字体颜色 */
        mViewCity.setStyle(style);
        if (!TextUtils.isEmpty(cityStr)) {
            for (int i = 0; i < mCitisListMap.get(provinceStr).size(); i++) {
                if (mCitisListMap.get(provinceStr).get(i) == cityStr) {
                    cPosition = i;
                    break;
                }
            }
        }

        setCityData();

        mydialog_custompanel.addView(wheelview);
        setMaxHeight(wheelview);
        return this;
    }

    /**
     * 设置市数据
     */
    int i = 1;

    private void setCityData() {
        i++;
        selectP = (String) mViewProvince.getSelectionItem();
        mCityList = mCitisListMap.get(selectP);

        mViewCity.setWheelData(mCityList);  // 数据集合
        if (i == 4) {
            mViewCity.setSelection(cPosition);
        } else {
            mViewCity.setSelection(0);
        }
        mViewCity.setWheelSize(5);
        /** 初始化选中的市 */
        selectC = mCityList.get(0);
        /** 添加市滚动的监听器 */
        mViewCity.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                selectC = mCityList.get(position);
            }
        });
    }

    /**
     * 获得选中省
     */
    public String getProvince() {
        return selectP;
    }

    /**
     * 获得选中市
     */
    public String getCity() {
        return selectC;
    }
    /**===========================================================================================================*/


    /**
     * ===========================================================================================================
     * 单选listview
     *
     * @param context
     * @param datas
     * @return
     */
    public MyDialogBuilder setSingleChoice(Context context, final String[] datas, int item,
                                           final SingleChoiceConvert convert, final SingleChoiceOnItemClick onItemClickListener) {
        List data = new ArrayList();
        for (int i = 0; i < datas.length; i++) {
            data.add(datas[i]);
        }
        mydialog_flview.setVisibility(View.VISIBLE);
        listviewSingle = View.inflate(context, R.layout.dialog_son_listview, null);
        mydialog_custompanel.addView(listviewSingle);
        ListView listView = (ListView) listviewSingle.findViewById(R.id.dialog_listview);
        listView.setVerticalScrollBarEnabled(false);
        listView.addFooterView(new ViewStub(context));
        listView.setAdapter(new CommonAdapter<String>(mContext, item, data) {
            @Override
            public void convert(ViewHolder holder, String s) {
                convert.convert(holder, s);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(datas[position]);
                dismiss();
            }
        });
        setMaxHeight(listviewSingle);
        return this;
    }

    // AdapterConvert
    public interface SingleChoiceConvert {
        void convert(ViewHolder holder, String s);
    }

    // listView item点击
    public interface SingleChoiceOnItemClick {
        void onItemClick(String data);
    }
    /**===========================================================================================================*/


    /**
     * ===========================================================================================================
     * 单选listview   带position
     *
     * @param context
     * @param datas
     * @return
     */
    public MyDialogBuilder setSingleChoice(Context context, final String[] datas, int item,
                                           final SingleChoiceConvert convert, final SingleChoiceOnItemClickWithPosition onItemClickListener) {
        List data = new ArrayList();
        for (int i = 0; i < datas.length; i++) {
            data.add(datas[i]);
        }
        mydialog_flview.setVisibility(View.VISIBLE);
        listviewSingle = View.inflate(context, R.layout.dialog_son_listview, null);
        mydialog_custompanel.addView(listviewSingle);
        ListView listView = (ListView) listviewSingle.findViewById(R.id.dialog_listview);
        listView.setVerticalScrollBarEnabled(false);
        listView.addFooterView(new ViewStub(context));
        listView.setAdapter(new CommonAdapter<String>(mContext, item, data) {
            @Override
            public void convert(ViewHolder holder, String s) {
                convert.convert(holder, s);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(datas[position], position);
                dismiss();
            }
        });
        setMaxHeight(listviewSingle);
        return this;
    }

    // listView item点击
    public interface SingleChoiceOnItemClickWithPosition {
        void onItemClick(String data, int position);
    }

    /**
     * 设置最大高度
     *
     * @return
     */
    public MyDialogBuilder setMaxHeight() {
        ViewTreeObserver vto = mydialog_content.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mydialog_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int cHeight = mydialog_content.getHeight();
                ViewGroup.LayoutParams lp = mydialog_content.getLayoutParams();
                if (cHeight >= wHeight * 1 / 2) {
                    lp.height = wHeight * 1 / 2;
                }
                mydialog_content.setLayoutParams(lp);
            }
        });
        return this;
    }

    /**
     * 设置最大高度
     *
     * @return
     */
    public MyDialogBuilder setMaxHeight(final View view) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int cHeight = view.getHeight();
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (cHeight >= wHeight * 1 / 3) {
                    lp.height = wHeight * 1 / 3;
                }
                view.setLayoutParams(lp);
            }
        });
        return this;
    }


    /**
     * 显示Dialog
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        new BaseEffects().start(mydialog_main, startEffects);
    }

    /**
     * 消失动画
     *
     * @param mAnimatorSet
     */
    private void dismissAnimation(BaseEffects mAnimatorSet) {
        mAnimatorSet.start(mydialog_main, dismissEffects);
    }


    /**
     * Dialog消失
     */
    @Override
    public void dismiss() {
        isDismissClickEveryWhere = false;
        instance = null;
        BaseEffects mAnimatorSet = new BaseEffects();
        dismissAnimation(mAnimatorSet);
        if (mAnimatorSet.getAnimatorSet() != null) {
            // 监听动画执行
            mAnimatorSet.getAnimatorSet().addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    superDismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            superDismiss();
        }
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(mydialog_content.getWindowToken(), 0);
        }
    }

    /**
     * Dialog消失无动画
     */
    public void dismissNoAnimator() {
        instance = null;
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(mydialog_content.getWindowToken(), 0);
        }
        superDismiss();
    }

    /**
     * 继承父类消失Dialog
     */
    public void superDismiss() {
        super.dismiss();
    }


    public class BaseEffects {
        public final int DURATION = 1 * 200; // 动画执行时间
        private AnimatorSet mAnimatorSet;

        {
            mAnimatorSet = new AnimatorSet();
        }

        protected void setupAnimation(View view, int showtype) {
            switch (showtype) {
                case SlideTop:
//                    mAnimatorSet = new AnimatorSet();
//                    mAnimatorSet.playTogether(
//                            ObjectAnimator.ofFloat(view, "translationY", -wHeight * 1 / 2, 0).setDuration(DURATION),
//                            ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(DURATION * 3 / 2));
                    break;
                case SlideTopDismiss:
                    mAnimatorSet = new AnimatorSet();
                    mAnimatorSet.playTogether(
                            ObjectAnimator.ofFloat(view, "translationY", 0, wHeight * 1 / 2).setDuration(DURATION),
                            ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(DURATION));
                    break;
                default:
                    break;
            }
        }

        // 执行动画
        public void start(View view, int showtype) {
            reset(view);
            setupAnimation(view, showtype);
            mAnimatorSet.start();
        }

        public void reset(View view) {
            ViewHelper.setAlpha(view, 1);
            ViewHelper.setScaleX(view, 1);
            ViewHelper.setScaleY(view, 1);
            ViewHelper.setTranslationX(view, 0);
            ViewHelper.setTranslationY(view, 0);
            ViewHelper.setRotation(view, 0);
            ViewHelper.setRotationY(view, 0);
            ViewHelper.setRotationX(view, 0);
            ViewHelper.setPivotX(view, view.getMeasuredWidth() / 2.0f);
            ViewHelper.setPivotY(view, view.getMeasuredHeight() / 2.0f);
        }

        public AnimatorSet getAnimatorSet() {
            return mAnimatorSet;
        }
    }

    // 获得屏幕高度
    public int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    // 获得屏幕宽度
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
