package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/7.
 */

public class LunboWraper {
    boolean success;
    String msg;
    String accessToken;
    List<LunboResult> content;

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

    public List<LunboResult> getContent() {
        return content;
    }

    public void setContent(List<LunboResult> content) {
        this.content = content;
    }
}
