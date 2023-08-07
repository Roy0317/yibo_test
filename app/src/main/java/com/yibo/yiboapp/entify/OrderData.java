package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/9/18.
 */

public class OrderData {
    String numbers;
    int beiShu;
    int mode;
    String playName;
    String playCode;
    String subPlayName;
    String subPlayCode;
    int zhushu;
    float fee;

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public int getBeiShu() {
        return beiShu;
    }

    public void setBeiShu(int beiShu) {
        this.beiShu = beiShu;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
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

    public int getZhushu() {
        return zhushu;
    }

    public void setZhushu(int zhushu) {
        this.zhushu = zhushu;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
}
