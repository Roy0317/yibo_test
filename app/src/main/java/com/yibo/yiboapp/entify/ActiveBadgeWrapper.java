package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/12/14.
 */

public class ActiveBadgeWrapper {
    boolean success;
    String msg;
    String accessToken;
    int content;
    int code;

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


    public void setContent(int content) {
        this.content = content;
    }

    public int getContent() {
        return content;
    }
}
