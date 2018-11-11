package com.gloiot.chatsdk.DataBase;

import com.gloiot.chatsdk.bean.ImMsgBean;

/**
 * Created by hygo02 on 2017/5/20.
 */

public interface MessageChatCallBack {
    /**
     * 聊天界面数据库回调方法
     *
     * @param imMsgBean 消息对象实体
     */
    void DoChatDBSucceed(ImMsgBean imMsgBean);

    void DoChatDBFault();
}
