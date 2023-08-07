package com.example.anuo.immodule.bean;

import com.example.anuo.immodule.bean.base.BaseBean;

import java.util.List;

public class ChatPrivateConversationBean extends BaseBean {


    /**
     * source : {"passiveUserName":"sky","createTime":"2019-12-02T06:36:00.877+0000","id":"2661586ce56b40bda623b2fa5f47a0ae","type":"1","userId":"a66baf6672aa467f908e2cb7f566ba79","roomId":"61aeae43d56b4c208425c12eadb39d46"}
     */

    private SourceBean source;

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public static class SourceBean {
        /**
         * passiveUserName : sky
         * createTime : 2019-12-02T06:36:00.877+0000
         * id : 2661586ce56b40bda623b2fa5f47a0ae
         * type : 1
         * userId : a66baf6672aa467f908e2cb7f566ba79
         * roomId : 61aeae43d56b4c208425c12eadb39d46
         */

        private int total ;
        private String passiveUserName;
        private String createTime;
        private String id;
        private String type;
        private String userId;
        private String roomId;

        //私聊列表
        private List<ChatUserListBean.ChatUserBean> roomList;


        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getPassiveUserName() {
            return passiveUserName;
        }

        public void setPassiveUserName(String passiveUserName) {
            this.passiveUserName = passiveUserName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public List<ChatUserListBean.ChatUserBean> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<ChatUserListBean.ChatUserBean> roomList) {
            this.roomList = roomList;
        }

//        public static class RoomListBean {
//            /**
//             * lastRecordTime :
//             * lastRecordType :
//             * fromUser : {"nickName":"sky","levelName":"帝尊会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"2661586ce56b40bda623b2fa5f47a0ae","account":"chat01","levelIcon":"https://img.rz520.com/uploadfile/2017/0928/20170928044331462_1.jpg"}
//             * lastRecord :
//             * isRead : 0
//             * roomId : 61aeae43d56b4c208425c12eadb39d46
//             * remarks :
//             */
//
//            private String lastRecordTime;
//            private String lastRecordType;
//            private FromUserBean fromUser;
//            private String lastRecord;
//            private int isRead;
//            @SerializedName("roomId")
//            private String roomIdX;
//            private String remarks;
//
//            public String getLastRecordTime() {
//                return lastRecordTime;
//            }
//
//            public void setLastRecordTime(String lastRecordTime) {
//                this.lastRecordTime = lastRecordTime;
//            }
//
//            public String getLastRecordType() {
//                return lastRecordType;
//            }
//
//            public void setLastRecordType(String lastRecordType) {
//                this.lastRecordType = lastRecordType;
//            }
//
//            public FromUserBean getFromUser() {
//                return fromUser;
//            }
//
//            public void setFromUser(FromUserBean fromUser) {
//                this.fromUser = fromUser;
//            }
//
//            public String getLastRecord() {
//                return lastRecord;
//            }
//
//            public void setLastRecord(String lastRecord) {
//                this.lastRecord = lastRecord;
//            }
//
//            public int getIsRead() {
//                return isRead;
//            }
//
//            public void setIsRead(int isRead) {
//                this.isRead = isRead;
//            }
//
//            public String getRoomIdX() {
//                return roomIdX;
//            }
//
//            public void setRoomIdX(String roomIdX) {
//                this.roomIdX = roomIdX;
//            }
//
//            public String getRemarks() {
//                return remarks;
//            }
//
//            public void setRemarks(String remarks) {
//                this.remarks = remarks;
//            }
//
//            public static class FromUserBean {
//                /**
//                 * nickName : sky
//                 * levelName : 帝尊会员
//                 * userType : 4
//                 * avatar : https://yj8.me/img/h3eM/iozdKN3wr.jpg
//                 * id : 2661586ce56b40bda623b2fa5f47a0ae
//                 * account : chat01
//                 * levelIcon : https://img.rz520.com/uploadfile/2017/0928/20170928044331462_1.jpg
//                 */
//
//                private String nickName;
//                private String levelName;
//                private int userType;
//                private String avatar;
//                @SerializedName("id")
//                private String idX;
//                private String account;
//                private String levelIcon;
//
//                public String getNickName() {
//                    return nickName;
//                }
//
//                public void setNickName(String nickName) {
//                    this.nickName = nickName;
//                }
//
//                public String getLevelName() {
//                    return levelName;
//                }
//
//                public void setLevelName(String levelName) {
//                    this.levelName = levelName;
//                }
//
//                public int getUserType() {
//                    return userType;
//                }
//
//                public void setUserType(int userType) {
//                    this.userType = userType;
//                }
//
//                public String getAvatar() {
//                    return avatar;
//                }
//
//                public void setAvatar(String avatar) {
//                    this.avatar = avatar;
//                }
//
//                public String getIdX() {
//                    return idX;
//                }
//
//                public void setIdX(String idX) {
//                    this.idX = idX;
//                }
//
//                public String getAccount() {
//                    return account;
//                }
//
//                public void setAccount(String account) {
//                    this.account = account;
//                }
//
//                public String getLevelIcon() {
//                    return levelIcon;
//                }
//
//                public void setLevelIcon(String levelIcon) {
//                    this.levelIcon = levelIcon;
//                }
//            }
//        }
    }
}
