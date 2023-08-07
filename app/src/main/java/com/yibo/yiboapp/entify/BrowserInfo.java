package com.yibo.yiboapp.entify;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class BrowserInfo {
    private String browserName;
    private Drawable imageurl;
    private String state;
    private int selected;

    public Drawable getImageurl() {
        return imageurl;
    }

    public void setImageurl(Drawable imageurl) {
        this.imageurl = imageurl;
    }

    public BrowserInfo(String browserName, Drawable imageurl, String state, int selected) {
        this.browserName = browserName;
        this.imageurl = imageurl;
        this.state = state;
        this.selected = selected;
    }

    public String getState() {
        return state;

    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }


    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }
}
