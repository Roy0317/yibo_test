package com.yibo.yiboapp.entify;

public class OnlinePay {
    String merchantCode;//商户ID
    String payGetway;//支付网关
    long maxFee;//最大充值金额
    long minFee;//最小充值金额
    String icon;//支付图标地址
    String merchantKey;//商户密钥
    String payName;//支付名称
    int status;//开关状态
    int id;
    String iconCss;//支付css,用于跳转支付地址或支付二维码时使用
    String payType;//支付方式,用于确定支付跳转时的支付方式类型
    int isFixedAmount;//是否开启固定金额
    String fixedAmount;//固定金额用逗号分割
    String onlinePayAlias;//前端显示的文字
    String frontDetails;//前台操作说明

    public final static int STATUS_ENABLE_SINGLE = 2;// 启用单一固定金额
    public final static int STATUS_ENABLE_MULTIPLE = 3;// 启用多个固定金额

    public String getFrontDetails() {
        return frontDetails;
    }

    public void setFrontDetails(String frontDetails) {
        this.frontDetails = frontDetails;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getIconCss() {
        return iconCss;
    }

    public void setIconCss(String iconCss) {
        this.iconCss = iconCss;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPayGetway() {
        return payGetway;
    }

    public void setPayGetway(String payGetway) {
        this.payGetway = payGetway;
    }

    public long getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(long maxFee) {
        this.maxFee = maxFee;
    }

    public long getMinFee() {
        return minFee;
    }

    public void setMinFee(long minFee) {
        this.minFee = minFee;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOnlinePayAlias() {
        return onlinePayAlias;
    }
}
