package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/15.
 */

public class SignResultWraper {
    boolean success;
    String msg;
    String accessToken;
    SignBean content;
    int code;

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

    public SignBean getContent() {
        return content;
    }

    public void setContent(SignBean content) {
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
