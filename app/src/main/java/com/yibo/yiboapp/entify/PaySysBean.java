package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2018/3/15.
 */

public class PaySysBean {
    PayBean scanpay;
    PayBean shunpay;
    PayBean straightpay;
    PayBean wappay;

    public PayBean getScanpay() {
        return scanpay;
    }

    public void setScanpay(PayBean scanpay) {
        this.scanpay = scanpay;
    }

    public PayBean getShunpay() {
        return shunpay;
    }

    public void setShunpay(PayBean shunpay) {
        this.shunpay = shunpay;
    }

    public PayBean getStraightpay() {
        return straightpay;
    }

    public void setStraightpay(PayBean straightpay) {
        this.straightpay = straightpay;
    }

    public PayBean getWappay() {
        return wappay;
    }

    public void setWappay(PayBean wappay) {
        this.wappay = wappay;
    }
}
