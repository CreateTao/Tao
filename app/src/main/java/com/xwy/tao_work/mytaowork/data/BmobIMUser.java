package com.xwy.tao_work.mytaowork.data;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.datatype.BmobFile;

public class BmobIMUser extends BmobIMUserInfo implements Parcelable{
    private String userId;
    private String name;
    private BmobIMMessage bmobIMMessage;
    private int Size;
    private String lastMessage;
    private String time;


    public BmobIMUser(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.bmobIMMessage = null;
        this.Size = 0;
        this.lastMessage = "";
    }

    public BmobIMUser(String userId, String name, BmobIMMessage bmobIMMessage, int Size, String lastMessage){
        this.userId = userId;
        this.name = name;
        this.bmobIMMessage = bmobIMMessage;
        this.Size = Size;
        this.lastMessage = lastMessage;
    }

    protected BmobIMUser(Parcel in) {
        userId = in.readString();
        name = in.readString();
        Size = in.readInt();
        lastMessage = in.readString();
        time = in.readString();
    }

    public static final Creator<BmobIMUser> CREATOR = new Creator<BmobIMUser>() {
        @Override
        public BmobIMUser createFromParcel(Parcel in) {
            return new BmobIMUser(in);
        }

        @Override
        public BmobIMUser[] newArray(int size) {
            return new BmobIMUser[size];
        }
    };

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setBmobIMMessage(BmobIMMessage bmobIMMessage) {
        this.bmobIMMessage = bmobIMMessage;
    }

    public BmobIMMessage getBmobIMMessage() {
        return bmobIMMessage;
    }

    public void setSize(int size) {
        Size = size;
    }

    public int getSize() {
        return Size;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeInt(Size);
        dest.writeString(lastMessage);
        dest.writeString(time);
    }
}
