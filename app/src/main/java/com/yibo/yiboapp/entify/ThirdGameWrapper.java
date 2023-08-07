package com.yibo.yiboapp.entify;

import java.util.List;

public class ThirdGameWrapper {

    boolean success;
    String msg;
    String accessToken;
    List<GameItemResultNew> content;

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

    public List<GameItemResultNew> getContent() {
        return content;
    }

    public void setContent(List<GameItemResultNew> content) {
        this.content = content;
    }
}
