package com.yibo.yiboapp.entify;

import android.os.Parcel;
import android.os.Parcelable;

import com.yibo.yiboapp.data.LotteryData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Ray
 * created on 2018/12/6
 * description :
 */
public class SimplePageBean implements Parcelable{

    private String pageName;
    private ArrayList<LotteryData> lotteryData;

    public SimplePageBean(){}

    public SimplePageBean(String pageName, ArrayList<LotteryData> lotteryData) {
        this.pageName = pageName;
        this.lotteryData = lotteryData;
    }

    protected SimplePageBean(Parcel in) {
        pageName = in.readString();
    }

    public static final Creator<SimplePageBean> CREATOR = new Creator<SimplePageBean>() {
        @Override
        public SimplePageBean createFromParcel(Parcel in) {
            return new SimplePageBean(in);
        }

        @Override
        public SimplePageBean[] newArray(int size) {
            return new SimplePageBean[size];
        }
    };

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public ArrayList<LotteryData> getLotteryData() {
        return lotteryData;
    }

    public void setLotteryData(ArrayList<LotteryData> lotteryData) {
        this.lotteryData = lotteryData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pageName);
    }
}
