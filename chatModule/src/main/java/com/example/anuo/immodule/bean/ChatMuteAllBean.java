package com.example.anuo.immodule.bean;

public class ChatMuteAllBean {


    /**
     * msg : 操作成功。
     * code : R0029
     * success : true
     * source : {"msg":"当前房间已禁言","code":"A0029","roomId":"008f0dcf7e7e4eecbf17537eb5bcdc1d_null","isBanSpeak":"1"}
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
         * msg : 当前房间已禁言
         * code : A0029
         * roomId : 008f0dcf7e7e4eecbf17537eb5bcdc1d_null
         * isBanSpeak : 1
         */

        private String msg;
        private String code;
        private String roomId;
        private String isBanSpeak;

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

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getIsBanSpeak() {
            return isBanSpeak;
        }

        public void setIsBanSpeak(String isBanSpeak) {
            this.isBanSpeak = isBanSpeak;
        }
    }
}
