package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/15.
 */

public class ExchangeConfigResultWraper {
    boolean success;
    String accessToken;
    String msg;
    int code;

    ExchangeResults content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public ExchangeResults getContent() {
        return content;
    }

    public void setContent(ExchangeResults content) {
        this.content = content;
    }
}
