package com.gloiot.hygoSupply.ui.activity.wode.shouyi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.evaluation.DisplayPictureActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.EvaluationAdapter;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import java.util.List;

/**
 * 作者：Ljy on 2017/9/13.
 * 功能：我的——我的资料
 */


public class GainRecoredAdapter extends RecyclerView.Adapter<GainRecoredAdapter.GainRecorderViewHolder> {
    private Context context;
    private List<String> list;

    public GainRecoredAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GainRecorderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GainRecoredAdapter.GainRecorderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gain_record_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(GainRecorderViewHolder holder, final int position) {
        CommonUtils.setDisplayImage(holder.imageView, list.get(position), 0, 0);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayPictureActivity.class);
                intent.putExtra("url",list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GainRecorderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public GainRecorderViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_item_gain_record);
        }
    }
}
