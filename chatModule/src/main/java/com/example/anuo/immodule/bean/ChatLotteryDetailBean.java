package com.example.anuo.immodule.bean;

import java.util.List;

public class ChatLotteryDetailBean {


    /**
     * success : true
     * accessToken : 98a8b13f-c2e8-4245-994f-8aaa7e4f5cbd
     * content : [{"date":1569928500000,"haoMa":"4,2,2,5,5","lotCode":"WFC","qiHao":"20191001232"}]
     */

    private boolean success;
    private String accessToken;
    private List<ContentBean> content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * date : 1569928500000
         * haoMa : 4,2,2,5,5
         * lotCode : WFC
         * qiHao : 20191001232
         */

        private long date;
        private String haoMa;
        private String lotCode;
        private String qiHao;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getHaoMa() {
            return haoMa;
        }

        public void setHaoMa(String haoMa) {
            this.haoMa = haoMa;
        }

        public String getLotCode() {
            return lotCode;
        }

        public void setLotCode(String lotCode) {
            this.lotCode = lotCode;
        }

        public String getQiHao() {
            return qiHao;
        }

        public void setQiHao(String qiHao) {
            this.qiHao = qiHao;
        }
    }
}
