package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/26.
 */

public class GameResult {
    boolean success;
    String url;
    String html;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
