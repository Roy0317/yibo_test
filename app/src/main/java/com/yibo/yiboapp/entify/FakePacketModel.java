package com.yibo.yiboapp.entify;

public class FakePacketModel {
    String account = "";//账号，带掩码
    long createDatetime = 0;//创建时间
    float money = 0;//中奖金额

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
