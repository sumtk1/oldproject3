package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleModel;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AttachView;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.Attacher;
import com.gloiot.hygoSupply.widget.AfterSaleProgressView;

/**
 * 作者：Ljy on 2017/9/11.
 * 功能：我的——我的资料
 */


public class ProgressAttcher extends Attacher {
    private AfterSaleModel afterSaleModel;

    public ProgressAttcher(AttachView attacher, Context context) {
        super(attacher, context);
    }

    public ProgressAttcher(AttachView attacher, Context context, AfterSaleModel afterSaleModel) {
        super(attacher, context);
        this.afterSaleModel = afterSaleModel;
    }

    //展示商家信息  status
    @Override
    public void attach(ViewGroup parent) {
        super.attach(parent);
        View attachView = getView(R.layout.item_aftersale_progress);
        if (null != attachView) {
            if ("退款成功".equals(afterSaleModel.getStatus()) || "退款关闭".equals(afterSaleModel.getStatus()) || "等待买家发货".equals(afterSaleModel.getStatus()) || afterSaleModel.getStatus().contains("待确认收货")) {
                AfterSaleProgressView progressView1 = (AfterSaleProgressView) attachView.findViewById(R.id.after_sale_progress1);
                AfterSaleProgressView progressView2 = (AfterSaleProgressView) attachView.findViewById(R.id.after_sale_progress2);
                AfterSaleProgressView progressView3 = (AfterSaleProgressView) attachView.findViewById(R.id.after_sale_progress3);
                AfterSaleProgressView progressView4 = (AfterSaleProgressView) attachView.findViewById(R.id.after_sale_progress4);
                if (null != afterSaleModel) {
                    initProgressView1(afterSaleModel, progressView1);
                    initProgressView2(afterSaleModel, progressView2);
                    initProgressView3(afterSaleModel, progressView3);
                    initProgressView4(afterSaleModel, progressView4);
                }
                parent.addView(attachView);
            }
        }


    }

    public void initProgressView1(AfterSaleModel afterSaleModel, AfterSaleProgressView progressView1) {
        if (afterSaleModel.getProgressInfo().size() > 0) {
            String[] info1 = afterSaleModel.getProgressInfo().get(0);
            Resources res = context.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_point);
            progressView1.setBitmap(bmp);
            progressView1.setPaintColor(Color.parseColor("#ff7f29"));
            progressView1.setRightLineColor(Color.parseColor("#ff7f29"));
            progressView1.setState(info1[0]);
            progressView1.setTimeTop(info1[1]);
            progressView1.setTimeBottom(info1[2]);
            progressView1.invalidate();
        } else {
            progressView1.setVisibility(View.GONE);
        }
    }

    public void initProgressView2(AfterSaleModel afterSaleModel, AfterSaleProgressView progressView2) {
        if (afterSaleModel.getProgressInfo().size() > 1) {
            String[] info1 = afterSaleModel.getProgressInfo().get(1);
            Resources res = context.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_point);
            progressView2.setBitmap(bmp);
            progressView2.setPaintColor(Color.parseColor("#ff7f29"));
            if ("等待买家发货".equals(afterSaleModel.getStatus())) {
                progressView2.setRightLineColor(Color.parseColor("#bbbbbb"));
            } else {
                progressView2.setRightLineColor(Color.parseColor("#ff7f29"));
            }

            progressView2.setLeftLineColor(Color.parseColor("#ff7f29"));
            progressView2.setState(info1[0]);
            progressView2.setTimeTop(info1[1]);
            progressView2.setTimeBottom(info1[2]);
            progressView2.invalidate();
        } else {
            progressView2.setVisibility(View.GONE);
        }
    }

    public void initProgressView3(AfterSaleModel afterSaleModel, AfterSaleProgressView progressView3) {
        if (afterSaleModel.getProgressInfo().size() > 2) {
            String[] info1 = afterSaleModel.getProgressInfo().get(2);
            Resources res = context.getResources();
            Bitmap bmp;
            if ("退款成功".equals(afterSaleModel.getStatus()) && "退款退货".equals(afterSaleModel.getAfterSaleType())) {
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_point);
                progressView3.setBitmap(bmp);
                progressView3.setRightLineColor(Color.parseColor("#ff7f29"));
                progressView3.setLeftLineColor(Color.parseColor("#ff7f29"));
            } else if ("退款关闭".equals(afterSaleModel.getStatus()) && afterSaleModel.getProgressInfo().size() > 3) {
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_point);
                progressView3.setBitmap(bmp);
                progressView3.setRightLineColor(Color.parseColor("#ff7f29"));
                progressView3.setLeftLineColor(Color.parseColor("#ff7f29"));
            } else if ("退款成功".equals(afterSaleModel.getStatus()) || "退款关闭".equals(afterSaleModel.getStatus())) {
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_success);
                progressView3.setBitmap2(bmp);
                progressView3.setLeftLineColor(Color.parseColor("#ff7f29"));
            } else if (afterSaleModel.getStatus().contains("待确认收货")) {
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_point);
                progressView3.setBitmap(bmp);
                progressView3.setRightLineColor(Color.parseColor("#bbbbbb"));
                progressView3.setLeftLineColor(Color.parseColor("#ff7f29"));
            }
            progressView3.setPaintColor(Color.parseColor("#ff7f29"));
            progressView3.setState(info1[0]);
            progressView3.setTimeTop(info1[1]);
            progressView3.setTimeBottom(info1[2]);
            progressView3.invalidate();
        } else {
            if ("等待买家发货".equals(afterSaleModel.getStatus())) {
                Resources res = context.getResources();
                Bitmap bmp;
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_garypoint);
                progressView3.setBitmap(bmp);
                progressView3.setRightLineColor(Color.parseColor("#bbbbbb"));
                progressView3.setLeftLineColor(Color.parseColor("#bbbbbb"));
                progressView3.setPaintColor(Color.parseColor("#bbbbbb"));
                progressView3.setState("待确认收货");
            } else {
                progressView3.setVisibility(View.GONE);
            }
        }
    }

    public void initProgressView4(AfterSaleModel afterSaleModel, AfterSaleProgressView progressView4) {
        if (afterSaleModel.getProgressInfo().size() > 3) {
            String[] info1 = afterSaleModel.getProgressInfo().get(3);
            Resources res = context.getResources();
            Bitmap bmp;
            if ("退款成功".equals(afterSaleModel.getStatus())) {
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_success);
                progressView4.setBitmap2(bmp);
            } else {
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_point);
                progressView4.setBitmap(bmp);
            }
            progressView4.setPaintColor(Color.parseColor("#ff7f29"));
//            progressView4.setRightLineColor(Color.parseColor("#ff7f29"));
            progressView4.setLeftLineColor(Color.parseColor("#ff7f29"));
            progressView4.setState(info1[0]);
            progressView4.setTimeTop(info1[1]);
            progressView4.setTimeBottom(info1[2]);
            progressView4.invalidate();
        } else {
            if ("等待买家发货".equals(afterSaleModel.getStatus()) || afterSaleModel.getStatus().contains("待确认收货")) {
                Resources res = context.getResources();
                Bitmap bmp;
                bmp = BitmapFactory.decodeResource(res, R.drawable.ic_after_sale_garypoint);
                progressView4.setBitmap(bmp);
                progressView4.setLeftLineColor(Color.parseColor("#bbbbbb"));
                progressView4.setPaintColor(Color.parseColor("#bbbbbb"));
                progressView4.setState("退款成功");
            } else {
                progressView4.setVisibility(View.GONE);
            }
        }
    }

}

