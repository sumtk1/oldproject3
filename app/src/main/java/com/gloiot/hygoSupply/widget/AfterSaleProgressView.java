package com.gloiot.hygoSupply.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.gloiot.hygoSupply.R;

/**
 * 作者：Ljy on 2017/9/18.
 * 功能：我的——我的资料
 */


public class AfterSaleProgressView extends View {

    private Paint paint;
    private String state, timeTop, timeBottom;
    private Bitmap bitmap;
    private int paintColor;
    private int leftLineColor;
    private int rightLineColor;
    private Rect rect;
    private Rect rect1;
    private Context context;
    int width, height;
    int bmpWidth, bmpHeight;

    public AfterSaleProgressView(Context context) {
        super(context);
        this.context = context;
        init();

    }

    public AfterSaleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AfterSaleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        rect1 = new Rect(dip2px(42), dip2px(19), dip2px(48), dip2px(25));
        paint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, rect, rect1, paint);
        }
        if (leftLineColor != 0) {
            paint.setColor(leftLineColor);
            canvas.drawLine(width / 2, dip2px(22), 0, dip2px(22), paint);
        }
        if (rightLineColor != 0) {
            paint.setColor(rightLineColor);
            canvas.drawLine(width / 2, dip2px(22), width, dip2px(22), paint);
        }
        paint.setColor(paintColor);
        paint.setTextSize(dip2px(12));
        canvas.drawText(state, width / 2, dip2px(45), paint);
        if (!TextUtils.isEmpty(timeTop)) {
            paint.setTextSize(dip2px(11));
            canvas.drawText(timeTop, width / 2, dip2px(68), paint);
        }
        if (!TextUtils.isEmpty(timeBottom)) {
            paint.setTextSize(dip2px(11));
            canvas.drawText(timeBottom, width / 2, dip2px(80), paint);
        }
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimeTop() {
        return timeTop;
    }

    public void setTimeTop(String timeTop) {
        this.timeTop = timeTop;
    }

    public String getTimeBottom() {
        return timeBottom;
    }

    public void setTimeBottom(String timeBottom) {
        this.timeBottom = timeBottom;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        bmpWidth = bitmap.getWidth();
        bmpHeight = bitmap.getHeight();
        rect = new Rect(0, 0, bmpWidth, bmpHeight);

    }

    public void setBitmap2(Bitmap bitmap) {
        this.bitmap = bitmap;
        bmpWidth = bitmap.getWidth();
        bmpHeight = bitmap.getHeight();
        rect = new Rect(0, 0, bmpWidth, bmpHeight);
        rect1 = new Rect(dip2px(40), dip2px(15), dip2px(55), dip2px(28));

    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    public int getLeftLineColor() {
        return leftLineColor;
    }

    public void setLeftLineColor(int leftLineColor) {
        this.leftLineColor = leftLineColor;
    }

    public int getRightLineColor() {
        return rightLineColor;
    }

    public void setRightLineColor(int rightLineColor) {
        this.rightLineColor = rightLineColor;
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
