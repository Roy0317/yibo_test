package com.yibo.yiboapp.Event;

public class ActiveDataEvent {
    private String tag;
    private String data;
    private int index;

    public ActiveDataEvent(String tag, String data, int index) {
        this.tag = tag;
        this.data = data;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
