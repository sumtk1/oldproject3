package com.gloiot.hygoSupply.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.GuigeModel;
import com.gloiot.hygoSupply.databinding.ItemShangpinGuigeNewBinding;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/3/17.
 */

public class GuigeAdapter extends RecyclerView.Adapter<GuigeViewHolder> {
    private List<GuigeModel> guigeModelList;
    private LayoutInflater inflaterl;
    private List<GuigeViewHolder> viewHolders;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public OnItemClickListener clickListener;

    public GuigeAdapter(Context context, final List<GuigeModel> guigeModelList) {
        this.guigeModelList = guigeModelList;
        viewHolders = new ArrayList<>();
        inflaterl = LayoutInflater.from(context);
        clickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GuigeModel model = guigeModelList.get(position);
                DecimalFormat df = new DecimalFormat("######0.00");
                switch (view.getId()) {
                    case R.id.iv_item_shangpin_guige_shanchu:
                        if (guigeModelList.size() == 2) {
                            guigeModelList.remove(position);
                            notifyItemRemoved(position);
                            guigeModelList.get(0).setOnlyOne(true);
                            notifyItemChanged(0);
                        } else {
                            guigeModelList.remove(position);
                            notifyItemRemoved(position);
                        }
                        break;
                    case R.id.iv_item_guige_gonghuojia_add:
                        if (model.isIncreaseGonghuojia()) {
                            MToastUtils.showToast("当月不可再提高价格");
                        } else {
                            Double gonghuijia = Double.parseDouble(model.getGonghuojia());
                            if (gonghuijia > 100) {
                                gonghuijia += 0.5;
                            } else {
                                gonghuijia += 0.1;
                            }

                            if (gonghuijia > model.getMaxGonghuojia()) {
                                MToastUtils.showToast("已达最大值");
                            } else {

                                model.setGonghuojia(df.format(gonghuijia));
                            }
                        }
                        break;
                    case R.id.iv_item_guige_jianyijia_add:
                        if (model.isIncreaseJianyijia()) {
                            MToastUtils.showToast("当月不可再提高价格");
                        } else {
                            Double jianyijia = Double.parseDouble(model.getJianyijia());
                            if (jianyijia > 100) {
                                jianyijia += 0.5;
                            } else {
                                jianyijia += 0.1;
                            }

                            if (jianyijia > model.getMaxJianyijia()) {
                                MToastUtils.showToast("已达最大值");
                            } else {
                                model.setJianyijia(df.format(jianyijia));
                            }
                        }
                        break;
                    case R.id.iv_item_guige_kucun_add:
                        int kucun = Integer.parseInt(model.getKucun());
                        kucun += 1;
                        if (kucun > 10000) {
                            MToastUtils.showToast("已达最大值");
                        } else {
                            model.setKucun(kucun + "");
                        }
                        break;
                    case R.id.iv_item_guige_gonghuojia_sub:
                        Double gonghuijia = Double.parseDouble(model.getGonghuojia());
                        if (gonghuijia > 100) {
                            gonghuijia -= 0.5;
                        } else {
                            gonghuijia -= 0.1;
                        }
                        if (gonghuijia <= 0) {
                            MToastUtils.showToast("已达最小值");
                        } else {
                            model.setGonghuojia(df.format(gonghuijia));
                        }
                        break;
                    case R.id.iv_item_guige_jianyijia_sub:
                        Double jianyijia = Double.parseDouble(model.getJianyijia());
                        if (jianyijia > 100) {
                            jianyijia -= 0.5;
                        } else {
                            jianyijia -= 0.1;
                        }

                        if (jianyijia <= 0) {
                            MToastUtils.showToast("已达最小值");
                        } else {
                            model.setJianyijia(df.format(jianyijia));
                        }

                        break;
                    case R.id.iv_item_guige_kucun_sub:
                        int kucunSub = Integer.parseInt(model.getKucun());
                        kucunSub -= 1;
                        if (kucunSub < 0) {
                            MToastUtils.showToast("已达最小值");
                        } else {
                            model.setKucun(kucunSub + "");
                        }
                        break;
                }
                notifyItemChanged(position);
            }

        };

    }

    @Override
    public GuigeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemShangpinGuigeNewBinding binding = DataBindingUtil.inflate(inflaterl, viewType, parent, false);
        GuigeViewHolder viewHolder = new GuigeViewHolder(binding, clickListener);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GuigeViewHolder holder, final int position) {
        holder.bind(guigeModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return guigeModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_shangpin_guige_new;
    }


    public void clearFouces() {
        for (GuigeViewHolder viewHolder : viewHolders) {
            viewHolder.clearFouces();
        }
    }
}
