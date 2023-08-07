package com.example.anuo.immodule.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
 * Date  :06/12/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class ChatUserListBean {

    /**
     * msg : 操作成功。
     * code : R0010
     * success : true
     * source : [{"avatarUrl":"nullhttps://yj8.me/img/h3eM/i5WRcWKRy.jpg","level":"渣渣会员","nickName":"小宝贝","privatePermission":false,"id":"eee69972b71441939bf1e2882c704c37","avatar":"https://yj8.me/img/h3eM/i5WRcWKRy.jpg","userType":1,"speakingFlag":"0","account":"abc1234","levelIcon":""},{"avatarUrl":"nullhttps://yj8.me/img/h3eM/i5niH0naV.jpg","level":"砖石会员","privatePermission":false,"id":"40b5f73d2b4c4ab9835aced969e09a34","avatar":"https://yj8.me/img/h3eM/i5niH0naV.jpg","userType":1,"speakingFlag":"0","account":"testray1","levelIcon":"https://yj8.me/img/7ZNl/OoJBuOtVy.png"},{"avatarUrl":"nullhttps://yj8.me/img/h3eM/i5W222uti.jpg","level":"渣渣会员","privatePermission":false,"id":"bedb8d749eb9485da0d8ce577c18bee8","avatar":"https://yj8.me/img/h3eM/i5W222uti.jpg","userType":1,"speakingFlag":"0","account":"test900","levelIcon":""},{"avatarUrl":"nullhttps://yj8.me/img/h3eM/iozdFlgtp.jpg","level":"王者会员","privatePermission":false,"id":"0d562d094c4c4453bd1375f7859f9539","avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","userType":1,"speakingFlag":"0","account":"test1","levelIcon":"https://yj8.me/img/PwcC/Oe7eMV7hG.png"},{"avatarUrl":"nullhttps://yj8.me/img/h3eM/iozdF8rpI.jpg","level":"王者会员","privatePermission":false,"id":"77114b0205ec4627af3ad65a50909a1f","avatar":"https://yj8.me/img/h3eM/iozdF8rpI.jpg","userType":1,"speakingFlag":"0","account":"test222","levelIcon":"https://yj8.me/img/PwcC/Oe7eMV7hG.png"},{"avatarUrl":"nullhttps://yj8.me/img/h3eM/i5W2RgpkH.jpg","level":"王者会员","privatePermission":false,"id":"b9d57eeb71cd4ef6aae9bc4f971f5af6","avatar":"https://yj8.me/img/h3eM/i5W2RgpkH.jpg","userType":1,"speakingFlag":"0","account":"test22","levelIcon":"https://yj8.me/img/PwcC/Oe7eMV7hG.png"},{"avatarUrl":"nullhttps://yj8.me/img/h3eM/iozdFlgtp.jpg","level":"王者会员","nickName":"baby","privatePermission":false,"id":"aaa8ae90d9304e8fbce21baa91d0e8c5","avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","userType":1,"speakingFlag":"0","account":"testray","levelIcon":"https://yj8.me/img/PwcC/Oe7eMV7hG.png"}]
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private String status;
    private List<ChatUserBean> source;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ChatUserBean> getSource() {
        return source;
    }

    public void setSource(List<ChatUserBean> source) {
        this.source = source;
    }

    public static class ChatUserBean {

        /**
         * avatarUrl : nullhttps://yj8.me/img/h3eM/i5WRcWKRy.jpg
         * level : 渣渣会员
         * nickName : 小宝贝
         * privatePermission : false
         * id : eee69972b71441939bf1e2882c704c37
         * avatar : https://yj8.me/img/h3eM/i5WRcWKRy.jpg
         * userType : 1
         * speakingFlag : 0
         * account : abc1234
         * levelIcon :
         */
        private String avatarUrl;
        private String level;
        private String nickName;
        private boolean privatePermission;
        private String id;
        private String avatar;
        private int userType;
        private String speakingFlag;
        private String account;
        private String levelIcon;

        /**
         * lastRecordTime :
         * lastRecordType :
         * fromUser : {"nickName":"sky","levelName":"帝尊会员","userType":4,"avatar":"https://yj8.me/img/h3eM/iozdKN3wr.jpg","id":"2661586ce56b40bda623b2fa5f47a0ae","account":"chat01","levelIcon":"https://img.rz520.com/uploadfile/2017/0928/20170928044331462_1.jpg"}
         * lastRecord :
         * isRead : 0
         * roomId : 61aeae43d56b4c208425c12eadb39d46
         * remarks :
         */

        private String lastRecordTime;
        private String lastRecordType;
        private FromUserBean fromUser;
        private String lastRecord;
        private int isRead;
        @SerializedName("roomId")
        private String roomIdX;
        private String remarks;

        public String getLastRecordTime() {
            return lastRecordTime;
        }

        public void setLastRecordTime(String lastRecordTime) {
            this.lastRecordTime = lastRecordTime;
        }

        public String getLastRecordType() {
            return lastRecordType;
        }

        public void setLastRecordType(String lastRecordType) {
            this.lastRecordType = lastRecordType;
        }

        public FromUserBean getFromUser() {
            return fromUser;
        }

        public void setFromUser(FromUserBean fromUser) {
            this.fromUser = fromUser;
        }

        public String getLastRecord() {
            return lastRecord;
        }

        public void setLastRecord(String lastRecord) {
            this.lastRecord = lastRecord;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public String getRoomIdX() {
            return roomIdX;
        }

        public void setRoomIdX(String roomIdX) {
            this.roomIdX = roomIdX;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }



        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public boolean isPrivatePermission() {
            return privatePermission;
        }

        public void setPrivatePermission(boolean privatePermission) {
            this.privatePermission = privatePermission;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getSpeakingFlag() {
            return speakingFlag;
        }

        public void setSpeakingFlag(String speakingFlag) {
            this.speakingFlag = speakingFlag;
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

        public static class FromUserBean {
            /**
             * nickName : sky
             * levelName : 帝尊会员
             * userType : 4
             * avatar : https://yj8.me/img/h3eM/iozdKN3wr.jpg
             * id : 2661586ce56b40bda623b2fa5f47a0ae
             * account : chat01
             * levelIcon : https://img.rz520.com/uploadfile/2017/0928/20170928044331462_1.jpg
             */

            private String nickName;
            private String levelName;
            private int userType;
            private String avatar;
            @SerializedName("id")
            private String idX;
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

            public String getIdX() {
                return idX;
            }

            public void setIdX(String idX) {
                this.idX = idX;
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
