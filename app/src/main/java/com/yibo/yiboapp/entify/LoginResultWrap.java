package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/5.
 */

public class LoginResultWrap {

    boolean success;
    String msg;
    String accessToken;
    LoginResult content;
      int code;
      long accountId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

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

    public LoginResult getContent() {
        return content;
    }

    public void setContent(LoginResult content) {
        this.content = content;
    }
}
