package com.example.anuo.immodule.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatCollectionImagesSelectBean implements Parcelable {

    private boolean isSelect;
    private String record;

    public  ChatCollectionImagesSelectBean(){}

    public ChatCollectionImagesSelectBean(Parcel in) {
        isSelect = in.readByte() != 0;
        record = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeString(record);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatCollectionImagesSelectBean> CREATOR = new Creator<ChatCollectionImagesSelectBean>() {
        @Override
        public ChatCollectionImagesSelectBean createFromParcel(Parcel in) {
            return new ChatCollectionImagesSelectBean(in);
        }

        @Override
        public ChatCollectionImagesSelectBean[] newArray(int size) {
            return new ChatCollectionImagesSelectBean[size];
        }
    };

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

}
