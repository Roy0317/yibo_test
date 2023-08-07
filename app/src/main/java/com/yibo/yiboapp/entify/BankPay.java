package com.yibo.yiboapp.entify;

public class BankPay {
    String bankCard;//银行帐号
    long maxFee;//最大支付金额
    long minFee;//最小支付金额
    String icon;//支付图标地址
    String bankAddress;//开户地址
    String bankName;//前端显示的文字
    String payName;//支付名称
    String payBankName;//银行名称
    int status;//开关状态
    String receiveName;//收款人姓名
    int id;//银行ID
    String iconCss;//支付css,用于跳转支付地址或支付二维码时使用

    Integer aliQrcodeStatus;
    String aliQrcodeLink;//支付宝跳转链接
    String qrCodeImg;

    String frontDetails;//前台操作说明
    /**
     * maxFee : 50000
     * minFee : 100
     * payType :
     */

    private String payType;

    public String getFrontDetails() {
        return frontDetails;
    }

    public void setFrontDetails(String frontDetails) {
        this.frontDetails = frontDetails;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public Integer getAliQrcodeStatus() {
        return aliQrcodeStatus;
    }

    public void setAliQrcodeStatus(Integer aliQrcodeStatus) {
        this.aliQrcodeStatus = aliQrcodeStatus;
    }

    public String getAliQrcodeLink() {
        return aliQrcodeLink;
    }

    public void setAliQrcodeLink(String aliQrcodeLink) {
        this.aliQrcodeLink = aliQrcodeLink;
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

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
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

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBankName() {
        return bankName;
    }
}
