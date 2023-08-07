package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2018/3/13.
 */

public class WinLostWraper {
    boolean success;
    String msg;
    int code;
    WinLost content;
    String accessToken;

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

    public WinLost getContent() {
        return content;
    }

    public void setContent(WinLost content) {
        this.content = content;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
