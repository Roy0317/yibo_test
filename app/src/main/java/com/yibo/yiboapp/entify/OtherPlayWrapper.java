package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/26.
 */

public class OtherPlayWrapper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    List<OtherPlay> content;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<OtherPlay> getContent() {
        return content;
    }

    public void setContent(List<OtherPlay> content) {
        this.content = content;
    }
}
