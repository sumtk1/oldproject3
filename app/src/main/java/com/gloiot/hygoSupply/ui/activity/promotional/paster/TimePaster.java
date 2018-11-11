package com.gloiot.hygoSupply.ui.activity.promotional.paster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.XuanZeiShanagPingLianJIeActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：商品信息页
 */


public class TimePaster extends PasterImpl {
    private PromotionalModel promotionalModel;

    public TimePaster(Context context, Paster paster, PromotionalModel promotionalModel) {
        super(context, paster);
        this.promotionalModel = promotionalModel;
    }

    @Override
    public void past(ViewGroup parent) {
        super.past(parent);
        View pasterView = getView(R.layout.paster_promotional_time);
        if (pasterView != null) {
            parent.addView(pasterView);
            final TextView tv_promotional_time_end = (TextView) pasterView.findViewById(R.id.tv_promotional_time_end);
            final TextView tv_promotional_time_start = (TextView) pasterView.findViewById(R.id.tv_promotional_time_start);
            tv_promotional_time_end.setText(promotionalModel.getEndTime());
            tv_promotional_time_start.setText(promotionalModel.getStartTime());

            tv_promotional_time_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtlis.towBtnDate(context, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePicker datePicker = (DatePicker) DialogUtlis.myDialogBuilder.getCustomView().findViewById(R.id.datePicker);
                            int month1 = datePicker.getMonth() + 1;
                            int data1 = datePicker.getDayOfMonth();
                            tv_promotional_time_end.setText(datePicker.getYear() + "-" + month1 + "-" + datePicker.getDayOfMonth());
                            promotionalModel.setEndTime(datePicker.getYear() + "-" + month1 + "-" + datePicker.getDayOfMonth());
                            DialogUtlis.myDialogBuilder.dismiss();
                        }
                    }, 6);
                }
            });
            tv_promotional_time_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtlis.towBtnDate(context, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatePicker datePicker = (DatePicker) DialogUtlis.myDialogBuilder.getCustomView().findViewById(R.id.datePicker);
                            int month1 = datePicker.getMonth() + 1;
                            int data1 = datePicker.getDayOfMonth();
                            tv_promotional_time_start.setText(datePicker.getYear() + "-" + month1 + "-" + datePicker.getDayOfMonth());
                            promotionalModel.setStartTime(datePicker.getYear() + "-" + month1 + "-" + datePicker.getDayOfMonth());
                            DialogUtlis.myDialogBuilder.dismiss();
                        }
                    }, 6);
                }
            });


        }
    }


}
