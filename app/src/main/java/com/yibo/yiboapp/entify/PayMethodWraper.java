package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/19.
 */

public class PayMethodWraper {
    boolean success;
    String msg;
    String accessToken;
    int code;

    PayMethodResult content;

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PayMethodResult getContent() {
        return content;
    }

    public void setContent(PayMethodResult content) {
        this.content = content;
    }
}
