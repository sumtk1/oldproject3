package com.gloiot.hygoSupply.utlis;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ZXL on 2017/6/22.
 */

public class ViewUtil {
    private static ViewUtil mViewUtil = null;

    public static ViewUtil getInstance () {
        if (mViewUtil == null) {
            mViewUtil = new ViewUtil();
        }
        return mViewUtil;
    }

    public void setVisible (Activity context, int resid, boolean visible){
        if (visible) {
            context.findViewById(resid).setVisibility(View.VISIBLE);
        } else {
            context.findViewById(resid).setVisibility(View.GONE);
        }
    }
    public void setVisible (TextView view, boolean visible){
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
