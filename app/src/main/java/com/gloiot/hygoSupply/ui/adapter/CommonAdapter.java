package com.gloiot.hygoSupply.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
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
        convert(holder, getItem(position),position);
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder, T t, int position);

}
