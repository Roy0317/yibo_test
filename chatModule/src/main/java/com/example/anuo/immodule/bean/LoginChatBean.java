package com.example.anuo.immodule.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
 * Date  :24/06/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class LoginChatBean implements Parcelable {

    /**
     * msg : 操作成功。
     * code : R70000
     * success : true
     * source : {"selfUserId":"a2c4633274724bb1b9d64f3a5e02b58e","userType":1,"agentRoom":"0","securityKey":"b471f2700b1141a3","userConfigRoom":{"roomImg":"http://3.1.222.66/storage/creation/6247/20190607/l2sOeLmsoXe94EhtMoRakX1uwIYiyNVQCIMkRQ3R.jpeg","name":"测试公共房间","id":"e61c982ebb6040f89d317e499eaa0d8f"},"stationId":"yunt0Chat_2"}
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private String dataKey;
    private SourceBean source;
    private String status;

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

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

    public static class SourceBean implements Parcelable {
        /**
         * selfUserId : a2c4633274724bb1b9d64f3a5e02b58e
         * userType : 1
         * agentRoom : 0
         * securityKey : b471f2700b1141a3
         * userConfigRoom : {"roomImg":"http://3.1.222.66/storage/creation/6247/20190607/l2sOeLmsoXe94EhtMoRakX1uwIYiyNVQCIMkRQ3R.jpeg","name":"测试公共房间","id":"e61c982ebb6040f89d317e499eaa0d8f"}
         * stationId : yunt0Chat_2
         */

        private String selfUserId;
        private int userType;
        private String securityKey;
        private UserConfigRoomBean agentRoom;
        private String stationId;
        private int accountType;
        private ChatRoomListBean.SourceBean.DataBean roomEntify;
        private ChatRoomListBean.SourceBean.DataBean userConfigRoom;

        public String getSelfUserId() {
            return selfUserId;
        }

        public void setSelfUserId(String selfUserId) {
            this.selfUserId = selfUserId;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }


        public String getSecurityKey() {
            return securityKey;
        }

        public void setSecurityKey(String securityKey) {
            this.securityKey = securityKey;
        }

        public UserConfigRoomBean getAgentRoom() {
            return agentRoom;
        }

        public void setAgentRoom(UserConfigRoomBean agentRoom) {
            this.agentRoom = agentRoom;
        }

        public ChatRoomListBean.SourceBean.DataBean getUserConfigRoom() {
            return userConfigRoom;
        }

        public void setUserConfigRoom(ChatRoomListBean.SourceBean.DataBean userConfigRoom) {
            this.userConfigRoom = userConfigRoom;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        public ChatRoomListBean.SourceBean.DataBean getRoomEntify() {
            return roomEntify;
        }

        public void setRoomEntify(ChatRoomListBean.SourceBean.DataBean roomEntify) {
            this.roomEntify = roomEntify;
        }

        public static class UserConfigRoomBean implements Parcelable {
            /**
             * roomImg : http://3.1.222.66/storage/creation/6247/20190607/l2sOeLmsoXe94EhtMoRakX1uwIYiyNVQCIMkRQ3R.jpeg
             * name : 测试公共房间
             * id : e61c982ebb6040f89d317e499eaa0d8f
             */

            private String roomImg;
            private String name;
            private String id;
            private String agentUserCode;
            private String switchAgentEnterPublicRoom;//代理线用户是否能进入公共房间
            private String needRoomKey;

            public String getRoomImg() {
                return roomImg;
            }

            public void setRoomImg(String roomImg) {
                this.roomImg = roomImg;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAgentUserCode() {
                return agentUserCode;
            }

            public void setAgentUserCode(String agentUserCode) {
                this.agentUserCode = agentUserCode;
            }

            public String getSwitchAgentEnterPublicRoom() {
                return switchAgentEnterPublicRoom;
            }

            public void setSwitchAgentEnterPublicRoom(String switchAgentEnterPublicRoom) {
                this.switchAgentEnterPublicRoom = switchAgentEnterPublicRoom;
            }

            public String getNeedRoomKey() {
                return needRoomKey;
            }

            public void setNeedRoomKey(String needRoomKey) {
                this.needRoomKey = needRoomKey;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.roomImg);
                dest.writeString(this.name);
                dest.writeString(this.id);
                dest.writeString(this.agentUserCode);
                dest.writeString(this.switchAgentEnterPublicRoom);
                dest.writeString(this.needRoomKey);
            }

            public UserConfigRoomBean() {
            }

            protected UserConfigRoomBean(Parcel in) {
                this.roomImg = in.readString();
                this.name = in.readString();
                this.id = in.readString();
                this.agentUserCode = in.readString();
                this.switchAgentEnterPublicRoom = in.readString();
                this.needRoomKey = in.readString();
            }

            public static final Creator<UserConfigRoomBean> CREATOR = new Creator<UserConfigRoomBean>() {
                @Override
                public UserConfigRoomBean createFromParcel(Parcel source) {
                    return new UserConfigRoomBean(source);
                }

                @Override
                public UserConfigRoomBean[] newArray(int size) {
                    return new UserConfigRoomBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.selfUserId);
            dest.writeInt(this.userType);
            dest.writeString(this.securityKey);
            dest.writeParcelable(this.agentRoom, flags);
            dest.writeString(this.stationId);
            dest.writeInt(this.accountType);
            dest.writeParcelable(this.roomEntify, flags);
            dest.writeParcelable(this.userConfigRoom, flags);
        }

        public SourceBean() {
        }

        protected SourceBean(Parcel in) {
            this.selfUserId = in.readString();
            this.userType = in.readInt();
            this.securityKey = in.readString();
            this.agentRoom = in.readParcelable(UserConfigRoomBean.class.getClassLoader());
            this.stationId = in.readString();
            this.accountType = in.readInt();
            this.roomEntify = in.readParcelable(ChatRoomListBean.SourceBean.DataBean.class.getClassLoader());
            this.userConfigRoom = in.readParcelable(ChatRoomListBean.SourceBean.DataBean.class.getClassLoader());
        }

        public static final Creator<SourceBean> CREATOR = new Creator<SourceBean>() {
            @Override
            public SourceBean createFromParcel(Parcel source) {
                return new SourceBean(source);
            }

            @Override
            public SourceBean[] newArray(int size) {
                return new SourceBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg);
        dest.writeString(this.code);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.dataKey);
        dest.writeParcelable(this.source, flags);
        dest.writeString(this.status);
    }

    public LoginChatBean() {
    }

    protected LoginChatBean(Parcel in) {
        this.msg = in.readString();
        this.code = in.readString();
        this.success = in.readByte() != 0;
        this.dataKey = in.readString();
        this.source = in.readParcelable(SourceBean.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Creator<LoginChatBean> CREATOR = new Creator<LoginChatBean>() {
        @Override
        public LoginChatBean createFromParcel(Parcel source) {
            return new LoginChatBean(source);
        }

        @Override
        public LoginChatBean[] newArray(int size) {
            return new LoginChatBean[size];
        }
    };
}
