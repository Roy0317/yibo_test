package com.example.anuo.immodule.bean;

import java.util.List;

public class LotteryDownBean {

    LotteryCurrent current;
    LotteryLast last;
    List<LotteryHistory> history;
    boolean success;
    int ago;//开封盘时间
    String msg;//错误信息

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LotteryCurrent getCurrent() {
        return current;
    }

    public void setCurrent(LotteryCurrent current) {
        this.current = current;
    }

    public LotteryLast getLast() {
        return last;
    }

    public void setLast(LotteryLast last) {
        this.last = last;
    }

    public List<LotteryHistory> getHistory() {
        return history;
    }

    public void setHistory(List<LotteryHistory> history) {
        this.history = history;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getAgo() {
        return ago;
    }

    public void setAgo(int ago) {
        this.ago = ago;
    }
}
