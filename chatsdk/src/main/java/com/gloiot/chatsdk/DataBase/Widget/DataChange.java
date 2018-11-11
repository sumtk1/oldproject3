package com.gloiot.chatsdk.DataBase.Widget;

import com.gloiot.chatsdk.bean.ConversationBean;
import com.gloiot.chatsdk.bean.ImMsgBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo02 on 2017/5/18.
 * 作用：用于聊天对象的数据转换（会话列表）
 */

public class DataChange {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<ImMsgBean> systemlist;
    private ImMsgBean imMsgBean;

    /**
     * 在线消息，返对象（系统消息）
     *
     * @param jsonObject
     * @return
     */
    public ImMsgBean JsonToSystemBean(JSONObject jsonObject) {
        imMsgBean = new ImMsgBean();
        if (jsonObject == null)
            return null;

        try {
            //sendid为关键字，不能为空，当sendid缺失时不插入本条聊天数据
            if (jsonObject.isNull("sendId")) {
                return null;
            } else {
                imMsgBean.setSendid(jsonObject.getString("sendId"));

                if (!jsonObject.isNull("msgId")) {
                    imMsgBean.setMsgid(jsonObject.getString("msgId"));
                }
                if (!jsonObject.isNull("receiveId")) {
                    imMsgBean.setReceiveid(jsonObject.getString("receiveId"));
                }
                if (!jsonObject.isNull("sessionType")) {
                    imMsgBean.setSessiontype(jsonObject.getString("sessionType"));
                }
                if (!jsonObject.isNull("msgType")) {
                    imMsgBean.setMsgtype(jsonObject.getString("msgType"));
                }
                if (!jsonObject.isNull("message")) {
                    imMsgBean.setMessage(jsonObject.getString("message"));
                }
                if (!jsonObject.isNull("pushData")) {
                    imMsgBean.setPushdata(jsonObject.getString("pushData"));
                }
                if (!jsonObject.isNull("sendTime")) {
                    imMsgBean.setSendTime(jsonObject.getString("sendTime"));
                }
                if (!jsonObject.isNull("extra")) {
                    imMsgBean.setExtra(jsonObject.getString("extra"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imMsgBean;
    }

    /**
     * 离线消息，返回List（系统消息）
     *
     * @param str
     * @return
     */
    public List<ImMsgBean> JsonToSystemList(String str) {
        systemlist = new ArrayList<>();
        ImMsgBean bean;

        try {
            JSONObject mes = new JSONObject(str);
            if (!mes.isNull("mes")) {
                JSONArray jsonArray = mes.getJSONArray("mes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    bean = new ImMsgBean();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    //sendid为关键字，不能为空，当sendid缺失时不插入本条聊天数据
                    if (jsonObject.isNull("sendId")) {
                        return null;
                    } else {
                        bean.setSendid(jsonObject.getString("sendId"));

                        if (!jsonObject.isNull("msgId")) {
                            bean.setMsgid(jsonObject.getString("msgId"));
                        }
                        if (!jsonObject.isNull("receiveId")) {
                            bean.setReceiveid(jsonObject.getString("receiveId"));
                        }
                        if (!jsonObject.isNull("sessionType")) {
                            bean.setSessiontype(jsonObject.getString("sessionType"));
                        }
                        if (!jsonObject.isNull("msgType")) {
                            bean.setMsgtype(jsonObject.getString("msgType"));
                        }
                        if (!jsonObject.isNull("message")) {
                            bean.setMessage(jsonObject.getString("message"));
                        }
                        if (!jsonObject.isNull("pushData")) {
                            bean.setPushdata(jsonObject.getString("pushData"));
                        }
                        if (!jsonObject.isNull("sendTime")) {
                            bean.setSendTime(jsonObject.getString("sendTime"));
                        }
                        if (!jsonObject.isNull("extra")) {
                            bean.setExtra(jsonObject.getString("extra"));
                        }
                    }
                    systemlist.add(bean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return systemlist;
    }

    /**
     * 将imMsgBean转成ConversationBean
     *
     * @param messageBean
     * @return
     */
    public ConversationBean MessageToConversation(ImMsgBean messageBean) {
        ConversationBean bean = new ConversationBean();

        bean.setSendid(messageBean.getSendid());
        bean.setReceiveid(messageBean.getReceiveid());

        if (messageBean.getSessiontype() != null) {
            bean.setSessiontype(messageBean.getSessiontype());
        }
        if (messageBean.getMsgtype() != null) {
            bean.setMsgtype(messageBean.getMsgtype());
        }
        if (messageBean.getMessage() != null) {
            bean.setMessage(messageBean.getMessage());
        }
        if (messageBean.getPushdata() != null) {
            bean.setPushdata(messageBean.getPushdata());
        }
        if (messageBean.getSendTime() != null) {
            bean.setSendTime(messageBean.getSendTime());
        }
        if (messageBean.getExtra() != null) {
            bean.setExtra(messageBean.getExtra());
        }

        int timestamp = 0;
        try {
            timestamp = (int) (sdf.parse(bean.getSendTime()).getTime() / 1000);
            bean.setTimestamp(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return bean;
    }
}