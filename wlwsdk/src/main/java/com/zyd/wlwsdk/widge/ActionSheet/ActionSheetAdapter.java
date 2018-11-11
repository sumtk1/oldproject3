package com.zyd.wlwsdk.widge.ActionSheet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zyd.wlwsdk.R;

import java.util.ArrayList;

/**
 * Created by hygo00 on 2016/9/24.
 */

public class ActionSheetAdapter extends RecyclerView.Adapter<ActionSheetAdapter.MyViewHolder> {

    private ArrayList<String[]> datas = new ArrayList<>();
    private int width;

    public ActionSheetAdapter(ArrayList<String[]> datas, int width) {
        this.datas = datas;
        this.width = width;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.as_item_recyclerview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.img.setImageResource(Integer.parseInt(datas.get(position)[0]));
        holder.tv.setText(datas.get(position)[1]);

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.ll.getLayoutParams();
        lp.width = width;
        lp.height = width;
        holder.ll.setLayoutParams(lp);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        ImageView img;
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_as_recyclerview);
            img = (ImageView) view.findViewById(R.id.img_as_recyclerview);
            tv = (TextView) view.findViewById(R.id.tv_as_recyclerview);
        }
    }
}
