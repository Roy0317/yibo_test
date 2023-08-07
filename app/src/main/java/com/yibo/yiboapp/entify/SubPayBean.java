package com.yibo.yiboapp.entify;


public class SubPayBean {
    String title;
    String content;
    String code;
    boolean input;
    String qrcode;
    String holderText;
    boolean showButton;//是否显示按钮
    boolean showQrcode;//是否显示二维码
    boolean showAutoMoney;//是否显示二维码
    String qrLink;//支付宝链接
    String qrcodeLink;//二维码图片链接
    String usdtUrl;//usdt存款教程链接
    boolean showCopy;

    String frontDetails;//前台操作说明

    public String getFrontDetails() {
        return frontDetails;
    }

    public void setFrontDetails(String frontDetails) {
        this.frontDetails = frontDetails;
    }

    public boolean isShowAutoMoney() {
        return showAutoMoney;
    }

    public void setShowAutoMoney(boolean showAutoMoney) {
        this.showAutoMoney = showAutoMoney;
    }

    public String getQrcodeLink() {
        return qrcodeLink;
    }

    public void setQrcodeLink(String qrcodeLink) {
        this.qrcodeLink = qrcodeLink;
    }

    public boolean isShowQrcode() {
        return showQrcode;
    }

    public void setShowQrcode(boolean showQrcode) {
        this.showQrcode = showQrcode;
    }

    public String getQrLink() {
        return qrLink;
    }

    public void setQrLink(String qrLink) {
        this.qrLink = qrLink;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    public String getHolderText() {
        return holderText;
    }

    public void setHolderText(String holderText) {
        this.holderText = holderText;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public String getUsdtUrl() {
        return usdtUrl;
    }

    public void setUsdtUrl(String usdtUrl) {
        this.usdtUrl = usdtUrl;
    }

    public boolean isShowCopy() {
        return showCopy;
    }

    public void setShowCopy(boolean showCopy) {
        this.showCopy = showCopy;
    }

    @Override
    public String toString() {
        return "SubPayBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", code='" + code + '\'' +
                ", input=" + input +
                ", qrcode='" + qrcode + '\'' +
                ", holderText='" + holderText + '\'' +
                ", showButton=" + showButton +
                ", showQrcode=" + showQrcode +
                ", showAutoMoney=" + showAutoMoney +
                ", qrLink='" + qrLink + '\'' +
                ", qrcodeLink='" + qrcodeLink + '\'' +
                ", usdtUrl='" + usdtUrl + '\'' +
                ", showCopy=" + showCopy +
                ", frontDetails='" + frontDetails + '\'' +
                '}';
    }
}
