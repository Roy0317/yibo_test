package com.example.anuo.immodule.eventbus;

public class UpdateTitleEvent {
    String title;
    String code;

    public UpdateTitleEvent(String title,String code) {
        this.title = title;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
