package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/12/16.
 */

public class NoticeResult {
    int code;
    String content;
    int status;
    String title;
    /**
     * cp : true
     * id : 144
     * index : true
     * modelStatus : 2
     * mutil : true
     * overTime : 1661875200000
     * recharge : true
     * reg : true
     * stationId : 23
     * updateTime : 1630425600000
     */

    private boolean cp;
    private int id;
    private boolean index;
    private int modelStatus;
    private boolean mutil;
    private long overTime;
    private boolean recharge;
    private boolean reg;
    private int stationId;
    private long updateTime;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isCp() {
        return cp;
    }

    public void setCp(boolean cp) {
        this.cp = cp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIndex() {
        return index;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }

    public int getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(int modelStatus) {
        this.modelStatus = modelStatus;
    }

    public boolean isMutil() {
        return mutil;
    }

    public void setMutil(boolean mutil) {
        this.mutil = mutil;
    }

    public long getOverTime() {
        return overTime;
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }

    public boolean isRecharge() {
        return recharge;
    }

    public void setRecharge(boolean recharge) {
        this.recharge = recharge;
    }

    public boolean isReg() {
        return reg;
    }

    public void setReg(boolean reg) {
        this.reg = reg;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

}
