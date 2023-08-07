package com.example.anuo.immodule.bean;

import java.util.List;

public class ChatPrivateMessageBean {


    /**
     * msg : 操作成功。
     * code : R0501
     * success : true
     * source : {"data":[{"date":"2020-02-03","msgType":1,"fromUser":{"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"},"stopTalkType":0,"isPlanUser":0,"accountType":2,"privatePermission":true,"userName":"发哥","nativeContent":"","userMessageId":"7a938e644d6548abbe7fb91df0823e79","createTime":"2020-02-03 19:43:05","record":"1","userType":4,"nativeAccount":"***tray","nativeNickName":"发哥"},{"msgType":1,"fromUser":{"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"},"stopTalkType":0,"isPlanUser":0,"accountType":2,"privatePermission":true,"userName":"发哥","nativeContent":"","userMessageId":"53931de2a1a446778666b06f573c99c1","createTime":"2020-02-03 19:43:07","record":"2","userType":4,"nativeAccount":"***tray","nativeNickName":"发哥"},{"msgType":5,"fromUser":{"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"},"stopTalkType":0,"isPlanUser":0,"accountType":2,"privatePermission":true,"userName":"发哥","nativeContent":"{\"betInfos\":[{\"ago\":0,\"buyMoney\":10.0,\"buyZhuShu\":1,\"haoMa\":\"大\",\"icon\":\"\",\"lotCode\":\"FKSC\",\"lotIcon\":\"\",\"lotName\":\"疯狂赛车\",\"lottery_amount\":\"10.0\",\"lottery_content\":\"大\",\"lottery_play\":\"整合[冠，亚和]\",\"lottery_qihao\":\"202002030947\",\"lottery_type\":\"疯狂赛车\",\"lottery_zhushu\":\"1\",\"model\":1,\"orderId\":\"C20020300004\",\"playName\":\"整合[冠，亚和]\",\"qiHao\":\"202002030947\",\"showZhuShu\":false,\"version\":\"2\"}],\"code\":\"R0501\",\"msgUUID\":\"a57cc4da2e8e4bea\",\"multiBet\":1,\"orders\":[{\"betMoney\":\"10.0\",\"orderId\":\"C20020300004\"}],\"roomId\":\"612dd32b83324109ba0e31810ce24c61\",\"source\":\"front\",\"stationId\":\"yunt0Chat_2\",\"userId\":\"906be71e97d540efad5a345fb950da0f\",\"userType\":0}","userMessageId":"a4c762708508452bac19ce4ff52a3136","createTime":"2020-02-03 19:43:28","record":"[{\"playName\":\"整合[冠，亚和]\",\"qiHao\":\"202002030947\",\"haoMa\":\"大\",\"buyMoney\":10,\"orderId\":\"C20020300004\",\"lotName\":\"疯狂赛车\",\"buyZhuShu\":1,\"lotCode\":\"FKSC\",\"icon\":\"\",\"showZhuShu\":false,\"version\":2,\"model\":1}]","userType":4,"nativeAccount":"***tray","nativeNickName":"发哥"}],"type":"2","roomId":"612dd32b83324109ba0e31810ce24c61"}
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
         * data : [{"date":"2020-02-03","msgType":1,"fromUser":{"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"},"stopTalkType":0,"isPlanUser":0,"accountType":2,"privatePermission":true,"userName":"发哥","nativeContent":"","userMessageId":"7a938e644d6548abbe7fb91df0823e79","createTime":"2020-02-03 19:43:05","record":"1","userType":4,"nativeAccount":"***tray","nativeNickName":"发哥"},{"msgType":1,"fromUser":{"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"},"stopTalkType":0,"isPlanUser":0,"accountType":2,"privatePermission":true,"userName":"发哥","nativeContent":"","userMessageId":"53931de2a1a446778666b06f573c99c1","createTime":"2020-02-03 19:43:07","record":"2","userType":4,"nativeAccount":"***tray","nativeNickName":"发哥"},{"msgType":5,"fromUser":{"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"},"stopTalkType":0,"isPlanUser":0,"accountType":2,"privatePermission":true,"userName":"发哥","nativeContent":"{\"betInfos\":[{\"ago\":0,\"buyMoney\":10.0,\"buyZhuShu\":1,\"haoMa\":\"大\",\"icon\":\"\",\"lotCode\":\"FKSC\",\"lotIcon\":\"\",\"lotName\":\"疯狂赛车\",\"lottery_amount\":\"10.0\",\"lottery_content\":\"大\",\"lottery_play\":\"整合[冠，亚和]\",\"lottery_qihao\":\"202002030947\",\"lottery_type\":\"疯狂赛车\",\"lottery_zhushu\":\"1\",\"model\":1,\"orderId\":\"C20020300004\",\"playName\":\"整合[冠，亚和]\",\"qiHao\":\"202002030947\",\"showZhuShu\":false,\"version\":\"2\"}],\"code\":\"R0501\",\"msgUUID\":\"a57cc4da2e8e4bea\",\"multiBet\":1,\"orders\":[{\"betMoney\":\"10.0\",\"orderId\":\"C20020300004\"}],\"roomId\":\"612dd32b83324109ba0e31810ce24c61\",\"source\":\"front\",\"stationId\":\"yunt0Chat_2\",\"userId\":\"906be71e97d540efad5a345fb950da0f\",\"userType\":0}","userMessageId":"a4c762708508452bac19ce4ff52a3136","createTime":"2020-02-03 19:43:28","record":"[{\"playName\":\"整合[冠，亚和]\",\"qiHao\":\"202002030947\",\"haoMa\":\"大\",\"buyMoney\":10,\"orderId\":\"C20020300004\",\"lotName\":\"疯狂赛车\",\"buyZhuShu\":1,\"lotCode\":\"FKSC\",\"icon\":\"\",\"showZhuShu\":false,\"version\":2,\"model\":1}]","userType":4,"nativeAccount":"***tray","nativeNickName":"发哥"}]
         * type : 2
         * roomId : 612dd32b83324109ba0e31810ce24c61
         */

        private String type;
        private String roomId;
        private List<DataBean> data;
        private String userMessageId;

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

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * date : 2020-02-03
             * msgType : 1
             * fromUser : {"nickName":"发哥","levelName":"荣耀会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"906be71e97d540efad5a345fb950da0f","account":"testray","levelIcon":"https://m.360buyimg.com/pop/jfs/t23221/324/1769258572/105423/a108396/5b697822N1e4b5e24.jpg"}
             * stopTalkType : 0
             * isPlanUser : 0
             * accountType : 2
             * privatePermission : true
             * userName : 发哥
             * nativeContent :
             * userMessageId : 7a938e644d6548abbe7fb91df0823e79
             * createTime : 2020-02-03 19:43:05
             * record : 1
             * userType : 4
             * nativeAccount : ***tray
             * nativeNickName : 发哥
             */

            private String date;
            private int msgType;
            private FromUserBean fromUser;
            private int stopTalkType;
            private int isPlanUser;
            private int accountType;
            private boolean privatePermission;
            private String userName;
            private String nativeContent;
            private String userMessageId;
            private String createTime;
            private String record;
            private int userType;
            private String nativeAccount;
            private String nativeNickName;
            private String remark;
            private String payId;

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getPayId() {
                return payId;
            }

            public void setPayId(String payId) {
                this.payId = payId;
            }


            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getMsgType() {
                return msgType;
            }

            public void setMsgType(int msgType) {
                this.msgType = msgType;
            }

            public FromUserBean getFromUser() {
                return fromUser;
            }

            public void setFromUser(FromUserBean fromUser) {
                this.fromUser = fromUser;
            }

            public int getStopTalkType() {
                return stopTalkType;
            }

            public void setStopTalkType(int stopTalkType) {
                this.stopTalkType = stopTalkType;
            }

            public int getIsPlanUser() {
                return isPlanUser;
            }

            public void setIsPlanUser(int isPlanUser) {
                this.isPlanUser = isPlanUser;
            }

            public int getAccountType() {
                return accountType;
            }

            public void setAccountType(int accountType) {
                this.accountType = accountType;
            }

            public boolean isPrivatePermission() {
                return privatePermission;
            }

            public void setPrivatePermission(boolean privatePermission) {
                this.privatePermission = privatePermission;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getNativeContent() {
                return nativeContent;
            }

            public void setNativeContent(String nativeContent) {
                this.nativeContent = nativeContent;
            }

            public String getUserMessageId() {
                return userMessageId;
            }

            public void setUserMessageId(String userMessageId) {
                this.userMessageId = userMessageId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getRecord() {
                return record;
            }

            public void setRecord(String record) {
                this.record = record;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public String getNativeAccount() {
                return nativeAccount;
            }

            public void setNativeAccount(String nativeAccount) {
                this.nativeAccount = nativeAccount;
            }

            public String getNativeNickName() {
                return nativeNickName;
            }

            public void setNativeNickName(String nativeNickName) {
                this.nativeNickName = nativeNickName;
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
}
