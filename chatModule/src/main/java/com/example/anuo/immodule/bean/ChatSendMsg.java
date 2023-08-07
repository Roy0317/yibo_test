package com.example.anuo.immodule.bean;

import com.google.gson.annotations.SerializedName;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :02/07/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class ChatSendMsg {

    /**
     * code : R7001
     * source : app
     * userId : 875e8b1815a0432c8f3be3c815a0fc32
     * content : {"msgType":1,"fromUser":{"avatarCode":0,"levelName":"默认等级","id":"875e8b1815a0432c8f3be3c815a0fc32","account":"test222","levelIcon":""},"record":"q","userType":1,"time":"2019-07-03 15:05:14","userMessageId":"1907ae2b468ebfc343778d814a7b2739c53e","userId":"875e8b1815a0432c8f3be3c815a0fc32","roomId":"76adbc043f4e4aea8c63c62a0638df68","stationId":"helloChat_1"}
     * roomId : 76adbc043f4e4aea8c63c62a0638df68
     * stationId : helloChat_1
     */
    //消息类型---指定client要发的是什么类型的消息，必须指定
    private int msgType;
    //接口编号；如"R7001"
    private String code;
    //来源 "app"；是否是原生app的请求，必传
    private String source;
    //用户id(UUID)
    private String userId;
    //消息内容json格式串(用在客户端发socket消息时真正传输的消息内容json串)
    private String content;
    //房间ID
    private String roomId;
    //安全密钥
    private String securityKey;
    //该消息用户对应的站点编号，必传
    private String stationId;
    /**
     * msgResult : {"fromUser":{"account":"test222","avatarCode":0,"id":"875e8b1815a0432c8f3be3c815a0fc32","levelIcon":""},"msgType":"1","record":"123","roomId":"76adbc043f4e4aea8c63c62a0638df68","stationId":"helloChat_1","time":"2019-07-03 15:57:09","userId":"875e8b1815a0432c8f3be3c815a0fc32","userMessageId":"1907caa78eef7fd94710881d0d928374dada","userType":1}
     */
    //服务端推送给房间或个人的消息内容Bean
    private MsgResultBean msgResult;
    /**
     * msgResult : {"userEntity":{"account":"test1001","accountType":1,"createTime":1562068747861,"id":"8d0380dc68cf4b51bbbf7759543203dd","levelName":"默认等级","stationId":"helloChat_1","updateTime":1562068747861,"userClust":"helloChat","userCode":"6","userDetailEntity":{"avatarCode":"0","id":"5148bcdd7468417d8d8f9471ca349dcb","userId":"8d0380dc68cf4b51bbbf7759543203dd"},"userType":1},"userMsgEntity":{"createTime":1562240880257,"enable":1,"id":"1907591912e3a7dc4d1f9084793152f70426","roomId":"76adbc043f4e4aea8c63c62a0638df68","stationId":"helloChat_1","tableName":"chat_user_msg_2019_07","textRecord":"111","type":1,"userId":"8d0380dc68cf4b51bbbf7759543203dd","userType":1}}
     * msgType : 1
     */
    //服务端推送给房间的消息内容串,base64编码
    private String msgStr;
    //房间名
    private String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public MsgResultBean getMsgResult() {
        return msgResult;
    }

    public void setMsgResult(MsgResultBean msgResult) {
        this.msgResult = msgResult;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getMsgStr() {
        return msgStr;
    }

    public void setMsgStr(String msgStr) {
        this.msgStr = msgStr;
    }

    public static class MsgResultBean {
        /**
         * userEntity : {"account":"test1001","accountType":1,"createTime":1562068747861,"id":"8d0380dc68cf4b51bbbf7759543203dd","levelName":"默认等级","stationId":"helloChat_1","updateTime":1562068747861,"userClust":"helloChat","userCode":"6","userDetailEntity":{"avatarCode":"0","id":"5148bcdd7468417d8d8f9471ca349dcb","userId":"8d0380dc68cf4b51bbbf7759543203dd"},"userType":1}
         * userMsgEntity : {"createTime":1562240880257,"enable":1,"id":"1907591912e3a7dc4d1f9084793152f70426","roomId":"76adbc043f4e4aea8c63c62a0638df68","stationId":"helloChat_1","tableName":"chat_user_msg_2019_07","textRecord":"111","type":1,"userId":"8d0380dc68cf4b51bbbf7759543203dd","userType":1}
         */

        private UserEntityBean userEntity;
        private UserMsgEntityBean userMsgEntity;

        public UserEntityBean getUserEntity() {
            return userEntity;
        }

        public void setUserEntity(UserEntityBean userEntity) {
            this.userEntity = userEntity;
        }

        public UserMsgEntityBean getUserMsgEntity() {
            return userMsgEntity;
        }

        public void setUserMsgEntity(UserMsgEntityBean userMsgEntity) {
            this.userMsgEntity = userMsgEntity;
        }

        public static class UserEntityBean {
            /**
             * account : test1001
             * accountType : 1
             * createTime : 1562068747861
             * id : 8d0380dc68cf4b51bbbf7759543203dd
             * levelName : 默认等级
             * stationId : helloChat_1
             * updateTime : 1562068747861
             * userClust : helloChat
             * userCode : 6
             * userDetailEntity : {"avatarCode":"0","id":"5148bcdd7468417d8d8f9471ca349dcb","userId":"8d0380dc68cf4b51bbbf7759543203dd"}
             * userType : 1
             */

            private String account;
            private int accountType;
            private long createTime;
            private String id;
            private String levelName;
            @SerializedName("stationId")
            private String stationIdX;
            private long updateTime;
            private String userClust;
            private String userCode;
            private UserDetailEntityBean userDetailEntity;
            private int userType;
            private String nickName;
            private String levelIcon;
            private String levelId;
            private int planUser;
            @SerializedName("roomId")
            private String roomIdX;
            private boolean privatePermission;

            public boolean isPrivatePermission() {
                return privatePermission;
            }

            public void setPrivatePermission(boolean privatePermission) {
                this.privatePermission = privatePermission;
            }


            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public int getAccountType() {
                return accountType;
            }

            public void setAccountType(int accountType) {
                this.accountType = accountType;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLevelName() {
                return levelName;
            }

            public void setLevelName(String levelName) {
                this.levelName = levelName;
            }

            public String getStationIdX() {
                return stationIdX;
            }

            public void setStationIdX(String stationIdX) {
                this.stationIdX = stationIdX;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public String getUserClust() {
                return userClust;
            }

            public void setUserClust(String userClust) {
                this.userClust = userClust;
            }

            public String getUserCode() {
                return userCode;
            }

            public void setUserCode(String userCode) {
                this.userCode = userCode;
            }

            public UserDetailEntityBean getUserDetailEntity() {
                return userDetailEntity;
            }

            public void setUserDetailEntity(UserDetailEntityBean userDetailEntity) {
                this.userDetailEntity = userDetailEntity;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getLevelIcon() {
                return levelIcon;
            }

            public void setLevelIcon(String levelIcon) {
                this.levelIcon = levelIcon;
            }

            public String getLevelId() {
                return levelId;
            }

            public void setLevelId(String levelId) {
                this.levelId = levelId;
            }

            public int getPlanUser() {
                return planUser;
            }

            public void setPlanUser(int planUser) {
                this.planUser = planUser;
            }

            public String getRoomIdX() {
                return roomIdX;
            }

            public void setRoomIdX(String roomIdX) {
                this.roomIdX = roomIdX;
            }

            public static class UserDetailEntityBean {
                /**
                 * avatarCode : 0
                 * id : 5148bcdd7468417d8d8f9471ca349dcb
                 * userId : 8d0380dc68cf4b51bbbf7759543203dd
                 */

                private String avatarCode;
                private String id;
                @SerializedName("userId")
                private String userIdX;
                private int invitation;
                private String userIp;

                public String getAvatarCode() {
                    return avatarCode;
                }

                public void setAvatarCode(String avatarCode) {
                    this.avatarCode = avatarCode;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getUserIdX() {
                    return userIdX;
                }

                public void setUserIdX(String userIdX) {
                    this.userIdX = userIdX;
                }

                public int getInvitation() {
                    return invitation;
                }

                public void setInvitation(int invitation) {
                    this.invitation = invitation;
                }

                public String getUserIp() {
                    return userIp;
                }

                public void setUserIp(String userIp) {
                    this.userIp = userIp;
                }
            }
        }

        public static class UserMsgEntityBean {
            /**
             * createTime : 1562240880257
             * enable : 1
             * id : 1907591912e3a7dc4d1f9084793152f70426
             * roomId : 76adbc043f4e4aea8c63c62a0638df68
             * stationId : helloChat_1
             * tableName : chat_user_msg_2019_07
             * textRecord : 111
             * type : 1
             * userId : 8d0380dc68cf4b51bbbf7759543203dd
             * userType : 1
             */

            private long createTime;
            private int enable;
            private String id;
            @SerializedName("roomId")
            private String roomIdX;
            @SerializedName("stationId")
            private String stationIdX;
            private String tableName;
            private String textRecord;
            private int type;
            @SerializedName("userId")
            private String userIdX;
            private int userType;

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getEnable() {
                return enable;
            }

            public void setEnable(int enable) {
                this.enable = enable;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRoomIdX() {
                return roomIdX;
            }

            public void setRoomIdX(String roomIdX) {
                this.roomIdX = roomIdX;
            }

            public String getStationIdX() {
                return stationIdX;
            }

            public void setStationIdX(String stationIdX) {
                this.stationIdX = stationIdX;
            }

            public String getTableName() {
                return tableName;
            }

            public void setTableName(String tableName) {
                this.tableName = tableName;
            }

            public String getTextRecord() {
                return textRecord;
            }

            public void setTextRecord(String textRecord) {
                this.textRecord = textRecord;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUserIdX() {
                return userIdX;
            }

            public void setUserIdX(String userIdX) {
                this.userIdX = userIdX;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }
        }
    }
}
