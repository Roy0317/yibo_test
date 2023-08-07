package com.example.anuo.immodule.bean;

import android.view.View;

/*
* 进房欢迎弹幕
* */
public class WelcomeTextBean {

    private String uuid;
    private View view;

    public WelcomeTextBean(String uuid, View view){
        this.uuid = uuid;
        this.view = view;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
