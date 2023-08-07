package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2018/1/26.
 */

public class BigWheelWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    BigWheelData content;

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

    public BigWheelData getContent() {
        return content;
    }

    public void setContent(BigWheelData content) {
        this.content = content;
    }
}
