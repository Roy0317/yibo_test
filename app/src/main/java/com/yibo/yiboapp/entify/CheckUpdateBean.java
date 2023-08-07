package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/16.
 */

public class CheckUpdateBean {
    String version;
    String content;
    long status;
    String url;//更新地址

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
