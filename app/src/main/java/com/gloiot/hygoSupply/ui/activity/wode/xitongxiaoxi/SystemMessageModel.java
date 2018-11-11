package com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：Ljy on 2018/1/4.
 * 功能：
 */


public class SystemMessageModel implements Parcelable {
    private String id;
    private String title;
    private String type;
    private String content;
    private boolean isEdit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public SystemMessageModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
    }

    protected SystemMessageModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.isEdit = in.readByte() != 0;
    }

    public static final Creator<SystemMessageModel> CREATOR = new Creator<SystemMessageModel>() {
        @Override
        public SystemMessageModel createFromParcel(Parcel source) {
            return new SystemMessageModel(source);
        }

        @Override
        public SystemMessageModel[] newArray(int size) {
            return new SystemMessageModel[size];
        }
    };
}
