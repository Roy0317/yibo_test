package com.yibo.yiboapp.entify;

public class FastPay {
    String qrCodeImg;//扫码二维码地址
    long maxFee;//最大支付金额
    long minFee;//最小支付金额
    String icon;//支付方式图标地址
    String payName;//支付名称
    int status;//开关状态
    String receiveName;//收款人姓名
    String receiveAccount;//收款帐号
    int id;
    String frontLabel;//快速支付前端显示的文字
    String iconCss;//支付css,用于跳转支付地址或支付二维码时使用
    String fixedAmount = "";//快速入款金额;
    /**
     * frontDetails : 支持币种：USDT；    转换比例为1：7
     * maxFee : 7000
     * minFee : 700
     * payType :
     */

    private String frontDetails;
    private String payType;

    public String getFixedAmoun() {
        return fixedAmount;
    }

    public void setFixedAmoun(String fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getIconCss() {
        return iconCss;
    }

    public void setIconCss(String iconCss) {
        this.iconCss = iconCss;
    }

    public String getFrontLabel() {
        return frontLabel;
    }

    public void setFrontLabel(String frontLabel) {
        this.frontLabel = frontLabel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
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

    public String getFrontDetails() {
        return frontDetails;
    }

    public void setFrontDetails(String frontDetails) {
        this.frontDetails = frontDetails;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
