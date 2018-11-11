package com.zyd.wlwsdk.widge.swipe;

import java.util.HashSet;

/**
 * Created by LinKorea on 2017/3/27.
 */

public class MySwipe {
    static HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

    public static SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener() {

        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mUnClosedLayouts.add(swipeLayout);
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
            mUnClosedLayouts.remove(swipeLayout);
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
            closeAllLayout();
            mUnClosedLayouts.add(swipeLayout);
        }
    };

    public static void closeAllLayout() {
        if (mUnClosedLayouts.size() == 0)
            return;

        for (SwipeLayout l : mUnClosedLayouts) {
            l.close(true, false);
        }
        mUnClosedLayouts.clear();
    }

    public static int getUnClosedCount() {
        return mUnClosedLayouts.size();
    }
}
