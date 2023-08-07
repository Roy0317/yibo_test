package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/12.
 */

public class GrabPacketWraper {
    boolean success;
    String accessToken;
    int code;
    String msg;
    float content;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public float getContent() {
        return content;
    }

    public void setContent(float content) {
        this.content = content;
    }
}
