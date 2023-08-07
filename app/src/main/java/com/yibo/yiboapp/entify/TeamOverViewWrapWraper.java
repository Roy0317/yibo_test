package com.yibo.yiboapp.entify;

public class TeamOverViewWrapWraper {
    boolean success;
    String accessToken;
    TeamOverViewWraper content;
    String msg;
    int code;

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

    public TeamOverViewWraper getContent() {
        return content;
    }

    public void setContent(TeamOverViewWraper content) {
        this.content = content;
    }
}
