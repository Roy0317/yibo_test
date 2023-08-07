package com.yibo.yiboapp.entify;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by johnson on 2017/10/20.
 */

public class MoneyRecordResult implements Parcelable{
    long recordId;
    String title;
    String betdate;
    String bettime;
    float money;
    long status;
    String opDesc;
    String remark;
    int lockFlag;
    int type = 0;
    String fee;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(int lockFlag) {
        this.lockFlag = lockFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBetdate() {
        return betdate;
    }

    public void setBetdate(String betdate) {
        this.betdate = betdate;
    }

    public String getBettime() {
        return bettime;
    }

    public void setBettime(String bettime) {
        this.bettime = bettime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getOpDesc() {
        return opDesc;
    }

    public void setOpDesc(String opDesc) {
        this.opDesc = opDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
