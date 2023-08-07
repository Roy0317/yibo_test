package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/17.
 */

public class OpenResultDetail {
    String qiHao;
    String date;
    String time;
    String weekday;
    List<String> haoMaList;

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getQiHao() {
        return qiHao;
    }

    public void setQiHao(String qiHao) {
        this.qiHao = qiHao;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getHaoMaList() {
        return haoMaList;
    }

    public void setHaoMaList(List<String> haoMaList) {
        this.haoMaList = haoMaList;
    }
}
