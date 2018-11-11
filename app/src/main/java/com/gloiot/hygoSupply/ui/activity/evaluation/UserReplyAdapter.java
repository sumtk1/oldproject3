package com.gloiot.hygoSupply.ui.activity.evaluation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.ShangpinDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.model.EvaluationModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import java.util.List;

/**
 * 作者：Ljy on 2017/8/24.
 * 功能：评论adapter
 */


public class UserReplyAdapter extends RecyclerView.Adapter<UserReplyAdapter.EvaluationViewHolder> {
    private Context context;
    private List<EvaluationModel> evaluationModels;

    public UserReplyAdapter(Context context, List<EvaluationModel> evaluationModels) {
        this.context = context;
        this.evaluationModels = evaluationModels;
    }

    @Override
    public EvaluationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EvaluationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_evaluation_userreply, parent, false));
    }

    @Override
    public void onBindViewHolder(EvaluationViewHolder holder, final int position) {
        EvaluationModel model = evaluationModels.get(position);
        CommonUtils.setDisplayImageOptions(holder.iv_evaluation_product, model.getImageUrl(), 1000);
        holder.tv_evaluation_product_name.setText(model.getName());
        holder.tv_evaluation_content.setText(model.getContent());
        holder.tv_evaluation_time.setText(model.getTime());
        if (model.getEvluationImages().size() > 0) {
            holder.ll_evaluation_images.setVisibility(View.VISIBLE);
            for (int i = 0; i < model.getEvluationImages().size(); i++) {
                CommonUtils.setDisplayImage(holder.imageViews[i], model.getEvluationImages().get(i), 0, 0);
            }
        } else {
            holder.ll_evaluation_images.setVisibility(View.GONE);
        }


        holder.rl_evaluation_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserEvaluationActivity.class);
                intent.putExtra("productId", evaluationModels.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return evaluationModels.size();
    }

    class EvaluationViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_evaluation_product;
        ImageView iv_evaluation_product;
        TextView tv_evaluation_product_name;
        TextView tv_evaluation_time;
        TextView tv_evaluation_content;
        LinearLayout ll_evaluation_images;
        ImageView iv_evaluation_image1;
        ImageView iv_evaluation_image2;
        ImageView iv_evaluation_image3;
        ImageView iv_evaluation_image4;
        ImageView[] imageViews;

        public EvaluationViewHolder(View itemView) {
            super(itemView);
            iv_evaluation_product = (ImageView) itemView.findViewById(R.id.iv_evaluation_product);
            tv_evaluation_product_name = (TextView) itemView.findViewById(R.id.tv_evaluation_product_name);
            tv_evaluation_time = (TextView) itemView.findViewById(R.id.tv_evaluation_time);
            tv_evaluation_content = (TextView) itemView.findViewById(R.id.tv_evaluation_content);
            ll_evaluation_images = (LinearLayout) itemView.findViewById(R.id.ll_evaluation_images);
            iv_evaluation_image1 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image1);
            iv_evaluation_image2 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image2);
            iv_evaluation_image3 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image3);
            iv_evaluation_image4 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image4);
            rl_evaluation_product = (RelativeLayout) itemView.findViewById(R.id.rl_evaluation_product);
            imageViews = new ImageView[]{iv_evaluation_image1, iv_evaluation_image2, iv_evaluation_image3, iv_evaluation_image4};

        }
    }
}
