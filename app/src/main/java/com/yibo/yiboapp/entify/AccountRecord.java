package com.yibo.yiboapp.entify;

/**
 * 用户帐号流长变化ITEM
 *
 * @author johnson
 */
public class AccountRecord {

    int type;//帐变类型
    float moneyBefore;
    float moneyAfter;
    String orderno;
    String timeStr;
    String account;
    String remark;
    String fee;//手续费

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getMoneyBefore() {
        return moneyBefore;
    }

    public void setMoneyBefore(float moneyBefore) {
        this.moneyBefore = moneyBefore;
    }

    public float getMoneyAfter() {
        return moneyAfter;
    }

    public void setMoneyAfter(float moneyAfter) {
        this.moneyAfter = moneyAfter;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}