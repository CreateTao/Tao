package com.xwy.tao_work.mytaowork.data;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

public class Work extends BmobObject implements Parcelable{

    private User user;
    private String workTitle;
    private int workBigType;
    private int workSmallType;
    private String workContent;
    private String workCondition;
    private int workNumber;
    private int workSupNumber;
    private String workStopTime;
    private String workTime;
    private int workSex;
    private int workMoney;

    public Work(){
        this.user = null;
        this.workTitle = "";
        this.workBigType = 0;
        this.workSmallType = 0;
        this.workContent = "";
        this.workCondition = "";
        this.workNumber = 0;
        this.workSupNumber = 0;
        this.workStopTime = "";
        this.workTime = "";
        this.workSex = 0;
        this.workMoney = 0;
    }

    public Work(Parcel in) {
        workTitle = in.readString();
        workBigType = in.readInt();
        workSmallType = in.readInt();
        workContent = in.readString();
        workCondition = in.readString();
        workNumber = in.readInt();
        workSupNumber = in.readInt();
        workStopTime = in.readString();
        workTime = in.readString();
        workSex = in.readInt();
        workMoney = in.readInt();
    }

    public static final Creator<Work> CREATOR = new Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel in) {
            return new Work(in);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };

    public Work setUser(User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Work setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
        return this;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public Work setWorkBigType(int workBigType) {
        this.workBigType = workBigType;
        return this;
    }

    public int getWorkBigType() {
        return workBigType;
    }

    public Work setWorkSmallType(int workSmallType) {
        this.workSmallType = workSmallType;
        return this;
    }

    public int getWorkSmallType() {
        return workSmallType;
    }

    public Work setWorkContent(String workContent) {
        this.workContent = workContent;
        return this;
    }

    public String getWorkContent() {
        return workContent;
    }

    public Work setWorkCondition(String workCondition) {
        this.workCondition = workCondition;
        return this;
    }

    public String getWorkCondition() {
        return workCondition;
    }

    public Work setWorkNumber(int workNumber) {
        this.workNumber = workNumber;
        return this;
    }

    public int getWorkNumber() {
        return workNumber;
    }

    public Work setWorkSupNumber(int workSupNumber) {
        this.workSupNumber = workSupNumber;
        return this;
    }

    public int getWorkSupNumber() {
        return workSupNumber;
    }

    public Work setWorkStopTime(String workStopTime) {
        this.workStopTime = workStopTime;
        return this;
    }

    public String getWorkStopTime() {
        return workStopTime;
    }

    public Work setWorkTime(String workTime) {
        this.workTime = workTime;
        return this;
    }

    public String getWorkTime() {
        return workTime;
    }

    public Work setWorkSex(int workSex) {
        this.workSex = workSex;
        return this;
    }

    public int getWorkSex() {
        return workSex;
    }

    public Work setWorkMoney(int workMoney) {
        this.workMoney = workMoney;
        return this;
    }

    public int getWorkMoney() {
        return workMoney;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workTitle);
        dest.writeInt(workBigType);
        dest.writeInt(workSmallType);
        dest.writeString(workContent);
        dest.writeString(workCondition);
        dest.writeInt(workNumber);
        dest.writeInt(workSupNumber);
        dest.writeString(workStopTime);
        dest.writeString(workTime);
        dest.writeInt(workSex);
        dest.writeInt(workMoney);
    }
}
