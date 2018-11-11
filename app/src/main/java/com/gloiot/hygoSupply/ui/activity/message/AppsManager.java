package com.gloiot.hygoSupply.ui.activity.message;

import android.content.Context;
import android.content.Intent;

import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendBean;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.XuanZeiShanagPingLianJIeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo00 on 2017/8/10.
 */

public class AppsManager {

    public static AppsManager instence;
    private Context context;

    public AppsManager(Context context){
        this.context = context;
    }

    public static AppsManager getInstence(Context context){
        if (instence == null){
            instence = new AppsManager(context);
        }
        return instence;
    }

    public List<ExtendBean> addApps(){
        List<ExtendBean> list = new ArrayList<>();
        list.add(new ExtendBean(com.gloiot.chatsdk.R.drawable.chat_shangpin_bg, "推荐商品"));
        return list;
    }

    public void setAppsOnClick(int position){
        switch (position) {
            case 2:
                Intent intent = new Intent(context, XuanZeiShanagPingLianJIeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tuijian", true);
                context.startActivity(intent);
                break;
            case 3:

                break;
        }
    }

}
