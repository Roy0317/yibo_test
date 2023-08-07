package com.example.anuo.immodule.bean;

public class ChatModifyPersonDataBean {

    /**
     * msg : 操作成功。
     * code : R7034
     * success : true
     * source : {"nickName":"","msgUUID":"0517d42a80f14189bb77cb3e63a3ad7a978307200.0"}
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
         * nickName :
         * msgUUID : 0517d42a80f14189bb77cb3e63a3ad7a978307200.0
         */

        private String nickName;
        private String msgUUID;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getMsgUUID() {
            return msgUUID;
        }

        public void setMsgUUID(String msgUUID) {
            this.msgUUID = msgUUID;
        }
    }
}
