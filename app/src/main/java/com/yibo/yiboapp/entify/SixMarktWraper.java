package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/7.
 */

public class SixMarktWraper {
    private boolean success;
    private String accessToken;
    private List<String> content;

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

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
