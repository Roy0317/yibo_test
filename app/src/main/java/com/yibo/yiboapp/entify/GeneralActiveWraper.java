package com.yibo.yiboapp.entify;


public class GeneralActiveWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    GeneralActivity content;

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

    public GeneralActivity getContent() {
        return content;
    }

    public void setContent(GeneralActivity content) {
        this.content = content;
    }
}
