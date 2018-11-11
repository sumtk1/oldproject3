package com.gloiot.hygoSupply.utlis;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;

/**
 * 作者：Ljy on 2017/11/29.
 * 功能：databingding的Adapter
 */


public class MyBindingAdapter {

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, boolean isChecked) {
        if (isChecked) {
            imageView.setImageResource(R.mipmap.ic_freight_type_check);
        } else {
            imageView.setImageResource(R.mipmap.ic_freight_type_normal);
        }
    }

    @BindingAdapter({"android:background"})
    public static void setRelativeLayoutBackground(RelativeLayout relativeLayout, boolean isChecked) {
        if (isChecked) {
            relativeLayout.setBackgroundResource(R.drawable.bg_corners_3_ff7f29);
        } else {
            relativeLayout.setBackgroundResource(R.drawable.bg_corners_3_ffffff);
        }
    }

    @BindingAdapter({"android:textColor"})
    public static void setTextViewTextColor(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setTextColor(Color.parseColor("#ff7f29"));
        } else {
            textView.setTextColor(Color.parseColor("#333333"));
        }
    }

    @BindingAdapter({"android:minHeight"})
    public static void setEdittextMinHeight(EditText editText, boolean isChecked) {
        if (isChecked) {
            editText.setMinHeight(DensityUtil.dip2px(editText.getContext(), 80.0f));
        } else {
            editText.setMinHeight(DensityUtil.dip2px(editText.getContext(), 50.0f));
        }
    }
}
