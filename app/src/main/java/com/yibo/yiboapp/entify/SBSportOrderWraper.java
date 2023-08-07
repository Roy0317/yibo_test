package com.yibo.yiboapp.entify;


public class SBSportOrderWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    SBResult content;

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

    public SBResult getContent() {
        return content;
    }

    public void setContent(SBResult content) {
        this.content = content;
    }
}
