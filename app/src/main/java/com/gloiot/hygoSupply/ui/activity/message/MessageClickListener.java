package com.gloiot.hygoSupply.ui.activity.message;

import android.content.Context;
import android.content.Intent;

import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.ImMsgSPBean;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.ShangpinDianpuActivity;

/**
 * 创建人： zengming on 2017/8/23.
 * 功能：消息点击事件处理类
 */

public class MessageClickListener {

    public static void MessageClick(Context context, ImMsgBean imMsgBean) {
//        L.e("-onMessageClick-", "--" + imMsgBean.toString());
        if (imMsgBean.getMsgtype().contains("商品")) {
            ImMsgSPBean imMsgSPBean = new ImMsgSPBean();
            imMsgSPBean = imMsgSPBean.typeCast(imMsgBean);
            Intent intent = new Intent(context, ShangpinDianpuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", imMsgSPBean.getSpId());
            context.startActivity(intent);
        }

    }
}
