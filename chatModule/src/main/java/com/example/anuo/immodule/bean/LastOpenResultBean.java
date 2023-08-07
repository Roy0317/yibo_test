package com.example.anuo.immodule.bean;

import java.util.List;

/**
 * @author: soxin
 * @version: 1
 * @project: trunk
 * @package: com.example.anuo.immodule.bean
 * @description:
 * @date: 2019-10-29
 * @time: 19:52
 */
public class LastOpenResultBean {


    /**
     * current : {"qiHao":"20191029121","activeTime":"2019-10-29 20:00:00","serverTime":"2019-10-29 19:51:25"}
     * last : {"year":2019,"qiHao":"20191029120","haoMa":"?,?,?,?,?,?,?"}
     * success : true
     * ago : 60
     * history : [{"date":"2019-10-29 19:40:00","qiHao":"20191029119","haoMa":"?,?,?,?,?,?,?"},{"date":"2019-10-29 19:30:00","qiHao":"20191029118","haoMa":"?,?,?,?,?,?,?"},{"date":"2019-10-29 19:20:00","qiHao":"20191029117","haoMa":"?,?,?,?,?,?,?"},{"date":"2019-10-29 19:10:00","qiHao":"20191029116","haoMa":"?,?,?,?,?,?,?"},{"date":"2019-10-29 19:00:00","qiHao":"20191029115","haoMa":"?,?,?,?,?,?,?"}]
     */

    private CurrentBean current;
    private LastBean last;
    private boolean success;
    private int ago;
    private List<HistoryBean> history;

    public CurrentBean getCurrent() {
        return current;
    }

    public void setCurrent(CurrentBean current) {
        this.current = current;
    }

    public LastBean getLast() {
        return last;
    }

    public void setLast(LastBean last) {
        this.last = last;
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

    public List<HistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryBean> history) {
        this.history = history;
    }

    public static class CurrentBean {
        /**
         * qiHao : 20191029121
         * activeTime : 2019-10-29 20:00:00
         * serverTime : 2019-10-29 19:51:25
         */

        private String qiHao;
        private String activeTime;
        private String serverTime;

        public String getQiHao() {
            return qiHao;
        }

        public void setQiHao(String qiHao) {
            this.qiHao = qiHao;
        }

        public String getActiveTime() {
            return activeTime;
        }

        public void setActiveTime(String activeTime) {
            this.activeTime = activeTime;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }
    }

    public static class LastBean {
        /**
         * year : 2019
         * qiHao : 20191029120
         * haoMa : ?,?,?,?,?,?,?
         */

        private int year;
        private String qiHao;
        private String haoMa;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getQiHao() {
            return qiHao;
        }

        public void setQiHao(String qiHao) {
            this.qiHao = qiHao;
        }

        public String getHaoMa() {
            return haoMa;
        }

        public void setHaoMa(String haoMa) {
            this.haoMa = haoMa;
        }
    }

    public static class HistoryBean {
        /**
         * date : 2019-10-29 19:40:00
         * qiHao : 20191029119
         * haoMa : ?,?,?,?,?,?,?
         */

        private String date;
        private String qiHao;
        private String haoMa;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getQiHao() {
            return qiHao;
        }

        public void setQiHao(String qiHao) {
            this.qiHao = qiHao;
        }

        public String getHaoMa() {
            return haoMa;
        }

        public void setHaoMa(String haoMa) {
            this.haoMa = haoMa;
        }
    }
}
