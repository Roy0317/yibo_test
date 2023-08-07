package com.example.anuo.immodule.bean;

public class PrivateMsgBody {


    /**
     * data : {"msgType":1,"code":"A0501","fromUser":{"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"},"accountType":2,"inputUserId":"906be71e97d540efad5a345fb950da0f","msgUUID":"98e643f8-67a1-4a71-88d0-c162c5b6495e","userMessageId":"67676a1d68304befa77dbb88aaa8a112","type":"1","userId":"1a192c287321480d82f0e5b339d8f7a9","roomId":"029fa0bfd9e149e1a17c15ce7bea5f22_yunt0Chat_2","record":"你哦要4和","outputUserId":"1a192c287321480d82f0e5b339d8f7a9","userType":4,"time":"2020-02-10 22:50:47"}
     * msgUUID : bb9e682e1e334a93
     * bets : {"code":"R7051","msgType":1,"msgUUID":"98e643f8-67a1-4a71-88d0-c162c5b6495e","passiveUserId":"1a192c287321480d82f0e5b339d8f7a9","record":"你哦要4和","roomId":"029fa0bfd9e149e1a17c15ce7bea5f22","source":"app","stationId":"yunt0Chat_2","type":1,"userId":"906be71e97d540efad5a345fb950da0f"}
     */

    private DataBean data;
    private String msgUUID;
    private String bets;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsgUUID() {
        return msgUUID;
    }

    public void setMsgUUID(String msgUUID) {
        this.msgUUID = msgUUID;
    }

    public String getBets() {
        return bets;
    }

    public void setBets(String bets) {
        this.bets = bets;
    }

    public static class DataBean {
        /**
         * msgType : 1
         * code : A0501
         * fromUser : {"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"}
         * accountType : 2
         * inputUserId : 906be71e97d540efad5a345fb950da0f
         * msgUUID : 98e643f8-67a1-4a71-88d0-c162c5b6495e
         * userMessageId : 67676a1d68304befa77dbb88aaa8a112
         * type : 1
         * userId : 1a192c287321480d82f0e5b339d8f7a9
         * roomId : 029fa0bfd9e149e1a17c15ce7bea5f22_yunt0Chat_2
         * record : 你哦要4和
         * outputUserId : 1a192c287321480d82f0e5b339d8f7a9
         * userType : 4
         * time : 2020-02-10 22:50:47
         */

        private int msgType;
        private String code;
        private FromUserBean fromUser;
        private int accountType;
        private String inputUserId;
        private String msgUUID;
        private String userMessageId;
        private String type;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
             * nickName : 发哥
             * levelName : 荣耀会员
             * userType : 4
             * avatar : https://yj8.me/img/h3eM/iozdKN3wr.jpg
             * id : 906be71e97d540efad5a345fb950da0f
             * account : testray
             * levelIcon : https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg
             */

            private String nickName;
            private String levelName;
            private int userType;
            private String avatar;
            private String id;
            private String account;
            private String levelIcon;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

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
}
