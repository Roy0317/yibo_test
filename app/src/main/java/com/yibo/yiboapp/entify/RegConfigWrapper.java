package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/5.
 */

public class RegConfigWrapper {
    boolean success;
    String msg;
    String accessToken;
    List<RegConfig> content;

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

    public List<RegConfig> getContent() {
        return content;
    }

    public void setContent(List<RegConfig> content) {
        this.content = content;
    }
}
