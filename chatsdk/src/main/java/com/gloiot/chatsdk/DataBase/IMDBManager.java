package com.gloiot.chatsdk.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gloiot.chatsdk.DataBase.Widget.DataChange;
import com.gloiot.chatsdk.bean.ConversationBean;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.UserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo02 on 2017/5/16.
 */

public class IMDBManager {
    private static final String TAG = "IMDBManager";


    private static Context mContext;
    private static IMDBManager instance;
    private static DBHelper hygoHelper;
    private static SQLiteDatabase sqLiteDatabase;

    private static String path = null;


    private IMDBManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static IMDBManager getInstance(Context context) {
        if (instance == null) {
            instance = new IMDBManager(context);
        }
        return instance;
    }

    public static IMDBManager getInstance(Context context, String userid) {
        if (instance == null) {
            instance = new IMDBManager(context);
        }
        //创建数据库文件目录等
        if (path == null) {
            instance.getDataBasePath(mContext, userid, "HygoIm");
        }
        //实例化HygoHelper对象
        if (hygoHelper == null) {
//            hygoHelper = new DBHelper(mContext, dbName, 1);
            hygoHelper = new DBHelper(mContext, path, 1);
        }
        //return SQLiteDatabase 对象
        if (sqLiteDatabase == null) {
            sqLiteDatabase = hygoHelper.getWritableDatabase();
        }
        return instance;
    }

//    /**
//     * @param userid  用户账号
//     * @param dbName  数据库名称
//     * @param version 数据库版本号
//     * @return
//     */
//    public synchronized void getHygoHelperInstance(String userid, String dbName, int version) {
//        //创建数据库文件目录等
//        if (path == null) {
//            getDataBasePath(mContext, userid, dbName);
//        }
//        //实例化HygoHelper对象
//        if (hygoHelper == null) {
////            hygoHelper = new DBHelper(mContext, dbName, version);
//            hygoHelper = new DBHelper(mContext, path, version);
//        }
//        //return SQLiteDatabase 对象
//        if (sqLiteDatabase == null) {
//            sqLiteDatabase = hygoHelper.getWritableDatabase();
//        }
//    }

    /**
     * 插入数据（userid主键存在则更新）
     *
     * @param imMsgBean
     * @param type                消息接受/消息发送    0为接收、1为发送
     * @param messageChatCallBack
     */
    public synchronized void insertConversationList(ImMsgBean imMsgBean, int type, MessageChatCallBack messageChatCallBack) {
        ConversationBean bean = new DataChange().MessageToConversation(imMsgBean);
        ContentValues contentValues = new ContentValues();

        //发送消息插入接受者id行，接收消息插入发送者id行
        // （反正就是会话列表都按照聊天对象更新数据库存储数据）
        String rowName = "";
        if (type == 0) {
            rowName = bean.getSendid();
            contentValues.put("sendid", bean.getSendid());
        } else if (type == 1) {
            rowName = bean.getReceiveid();
            contentValues.put("sendid", bean.getReceiveid());
        }

        contentValues.put("sessiontype", bean.getSessiontype());
        contentValues.put("msgtype", bean.getMsgtype());
        contentValues.put("message", bean.getMessage());
        contentValues.put("pushdata", bean.getPushdata());
        contentValues.put("sendTime", bean.getSendTime());
        contentValues.put("isNoDiaturb", bean.getIsNoDiaturb());
        contentValues.put("noReadNum", bean.getNoReadNum());
        contentValues.put("isTop", bean.getIsTop());
        contentValues.put("timestamp", bean.getTimestamp());
        if (bean.getExtra() != null)
            contentValues.put("extra", bean.getExtra());

        //说明：该方法做了判断表是否存在操作，移植时可自行修改
        //判断本地数据是否存在  sendid
        boolean flag = false;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT sendid FROM DB_ConversationList WHERE sendid == ?", new String[]{rowName});
        while (cursor.moveToNext()) {
            flag = true;
            Log.e(TAG, "insertConversationList: 2");
            break;
        }

        //插入新数据时默认为   无免打扰、不置顶，未读消息数为1
        // 更新数据时不更新    是否免打扰、是否置顶；但刷新温度消息数
        if (flag) {
            Log.e("----", "数据存在");

            //TODO 这里需要后期根据实际情况修改（发送消息时候本地的操作逻辑）
            //是否修改未读消息数
            if (type == 0)
                contentValues.put("noReadNum", NoReadNumAddOne(rowName));

            long i = sqLiteDatabase.update("DB_ConversationList", contentValues, "sendid=?", new String[]{rowName});
            if (i > 0) {
                imMsgBean.setMsgtype(imMsgBean.getMsgtype() + "_" + type);
                messageChatCallBack.DoChatDBSucceed(imMsgBean);
                Log.e(TAG, "insertConversationList: 3 1");
            } else {
                messageChatCallBack.DoChatDBFault();
                Log.e(TAG, "insertConversationList: 3 2");
            }
        } else {
            Log.e("----", "数据不存在");
            //插入时才操作
            contentValues.put("isNoDiaturb", 0);
            contentValues.put("noReadNum", 1);
            contentValues.put("isTop", 0);

            long i = sqLiteDatabase.insert("DB_ConversationList", null, contentValues);
            if (i > 0) {
                imMsgBean.setMsgtype(imMsgBean.getMsgtype() + "_" + type);
                messageChatCallBack.DoChatDBSucceed(imMsgBean);
                Log.e(TAG, "insertConversationList: 3 1");
            } else {
                messageChatCallBack.DoChatDBFault();
                Log.e(TAG, "insertConversationList: 3 2");
            }
        }
        cursor.close();
    }


    /**
     * 数据查询
     *
     * @return
     */
    public synchronized List<ConversationBean> queryConversationList() {
        List<ConversationBean> list = new ArrayList<>();
        ConversationBean bean;

        Cursor cursor = sqLiteDatabase.query("DB_ConversationList", new String[]{"sendid", "sessiontype", "msgtype", "message",
                "pushdata", "sendTime", "isNoDiaturb", "noReadNum", "isTop", "timestamp", "extra"}, null, null, null, null, "isTop desc, timeStamp desc");
        System.out.println("查到的数据为：");
        while (cursor.moveToNext()) {
            bean = new ConversationBean();
            bean.setSendid(cursor.getString(cursor.getColumnIndex("sendid")));
            bean.setSessiontype(cursor.getString(cursor.getColumnIndex("sessiontype")));
            bean.setMsgtype(cursor.getString(cursor.getColumnIndex("msgtype")));
            bean.setMessage(cursor.getString(cursor.getColumnIndex("message")));
            bean.setPushdata(cursor.getString(cursor.getColumnIndex("pushdata")));
            bean.setSendTime(cursor.getString(cursor.getColumnIndex("sendTime")));
            bean.setIsNoDiaturb(cursor.getInt(cursor.getColumnIndex("isNoDiaturb")));
            bean.setNoReadNum(cursor.getInt(cursor.getColumnIndex("noReadNum")));
            bean.setIsTop(cursor.getInt(cursor.getColumnIndex("isTop")));
            bean.setTimestamp(cursor.getInt(cursor.getColumnIndex("timestamp")));
            bean.setExtra(cursor.getString(cursor.getColumnIndex("extra")));

            System.out.println("-->" + bean.toString());
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    /**
     * 删除表中数据
     *
     * @param arrayList
     */
    public synchronized void deleteConversationList(List<String> arrayList, DataBaseCallBack dataBaseCallBack) {
        for (String str : arrayList) {
            long i = sqLiteDatabase.delete("DB_ConversationList", "sendid = ?", new String[]{str});
            if (i > 0) {
                dataBaseCallBack.operationState(true);
            } else {
                dataBaseCallBack.operationState(false);
            }
            Log.e(TAG, "删除sendid为" + str + "的数据");
        }
    }

    /**
     * 删除表中数据
     *
     * @param str
     */
    public synchronized void deleteConversationList(String str, DataBaseCallBack dataBaseCallBack) {
        long i = sqLiteDatabase.delete("DB_ConversationList", "sendid = ?", new String[]{str});
        if (i > 0) {
            dataBaseCallBack.operationState(true);
        } else {
            dataBaseCallBack.operationState(false);
        }
        Log.e(TAG, "删除sendid为" + str + "的数据");
    }

    /**
     * 插入系统消息
     *
     * @param list
     * @param type                消息接受/消息发送    0为接收、1为发送
     * @param messageChatCallBack
     */
    public synchronized void insertChatMsg(List<ImMsgBean> list, int type, MessageChatCallBack messageChatCallBack) {
        Log.e("----------", "3");

        String tableName = "";
        ContentValues contentValues;

        for (ImMsgBean bean : list) {
            Log.e("----------", "4");
            contentValues = new ContentValues();

            if (type == 0) {
                //单聊创建表
                CreateChatTable("DB_" + bean.getSendid());
                tableName = "DB_" + bean.getSendid();
            } else if (type == 1) {
                //单聊创建表
                CreateChatTable("DB_" + bean.getReceiveid());
                tableName = "DB_" + bean.getReceiveid();
            }


            contentValues.put("msgid", bean.getMsgid());
            contentValues.put("sendid", bean.getSendid());
            contentValues.put("receiveid", bean.getReceiveid());
            contentValues.put("sessiontype", bean.getSessiontype());
            contentValues.put("msgtype", bean.getMsgtype() + "_" + type);
            contentValues.put("message", bean.getMessage());
            contentValues.put("pushdata", bean.getPushdata());
            contentValues.put("sendTime", bean.getSendTime());
            if (bean.getExtra() != null)
                contentValues.put("extra", bean.getExtra());

            // 消息为发送消息时给予默认的发送成功失败
            // 0为发送发送中，1为发送成功, 2为失败。
            // 消息发送后，移动端收到后台的信息接受成功提示后，通过方法修改messageState值，修改为1
            if (type == 1) {
                contentValues.put("messageState", 0);
            }

            long i = sqLiteDatabase.insert(tableName, null, contentValues);
            if (i > 0) {
                Log.e("----------", "5插入数据成功");
                insertConversationList(bean, type, messageChatCallBack);
            }
        }
    }

    /**
     * 插入消息
     *
     * @param bean
     * @param type                消息接受/消息发送    0为接收、1为发送
     * @param messageChatCallBack
     */
    public synchronized void insertChatMsg(ImMsgBean bean, int type, MessageChatCallBack messageChatCallBack) {
        Log.e("----------", "3");

        if (bean == null)
            return;

        String tableName = "";
        ContentValues contentValues = new ContentValues();

        try {
            if (type == 0) {
                //单聊创建表
                CreateChatTable("DB_" + bean.getSendid());
                tableName = "DB_" + bean.getSendid();
            } else if (type == 1) {
                //单聊创建表
                CreateChatTable("DB_" + bean.getReceiveid());
                tableName = "DB_" + bean.getReceiveid();
            }


            contentValues.put("msgid", bean.getMsgid());
            contentValues.put("sendid", bean.getSendid());
            contentValues.put("receiveid", bean.getReceiveid());
            contentValues.put("sessiontype", bean.getSessiontype());
            contentValues.put("msgtype", bean.getMsgtype() + "_" + type);
            contentValues.put("message", bean.getMessage());
            contentValues.put("pushdata", bean.getPushdata());
            contentValues.put("sendTime", bean.getSendTime());
            if (bean.getExtra() != null)
                contentValues.put("extra", bean.getExtra());

            //消息为发送消息时给予默认的发送成功失败
            //0为发送中，1为发送成功, 2为发送失败。
            // 消息发送后，移动端收到后台的信息接受成功提示后，通过方法修改messageState值，修改为1
            if (type == 1) {
                contentValues.put("messageState", 0);
            }

            long i = sqLiteDatabase.insert(tableName, null, contentValues);
            if (i > 0) {
                insertConversationList(bean, type, messageChatCallBack);
            }
        } catch (Exception e) {
            Log.e("----------", "3"+e);
        }
    }

    /**
     * 查询消息
     *
     * @param tiaoshu 系统消息分页查询，tiaoshu 为当前已有条数
     * @return
     */
    public synchronized List<ImMsgBean> queryChatMsg(String tableName, int tiaoshu) {
        CreateChatTable("DB_" + tableName);

        List<ImMsgBean> list = new ArrayList<>();
        ImMsgBean bean;

        Cursor cursor = sqLiteDatabase.query("DB_" + tableName, new String[]{"id", "msgid", "sendid", "receiveid", "sessiontype",
                "msgtype", "message", "pushdata", "sendTime", "messageState", "extra"}, null, null, null, null, "sendTime desc, id desc", tiaoshu + ", 20");

//        Cursor cursor = sqLiteDatabase.query("DB_" + tableName, new String[]{"id", "msgid", "sendid", "receiveid", "sessiontype",
//                "msgtype", "message", "pushdata", "sendTime", "messageState", "extra"}, null, null, null, null, "id desc", tiaoshu + ", 20");

        System.out.println("查到的数据为：");
        while (cursor.moveToNext()) {
            bean = new ImMsgBean();
            try {
                bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bean.setMsgid(cursor.getString(cursor.getColumnIndex("msgid")));
                bean.setSendid(cursor.getString(cursor.getColumnIndex("sendid")));
                bean.setReceiveid(cursor.getString(cursor.getColumnIndex("receiveid")));
                bean.setSessiontype(cursor.getString(cursor.getColumnIndex("sessiontype")));
                bean.setMsgtype(cursor.getString(cursor.getColumnIndex("msgtype")));
                bean.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                bean.setPushdata(cursor.getString(cursor.getColumnIndex("pushdata")));
                bean.setSendTime(cursor.getString(cursor.getColumnIndex("sendTime")));
                bean.setMessageState(cursor.getInt(cursor.getColumnIndex("messageState")));
                bean.setExtra(cursor.getString(cursor.getColumnIndex("extra")));

                System.out.println("-->" + bean.toString());
                list.add(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 查询单条聊天消息内容
     *
     * @param tableName
     * @param messageId
     * @return
     */
    public synchronized ImMsgBean queryOneMessage(String tableName, String messageId) {
        ImMsgBean bean = null;
        try {
            Cursor cursor = sqLiteDatabase.query("DB_" + tableName, new String[]{"id", "msgid", "sendid", "receiveid", "sessiontype",
                    "msgtype", "message", "pushdata", "sendTime", "messageState", "extra"}, "msgid=?", new String[]{messageId}, null, null, null, null);

            System.out.println("queryOneMessage：");
            while (cursor.moveToNext()) {
                bean = new ImMsgBean();
                bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bean.setMsgid(cursor.getString(cursor.getColumnIndex("msgid")));
                bean.setSendid(cursor.getString(cursor.getColumnIndex("sendid")));
                bean.setReceiveid(cursor.getString(cursor.getColumnIndex("receiveid")));
                bean.setSessiontype(cursor.getString(cursor.getColumnIndex("sessiontype")));
                bean.setMsgtype(cursor.getString(cursor.getColumnIndex("msgtype")));
                bean.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                bean.setPushdata(cursor.getString(cursor.getColumnIndex("pushdata")));
                bean.setSendTime(cursor.getString(cursor.getColumnIndex("sendTime")));
                bean.setMessageState(cursor.getInt(cursor.getColumnIndex("messageState")));
                bean.setExtra(cursor.getString(cursor.getColumnIndex("extra")));

                System.out.println("-->" + bean.toString());
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println("数据库存储错误queryOneMessage-->" + e);
        }
        return bean;
    }

    /**
     * 删除消息
     *
     * @param arrayList
     */
    public synchronized void deleteChatMsg(String tableName, List<ImMsgBean> arrayList, DataBaseCallBack dataBaseCallBack) {
        for (ImMsgBean bean : arrayList) {
            long i = sqLiteDatabase.delete("DB_" + tableName, "id = ?", new String[]{bean.getId() + ""});
            if (i > 0) {
                dataBaseCallBack.operationState(true);
            } else {
                dataBaseCallBack.operationState(false);

            }
            Log.e(TAG, "删除id为" + bean.getId() + "的数据");
        }
    }

    /**
     * 消息发送状态改变方法，我发送的就对找接受者（receiveid）相关表的消息id
     *
     * @param receiveid
     * @param msgid
     */
    public synchronized void ChangeMessageState(String receiveid, String msgid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("messageState", 1);

        long i = sqLiteDatabase.update("DB_" + receiveid, contentValues, "msgid=?", new String[]{msgid});
    }

    /**
     * 插入用户信息
     *
     * @param userInfo
     */
    public synchronized void insertUserInfo(UserInfo userInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sendid", userInfo.getId());
        contentValues.put("imgUrl", userInfo.getUrl());
        contentValues.put("nickName", userInfo.getName());
        if (userInfo.getNoteName() != null)
            contentValues.put("noteName", userInfo.getNoteName());


        //说明：该方法做了判断表是否存在操作，移植时可自行修改
        //判断本地数据是否存在  sendid
        boolean flag = false;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT sendid FROM DB_UserInfo WHERE sendid == ?", new String[]{userInfo.getId()});
        while (cursor.moveToNext()) {
            flag = true;
            Log.e(TAG, "insertPersonInfo: 2");
            break;
        }

        //插入新数据时默认为   无免打扰、不置顶，未读消息数为1
        // 更新数据时不更新    是否免打扰、是否置顶；但刷新温度消息数
        if (flag) {
            Log.e("----", "数据存在");

            long i = sqLiteDatabase.update("DB_UserInfo", contentValues, "sendid=?", new String[]{userInfo.getId()});
        } else {
            Log.e("----", "数据不存在");
            //插入时才操作

            long i = sqLiteDatabase.insert("DB_UserInfo", null, contentValues);
        }
        cursor.close();
    }

    /**
     * 查询用户信息，本地不存在返回对象为null，使用时请先判空
     *
     * @param sendid
     * @return
     */
    public synchronized UserInfo queryUserInfo(String sendid) {
        try {
            UserInfo userInfo = null;
            Cursor cursor = sqLiteDatabase.query("DB_UserInfo", new String[]{"sendid", "imgUrl", "nickName", "noteName"}, "sendid=?", new String[]{sendid}, null, null, null);

            System.out.println("查到的数据为：");
            while (cursor.moveToNext()) {
                userInfo = new UserInfo();
                userInfo.setId(cursor.getString(cursor.getColumnIndex("sendid")));
                userInfo.setUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
                userInfo.setName(cursor.getString(cursor.getColumnIndex("nickName")));
                userInfo.setNoteName(cursor.getString(cursor.getColumnIndex("noteName")));

                System.out.println("-->" + userInfo.toString());
            }
            cursor.close();
            return userInfo;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除用户信息
     *
     * @param sendid
     */
    public synchronized void deleteUserInfo(String sendid) {
        sqLiteDatabase.delete("DB_UserInfo", "sendid = ?", new String[]{sendid});
        Log.e(TAG, "删除id为" + sendid + "的用户信息");
    }

    /**
     * 创建指定目录数据库
     *
     * @param context
     * @param name
     * @return
     */
    private synchronized String getDataBasePath(Context context, String userid, String name) {
        //用户ID为空时进入默认名称的文件夹
        if (userid == null || userid.length() == 0)
            userid = "default";

        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + userid + File.separator + name);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Log.e("数据库位置", context.getFilesDir().getAbsolutePath() + File.separator + userid + File.separator + name);
        path = file.getAbsolutePath();
        return path;
    }

    /**
     * 更新发送消息储存信息
     *
     * @param msgId
     * @param msgIdFse
     * @param receiveId
     * @param messageState
     * @param time
     * @param messageChatCallBack
     */
    public synchronized void UpDateMessageInfo(String msgId, String msgIdFse, String receiveId, int messageState, String time, MessageChatCallBack messageChatCallBack) {
        ImMsgBean bean = new ImMsgBean();
        String tableName = "DB_" + receiveId;
        ContentValues contentValues = new ContentValues();
        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{"id", "msgid", "sendid", "receiveid", "sessiontype",
                "msgtype", "message", "pushdata", "sendTime", "messageState", "extra"}, "msgid=?", new String[]{msgIdFse}, null, null, null);

        System.out.println("查到的数据为：");
        while (cursor.moveToNext()) {
            if (messageState == 2 || messageState == 0) {  //失败
                contentValues.put("messageState", messageState);
                contentValues.put("sendTime", time);

                bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bean.setMsgid(msgId);
                bean.setSendid(cursor.getString(cursor.getColumnIndex("sendid")));
                bean.setReceiveid(cursor.getString(cursor.getColumnIndex("receiveid")));
                bean.setSessiontype(cursor.getString(cursor.getColumnIndex("sessiontype")));
                bean.setMsgtype(cursor.getString(cursor.getColumnIndex("msgtype")));
                bean.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                bean.setPushdata(cursor.getString(cursor.getColumnIndex("pushdata")));
                bean.setSendTime(time);
                bean.setMessageState(messageState);
                bean.setExtra(cursor.getString(cursor.getColumnIndex("extra")));

                long i = sqLiteDatabase.update(tableName, contentValues, "id=?", new String[]{cursor.getString(cursor.getColumnIndex("id"))});

                if (i > 0) {
                    messageChatCallBack.DoChatDBSucceed(bean);
                } else {
                    messageChatCallBack.DoChatDBFault();
                }
            } else if (messageState == 1) { //成功
                contentValues.put("msgid", msgId);
                contentValues.put("sendid", cursor.getString(cursor.getColumnIndex("sendid")));
                contentValues.put("receiveid", cursor.getString(cursor.getColumnIndex("receiveid")));
                contentValues.put("sessiontype", cursor.getString(cursor.getColumnIndex("sessiontype")));
                contentValues.put("msgtype", cursor.getString(cursor.getColumnIndex("msgtype")));
                contentValues.put("message", cursor.getString(cursor.getColumnIndex("message")));
                contentValues.put("pushdata", cursor.getString(cursor.getColumnIndex("pushdata")));
                contentValues.put("sendTime", time);
                contentValues.put("messageState", messageState);
                contentValues.put("extra", cursor.getString(cursor.getColumnIndex("extra")));

                bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                bean.setMsgid(msgId);
                bean.setSendid(cursor.getString(cursor.getColumnIndex("sendid")));
                bean.setReceiveid(cursor.getString(cursor.getColumnIndex("receiveid")));
                bean.setSessiontype(cursor.getString(cursor.getColumnIndex("sessiontype")));
                bean.setMsgtype(cursor.getString(cursor.getColumnIndex("msgtype")));
                bean.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                bean.setPushdata(cursor.getString(cursor.getColumnIndex("pushdata")));
                bean.setSendTime(time);
                bean.setMessageState(messageState);
                bean.setExtra(cursor.getString(cursor.getColumnIndex("extra")));

                long i = sqLiteDatabase.update(tableName, contentValues, "id=?", new String[]{cursor.getString(cursor.getColumnIndex("id"))});

                if (i > 0) {
                    messageChatCallBack.DoChatDBSucceed(bean);
                } else {
                    messageChatCallBack.DoChatDBFault();
                }
            }
            System.out.println("UpDateMessageInfo-->" + bean.toString());
        }
        cursor.close();
    }

    /**
     * 更新发送消息储存信息(message)
     *
     * @param msgId
     * @param receiveId
     * @param message
     * @param messageChatCallBack
     */
    public synchronized void UpDateMessage(String msgId, String receiveId, String message, MessageChatCallBack messageChatCallBack) {
        ImMsgBean bean = new ImMsgBean();
        String tableName = "DB_" + receiveId;
        ContentValues contentValues = new ContentValues();
        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{"id", "msgid", "sendid", "receiveid", "sessiontype",
                "msgtype", "message", "pushdata", "sendTime", "messageState", "extra"}, "msgid=?", new String[]{msgId}, null, null, null);
        while (cursor.moveToNext()) {
            contentValues.put("message", message);
            bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            bean.setMsgid(msgId);
            bean.setSendid(cursor.getString(cursor.getColumnIndex("sendid")));
            bean.setReceiveid(cursor.getString(cursor.getColumnIndex("receiveid")));
            bean.setSessiontype(cursor.getString(cursor.getColumnIndex("sessiontype")));
            bean.setMsgtype(cursor.getString(cursor.getColumnIndex("msgtype")));
            bean.setMessage(cursor.getString(cursor.getColumnIndex("message")));
            bean.setPushdata(cursor.getString(cursor.getColumnIndex("pushdata")));
            bean.setSendTime(cursor.getString(cursor.getColumnIndex("sendTime")));
            bean.setMessageState(cursor.getInt(cursor.getColumnIndex("messageState")));
            bean.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
            long i = sqLiteDatabase.update(tableName, contentValues, "id=?", new String[]{cursor.getString(cursor.getColumnIndex("id"))});
            if (i > 0) {
                messageChatCallBack.DoChatDBSucceed(bean);
            } else {
                messageChatCallBack.DoChatDBFault();
            }
        }
    }

    /**
     * 消息重发更新会话列表（后期消息删除也要处理）
     *
     * @param imMsgBean
     */
    private void updateConversationList(ImMsgBean imMsgBean) {
        ConversationBean bean = new DataChange().MessageToConversation(imMsgBean);
        ContentValues contentValues = new ContentValues();

        contentValues.put("sendid", bean.getReceiveid());
        contentValues.put("sessiontype", bean.getSessiontype());
        contentValues.put("msgtype", bean.getMsgtype());
        contentValues.put("message", bean.getMessage());
        contentValues.put("pushdata", bean.getPushdata());
        contentValues.put("sendTime", bean.getSendTime());
        contentValues.put("isNoDiaturb", bean.getIsNoDiaturb());
        contentValues.put("noReadNum", bean.getNoReadNum());
        contentValues.put("isTop", bean.getIsTop());
        contentValues.put("timestamp", bean.getTimestamp());
        if (bean.getExtra() != null)
            contentValues.put("extra", bean.getExtra());

        sqLiteDatabase.update("DB_ConversationList", contentValues, "sendid=?", new String[]{bean.getReceiveid()});
    }

    /**
     * 消息未读消息数置 0
     *
     * @param sendid
     */
    public synchronized void NoReadNumClean(String sendid, DataBaseCallBack dataBaseCallBack) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("noReadNum", 0);
        long i = sqLiteDatabase.update("DB_ConversationList", contentValues, "sendid=?", new String[]{sendid});
        if (i > 0) {
            dataBaseCallBack.operationState(true);
        } else {
            dataBaseCallBack.operationState(false);
        }
    }

    /**
     * 清除所有未读数
     *
     * @param dataBaseCallBack
     */
    public synchronized void CleanAllReadNum(DataBaseCallBack dataBaseCallBack) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("noReadNum", 0);
        long i = sqLiteDatabase.update("DB_ConversationList", contentValues, "noReadNum<>?", new String[]{"0"});
        if (i > 0) {
            dataBaseCallBack.operationState(true);
        } else {
            dataBaseCallBack.operationState(false);
        }
    }

    /**
     * 获取所有未读数
     *
     * @return
     */
    public synchronized int GetAllReadNum() {
        int num = 0;
        Cursor cursor = sqLiteDatabase.query("DB_ConversationList", new String[]{"sum(noReadNum) as num"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            num = cursor.getInt(cursor.getColumnIndex("num"));

        }
        cursor.close();
        return num;
    }

    /**
     * 未读消息数+1
     *
     * @param sendid
     * @return
     */

    private synchronized int NoReadNumAddOne(String sendid) {
        int num = 0;
        Cursor cursor = sqLiteDatabase.query("DB_ConversationList", new String[]{"noReadNum"}, "sendid=?", new String[]{sendid}, null, null, null);
        while (cursor.moveToNext()) {
            num = cursor.getInt(cursor.getColumnIndex("noReadNum")) + 1;
        }
        cursor.close();
        return num;
    }

    /**
     * 消息置顶
     *
     * @param sendid           发送者id
     * @param flag             置顶/取消置顶   true为置顶，false为取消置顶
     * @param dataBaseCallBack 数据库操作回调
     */
    public synchronized void MessageToTop(String sendid, boolean flag, DataBaseCallBack dataBaseCallBack) {
        ContentValues contentValues = new ContentValues();
        if (flag)
            contentValues.put("isTop", 1);
        else
            contentValues.put("isTop", 0);

        long i = sqLiteDatabase.update("DB_ConversationList", contentValues, "sendid=?", new String[]{sendid});

        if (i > 0) {
            dataBaseCallBack.operationState(true);
        } else {
            dataBaseCallBack.operationState(false);
        }
    }

    /**
     * 单聊根据聊天对象创建表
     *
     * @param tableName 表名，即sendid
     */
    public void CreateChatTable(String tableName) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE if not exists " + tableName + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +         //id，做主键，自增
                    "msgid VARCHAR UNIQUE, " +                         //消息id
                    "sendid VARCHAR, " +                               //发送者id（其实可以不要，避免造成数据冗余）
                    "receiveid VARCHAR, " +                            //接受者id（其实可以不要，避免造成数据冗余）
                    "sessiontype VARCHAR, " +                          //聊天类型（单聊，群聊...）（其实可以不要，避免造成数据冗余）
                    "msgtype VARCHAR, " +                              //消息类型
                    "message TEXT, " +                                 //消息体（String类型的json对象）
                    "pushdata VARCHAR, " +                             //通知消息
                    "sendTime datetime, " +                             //发送时间
                    "messageState INTEGER default -1, " +              //消息状态
                    "extra TEXT default '')");                         //附属消息（没啥用的）
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录时调用
     */
    public void ClearnData() {
        //关闭数据库
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
        if (hygoHelper != null) {
            hygoHelper.close();
            hygoHelper = null;
        }
        if (path != null)
            path = null;
    }
//
//    /**
//     * 关闭数据库
//     */
//    private void close() {
//        //关闭数据库
//        if (sqLiteDatabase != null) {
//            sqLiteDatabase.close();
//            sqLiteDatabase = null;
//        }
//    }
//
//    /**
//     * 打开数据库
//     */
//    private void open() {
//        sqLiteDatabase = hygoHelper.getWritableDatabase();
//    }
}