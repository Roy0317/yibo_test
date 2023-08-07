package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/7.
 */

public class LunboResult {
    //jumpType 未回传值预设为 0
    int jumpType = 0;
    String title;
    String titleImg;
    String titleUrl;

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImg() {
        return titleImg.trim();
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getTitleUrl() {
        return titleUrl.trim();
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }
}
