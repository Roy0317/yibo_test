package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/9/19.
 */

public class AdjustData {
    int zhushu;
    float money;
    float jianjian;//最高奖金
    float maxRakeRate;//最大返水
    int modeIndex;
    int beishu;
    int basicMoney = 2;
    int calcZhushu;

    public float getMaxRakeRate() {
        return maxRakeRate;
    }

    public void setMaxRakeRate(float maxRakeRate) {
        this.maxRakeRate = maxRakeRate;
    }

    public int getBasicMoney() {
        return basicMoney;
    }

    public void setBasicMoney(int basicMoney) {
        this.basicMoney = basicMoney;
    }

    public int getCalcZhushu() {
        return calcZhushu;
    }

    public void setCalcZhushu(int calcZhushu) {
        this.calcZhushu = calcZhushu;
    }

    public float getJianjian() {
        return jianjian;
    }

    public void setJianjian(float jianjian) {
        this.jianjian = jianjian;
    }


    public int getZhushu() {
        return zhushu;
    }

    public void setZhushu(int zhushu) {
        this.zhushu = zhushu;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getModeIndex() {
        return modeIndex;
    }

    public void setModeIndex(int modeIndex) {
        this.modeIndex = modeIndex;
    }

    public int getBeishu() {
        return beishu;
    }

    public void setBeishu(int beishu) {
        this.beishu = beishu;
    }
}
