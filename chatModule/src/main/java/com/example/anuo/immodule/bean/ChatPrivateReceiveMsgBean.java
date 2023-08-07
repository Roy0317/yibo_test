package com.example.anuo.immodule.bean;

public class ChatPrivateReceiveMsgBean {

    /**
     * msgType : 1
     * code : A0501
     * fromUser : {"levelName":"渣渣会员","userType":1,"avatar":"https://yj8.me/img/h3eM/i5niH0naV.jpg","id":"1a192c287321480d82f0e5b339d8f7a9","account":"testray1","levelIcon":"https://gss0.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/d788d43f8794a4c21425894e03f41bd5ad6e3954.jpg"}
     * accountType : 2
     * inputUserId : 1a192c287321480d82f0e5b339d8f7a9
     * msgUUID : 897531fa-b39a-43bb-b332-7b47919c0537
     * userMessageId : 19408bdb1f264ffabc78251c578f926c
     * userId : 906be71e97d540efad5a345fb950da0f
     * roomId : 029fa0bfd9e149e1a17c15ce7bea5f22_yunt0Chat_2
     * record : 3
     * outputUserId : 906be71e97d540efad5a345fb950da0f
     * userType : 1
     * time : 2020-02-04 17:21:09
     */

    private int msgType;
    private String code;
    private FromUserBean fromUser;
    private int accountType;
    private String inputUserId;
    private String msgUUID;
    private String userMessageId;
    private String userId;
    private String roomId;
    private String record;
    private String outputUserId;
    private int userType;
    private String time;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public FromUserBean getFromUser() {
        return fromUser;
    }

    public void setFromUser(FromUserBean fromUser) {
        this.fromUser = fromUser;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }

    public String getMsgUUID() {
        return msgUUID;
    }

    public void setMsgUUID(String msgUUID) {
        this.msgUUID = msgUUID;
    }

    public String getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(String userMessageId) {
        this.userMessageId = userMessageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getOutputUserId() {
        return outputUserId;
    }

    public void setOutputUserId(String outputUserId) {
        this.outputUserId = outputUserId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class FromUserBean {
        /**
         * levelName : 渣渣会员
         * userType : 1
         * avatar : https://yj8.me/img/h3eM/i5niH0naV.jpg
         * id : 1a192c287321480d82f0e5b339d8f7a9
         * account : testray1
         * levelIcon : https://gss0.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/d788d43f8794a4c21425894e03f41bd5ad6e3954.jpg
         */

        private String levelName;
        private int userType;
        private String avatar;
        private String id;
        private String account;
        private String levelIcon;

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getLevelIcon() {
            return levelIcon;
        }

        public void setLevelIcon(String levelIcon) {
            this.levelIcon = levelIcon;
        }
    }
}
