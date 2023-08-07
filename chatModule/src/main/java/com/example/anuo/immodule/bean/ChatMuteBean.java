package com.example.anuo.immodule.bean;

public class ChatMuteBean {

    /**
     * msg : 操作成功。
     * code : R0009
     * success : true
     * source : {"code":"A0009","speakingClose":1,"userId":"a7c567bb195847059474842894e4210a"}
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
         * code : A0009
         * speakingClose : 1
         * userId : a7c567bb195847059474842894e4210a
         */

        private String code;
        private int speakingClose;
        private String userId;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getSpeakingClose() {
            return speakingClose;
        }

        public void setSpeakingClose(int speakingClose) {
            this.speakingClose = speakingClose;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
