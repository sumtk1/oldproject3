package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleModel;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.AttachView;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.Attacher;

/**
 * 作者：Ljy on 2017/9/11.
 * 功能：我的——我的资料
 */


public class NoticeAttcher extends Attacher {
    private AfterSaleModel afterSaleModel;

    public NoticeAttcher(AttachView attacher, Context context) {
        super(attacher, context);
    }

    public NoticeAttcher(AttachView attacher, Context context, AfterSaleModel afterSaleModel) {
        super(attacher, context);
        this.afterSaleModel = afterSaleModel;
    }

    //展示商品信息  status
    @Override
    public void attach(ViewGroup parent) {
        super.attach(parent);
        View attachView = getView(R.layout.item_aftersale_notice);
        if (null != attachView) {
            parent.addView(attachView);
            TextView notice = (TextView) attachView.findViewById(R.id.tv_aftersale_notice);
            if (null != afterSaleModel) {
                notice.setText(afterSaleModel.getNotice());
            }
        }
    }
}
