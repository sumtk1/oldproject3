package com.gloiot.hygoSupply.ui.activity.evaluation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Ljy on 2017/8/24.
 * 功能：评价model
 */


public class EvaluationModel {
    private String id;
    private String name;
    private String nickName;
    private String imageUrl;
    private String time;
    private String describe;
    private String content;
    private String reply;
    private String replyid;
    private String add;
    private String addTime;
    private String replyadd;
    private String replyaddId;

    //    private List<ReplyModel> reply = new ArrayList<>();
    private List<String> evluationImages = new ArrayList<>();
    private List<String> additionalImages = new ArrayList<>();

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getReplyaddId() {
        return replyaddId;
    }

    public void setReplyaddId(String replyaddId) {
        this.replyaddId = replyaddId;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getReplyadd() {
        return replyadd;
    }

    public void setReplyadd(String replyadd) {
        this.replyadd = replyadd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(List<String> additionalImages) {
        this.additionalImages = additionalImages;
    }

    public List<String> getEvluationImages() {
        return evluationImages;
    }

    public void setEvluationImages(List<String> evluationImages) {
        this.evluationImages = evluationImages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

//    public List<ReplyModel> getReply() {
//        return reply;
//    }
//
//    public void setReply(List<ReplyModel> reply) {
//        this.reply = reply;
//    }
}
