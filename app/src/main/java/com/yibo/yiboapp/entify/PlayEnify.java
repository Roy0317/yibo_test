package com.yibo.yiboapp.entify;

import java.util.List;

public class PlayEnify {


    String name;//彩种名称
    String code;
    long duration;//开奖时长,ms

    List<PlayItem> content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlayItem> getContent() {
        return content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setContent(List<PlayItem> content) {
        this.content = content;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
