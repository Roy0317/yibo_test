package com.yibo.yiboapp.entify;

public class CaipiaoListItem {

    String imgUrl;
    String cpName;
    String cpResult;
    String deadLineTime;
    String qiHao;
    String cpCode;
    boolean hasOpen = true;//是否开奖

    public boolean isHasOpen() {
        return hasOpen;
    }

    public void setHasOpen(boolean hasOpen) {
        this.hasOpen = hasOpen;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public String getQiHao() {
        return qiHao;
    }

    public void setQiHao(String qiHao) {
        this.qiHao = qiHao;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCpResult() {
        return cpResult;
    }

    public void setCpResult(String cpResult) {
        this.cpResult = cpResult;
    }

    public String getDeadLineTime() {
        return deadLineTime;
    }

    public void setDeadLineTime(String deadLineTime) {
        this.deadLineTime = deadLineTime;
    }
}
