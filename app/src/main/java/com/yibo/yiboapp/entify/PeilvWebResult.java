package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/21.
 * 每种玩法项对应的赔率数据
 */

public class PeilvWebResult {
    long id;//赔率ID
    int isNowYear;////当为本命年和尾数0时，状态为1，其余状态0
    String markType;////数字号码或者类型
    String name;//号码名称
    float odds;//赔率
    float maxBetAmmount;//最大下注金额
    float minBetAmmount;//最小下注金额
    String playCode;//玩法小类code
    int sortNo;//序号
    int status;//状态
    // 如果是选中按钮时，提供最少选择个数
    int minSelected;
    //反水
    int rakeBack;
    int lotType;
    long stationId;//站点编号

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIsNowYear() {
        return isNowYear;
    }

    public void setIsNowYear(int isNowYear) {
        this.isNowYear = isNowYear;
    }

    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String markType) {
        this.markType = markType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOdds() {
        return odds;
    }

    public void setOdds(float odds) {
        this.odds = odds;
    }

    public float getMaxBetAmmount() {
        return maxBetAmmount;
    }

    public void setMaxBetAmmount(float maxBetAmmount) {
        this.maxBetAmmount = maxBetAmmount;
    }

    public float getMinBetAmmount() {
        return minBetAmmount;
    }

    public void setMinBetAmmount(float minBetAmmount) {
        this.minBetAmmount = minBetAmmount;
    }

    public String getPlayCode() {
        return playCode;
    }

    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMinSelected() {
        return minSelected;
    }

    public void setMinSelected(int minSelected) {
        this.minSelected = minSelected;
    }

    public int getRakeBack() {
        return rakeBack;
    }

    public void setRakeBack(int rakeBack) {
        this.rakeBack = rakeBack;
    }

    public int getLotType() {
        return lotType;
    }

    public void setLotType(int lotType) {
        this.lotType = lotType;
    }
}
