package com.example.anuo.immodule.bean;

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
public class ChatShareDataBean {

    /**
     * msg : 操作成功。
     * code : R7001
     * success : true
     * source : {"code":"A0001","msgType":1,"speakType":"2","fromUser":{"levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","id":"0d562d094c4c4453bd1375f7859f9539","account":"test1","levelIcon":"https://yj8.me/img/PwcC/Oe7eMV7hG.png"},"isPlanUser":0,"msgId":"1912329f9ffaaec64454a3373c51b51ad48a","type":1,"userMessageId":"1912329f9ffaaec64454a3373c51b51ad48a","roomId":"7ea73d31fe844cd38aece402001be771_yjtest1_3","record":"{\"touzhu\":\"0.0\",\"yingkui\":\"0.0\",\"yingli\":\"0.0\"}","userType":1,"time":"2019-12-06 22:02:28"}
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
         * code : A0001
         * msgType : 1
         * speakType : 2
         * fromUser : {"levelName":"王者会员","userType":1,"avatar":"https://yj8.me/img/h3eM/iozdFlgtp.jpg","id":"0d562d094c4c4453bd1375f7859f9539","account":"test1","levelIcon":"https://yj8.me/img/PwcC/Oe7eMV7hG.png"}
         * isPlanUser : 0
         * msgId : 1912329f9ffaaec64454a3373c51b51ad48a
         * type : 1
         * userMessageId : 1912329f9ffaaec64454a3373c51b51ad48a
         * roomId : 7ea73d31fe844cd38aece402001be771_yjtest1_3
         * record : {"touzhu":"0.0","yingkui":"0.0","yingli":"0.0"}
         * userType : 1
         * time : 2019-12-06 22:02:28
         */

        private String code;
        private int msgType;
        private String speakType;
        private FromUserBean fromUser;
        private int isPlanUser;
        private String msgId;
        private int type;
        private String userMessageId;
        private String roomId;
        private String record;
        private int userType;
        private String time;
        private String money;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getSpeakType() {
            return speakType;
        }

        public void setSpeakType(String speakType) {
            this.speakType = speakType;
        }

        public FromUserBean getFromUser() {
            return fromUser;
        }

        public void setFromUser(FromUserBean fromUser) {
            this.fromUser = fromUser;
        }

        public int getIsPlanUser() {
            return isPlanUser;
        }

        public void setIsPlanUser(int isPlanUser) {
            this.isPlanUser = isPlanUser;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUserMessageId() {
            return userMessageId;
        }

        public void setUserMessageId(String userMessageId) {
            this.userMessageId = userMessageId;
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

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public static class FromUserBean {
            /**
             * levelName : 王者会员
             * userType : 1
             * avatar : https://yj8.me/img/h3eM/iozdFlgtp.jpg
             * id : 0d562d094c4c4453bd1375f7859f9539
             * account : test1
             * levelIcon : https://yj8.me/img/PwcC/Oe7eMV7hG.png
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
}
