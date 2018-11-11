package com.zyd.wlwsdk.widge.swipe;

/**
 * Created by LinKorea on 2017/3/27.
 */



public interface SwipeLayoutInterface {
    SwipeLayout.Status getCurrentStatus();

    void close();

    void open();
}
