package com.gloiot.hygoSupply.ui.fragment;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.gloiot.hygoSupply.R;

/**
 * 作者：Ljy on 2017/3/24.
 * 邮箱：enjoy_azad@sina.com
 */

public class HomePageFragmentBehavior extends CoordinatorLayout.Behavior<Button> {
    int i = 0;

    public HomePageFragmentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Button child, View dependency) {
        return dependency.getId() == R.id.nested_scroll_view;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Button child, View dependency) {
        i++;
        child.setText("收到变化" + i);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, Button child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, Button child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        child.setText(dxConsumed+"");
    }
}
