package com.gloiot.chatsdk.DataBase;

/**
 * Created by hygo02 on 2017/5/20.
 */

public interface DataBaseCallBack {
    /**
     * 数据库操作回调方法
     * @param flag  true为操作成功
     */
    void operationState(boolean flag);
}
