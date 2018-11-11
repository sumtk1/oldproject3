package com.gloiot.chatsdk.chatui.keyboard.interfaces;

import android.view.ViewGroup;

import com.gloiot.chatsdk.chatui.keyboard.adpater.EmoticonsAdapter;


public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
