package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/18.
 * {"success":true,"accessToken":"14daf871-38e4-4743-9163-07c63bab5b5e","content":{"bankName":"支付宝","repPwd":"89F40548441B60D71ED339283E8B9FF5","userName":"无法无天","bankAddress":"","cardNo":"6258895523678366978"}}
 */

public class CheckPickAccountWrapper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    PickBankAccount content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public PickBankAccount getContent() {
        return content;
    }

    public void setContent(PickBankAccount content) {
        this.content = content;
    }
}
