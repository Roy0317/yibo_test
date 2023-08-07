package com.example.anuo.immodule.jsonmodel;

import java.util.List;

public class LotteryHistoryResultResponse {
    boolean success;
    String msg;
    String accessToken;
    int code;
    List<OpenResultDetail> content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<OpenResultDetail> getContent() {
        return content;
    }

    public void setContent(List<OpenResultDetail> content) {
        this.content = content;
    }

    public static class OpenResultDetail {
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
}
