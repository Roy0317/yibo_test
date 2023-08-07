package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/3.
 */

public class SportOrder {
    int sportType;//0-全部 1-足球 2-篮球
    long bettingDate;//投注时间
    float bettingMoney;//投注金额
    long bettingStatus;//投注状态
    long balance;//结算状态
    float bettingResult;//结算结果
    String statusRemark;//状态

    long id;

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public float getBettingResult() {
        return bettingResult;
    }

    public void setBettingResult(float bettingResult) {
        this.bettingResult = bettingResult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSportType() {
        return sportType;
    }

    public void setSportType(int sportType) {
        this.sportType = sportType;
    }

    public long getBettingDate() {
        return bettingDate;
    }

    public void setBettingDate(long bettingDate) {
        this.bettingDate = bettingDate;
    }

    public float getBettingMoney() {
        return bettingMoney;
    }

    public void setBettingMoney(float bettingMoney) {
        this.bettingMoney = bettingMoney;
    }

    public long getBettingStatus() {
        return bettingStatus;
    }

    public void setBettingStatus(long bettingStatus) {
        this.bettingStatus = bettingStatus;
    }
}
