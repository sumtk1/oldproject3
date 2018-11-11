package com.gloiot.hygoSupply.widget;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.jn.chart.components.MarkerView;
import com.jn.chart.data.CandleEntry;
import com.jn.chart.data.Entry;
import com.jn.chart.highlight.Highlight;
import com.jn.chart.utils.Utils;

/**
 * Created by hygo03 on 2017/4/11.
 */

public class MyMakerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView tvContent;
    private int count;

    public MyMakerView(Context context, int layoutResource, int count) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.count = count;
    }

    /* 每次畫 MakerView 時都會觸發這個 Callback 方法，通常會在此方法內更新 View 的內容 */
    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        if (entry instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) entry;
            switch (count) {
                case 0:
                    tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true) + "元");
                    break;
                case 1:
                    tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true) + "人");
                    break;
                case 2:
                    tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true) + "个");
                    break;
            }
        } else {
            switch (count) {
                case 0:
                    tvContent.setText("" +(int)entry.getVal() + "元");
                    Log.e("highlight.getXIndex()",highlight.getXIndex()+"");
                    break;
                case 1:
                    tvContent.setText("" + (int)entry.getVal() + "人");
                    Log.e("highlight.getXIndex()",highlight.getXIndex()+"");
                    break;
                case 2:
                    tvContent.setText("" + (int)entry.getVal() + "个");
                    break;
            }
        }

    }

    /*
    * offset 是以點到的那個點作為 (0,0) 中心然後往右下角畫出來
    * 所以如果要顯示在點的上方
    * X=寬度的一半，負數
    * Y=高度的負數
     */
    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }


}
