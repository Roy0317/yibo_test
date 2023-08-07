package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by Rookie on 2021/2/19.
 */

public class CheckAnswerWrap {

    boolean success;
    String msg;
    String accessToken;
    String failCountQuota;
    String uuid;


    public String getFailCountQuota() {
        return failCountQuota;
    }

    public void setFailCountQuota(String failCountQuota) {
        this.failCountQuota = failCountQuota;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
