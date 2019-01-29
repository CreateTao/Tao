package com.xwy.tao_work.mytaowork.data;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser implements Parcelable{
    private Boolean sex;
    private String master_computer;
    private BmobFile head_portrait;
    private Integer age;

    protected User(Parcel in) {
        byte tmpSex = in.readByte();
        sex = tmpSex == 0 ? null : tmpSex == 1;
        master_computer = in.readString();
        if (in.readByte() == 0) {
            age = null;
        } else {
            age = in.readInt();
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User setSex(Boolean sex){
        this.sex = sex;
        return this;
    }

    public Boolean getSexx(){
        return sex;
    }

    public User setMaster_computer(String master_computer){
        this.master_computer = master_computer;
        return this;
    }

    public String getMaster_computer(){
        return master_computer;
    }

    public User setHead_portrait(BmobFile head_portrait){
        this.head_portrait = head_portrait;
        return this;
    }

    public BmobFile getHead_portrait(){
        return head_portrait;
    }

    public User setAge(Integer age){
        this.age = age;
        return this;
    }

    public Integer getAge(){
        return age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (sex == null ? 0 : sex ? 1 : 2));
        dest.writeString(master_computer);
        if (age == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(age);
        }
    }
}
