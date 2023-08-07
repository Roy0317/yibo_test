package com.yibo.yiboapp.entify;

import androidx.annotation.Nullable;

/**
 * Author: Ray
 * created on 2018/10/15
 * description :添加我的会员
 */
public class AddMyMemberBean {


    /**
     * success : true
     * accessToken : 90acb614-e3eb-403f-a76e-fb69030e9971
     * content : true
     */

    private boolean success;
    private String accessToken;
    private boolean content;
    @Nullable
     String msg;
    @Nullable
     int code;

    @Nullable
    public String getMsg() {
        return msg;
    }

    public void setMsg(@Nullable String msg) {
        this.msg = msg;
    }

    @Nullable
    public int getCode() {
        return code;
    }

    public void setCode(@Nullable int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isContent() {
        return content;
    }

    public void setContent(boolean content) {
        this.content = content;
    }
}
