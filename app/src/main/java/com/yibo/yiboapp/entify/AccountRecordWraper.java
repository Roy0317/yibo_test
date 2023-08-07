package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/13.
 */

public class AccountRecordWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    AccountChangeResult content;

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

    public AccountChangeResult getContent() {
        return content;
    }

    public void setContent(AccountChangeResult content) {
        this.content = content;
    }
}
