package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/6.
 */

public class RegisterResultWrapper {

    boolean success;
    String msg;
    String accessToken;
    RegisterResult content;

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

    public RegisterResult getContent() {
        return content;
    }

    public void setContent(RegisterResult content) {
        this.content = content;
    }
}
