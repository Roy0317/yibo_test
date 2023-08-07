package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/17.
 */

public class OpenResultDetailWraper {
    boolean success;
    String msg;
    String accessToken;
    int code;
    List<OpenResultDetail> content;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<OpenResultDetail> getContent() {
        return content;
    }

    public void setContent(List<OpenResultDetail> content) {
        this.content = content;
    }
}
