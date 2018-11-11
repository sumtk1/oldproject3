package com.gloiot.hygoSupply.utlis;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

/**
 * Created by ZXL on 2017/6/22.
 *
 * 文字图片混排
 *
 */

public class ImageSpanUtil {
    private static ImageSpanUtil mViewUtil = null;

    public static ImageSpanUtil getInstance () {
        if (mViewUtil == null) {
            mViewUtil = new ImageSpanUtil();
        }
        return mViewUtil;
    }

    //显示图片
    public void setImage(Context context,TextView mShowTv, int dra, String content) {
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(dra, null);
        } else {
            drawable = context.getResources().getDrawable(dra);
        }
        if (drawable == null) return;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        SpannableString spannableString = new SpannableString("pics");

        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);

        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

//        mShowTv.append(colourTxtCashBalance("100.00"));
        mShowTv.append(spannableString);//将图片转为文字显示，这个是重点
        mShowTv.append(content);

    }


    //文字变色的部分
    private Spanned colourTxtCashBalance(String money) {
        return Html.fromHtml("<font color='#1E1E1E'>现在将变色</font><font color='#FF6138'>￥" + money+ "</font><font color='#1E1E1E'>元啦！</font>");
    }
    private Spanned colourTxtNoncashBalance(String money) {
        return Html.fromHtml("<font color='#1E1E1E'>再次变色</font><font color='#FF5678'>￥" + money+ "</font><font color='#1E1E1E'>元，瞬间升值10%！厉害吧。</font>");
    }

}
