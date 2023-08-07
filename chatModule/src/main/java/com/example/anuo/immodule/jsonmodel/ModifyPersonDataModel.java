package com.example.anuo.immodule.jsonmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/*
* 修改用户资料
* */
public class ModifyPersonDataModel implements Parcelable{

    private String code;
    private String nickName;
    private String avatar;
    private String stationId;
    private String msgUUID = UUID.randomUUID().toString();
    private String userId;
    private String source = "app";

    public ModifyPersonDataModel(String code, String nickName){
        this.code = code;
        this.nickName = nickName;
    }

    public ModifyPersonDataModel(String code, String nickName, String avatar, String stationId, String userId){
        this.code = code;
        this.nickName = nickName;
        this.avatar = avatar;
        this.stationId = stationId;
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.nickName);
        dest.writeString(this.avatar);
        dest.writeString(this.stationId);
        dest.writeString(this.msgUUID);
        dest.writeString(this.userId);
        dest.writeString(this.source);
    }

    protected ModifyPersonDataModel(Parcel in) {
        this.code = in.readString();
        this.nickName = in.readString();
        this.avatar = in.readString();
        this.stationId = in.readString();
        this.msgUUID = in.readString();
        this.userId = in.readString();
        this.source = in.readString();
    }

    public static final Creator<ModifyPersonDataModel> CREATOR = new Creator<ModifyPersonDataModel>() {
        @Override
        public ModifyPersonDataModel createFromParcel(Parcel source) {
            return new ModifyPersonDataModel(source);
        }

        @Override
        public ModifyPersonDataModel[] newArray(int size) {
            return new ModifyPersonDataModel[size];
        }
    };
}
