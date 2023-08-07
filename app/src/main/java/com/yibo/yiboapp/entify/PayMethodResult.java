package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/19.
 */

public class PayMethodResult {
    List<OnlinePay> online;
    List<FastPay> fast;
    List<BankPay> bank;

    String minMoney;
    String serverStartTime;
    String serverEndTime;

    public List<OnlinePay> getOnline() {
        return online;
    }

    public void setOnline(List<OnlinePay> online) {
        this.online = online;
    }

    public List<FastPay> getFast() {
        return fast;
    }

    public void setFast(List<FastPay> fast) {
        this.fast = fast;
    }

    public List<BankPay> getBank() {
        return bank;
    }

    public void setBank(List<BankPay> bank) {
        this.bank = bank;
    }

    public String getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(String minMoney) {
        this.minMoney = minMoney;
    }

    public String getServerStartTime() {
        return serverStartTime;
    }

    public void setServerStartTime(String serverStartTime) {
        this.serverStartTime = serverStartTime;
    }

    public String getServerEndTime() {
        return serverEndTime;
    }

    public void setServerEndTime(String serverEndTime) {
        this.serverEndTime = serverEndTime;
    }
}
