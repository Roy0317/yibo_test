package com.yibo.yiboapp.entify;

import com.yibo.yiboapp.data.PeilvPlayData;

/**
 * Created by johnson on 2017/9/19.
 */

public class OrderDataInfo {
    String playName;
    String playCode;
    String subPlayName;
    String subPlayCode;
    int beishu;
    String numbers;
    int mode;
    int zhushu;
    double money;
    long saveTime;
    String user;
    String orderno;
    String lotcode;
    String lottype;
    float rate;
    String cpCode;//彩票代码

    PeilvPlayData playData;

    public PeilvPlayData getPlayData() {
        return playData;
    }

    public void setPlayData(PeilvPlayData playData) {
        this.playData = playData;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public String getLotcode() {
        return lotcode;
    }

    public void setLotcode(String lotcode) {
        this.lotcode = lotcode;
    }

    public String getLottype() {
        return lottype;
    }

    public void setLottype(String lottype) {
        this.lottype = lottype;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getPlayCode() {
        return playCode;
    }

    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }

    public String getSubPlayName() {
        return subPlayName;
    }

    public void setSubPlayName(String subPlayName) {
        this.subPlayName = subPlayName;
    }

    public String getSubPlayCode() {
        return subPlayCode;
    }

    public void setSubPlayCode(String subPlayCode) {
        this.subPlayCode = subPlayCode;
    }

    public int getBeishu() {
        return beishu;
    }

    public void setBeishu(int beishu) {
        this.beishu = beishu;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getZhushu() {
        return zhushu;
    }

    public void setZhushu(int zhushu) {
        this.zhushu = zhushu;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
