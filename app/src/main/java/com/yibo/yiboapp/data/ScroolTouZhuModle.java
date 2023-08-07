package com.yibo.yiboapp.data;

public class ScroolTouZhuModle {
    String number;
    int background;

    public ScroolTouZhuModle(String number, int background) {
        this.number = number;
        this.background = background;
    }
    public ScroolTouZhuModle() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
