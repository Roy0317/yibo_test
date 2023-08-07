package com.yibo.yiboapp.entify;

import java.util.List;

public class GroupPayResult {
    String name;
    String imgUrl;
    float minFee;
    float maxFee;
    String payType;
    String iconCss;
    int isFixedAmount;//是否开启固定金额
    String fixedAmount;//固定金额用逗号分割
    List<GroupPayResult> childrens;
    int payCategory;//0--在线充值 1-快速充值 2--线下充值
    String payName;

    String kfUrl;//在线客服地址
    boolean clickJump;//是否点击跳转

    public boolean isClickJump() {
        return clickJump;
    }

    public void setClickJump(boolean clickJump) {
        this.clickJump = clickJump;
    }

    public String getKfUrl() {
        return kfUrl;
    }

    public void setKfUrl(String kfUrl) {
        this.kfUrl = kfUrl;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public int getPayCategory() {
        return payCategory;
    }

    public void setPayCategory(int payCategory) {
        this.payCategory = payCategory;
    }

    public int getIsFixedAmount() {
        return isFixedAmount;
    }

    public void setIsFixedAmount(int isFixedAmount) {
        this.isFixedAmount = isFixedAmount;
    }

    public String getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(String fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getIconCss() {
        return iconCss;
    }

    public void setIconCss(String iconCss) {
        this.iconCss = iconCss;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getMinFee() {
        return minFee;
    }

    public void setMinFee(float minFee) {
        this.minFee = minFee;
    }

    public float getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(float maxFee) {
        this.maxFee = maxFee;
    }

    public List<GroupPayResult> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<GroupPayResult> childrens) {
        this.childrens = childrens;
    }
}
