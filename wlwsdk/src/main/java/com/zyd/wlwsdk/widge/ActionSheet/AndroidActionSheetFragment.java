package com.zyd.wlwsdk.widge.ActionSheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zyd.wlwsdk.R;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.recyclerview.CommonAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by hygo00 on 2016/9/24.
 */
public class AndroidActionSheetFragment extends Fragment {

    //是否已经关闭
    boolean isDismiss = true;

    View decorView;
    //添加进入的view
    View realView;
    //添加进入的第一个view
    View ll_as_child;

    TextView tv_as_cancel;
    RecyclerView rv_as_top;
    RecyclerView rv_as_bottom;

    public static AndroidActionSheetFragment newGridInstance(String[] items, int[] images, String[] items2, int[] images2) {
        AndroidActionSheetFragment fragment = new AndroidActionSheetFragment();
        Bundle bundle = new Bundle();
        if (items==null){
            bundle.putStringArray("items", items);
            bundle.putIntArray("images", images);
        }
        bundle.putStringArray("items2", items2);
        bundle.putIntArray("images2", images2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 判断键盘是否弹出，隐藏软键盘
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            View focusView = getActivity().getCurrentFocus();
            manager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }

        realView = inflater.inflate(R.layout.as_view, container, false);
        initViews(realView);
        decorView = getActivity().getWindow().getDecorView();
        ((ViewGroup) decorView).addView(realView);
        startPlay();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initViews(View view) {
        ll_as_child = view.findViewById(R.id.ll_as_child);
        ll_as_child.setVisibility(View.INVISIBLE);
        tv_as_cancel = (TextView) view.findViewById(R.id.tv_as_cancel);
        rv_as_top = (RecyclerView) view.findViewById(R.id.rv_as_top);
        rv_as_bottom = (RecyclerView) view.findViewById(R.id.rv_as_bottom);


        realView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_as_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelListener != null) {
                    onCancelListener.onCancelClick();
                }
                dismiss();
            }
        });

//        final int width = (getScreenWidth(getActivity()) - dp2px(getActivity(), 20)) / 4;

        final int width = getScreenWidth(getActivity()) / 5;
        if (getArguments().getStringArray("items")!= null){
            ArrayList<String[]> mDatas = new ArrayList<>();
            for (int i = 0; i < getArguments().getStringArray("items").length; i++) {
                String[] d = new String[2];
                d[0] = String.valueOf(getArguments().getIntArray("images")[i]);
                d[1] = getArguments().getStringArray("items")[i];
                mDatas.add(d);
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            rv_as_top.setLayoutManager(linearLayoutManager);
            ActionSheetAdapter actionSheetAdapter2 = new ActionSheetAdapter(mDatas, width);
            rv_as_top.setAdapter(actionSheetAdapter2);
            rv_as_top.setAdapter(new CommonAdapter<String[]>(getActivity(), R.layout.as_item_recyclerview, mDatas) {
                @Override
                public void convert(final ViewHolder holder, String[] s) {
                    holder.setText(R.id.tv_as_recyclerview, s[1]);
                    holder.setImageResource(R.id.img_as_recyclerview, Integer.parseInt(s[0]));
                    LinearLayout linearLayout = holder.getView(R.id.ll_as_recyclerview);
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) linearLayout.getLayoutParams();
                    lp.width = width;
                    lp.height = width;
                    linearLayout.setLayoutParams(lp);

                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onItemClickListener_top != null) {
                                onItemClickListener_top.onItemClick(holder.getmPosition());
                                dismiss();
                            }
                        }
                    });
                }
            });
        }


        ArrayList<String[]> mDatas2 = new ArrayList<>();
        for (int i = 0; i < getArguments().getStringArray("items2").length; i++) {
            String[] d = new String[2];
            d[0] = String.valueOf(getArguments().getIntArray("images2")[i]);
            d[1] = getArguments().getStringArray("items2")[i];
            mDatas2.add(d);
        }
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(OrientationHelper.HORIZONTAL);
        rv_as_bottom.setLayoutManager(linearLayoutManager2);
        rv_as_bottom.setAdapter(new CommonAdapter<String[]>(getActivity(), R.layout.as_item_recyclerview, mDatas2) {
            @Override
            public void convert(final ViewHolder holder, String[] s) {
                holder.setText(R.id.tv_as_recyclerview, s[1]);
                holder.setImageResource(R.id.img_as_recyclerview, Integer.parseInt(s[0]));
                LinearLayout linearLayout = holder.getView(R.id.ll_as_recyclerview);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) linearLayout.getLayoutParams();
                lp.width = width;
                lp.height = width+100;
                linearLayout.setLayoutParams(lp);

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener_bottom != null) {
                            onItemClickListener_bottom.onItemClick(holder.getmPosition());
                            dismiss();
                        }
                    }
                });
            }
        });

    }

    private void show(final FragmentManager manager, final String tag) {
        if (manager.isDestroyed() || !isDismiss) {
            return;
        }
        isDismiss = false;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(AndroidActionSheetFragment.this, tag);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    private void dismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss = true;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getChildFragmentManager().popBackStack();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.remove(AndroidActionSheetFragment.this);
                transaction.commitAllowingStateLoss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopPlay();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ViewGroup) decorView).removeView(realView);
            }
        }, 500);
    }

    public static Builder build(FragmentManager fragmentManager) {
        Builder builder = new Builder(fragmentManager);
        return builder;
    }

    public static class Builder {

        FragmentManager fragmentManager;

        //默认tag，用来校验fragment是否存在
        String tag = "ActionSheetFragment";
        //ActionSheet上ListView或者GridLayout上相关文字、图片
        String[] items;
        int[] images;
        String[] items2;
        int[] images2;
        //ActionSheet点击后的回调
        OnItemClickListener onItemClickListenerTop;
        OnItemClickListener onItemClickListenerBottom;
        //点击取消之后的回调
        OnCancelListener onCancelListener;


        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setItems(String[] items) {
            this.items = items;
            return this;
        }

        public Builder setImages(int[] images) {
            this.images = images;
            return this;
        }

        public Builder setItems2(String[] items) {
            this.items2 = items;
            return this;
        }

        public Builder setImages2(int[] images) {
            this.images2 = images;
            return this;
        }

        public Builder setOnItemClickListenerTop(OnItemClickListener onItemClickListener) {
            this.onItemClickListenerTop = onItemClickListener;
            return this;
        }

        public Builder setOnItemClickListenerBottom(OnItemClickListener onItemClickListener) {
            this.onItemClickListenerBottom = onItemClickListener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }


        public void show() {
            AndroidActionSheetFragment fragment;
            fragment = AndroidActionSheetFragment.newGridInstance(items, images, items2, images2);
            fragment.setOnItemClickListenerTop(onItemClickListenerTop);
            fragment.setOnItemClickListenerBottom(onItemClickListenerBottom);
            fragment.setOnCancelListener(onCancelListener);
            fragment.show(fragmentManager, tag);
        }

    }

    OnItemClickListener onItemClickListener_top;
    OnItemClickListener onItemClickListener_bottom;
    OnCancelListener onCancelListener;

    public interface OnCancelListener {
        void onCancelClick();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListenerTop(OnItemClickListener onItemClickListener) {
        this.onItemClickListener_top = onItemClickListener;
    }

    public void setOnItemClickListenerBottom(OnItemClickListener onItemClickListener) {
        this.onItemClickListener_bottom = onItemClickListener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 得到屏幕高度
     *
     * @return 单位:px
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 得到屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private void startPlay() {
        ll_as_child.post(new Runnable() {
            @Override
            public void run() {
                final int moveHeight = ll_as_child.getMeasuredHeight();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(100);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        ll_as_child.setVisibility(View.VISIBLE);
                    }
                });
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                        realView.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(), Color.parseColor("#00000000"), Color.parseColor("#70000000")));
                        //当底部存在导航栏并且decorView获取的高度不包含底部状态栏的时候，需要去掉这个高度差
                        if (getNavBarHeight(ll_as_child.getContext()) > 0 && decorView.getMeasuredHeight() != getScreenHeight(ll_as_child.getContext())) {
                            ll_as_child.setTranslationY((moveHeight + getNavBarHeight(ll_as_child.getContext())) * (1 - animation.getAnimatedFraction()) - getNavBarHeight(ll_as_child.getContext()));
                        } else {
                            ll_as_child.setTranslationY(moveHeight * (1 - animation.getAnimatedFraction()));
                        }
                    }
                });
                valueAnimator.start();
            }
        });
    }

    private void stopPlay() {
        ll_as_child.post(new Runnable() {
            @Override
            public void run() {
                final int moveHeight = ll_as_child.getMeasuredHeight();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(100);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                        realView.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(), Color.parseColor("#70000000"), Color.parseColor("#00000000")));
                        if (getNavBarHeight(ll_as_child.getContext()) > 0 && decorView.getMeasuredHeight() != getScreenHeight(ll_as_child.getContext())) {
                            ll_as_child.setTranslationY((moveHeight + getNavBarHeight(ll_as_child.getContext())) * animation.getAnimatedFraction() - getNavBarHeight(ll_as_child.getContext()));
                        } else {
                            ll_as_child.setTranslationY(moveHeight * animation.getAnimatedFraction());
                        }
                    }
                });
                valueAnimator.start();
            }
        });
    }

    /**
     * 获取底部导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }

        return navigationBarHeight;
    }

    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasNavigationBar;
    }
}
