package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/11/3.
 */

public class SportOrderWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    List<SportOrder> content;

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

    public List<SportOrder> getContent() {
        return content;
    }

    public void setContent(List<SportOrder> content) {
        this.content = content;
    }
}
