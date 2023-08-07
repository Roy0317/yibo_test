package com.example.anuo.immodule.bean;

import java.util.List;

public class ChatPlanNewsBean {
    /**
     * msg : 操作成功。
     * code : R0028
     * success : true
     * source : {"lotteryCode":"WFLHC","playList":["总和大小","总和单双","特码大小","特码单双","尾特大小","家禽野兽"],"lotteryName":"五分六合彩","resultList":[],"lotteryList":[{"source":0,"lotteryCode":"WFLHC","lotteryName":"五分六合彩","stationId":"ha11_237"}],"option":"1"}
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private SourceBean source;
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class SourceBean {
        /**
         * lotteryCode : WFLHC
         * playList : ["总和大小","总和单双","特码大小","特码单双","尾特大小","家禽野兽"]
         * lotteryName : 五分六合彩
         * resultList : []
         * lotteryList : [{"source":0,"lotteryCode":"WFLHC","lotteryName":"五分六合彩","stationId":"ha11_237"}]
         * option : 1
         */

        private String lotteryCode;
        private String lotteryName;
        private String option;
        private List<String> playList;
        private List<ResultListBean> resultList;
        private List<LotteryListBean> lotteryList;

        public String getLotteryCode() {
            return lotteryCode;
        }

        public void setLotteryCode(String lotteryCode) {
            this.lotteryCode = lotteryCode;
        }

        public String getLotteryName() {
            return lotteryName;
        }

        public void setLotteryName(String lotteryName) {
            this.lotteryName = lotteryName;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public List<String> getPlayList() {
            return playList;
        }

        public void setPlayList(List<String> playList) {
            this.playList = playList;
        }

        public List<ResultListBean> getResultList() {
            return resultList;
        }

        public void setResultList(List<ResultListBean> resultList) {
            this.resultList = resultList;
        }

        public List<LotteryListBean> getLotteryList() {
            return lotteryList;
        }

        public void setLotteryList(List<LotteryListBean> lotteryList) {
            this.lotteryList = lotteryList;
        }


        public static class ResultListBean {
            /**
             * lotteryNum : 201912040979
             * playName : 和值单双
             * resultDate : 16:18:00
             * forecast : 双
             * source : 0
             * lotteryCode : FFC
             * lotteryName : 分分彩
             * stationId : yjtest1_3
             * lotteryResult : 双
             */

            private String lotteryNum;
            private String playName;
            private String resultDate;
            private String forecast;
            private int source;
            private String lotteryCode;
            private String lotteryName;
            private String stationId;
            private String lotteryResult;

            public String getLotteryNum() {
                return lotteryNum;
            }

            public void setLotteryNum(String lotteryNum) {
                this.lotteryNum = lotteryNum;
            }

            public String getPlayName() {
                return playName;
            }

            public void setPlayName(String playName) {
                this.playName = playName;
            }

            public String getResultDate() {
                return resultDate;
            }

            public void setResultDate(String resultDate) {
                this.resultDate = resultDate;
            }

            public String getForecast() {
                return forecast;
            }

            public void setForecast(String forecast) {
                this.forecast = forecast;
            }

            public int getSource() {
                return source;
            }

            public void setSource(int source) {
                this.source = source;
            }

            public String getLotteryCode() {
                return lotteryCode;
            }

            public void setLotteryCode(String lotteryCode) {
                this.lotteryCode = lotteryCode;
            }

            public String getLotteryName() {
                return lotteryName;
            }

            public void setLotteryName(String lotteryName) {
                this.lotteryName = lotteryName;
            }

            public String getStationId() {
                return stationId;
            }

            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            public String getLotteryResult() {
                return lotteryResult;
            }

            public void setLotteryResult(String lotteryResult) {
                this.lotteryResult = lotteryResult;
            }
        }


        public static class LotteryListBean {
            /**
             * source : 0
             * lotteryCode : WFLHC
             * lotteryName : 五分六合彩
             * stationId : ha11_237
             */

            private int source;
            private String lotteryCode;
            private String lotteryName;
            private String stationId;
            private boolean isClick;

            public boolean isClick() {
                return isClick;
            }

            public void setClick(boolean click) {
                isClick = click;
            }

            public int getSource() {
                return source;
            }

            public void setSource(int source) {
                this.source = source;
            }

            public String getLotteryCode() {
                return lotteryCode;
            }

            public void setLotteryCode(String lotteryCode) {
                this.lotteryCode = lotteryCode;
            }

            public String getLotteryName() {
                return lotteryName;
            }

            public void setLotteryName(String lotteryName) {
                this.lotteryName = lotteryName;
            }

            public String getStationId() {
                return stationId;
            }

            public void setStationId(String stationId) {
                this.stationId = stationId;
            }
        }
    }

//    /**
//     * msg : 操作成功。
//     * code : R0028
//     * success : true
//     * source : {"msg":{"lotteryCode":"FFC","playList":["和值单双"],"lotteryName":"分分彩","resultList":[{"lotteryNum":"201912040979","playName":"和值单双","resultDate":"16:18:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040978","playName":"和值单双","resultDate":"16:17:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040977","playName":"和值单双","resultDate":"16:16:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040976","playName":"和值单双","resultDate":"16:15:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040975","playName":"和值单双","resultDate":"16:14:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040974","playName":"和值单双","resultDate":"16:13:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040973","playName":"和值单双","resultDate":"16:12:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040972","playName":"和值单双","resultDate":"16:11:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040971","playName":"和值单双","resultDate":"16:10:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040970","playName":"和值单双","resultDate":"16:09:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040969","playName":"和值单双","resultDate":"16:08:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040968","playName":"和值单双","resultDate":"16:07:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040967","playName":"和值单双","resultDate":"16:06:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040966","playName":"和值单双","resultDate":"16:05:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040965","playName":"和值单双","resultDate":"16:04:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040964","playName":"和值单双","resultDate":"16:03:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040963","playName":"和值单双","resultDate":"16:02:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040962","playName":"和值单双","resultDate":"16:01:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040961","playName":"和值单双","resultDate":"16:00:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040960","playName":"和值单双","resultDate":"15:59:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040959","playName":"和值单双","resultDate":"15:58:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040958","playName":"和值单双","resultDate":"15:57:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040957","playName":"和值单双","resultDate":"15:56:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040956","playName":"和值单双","resultDate":"15:55:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040955","playName":"和值单双","resultDate":"15:54:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040954","playName":"和值单双","resultDate":"15:53:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040953","playName":"和值单双","resultDate":"15:52:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040952","playName":"和值单双","resultDate":"15:51:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040951","playName":"和值单双","resultDate":"15:50:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040950","playName":"和值单双","resultDate":"15:49:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040949","playName":"和值单双","resultDate":"15:48:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040948","playName":"和值单双","resultDate":"15:47:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040947","playName":"和值单双","resultDate":"15:46:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040946","playName":"和值单双","resultDate":"15:45:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040945","playName":"和值单双","resultDate":"15:44:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040944","playName":"和值单双","resultDate":"15:43:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040943","playName":"和值单双","resultDate":"15:42:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040942","playName":"和值单双","resultDate":"15:41:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040941","playName":"和值单双","resultDate":"15:40:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040940","playName":"和值单双","resultDate":"15:39:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"}],"lotteryList":[{"source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"}],"option":"1"},"code":"R0028","success":true,"status":"b1"}
//     * status : b1
//     */
//
//    private String msg;
//    private String code;
//    private boolean success;
//    private SourceBean source;
//    private String status;
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public SourceBean getSource() {
//        return source;
//    }
//
//    public void setSource(SourceBean source) {
//        this.source = source;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public static class SourceBean {
//        /**
//         * msg : {"lotteryCode":"FFC","playList":["和值单双"],"lotteryName":"分分彩","resultList":[{"lotteryNum":"201912040979","playName":"和值单双","resultDate":"16:18:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3","lotteryResult":"双"},{"lotteryResult":"双","lotteryNum":"201912040978","playName":"和值单双","resultDate":"16:17:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040977","playName":"和值单双","resultDate":"16:16:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040976","playName":"和值单双","resultDate":"16:15:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040975","playName":"和值单双","resultDate":"16:14:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040974","playName":"和值单双","resultDate":"16:13:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040973","playName":"和值单双","resultDate":"16:12:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040972","playName":"和值单双","resultDate":"16:11:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040971","playName":"和值单双","resultDate":"16:10:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040970","playName":"和值单双","resultDate":"16:09:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040969","playName":"和值单双","resultDate":"16:08:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040968","playName":"和值单双","resultDate":"16:07:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040967","playName":"和值单双","resultDate":"16:06:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040966","playName":"和值单双","resultDate":"16:05:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040965","playName":"和值单双","resultDate":"16:04:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040964","playName":"和值单双","resultDate":"16:03:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040963","playName":"和值单双","resultDate":"16:02:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040962","playName":"和值单双","resultDate":"16:01:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040961","playName":"和值单双","resultDate":"16:00:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040960","playName":"和值单双","resultDate":"15:59:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040959","playName":"和值单双","resultDate":"15:58:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040958","playName":"和值单双","resultDate":"15:57:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040957","playName":"和值单双","resultDate":"15:56:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040956","playName":"和值单双","resultDate":"15:55:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040955","playName":"和值单双","resultDate":"15:54:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040954","playName":"和值单双","resultDate":"15:53:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040953","playName":"和值单双","resultDate":"15:52:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040952","playName":"和值单双","resultDate":"15:51:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040951","playName":"和值单双","resultDate":"15:50:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040950","playName":"和值单双","resultDate":"15:49:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040949","playName":"和值单双","resultDate":"15:48:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040948","playName":"和值单双","resultDate":"15:47:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040947","playName":"和值单双","resultDate":"15:46:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040946","playName":"和值单双","resultDate":"15:45:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040945","playName":"和值单双","resultDate":"15:44:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040944","playName":"和值单双","resultDate":"15:43:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040943","playName":"和值单双","resultDate":"15:42:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040942","playName":"和值单双","resultDate":"15:41:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040941","playName":"和值单双","resultDate":"15:40:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040940","playName":"和值单双","resultDate":"15:39:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"}],"lotteryList":[{"source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"}],"option":"1"}
//         * code : R0028
//         * success : true
//         * status : b1
//         */
//
//        private MsgBean msg;
//        private String code;
//        private boolean success;
//        private String status;
//
//        public MsgBean getMsg() {
//            return msg;
//        }
//
//        public void setMsg(MsgBean msg) {
//            this.msg = msg;
//        }
//
//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//
//        public boolean isSuccess() {
//            return success;
//        }
//
//        public void setSuccess(boolean success) {
//            this.success = success;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//
//        public void setStatus(String status) {
//            this.status = status;
//        }
//
//        public static class MsgBean {
//            /**
//             * lotteryCode : FFC
//             * playList : ["和值单双"]
//             * lotteryName : 分分彩
//             * resultList : [{"lotteryNum":"201912040979","playName":"和值单双","resultDate":"16:18:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040978","playName":"和值单双","resultDate":"16:17:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040977","playName":"和值单双","resultDate":"16:16:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040976","playName":"和值单双","resultDate":"16:15:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040975","playName":"和值单双","resultDate":"16:14:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040974","playName":"和值单双","resultDate":"16:13:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040973","playName":"和值单双","resultDate":"16:12:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040972","playName":"和值单双","resultDate":"16:11:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040971","playName":"和值单双","resultDate":"16:10:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040970","playName":"和值单双","resultDate":"16:09:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040969","playName":"和值单双","resultDate":"16:08:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040968","playName":"和值单双","resultDate":"16:07:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040967","playName":"和值单双","resultDate":"16:06:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040966","playName":"和值单双","resultDate":"16:05:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040965","playName":"和值单双","resultDate":"16:04:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040964","playName":"和值单双","resultDate":"16:03:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040963","playName":"和值单双","resultDate":"16:02:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040962","playName":"和值单双","resultDate":"16:01:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040961","playName":"和值单双","resultDate":"16:00:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040960","playName":"和值单双","resultDate":"15:59:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040959","playName":"和值单双","resultDate":"15:58:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040958","playName":"和值单双","resultDate":"15:57:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040957","playName":"和值单双","resultDate":"15:56:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040956","playName":"和值单双","resultDate":"15:55:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040955","playName":"和值单双","resultDate":"15:54:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040954","playName":"和值单双","resultDate":"15:53:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040953","playName":"和值单双","resultDate":"15:52:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040952","playName":"和值单双","resultDate":"15:51:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040951","playName":"和值单双","resultDate":"15:50:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040950","playName":"和值单双","resultDate":"15:49:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040949","playName":"和值单双","resultDate":"15:48:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040948","playName":"和值单双","resultDate":"15:47:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040947","playName":"和值单双","resultDate":"15:46:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040946","playName":"和值单双","resultDate":"15:45:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040945","playName":"和值单双","resultDate":"15:44:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040944","playName":"和值单双","resultDate":"15:43:00","forecast":"双","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"双","lotteryNum":"201912040943","playName":"和值单双","resultDate":"15:42:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040942","playName":"和值单双","resultDate":"15:41:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040941","playName":"和值单双","resultDate":"15:40:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"},{"lotteryResult":"单","lotteryNum":"201912040940","playName":"和值单双","resultDate":"15:39:00","forecast":"单","source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"}]
//             * lotteryList : [{"source":0,"lotteryCode":"FFC","lotteryName":"分分彩","stationId":"yjtest1_3"}]
//             * option : 1
//             */
//
//            private String lotteryCode;
//            private String lotteryName;
//            private String option;
//            private List<String> playList;
//            private List<ResultListBean> resultList;
//            private List<LotteryListBean> lotteryList;
//
//            public String getLotteryCode() {
//                return lotteryCode;
//            }
//
//            public void setLotteryCode(String lotteryCode) {
//                this.lotteryCode = lotteryCode;
//            }
//
//            public String getLotteryName() {
//                return lotteryName;
//            }
//
//            public void setLotteryName(String lotteryName) {
//                this.lotteryName = lotteryName;
//            }
//
//            public String getOption() {
//                return option;
//            }
//
//            public void setOption(String option) {
//                this.option = option;
//            }
//
//            public List<String> getPlayList() {
//                return playList;
//            }
//
//            public void setPlayList(List<String> playList) {
//                this.playList = playList;
//            }
//
//            public List<ResultListBean> getResultList() {
//                return resultList;
//            }
//
//            public void setResultList(List<ResultListBean> resultList) {
//                this.resultList = resultList;
//            }
//
//            public List<LotteryListBean> getLotteryList() {
//                return lotteryList;
//            }
//
//            public void setLotteryList(List<LotteryListBean> lotteryList) {
//                this.lotteryList = lotteryList;
//            }
//
//            public static class ResultListBean {
//                /**
//                 * lotteryNum : 201912040979
//                 * playName : 和值单双
//                 * resultDate : 16:18:00
//                 * forecast : 双
//                 * source : 0
//                 * lotteryCode : FFC
//                 * lotteryName : 分分彩
//                 * stationId : yjtest1_3
//                 * lotteryResult : 双
//                 */
//
//                private String lotteryNum;
//                private String playName;
//                private String resultDate;
//                private String forecast;
//                private int source;
//                private String lotteryCode;
//                private String lotteryName;
//                private String stationId;
//                private String lotteryResult;
//
//                public String getLotteryNum() {
//                    return lotteryNum;
//                }
//
//                public void setLotteryNum(String lotteryNum) {
//                    this.lotteryNum = lotteryNum;
//                }
//
//                public String getPlayName() {
//                    return playName;
//                }
//
//                public void setPlayName(String playName) {
//                    this.playName = playName;
//                }
//
//                public String getResultDate() {
//                    return resultDate;
//                }
//
//                public void setResultDate(String resultDate) {
//                    this.resultDate = resultDate;
//                }
//
//                public String getForecast() {
//                    return forecast;
//                }
//
//                public void setForecast(String forecast) {
//                    this.forecast = forecast;
//                }
//
//                public int getSource() {
//                    return source;
//                }
//
//                public void setSource(int source) {
//                    this.source = source;
//                }
//
//                public String getLotteryCode() {
//                    return lotteryCode;
//                }
//
//                public void setLotteryCode(String lotteryCode) {
//                    this.lotteryCode = lotteryCode;
//                }
//
//                public String getLotteryName() {
//                    return lotteryName;
//                }
//
//                public void setLotteryName(String lotteryName) {
//                    this.lotteryName = lotteryName;
//                }
//
//                public String getStationId() {
//                    return stationId;
//                }
//
//                public void setStationId(String stationId) {
//                    this.stationId = stationId;
//                }
//
//                public String getLotteryResult() {
//                    return lotteryResult;
//                }
//
//                public void setLotteryResult(String lotteryResult) {
//                    this.lotteryResult = lotteryResult;
//                }
//            }
//
//            public static class LotteryListBean {
//                /**
//                 * source : 0
//                 * lotteryCode : FFC
//                 * lotteryName : 分分彩
//                 * stationId : yjtest1_3
//                 */
//                private boolean isClick = false;
//                private int source;
//                private String lotteryCode;
//                private String lotteryName;
//                private String stationId;
//
//                public boolean isClick() {
//                    return isClick;
//                }
//
//                public void setClick(boolean click) {
//                    isClick = click;
//                }
//
//                public int getSource() {
//                    return source;
//                }
//
//                public void setSource(int source) {
//                    this.source = source;
//                }
//
//                public String getLotteryCode() {
//                    return lotteryCode;
//                }
//
//                public void setLotteryCode(String lotteryCode) {
//                    this.lotteryCode = lotteryCode;
//                }
//
//                public String getLotteryName() {
//                    return lotteryName;
//                }
//
//                public void setLotteryName(String lotteryName) {
//                    this.lotteryName = lotteryName;
//                }
//
//                public String getStationId() {
//                    return stationId;
//                }
//
//                public void setStationId(String stationId) {
//                    this.stationId = stationId;
//                }
//            }
//        }
//    }


}
