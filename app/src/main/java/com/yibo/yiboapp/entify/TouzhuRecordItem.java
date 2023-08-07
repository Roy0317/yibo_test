package com.yibo.yiboapp.entify;

/**
 * 投注记录数据结构
 * @author johnson
 */
public class TouzhuRecordItem {

    String cpName;
    long tzTime;
    int tzMoney;
    int pjMoney;
    int state;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public long getTzTime() {
        return tzTime;
    }

    public void setTzTime(long tzTime) {
        this.tzTime = tzTime;
    }

    public int getPjMoney() {
        return pjMoney;
    }

    public void setPjMoney(int pjMoney) {
        this.pjMoney = pjMoney;
    }

    public int getTzMoney() {
        return tzMoney;
    }

    public void setTzMoney(int tzMoney) {
        this.tzMoney = tzMoney;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
