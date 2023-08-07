package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2018/3/15.
 */

public class PaySysMethodWraper {
    boolean success;
    String msg;
    String accessToken;
    PaySysBean content;

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

    public PaySysBean getContent() {
        return content;
    }

    public void setContent(PaySysBean content) {
        this.content = content;
    }
}
