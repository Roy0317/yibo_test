package com.yibo.yiboapp.entify;

import java.util.List;

public class GradFakeRecordWraper {

    boolean success;
    String msg;
    int code;
    String accessToken;
    List<FakePacketModel> content;

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

    public List<FakePacketModel> getContent() {
        return content;
    }

    public void setContent(List<FakePacketModel> content) {
        this.content = content;
    }
}
