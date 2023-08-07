package com.yibo.yiboapp.entify;

public class TeamMap {
    float money;
    int accountcount;//總帳號數
    int notlogincount;//未登錄帳號數
    int todaylogincount;//今日登錄帳號

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getAccountcount() {
        return accountcount;
    }

    public void setAccountcount(int accountcount) {
        this.accountcount = accountcount;
    }

    public int getNotlogincount() {
        return notlogincount;
    }

    public void setNotlogincount(int notlogincount) {
        this.notlogincount = notlogincount;
    }

    public int getTodaylogincount() {
        return todaylogincount;
    }

    public void setTodaylogincount(int todaylogincount) {
        this.todaylogincount = todaylogincount;
    }
}
