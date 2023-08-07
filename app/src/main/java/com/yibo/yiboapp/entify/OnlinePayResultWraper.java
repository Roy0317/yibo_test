package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/14.
 */

public class OnlinePayResultWraper {
    String msg;
    boolean success;
    OnlinePayResult data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public OnlinePayResult getData() {
        return data;
    }

    public void setData(OnlinePayResult data) {
        this.data = data;
    }
}
