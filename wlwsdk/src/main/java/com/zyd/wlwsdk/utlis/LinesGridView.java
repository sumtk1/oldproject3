package com.zyd.wlwsdk.utlis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * 创建人： zengming on 2017/10/12.
 * 功能：带分割线的GridView（不能单独使用，失去了滑动功能）
 * 与ScrollView配合使用时，不需要动态计算自身的高度
 */
public class LinesGridView extends GridView {
    public LinesGridView(Context context) {
        super(context);
    }

    public LinesGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinesGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        View localView1 = getChildAt(0);
        int column = getWidth() / localView1.getWidth();//列数
        int childCount = getChildCount();
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStrokeWidth(2.0f);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setColor(Color.parseColor("#E0E0E0"));//这个就是设置分割线的颜色
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);
            if ((i + 1) % column == 0) {//每一行最后一个
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            } else if ((i + 1) > (childCount - (childCount % column))) {//最后一行的item
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
            } else {
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            }
        }
    }
}