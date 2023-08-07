package com.yibo.yiboapp.entify;

import com.yibo.yiboapp.data.LotteryData;

/**
 * Created by johnson on 2017/10/10.
 */

public class LocPlaysWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    LotteryData content;

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

    public LotteryData getContent() {
        return content;
    }

    public void setContent(LotteryData content) {
        this.content = content;
    }
}
