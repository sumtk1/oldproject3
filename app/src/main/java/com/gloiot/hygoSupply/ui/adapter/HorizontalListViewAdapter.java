package com.gloiot.hygoSupply.ui.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;


public class HorizontalListViewAdapter extends BaseAdapter {
    private String[] mTitles;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(String[] mTitles, Context mContext) {
        this.mTitles = mTitles;
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            Log.e("convertView", "convertView空");
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_horizontal_monthlist, null);
            holder.mTitle = (TextView) convertView.findViewById(R.id.text_list_item);
            holder.view_bottom = convertView.findViewById(R.id.view_bottom);
            convertView.setTag(holder);
        } else {
            Log.e("convertView", "convertView非空");
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == selectIndex) {
            Log.e("aaa", "设置" + position + "显示底边");
            holder.view_bottom.setVisibility(View.VISIBLE);
        } else {
            holder.view_bottom.setVisibility(View.GONE);
        }
        holder.mTitle.setText(mTitles[position]);
        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle;
        private View view_bottom;

    }

    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}