package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/11/11.
 */

public class RealBetResultWraper {
    String message;
    float sumBet;
    float sumWin;
    int code;
    String msg;
    boolean success;
    List<RealBetBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getSumBet() {
        return sumBet;
    }

    public void setSumBet(float sumBet) {
        this.sumBet = sumBet;
    }

    public float getSumWin() {
        return sumWin;
    }

    public void setSumWin(float sumWin) {
        this.sumWin = sumWin;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<RealBetBean> getData() {
        return data;
    }

    public void setData(List<RealBetBean> data) {
        this.data = data;
    }
}
