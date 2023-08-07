package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/18.
 */

public class PickMoneyDataWraper {
    boolean success;
    String msg;
    String accessToken;
    int code;
    PickMoneyData content;

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

    public PickMoneyData getContent() {
        return content;
    }

    public void setContent(PickMoneyData content) {
        this.content = content;
    }
}
