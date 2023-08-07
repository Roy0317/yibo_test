package com.example.anuo.immodule.bean;

public class ChatRecallBean {


    /**
     * msg : 操作成功。
     * code : R0102
     * success : true
     * source : {"code":"A0102","nickName":"发哥","msgId":"2001d51693882959434a9a79a0c180247970","userType":4,"roomId":"008f0dcf7e7e4eecbf17537eb5bcdc1d_yunt0Chat_2"}
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
         * code : A0102
         * nickName : 发哥
         * msgId : 2001d51693882959434a9a79a0c180247970
         * userType : 4
         * roomId : 008f0dcf7e7e4eecbf17537eb5bcdc1d_yunt0Chat_2
         */

        private String code;
        private String nickName;
        private String msgId;
        private int userType;
        private String roomId;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }
    }
}
