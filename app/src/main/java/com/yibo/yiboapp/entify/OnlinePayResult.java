package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/14.
 */

public class OnlinePayResult {
    String account;//支付帐号
    String amount;//支付金额，元
    String formAction;
    String orderId;//订单号
    String payReferer;//支付源网关域名
    String payType;//支付类型
    QrcodeParams formParams;//获取扫码二维码图片的表单请求数据

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFormAction() {
        return formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayReferer() {
        return payReferer;
    }

    public void setPayReferer(String payReferer) {
        this.payReferer = payReferer;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public QrcodeParams getFormParams() {
        return formParams;
    }

    public void setFormParams(QrcodeParams formParams) {
        this.formParams = formParams;
    }
}
