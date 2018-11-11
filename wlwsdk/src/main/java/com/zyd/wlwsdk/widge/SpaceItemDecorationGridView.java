package com.zyd.wlwsdk.widge;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Admin on 2016/9/18.
 */
public class SpaceItemDecorationGridView extends RecyclerView.ItemDecoration {

    private int space;
    private int ColumnsNum;

    public SpaceItemDecorationGridView(int space, int ColumnsNum) {
        this.space = space;
        this.ColumnsNum = ColumnsNum;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距

        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (ColumnsNum == 1) {
            outRect.left = 0;
            outRect.bottom = space;
        } else if (parent.getChildLayoutPosition(view) % ColumnsNum == 0) {
            outRect.left = 0;
            outRect.bottom = space;
        } else {
            outRect.left = space;
            outRect.bottom = space;
        }
    }
}

