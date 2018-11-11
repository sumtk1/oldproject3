package com.gloiot.chatsdk.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hygo02 on 2017/5/16.
 * 作用：创建数据库,返回的HygoHelper对象用于SQLiteDatabase对象获取
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists DB_ConversationList (" +
                "sendid VARCHAR PRIMARY KEY, " +    //发送者id
                "sessiontype VARCHAR, " +           //聊天类型（单聊，群聊...）
                "msgtype VARCHAR, " +               //消息类型
                "message TEXT, " +                  //消息体（String类型的json对象）
                "pushdata VARCHAR, " +              //通知消息
                "sendTime VARCHAR, " +              //发送时间
                "isNoDiaturb INTEGER, " +               //是否免打扰
                "noReadNum INTEGER DEFAULT 0, " +   //未读消息数
                "messageState INTEGER DEFAULT 2, " +   //未读消息数
                "isTop INTEGER, " +                     //是否置顶
                "timestamp INTEGER, " +             //时间戳（用于查询排序）
                "extra TEXT default '')");          //附属消息（没啥用的）

        db.execSQL("CREATE TABLE if not exists DB_UserInfo (" +
                "sendid VARCHAR PRIMARY KEY, " +                //用户id
                "imgUrl VARCHAR, " +                            //用户头像
                "nickName VARCHAR, " +                          //用户昵称
                "noteName VARCHAR default '')"                  //用户备注
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
