package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/21.
 */

public class PeilvWebResultWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    List<PeilvWebResult> content;

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

    public List<PeilvWebResult> getContent() {
        return content;
    }

    public void setContent(List<PeilvWebResult> content) {
        this.content = content;
    }
}
