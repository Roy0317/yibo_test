package com.yibo.yiboapp.entify;

import com.example.anuo.immodule.bean.ChatBetBean;

/**
 * Created by johnson on 2017/10/14.
 */

public class DoBetForChatWraper {
    boolean success;
    String msg;
    int code;
    String accessToken;
    ChatBetBean content;

    public ChatBetBean getContent() {
        return content;
    }

    public void setContent(ChatBetBean content) {
        this.content = content;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
