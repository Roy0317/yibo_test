package com.yibo.yiboapp.entify;


import com.yibo.yiboapp.data.LotteryData;

import java.util.List;

/**
 * Created by johnson on 2017/10/9.
 */

public class LotterysWraper {

    boolean success;
    String msg;
    String accessToken;
    int code;
    List<LotteryData> content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<LotteryData> getContent() {
        return content;
    }

    public void setContent(List<LotteryData> content) {
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
