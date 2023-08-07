package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/7.
 */

public class SysConfigWraper {
    boolean success;
    String msg;
    String accessToken;
    SysConfig content;
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

    public SysConfig getContent() {
        return content;
    }

    public void setContent(SysConfig content) {
        this.content = content;
    }
}
