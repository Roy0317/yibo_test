package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/12.
 */

public class RedPacketResult {
    long beginDatetime;
    int betRate;
    long endDatetime;
    long id;
    long ipNumber;
    float minMoney;
    float remainMoney;
    int remainNumber;
    int status;
    String title;
    float totalMoney;
    long totalNumber;

    public long getBeginDatetime() {
        return beginDatetime;
    }

    public void setBeginDatetime(long beginDatetime) {
        this.beginDatetime = beginDatetime;
    }

    public int getBetRate() {
        return betRate;
    }

    public void setBetRate(int betRate) {
        this.betRate = betRate;
    }

    public long getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(long endDatetime) {
        this.endDatetime = endDatetime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIpNumber() {
        return ipNumber;
    }

    public void setIpNumber(long ipNumber) {
        this.ipNumber = ipNumber;
    }

    public float getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(float minMoney) {
        this.minMoney = minMoney;
    }

    public float getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(float remainMoney) {
        this.remainMoney = remainMoney;
    }

    public int getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(int remainNumber) {
        this.remainNumber = remainNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }
}
