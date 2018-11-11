package com.gloiot.hygoSupply.ui.activity.evaluation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.ShangpinDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.WodeDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.model.EvaluationModel;
import com.gloiot.hygoSupply.ui.activity.evaluation.model.ReplyModel;
import com.gloiot.hygoSupply.ui.adapter.CommonAdapter;
import com.gloiot.hygoSupply.ui.adapter.ViewHolder;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import java.util.List;

/**
 * 作者：Ljy on 2017/8/24.
 * 功能：评论adapter
 */


public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.EvaluationViewHolder> {
    private Context context;
    private List<EvaluationModel> evaluationModels;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public EvaluationAdapter(Context context, List<EvaluationModel> evaluationModels) {
        this.context = context;
        this.evaluationModels = evaluationModels;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public EvaluationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EvaluationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_evaluation_manage_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(final EvaluationViewHolder holder, final int position) {
        final EvaluationModel model = evaluationModels.get(position);
        CommonUtils.setDisplayImage(holder.iv_evaluation_product, model.getImageUrl(), 0, 0);
        holder.tv_evaluation_product_name.setText(model.getNickName());
        holder.tv_evaluation_content.setText(model.getContent());
        holder.tv_evaluation_product_describe.setText(model.getDescribe());
        holder.tv_evaluation_time.setText(model.getTime());
//        Log.e("TAG", "onBindViewHolder: " + model.getReply().size());
//        holder.lv_evaluation.setAdapter(new CommonAdapter<ReplyModel>(context, R.layout.item_evaluation_manage_lvitem, model.getReply()) {
//            @Override
//            public void convert(ViewHolder holder, ReplyModel replyModel, int position) {
//                if (replyModel.isClient()) {
//                    holder.setText(R.id.tv_item_evaluation_type, "追加评价:");
//                    holder.setTextColor(R.id.tv_item_evaluation_type, R.color.cl_activity_newmain);
//                } else {
//                    holder.setText(R.id.tv_item_evaluation_type, "掌柜回复:");
//                    holder.setTextColor(R.id.tv_item_evaluation_type, R.color.cl_666);
//                }
//                holder.setText(R.id.tv_item_evaluation_content, replyModel.getContent());
//            }
//        });
//        setListViewHeightBasedOnChildren(holder.lv_evaluation);

//        holder.lv_evaluation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MToastUtils.showToast("点击item" + position);
//            }
//        });
        if (model.getEvluationImages().size() > 0) {
            holder.ll_evaluation_images.setVisibility(View.VISIBLE);
            for (int i = 0; i < holder.imageViews.length; i++) {
                final int s = i;
                if (s < model.getEvluationImages().size()) {
                    holder.imageViews[i].setVisibility(View.VISIBLE);
                    CommonUtils.setDisplayImage(holder.imageViews[i], model.getEvluationImages().get(i), 0, 0);
                    holder.imageViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DisplayPictureActivity.class);
                            intent.putExtra("url", model.getEvluationImages().get(s));
                            Log.e("TAG", "onClick: " + model.getEvluationImages().get(s));
                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.imageViews[i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
            holder.ll_evaluation_images.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(model.getAddTime())) {
            holder.ll_evaluation_addTime.setVisibility(View.VISIBLE);
            holder.ll_evaluation_add.setVisibility(View.GONE);
            holder.tv_item_evaluation_addTime.setText(model.getAddTime());
            holder.tv_item_evaluation_addcontentTime.setText(model.getAdd());
        } else {
            if (TextUtils.isEmpty(model.getAdd())) {
                holder.ll_evaluation_add.setVisibility(View.GONE);
            } else {
                holder.ll_evaluation_add.setVisibility(View.VISIBLE);
                holder.tv_item_evaluation_addcontent.setText(model.getAdd());
            }
            holder.ll_evaluation_addTime.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(model.getReply())) {
            holder.ll_evaluation_merchantreply.setVisibility(View.GONE);
        } else {
            holder.ll_evaluation_merchantreply.setVisibility(View.VISIBLE);
            holder.tv_item_evaluation_replaycontent.setText(model.getReply());
        }
        if (model.getAdditionalImages().size() > 0) {
            holder.ll_evaluation_addimages.setVisibility(View.VISIBLE);
            for (int i = 0; i < holder.addImageViews.length; i++) {
                final int s = i;
                if (s < model.getAdditionalImages().size()) {
                    holder.addImageViews[i].setVisibility(View.VISIBLE);
                    CommonUtils.setDisplayImage(holder.addImageViews[i], model.getAdditionalImages().get(i), 0, 0);
                    holder.addImageViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DisplayPictureActivity.class);
                            intent.putExtra("url", model.getAdditionalImages().get(s));
                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.addImageViews[i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
            holder.ll_evaluation_addimages.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(model.getReplyadd())) {
            holder.ll_evaluation_replayadd.setVisibility(View.GONE);
        } else {
            holder.ll_evaluation_replayadd.setVisibility(View.VISIBLE);
            holder.tv_item_evaluation_replayaddcontent.setText(model.getReplyadd());
        }

        holder.iv_evaluation_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(holder.iv_evaluation_product, position);
                }
            }
        });
        holder.ll_evaluation_addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(holder.iv_evaluation_product, position);
                }
            }
        });
        holder.rl_evaluation_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(holder.rl_evaluation_manager, position);
                }
            }
        });

        holder.tv_evaluation_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(holder.tv_evaluation_content, position);
                }
            }
        });

        holder.tv_item_evaluation_addcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(holder.tv_item_evaluation_addcontent, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return evaluationModels.size();
    }

    class EvaluationViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_evaluation_manager;
        ImageView iv_evaluation_product;
        TextView tv_evaluation_product_name;
        TextView tv_evaluation_time;
        TextView tv_evaluation_product_describe;
        TextView tv_evaluation_content;
        TextView tv_item_evaluation_add;
        //       ListView lv_evaluation;
        LinearLayout ll_evaluation_images;
        ImageView iv_evaluation_image1;
        ImageView iv_evaluation_image2;
        ImageView iv_evaluation_image3;
        ImageView iv_evaluation_image4;
        LinearLayout ll_evaluation_merchantreply;
        TextView tv_item_evaluation_replaycontent;
        LinearLayout ll_evaluation_add;
        TextView tv_item_evaluation_addcontent;
        LinearLayout ll_evaluation_addimages;
        ImageView iv_evaluation_addimage1;
        ImageView iv_evaluation_addimage2;
        ImageView iv_evaluation_addimage3;
        ImageView iv_evaluation_addimage4;
        LinearLayout ll_evaluation_replayadd;
        TextView tv_item_evaluation_replayaddcontent;
        ImageView[] imageViews;
        ImageView[] addImageViews;

        LinearLayout ll_evaluation_addTime;
        TextView tv_item_evaluation_addTime;
        TextView tv_item_evaluation_addcontentTime;


        public EvaluationViewHolder(View itemView) {
            super(itemView);
            iv_evaluation_product = (ImageView) itemView.findViewById(R.id.iv_evaluation_product);
            tv_evaluation_product_name = (TextView) itemView.findViewById(R.id.tv_evaluation_product_name);
            tv_evaluation_time = (TextView) itemView.findViewById(R.id.tv_evaluation_time);
            tv_evaluation_product_describe = (TextView) itemView.findViewById(R.id.tv_evaluation_product_describe);
            tv_evaluation_content = (TextView) itemView.findViewById(R.id.tv_evaluation_content);
//          lv_evaluation = (ListView) itemView.findViewById(R.id.lv_evaluation);
            ll_evaluation_images = (LinearLayout) itemView.findViewById(R.id.ll_evaluation_images);
            ll_evaluation_merchantreply = (LinearLayout) itemView.findViewById(R.id.ll_evaluation_merchantreply);
            ll_evaluation_add = (LinearLayout) itemView.findViewById(R.id.ll_evaluation_add);
            ll_evaluation_addimages = (LinearLayout) itemView.findViewById(R.id.ll_evaluation_addimages);
            ll_evaluation_replayadd = (LinearLayout) itemView.findViewById(R.id.ll_evaluation_replayadd);
            iv_evaluation_image1 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image1);
            iv_evaluation_image2 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image2);
            iv_evaluation_image3 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image3);
            iv_evaluation_image4 = (ImageView) itemView.findViewById(R.id.iv_evaluation_image4);
            iv_evaluation_addimage1 = (ImageView) itemView.findViewById(R.id.iv_evaluation_addimage1);
            iv_evaluation_addimage2 = (ImageView) itemView.findViewById(R.id.iv_evaluation_addimage2);
            iv_evaluation_addimage3 = (ImageView) itemView.findViewById(R.id.iv_evaluation_addimage3);
            iv_evaluation_addimage4 = (ImageView) itemView.findViewById(R.id.iv_evaluation_addimage4);
            tv_item_evaluation_replaycontent = (TextView) itemView.findViewById(R.id.tv_item_evaluation_replaycontent);
            tv_item_evaluation_add = (TextView) itemView.findViewById(R.id.tv_item_evaluation_add);
            tv_item_evaluation_addcontent = (TextView) itemView.findViewById(R.id.tv_item_evaluation_addcontent);
            tv_item_evaluation_replayaddcontent = (TextView) itemView.findViewById(R.id.tv_item_evaluation_replayaddcontent);
            rl_evaluation_manager = (RelativeLayout) itemView.findViewById(R.id.rl_evaluation_manager);
            ll_evaluation_addTime = (LinearLayout) itemView.findViewById(R.id.ll_evaluation_addTime);
            tv_item_evaluation_addTime = (TextView) itemView.findViewById(R.id.tv_item_evaluation_addTime);
            tv_item_evaluation_addcontentTime = (TextView) itemView.findViewById(R.id.tv_item_evaluation_addcontentTime);
            imageViews = new ImageView[]{iv_evaluation_image1, iv_evaluation_image2, iv_evaluation_image3, iv_evaluation_image4};
            addImageViews = new ImageView[]{iv_evaluation_addimage1, iv_evaluation_addimage2, iv_evaluation_addimage3, iv_evaluation_addimage4};
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
