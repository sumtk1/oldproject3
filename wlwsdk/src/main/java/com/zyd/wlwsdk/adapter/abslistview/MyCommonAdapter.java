package com.zyd.wlwsdk.adapter.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.zyd.wlwsdk.adapter.ViewHolder;

import java.util.List;

public abstract class MyCommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public MyCommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = null;
        ViewHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = ViewHolder.get(mContext, view, parent,
                    layoutId, position);
        } else {
            holder = ViewHolder.get(mContext, convertView, parent,
                    layoutId, position);
        }
//        holder.setTag(135303903, position);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, int position);

}
