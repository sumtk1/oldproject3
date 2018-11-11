package com.gloiot.hygoSupply.ui.activity.wode.fadongtai.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.BianJiNeiRongActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.BianjiShangpinActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.TianJiaNeiRongActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.XuanZeiShanagPingLianJIeActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.DynamicProductModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.ProductModel;
import com.gloiot.hygoSupply.ui.activity.wode.shimingrenzheng.ShiMingRenZhengActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

import java.util.List;


/**
 * 作者：Ljy on 2017/8/12.
 * 功能：我的——我的资料
 */


public abstract class DynamicProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_ONE = 1;
    private static final int ITEM_TYPE_TWO = 2;
    private static final int ITEM_TYPE_HEADER = -1;
    private static final int ITEM_TYPE_FOOTER = -2;
    private Context context;
    private DynamicProductModel dynamicProductModel;


    public DynamicProductAdapter(Context context, DynamicProductModel dynamicProductModel) {
        this.context = context;
        this.dynamicProductModel = dynamicProductModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_ONE:
                return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dynamic_pruduct2, parent, false), viewType);
            case ITEM_TYPE_TWO:
                return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dynamic_pruduct3, parent, false), viewType);
            case ITEM_TYPE_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dynamic_pruduct1, parent, false));
            case ITEM_TYPE_FOOTER:
                return new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dynamic_pruduct4, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            if (dynamicProductModel.isMulit()) {
                headerViewHolder.rl_dynamic_product_link.setVisibility(View.GONE);
                headerViewHolder.rl_dynamic_product_addimage.setVisibility(View.GONE);
                headerViewHolder.ll_dynamic_product_single.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_white));
                headerViewHolder.ll_dynamic_product_multi.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_ff7f29));
                headerViewHolder.tv_dynamic_product_single.setTextColor(Color.parseColor("#999999"));
                headerViewHolder.tv_dynamic_product_multi.setTextColor(Color.parseColor("#ff7f29"));
                CommonUtils.setDisplayImageOptions(headerViewHolder.iv_dynamic_product_multi, R.mipmap.dianji, 0);
                CommonUtils.setDisplayImageOptions(headerViewHolder.iv_dynamic_product_single, R.mipmap.weidianji, 0);
            } else {
                headerViewHolder.rl_dynamic_product_link.setVisibility(View.VISIBLE);
                headerViewHolder.rl_dynamic_product_addimage.setVisibility(View.VISIBLE);
                headerViewHolder.ll_dynamic_product_multi.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_white));
                headerViewHolder.ll_dynamic_product_single.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_ff7f29));
                headerViewHolder.tv_dynamic_product_multi.setTextColor(Color.parseColor("#999999"));
                headerViewHolder.tv_dynamic_product_single.setTextColor(Color.parseColor("#ff7f29"));
                headerViewHolder.tv_dynamic_product_link.setText(dynamicProductModel.getLink());
                CommonUtils.setDisplayImageOptions(headerViewHolder.iv_dynamic_product_single, R.mipmap.dianji, 0);
                CommonUtils.setDisplayImageOptions(headerViewHolder.iv_dynamic_product_multi, R.mipmap.weidianji, 0);
                CommonUtils.setDisplayImageOptions(headerViewHolder.iv_dynamic_product_showimg, dynamicProductModel.getTitleImg(), 0);
            }
            headerViewHolder.et_dynamic_product_title.setText(dynamicProductModel.getTitle());
            headerViewHolder.et_dynamic_product_title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    headerViewHolder.tv_dynamic_product_titlenum.setText(charSequence.toString().length() + "/50");
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    dynamicProductModel.setTitle(editable.toString());
                }
            });
            headerViewHolder.ll_dynamic_product_single.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dynamicProductModel.setMulit(false);
                    notifyDataSetChanged();
                }
            });
            headerViewHolder.ll_dynamic_product_multi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dynamicProductModel.setMulit(true);
                    notifyDataSetChanged();
                }
            });
            headerViewHolder.tv_dynamic_product_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).startActivityForResult(new Intent(context, XuanZeiShanagPingLianJIeActivity.class), 0);
                }
            });
            headerViewHolder.iv_dynamic_product_addimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPicture();
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            if (dynamicProductModel.isMulit()) {
                if (dynamicProductModel.getProductModelsMulit().size() > 8) {
                    footerViewHolder.rl_dynamic_product_footer.setVisibility(View.GONE);
                } else {
                    footerViewHolder.rl_dynamic_product_footer.setVisibility(View.VISIBLE);
                }
            } else {
                if (dynamicProductModel.getProductModelsSingle().size() > 8) {
                    footerViewHolder.rl_dynamic_product_footer.setVisibility(View.GONE);
                } else {
                    footerViewHolder.rl_dynamic_product_footer.setVisibility(View.VISIBLE);
                }
            }


            if (dynamicProductModel.isMulit()) {
                footerViewHolder.tv_dynamic_product_footer.setText("添加商品");
            } else {
                footerViewHolder.tv_dynamic_product_footer.setText("添加内容");
            }

            footerViewHolder.iv_dynamic_product_footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转
                    if (dynamicProductModel.isMulit()) {
                        Intent intent = new Intent(context, BianjiShangpinActivity.class);
                        ((Activity) context).startActivityForResult(intent, 2);
                    } else {
                        Intent intent = new Intent(context, TianJiaNeiRongActivity.class);
                        intent.putExtra("content", "添加内容");
                        intent.putExtra("type", "商品");
                        ((Activity) context).startActivityForResult(intent, 1);
                    }

                }
            });
        } else if (holder instanceof ProductViewHolder) {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            if (dynamicProductModel.isMulit()) {
                productViewHolder.btn_item_dynamic_product_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除

                        DialogUtlis.twoBtnNormal(context, "是否删除商品", "提示", false, "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                                dynamicProductModel.getProductModelsMulit().remove(position - 1);
                                notifyDataSetChanged();
                                MToastUtils.showToast("删除成功");
                            }
                        });


                    }
                });

                productViewHolder.btn_item_dynamic_product_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //edit
                        Intent intent = new Intent(context, BianjiShangpinActivity.class);
                        ProductModel model = dynamicProductModel.getProductModelsMulit().get(position - 1);
                        intent.putExtra("detail", model.getDescribe());
                        intent.putExtra("url", model.getImgUrl());
                        intent.putExtra("name", model.getTitle());
                        intent.putExtra("id", model.getId());
                        intent.putExtra("price", model.getPrice());
                        intent.putExtra("num", model.getNum());
                        intent.putExtra("position", position - 1);
                        ((Activity) context).startActivityForResult(intent, 2);
                    }
                });

                productViewHolder.tv_dynamic_product_name.setText(dynamicProductModel.getProductModelsMulit().get(position - 1).getTitle());
                productViewHolder.tv_dynamic_product_describe.setText(dynamicProductModel.getProductModelsMulit().get(position - 1).getDescribe());

                CommonUtils.setDisplayImageOptions(productViewHolder.iv_dynamic_product_image, dynamicProductModel.getProductModelsMulit().get(position - 1).getImgUrl(), 0);
            } else {
                if (TextUtils.isEmpty(dynamicProductModel.getProductModelsSingle().get(position - 1).getImgUrl())) {
                    productViewHolder.iv_dynamic_product_image.setVisibility(View.GONE);
                } else {
                    productViewHolder.iv_dynamic_product_image.setVisibility(View.VISIBLE);
                    CommonUtils.setDisplayImageOptions(productViewHolder.iv_dynamic_product_image, dynamicProductModel.getProductModelsSingle().get(position - 1).getImgUrl(), 0);
                }
                productViewHolder.tv_dynamic_product_describe.setText(dynamicProductModel.getProductModelsSingle().get(position - 1).getDescribe());
            }

        }
    }

    @Override
    public int getItemCount() {
        if (dynamicProductModel.isMulit()) {
            return dynamicProductModel.getProductModelsMulit().size() + 2;
        } else {
            return dynamicProductModel.getProductModelsSingle().size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        } else {
            if (dynamicProductModel.isMulit()) {
                return ITEM_TYPE_TWO;
            } else {
                return ITEM_TYPE_ONE;
            }

        }
    }

    public abstract void setPicture();

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_dynamic_product_image;
        TextView tv_dynamic_product_describe;
        TextView tv_dynamic_product_name;
        Button btn_item_dynamic_product_delete;
        Button btn_item_dynamic_product_edit;

        public ProductViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case ITEM_TYPE_ONE:
                    iv_dynamic_product_image = (ImageView) itemView.findViewById(R.id.iv_dynamic_product_image);
                    tv_dynamic_product_describe = (TextView) itemView.findViewById(R.id.tv_dynamic_product_describe);
                    break;
                case ITEM_TYPE_TWO:
                    iv_dynamic_product_image = (ImageView) itemView.findViewById(R.id.iv_dynamic_product_image);
                    tv_dynamic_product_describe = (TextView) itemView.findViewById(R.id.tv_dynamic_product_describe);
                    tv_dynamic_product_name = (TextView) itemView.findViewById(R.id.tv_dynamic_product_name);
                    btn_item_dynamic_product_delete = (Button) itemView.findViewById(R.id.btn_item_dynamic_product_delete);
                    btn_item_dynamic_product_edit = (Button) itemView.findViewById(R.id.btn_item_dynamic_product_edit);
                    break;
            }
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_dynamic_product_footer;
        ImageView iv_dynamic_product_footer;
        TextView tv_dynamic_product_footer;

        public FooterViewHolder(View itemView) {
            super(itemView);
            rl_dynamic_product_footer = (RelativeLayout) itemView.findViewById(R.id.rl_dynamic_product_footer);
            iv_dynamic_product_footer = (ImageView) itemView.findViewById(R.id.iv_dynamic_product_footer);
            tv_dynamic_product_footer = (TextView) itemView.findViewById(R.id.tv_dynamic_product_footer);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_dynamic_product_single;
        LinearLayout ll_dynamic_product_multi;
        ImageView iv_dynamic_product_single;
        TextView tv_dynamic_product_single;
        ImageView iv_dynamic_product_multi;
        TextView tv_dynamic_product_multi;
        RelativeLayout rl_dynamic_product_link;
        TextView tv_dynamic_product_link;
        TextView tv_dynamic_product_titlenum;
        EditText et_dynamic_product_title;
        RelativeLayout rl_dynamic_product_addimage;
        ImageView iv_dynamic_product_addimage;
        ImageView iv_dynamic_product_showimg;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ll_dynamic_product_multi = (LinearLayout) itemView.findViewById(R.id.ll_dynamic_product_multi);
            ll_dynamic_product_single = (LinearLayout) itemView.findViewById(R.id.ll_dynamic_product_single);
            iv_dynamic_product_single = (ImageView) itemView.findViewById(R.id.iv_dynamic_product_single);
            tv_dynamic_product_single = (TextView) itemView.findViewById(R.id.tv_dynamic_product_single);
            iv_dynamic_product_multi = (ImageView) itemView.findViewById(R.id.iv_dynamic_product_multi);
            tv_dynamic_product_multi = (TextView) itemView.findViewById(R.id.tv_dynamic_product_multi);
            rl_dynamic_product_link = (RelativeLayout) itemView.findViewById(R.id.rl_dynamic_product_link);
            tv_dynamic_product_link = (TextView) itemView.findViewById(R.id.tv_dynamic_product_link);
            tv_dynamic_product_titlenum = (TextView) itemView.findViewById(R.id.tv_dynamic_product_titlenum);
            et_dynamic_product_title = (EditText) itemView.findViewById(R.id.et_dynamic_product_title);
            rl_dynamic_product_addimage = (RelativeLayout) itemView.findViewById(R.id.rl_dynamic_product_addimage);
            iv_dynamic_product_addimage = (ImageView) itemView.findViewById(R.id.iv_dynamic_product_addimage);
            iv_dynamic_product_showimg = (ImageView) itemView.findViewById(R.id.iv_dynamic_product_showimg);
        }
    }
}
