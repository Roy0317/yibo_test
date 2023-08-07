package com.example.anuo.immodule.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
 * Date  :25/06/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class ChatRoomListBean {


    /**
     * msg : 操作成功。
     * code : R7023
     * success : true
     * source : {"data":[{"id":"008f0dcf7e7e4eecbf17537eb5bcdc1d","name":"无密码房","index":143,"levelCondition":"","remark":"无密码的","updateTime":"2019-10-14T08:31:03.075+0000","createTime":"2019-10-14T07:31:24.284+0000","stationId":"yunt0Chat_2","type":1,"pcBackGroundImg":"","wapBackGroundImg":"http://image.miaoxingzuo.com/uploadfile/2017/1101/20171101033759595.jpg","contentColor":"","enable":1,"isBanspeak":0,"joinCondition":"f1557feee03b44acb86f806f5e6abc94","roomImg":"","roomKey":"","roomConditionEntity":{"id":"f1557feee03b44acb86f806f5e6abc94","entryLevel":"","speakingLevel":"","name":"房间配置器1","entryMaxMoney":"","entryMinMoney":"","createTime":"2019-08-13T09:05:30.005+0000","updateTime":"2019-08-15T08:37:49.829+0000","depostType":2,"speakDepostType":1,"enable":1,"speakRechargeMoney":"","speakBetMoney":"","playBetLimit":1},"roomUserCount":14},{"id":"77214bf37cba4b8695a0f379218a6e95","name":"房间1","index":1,"levelCondition":"","remark":"测试房间","updateTime":"2019-10-14T06:46:11.417+0000","createTime":"2019-08-13T09:05:30.013+0000","stationId":"yunt0Chat_2","type":1,"pcBackGroundImg":"","wapBackGroundImg":"","contentColor":"","enable":1,"isBanspeak":0,"joinCondition":"f1557feee03b44acb86f806f5e6abc94","roomImg":"","roomKey":"111111","roomConditionEntity":{"id":"f1557feee03b44acb86f806f5e6abc94","entryLevel":"","speakingLevel":"","name":"房间配置器1","entryMaxMoney":"","entryMinMoney":"","createTime":"2019-08-13T09:05:30.005+0000","updateTime":"2019-08-15T08:37:49.829+0000","depostType":2,"speakDepostType":1,"enable":1,"speakRechargeMoney":"","speakBetMoney":"","playBetLimit":1},"roomUserCount":28},{"id":"8ed05d540f4e4fc7bec6d31b71d05ca9","name":"原生第一房","index":1,"levelCondition":"","remark":"这是首个宝贝房","updateTime":"2019-09-24T08:28:20.177+0000","createTime":"2019-08-13T09:28:37.152+0000","stationId":"yunt0Chat_2","type":1,"pcBackGroundImg":"","wapBackGroundImg":"http://img2.imgtn.bdimg.com/it/u=3114044930,303817530&fm=26&gp=0.jpg","contentColor":"","enable":1,"isBanspeak":0,"joinCondition":"f1557feee03b44acb86f806f5e6abc94","roomImg":"","roomKey":"","roomConditionEntity":{"id":"f1557feee03b44acb86f806f5e6abc94","entryLevel":"","speakingLevel":"","name":"房间配置器1","entryMaxMoney":"","entryMinMoney":"","createTime":"2019-08-13T09:05:30.005+0000","updateTime":"2019-08-15T08:37:49.829+0000","depostType":2,"speakDepostType":1,"enable":1,"speakRechargeMoney":"","speakBetMoney":"","playBetLimit":1},"roomUserCount":18}],"success":true}
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
         * data : [{"id":"008f0dcf7e7e4eecbf17537eb5bcdc1d","name":"无密码房","index":143,"levelCondition":"","remark":"无密码的","updateTime":"2019-10-14T08:31:03.075+0000","createTime":"2019-10-14T07:31:24.284+0000","stationId":"yunt0Chat_2","type":1,"pcBackGroundImg":"","wapBackGroundImg":"http://image.miaoxingzuo.com/uploadfile/2017/1101/20171101033759595.jpg","contentColor":"","enable":1,"isBanspeak":0,"joinCondition":"f1557feee03b44acb86f806f5e6abc94","roomImg":"","roomKey":"","roomConditionEntity":{"id":"f1557feee03b44acb86f806f5e6abc94","entryLevel":"","speakingLevel":"","name":"房间配置器1","entryMaxMoney":"","entryMinMoney":"","createTime":"2019-08-13T09:05:30.005+0000","updateTime":"2019-08-15T08:37:49.829+0000","depostType":2,"speakDepostType":1,"enable":1,"speakRechargeMoney":"","speakBetMoney":"","playBetLimit":1},"roomUserCount":14},{"id":"77214bf37cba4b8695a0f379218a6e95","name":"房间1","index":1,"levelCondition":"","remark":"测试房间","updateTime":"2019-10-14T06:46:11.417+0000","createTime":"2019-08-13T09:05:30.013+0000","stationId":"yunt0Chat_2","type":1,"pcBackGroundImg":"","wapBackGroundImg":"","contentColor":"","enable":1,"isBanspeak":0,"joinCondition":"f1557feee03b44acb86f806f5e6abc94","roomImg":"","roomKey":"111111","roomConditionEntity":{"id":"f1557feee03b44acb86f806f5e6abc94","entryLevel":"","speakingLevel":"","name":"房间配置器1","entryMaxMoney":"","entryMinMoney":"","createTime":"2019-08-13T09:05:30.005+0000","updateTime":"2019-08-15T08:37:49.829+0000","depostType":2,"speakDepostType":1,"enable":1,"speakRechargeMoney":"","speakBetMoney":"","playBetLimit":1},"roomUserCount":28},{"id":"8ed05d540f4e4fc7bec6d31b71d05ca9","name":"原生第一房","index":1,"levelCondition":"","remark":"这是首个宝贝房","updateTime":"2019-09-24T08:28:20.177+0000","createTime":"2019-08-13T09:28:37.152+0000","stationId":"yunt0Chat_2","type":1,"pcBackGroundImg":"","wapBackGroundImg":"http://img2.imgtn.bdimg.com/it/u=3114044930,303817530&fm=26&gp=0.jpg","contentColor":"","enable":1,"isBanspeak":0,"joinCondition":"f1557feee03b44acb86f806f5e6abc94","roomImg":"","roomKey":"","roomConditionEntity":{"id":"f1557feee03b44acb86f806f5e6abc94","entryLevel":"","speakingLevel":"","name":"房间配置器1","entryMaxMoney":"","entryMinMoney":"","createTime":"2019-08-13T09:05:30.005+0000","updateTime":"2019-08-15T08:37:49.829+0000","depostType":2,"speakDepostType":1,"enable":1,"speakRechargeMoney":"","speakBetMoney":"","playBetLimit":1},"roomUserCount":18}]
         * success : true
         */

        private boolean success;
        private List<DataBean> data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Parcelable{
            /**
             * id : 008f0dcf7e7e4eecbf17537eb5bcdc1d
             * name : 无密码房
             * index : 143
             * levelCondition :
             * remark : 无密码的
             * updateTime : 2019-10-14T08:31:03.075+0000
             * createTime : 2019-10-14T07:31:24.284+0000
             * stationId : yunt0Chat_2
             * type : 1
             * pcBackGroundImg :
             * wapBackGroundImg : http://image.miaoxingzuo.com/uploadfile/2017/1101/20171101033759595.jpg
             * contentColor :
             * enable : 1
             * isBanspeak : 0
             * joinCondition : f1557feee03b44acb86f806f5e6abc94
             * roomImg :
             * roomKey :
             * roomConditionEntity : {"id":"f1557feee03b44acb86f806f5e6abc94","entryLevel":"","speakingLevel":"","name":"房间配置器1","entryMaxMoney":"","entryMinMoney":"","createTime":"2019-08-13T09:05:30.005+0000","updateTime":"2019-08-15T08:37:49.829+0000","depostType":2,"speakDepostType":1,"enable":1,"speakRechargeMoney":"","speakBetMoney":"","playBetLimit":1}
             * roomUserCount : 14
             */

            private String id;
            private String name;
            private int index;
            private String levelCondition;
            private String remark;
            private String updateTime;
            private String createTime;
            private String stationId;
            private int type;
            private String pcBackGroundImg;
            private String wapBackGroundImg;
            private String contentColor;
            private int enable;
            private int isBanspeak;
            private String joinCondition;
            private String roomImg;
            private String roomKey;
            private RoomConditionEntityBean roomConditionEntity;
            private int roomUserCount;
            private String defaultLotteryCode;
            private int fakePerNum;
            private String mentorName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getLevelCondition() {
                return levelCondition;
            }

            public void setLevelCondition(String levelCondition) {
                this.levelCondition = levelCondition;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getStationId() {
                return stationId;
            }

            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getPcBackGroundImg() {
                return pcBackGroundImg;
            }

            public void setPcBackGroundImg(String pcBackGroundImg) {
                this.pcBackGroundImg = pcBackGroundImg;
            }

            public String getWapBackGroundImg() {
                return wapBackGroundImg;
            }

            public void setWapBackGroundImg(String wapBackGroundImg) {
                this.wapBackGroundImg = wapBackGroundImg;
            }

            public String getContentColor() {
                return contentColor;
            }

            public void setContentColor(String contentColor) {
                this.contentColor = contentColor;
            }

            public int getEnable() {
                return enable;
            }

            public void setEnable(int enable) {
                this.enable = enable;
            }

            public int getIsBanspeak() {
                return isBanspeak;
            }

            public void setIsBanspeak(int isBanspeak) {
                this.isBanspeak = isBanspeak;
            }

            public String getJoinCondition() {
                return joinCondition;
            }

            public void setJoinCondition(String joinCondition) {
                this.joinCondition = joinCondition;
            }

            public String getRoomImg() {
                return roomImg;
            }

            public void setRoomImg(String roomImg) {
                this.roomImg = roomImg;
            }

            public String getRoomKey() {
                return roomKey;
            }

            public void setRoomKey(String roomKey) {
                this.roomKey = roomKey;
            }

            public RoomConditionEntityBean getRoomConditionEntity() {
                return roomConditionEntity;
            }

            public void setRoomConditionEntity(RoomConditionEntityBean roomConditionEntity) {
                this.roomConditionEntity = roomConditionEntity;
            }

            public int getRoomUserCount() {
                return roomUserCount;
            }

            public void setRoomUserCount(int roomUserCount) {
                this.roomUserCount = roomUserCount;
            }

            public String getDefaultLotteryCode() {
                return defaultLotteryCode;
            }

            public void setDefaultLotteryCode(String defaultLotteryCode) {
                this.defaultLotteryCode = defaultLotteryCode;
            }

            public int getFakePerNum() {
                return fakePerNum;
            }

            public String getMentorName() {
                return mentorName;
            }

            public void setMentorName(String mentorName) {
                this.mentorName = mentorName;
            }

            public void setFakePerNum(int fakePerNum) {
                this.fakePerNum = fakePerNum;
            }

            public static class RoomConditionEntityBean implements Parcelable{
                /**
                 * id : f1557feee03b44acb86f806f5e6abc94
                 * entryLevel :
                 * speakingLevel :
                 * name : 房间配置器1
                 * entryMaxMoney :
                 * entryMinMoney :
                 * createTime : 2019-08-13T09:05:30.005+0000
                 * updateTime : 2019-08-15T08:37:49.829+0000
                 * depostType : 2
                 * speakDepostType : 1
                 * enable : 1
                 * speakRechargeMoney :
                 * speakBetMoney :
                 * playBetLimit : 1
                 */

                private String id;
                private String entryLevel;
                private String speakingLevel;
                private String name;
                private String entryMaxMoney;
                private String entryMinMoney;
                private String createTime;
                private String updateTime;
                private int depostType;
                private int speakDepostType;
                private int enable;
                private String speakRechargeMoney;
                private String speakBetMoney;
                private int playBetLimit;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getEntryLevel() {
                    return entryLevel;
                }

                public void setEntryLevel(String entryLevel) {
                    this.entryLevel = entryLevel;
                }

                public String getSpeakingLevel() {
                    return speakingLevel;
                }

                public void setSpeakingLevel(String speakingLevel) {
                    this.speakingLevel = speakingLevel;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getEntryMaxMoney() {
                    return entryMaxMoney;
                }

                public void setEntryMaxMoney(String entryMaxMoney) {
                    this.entryMaxMoney = entryMaxMoney;
                }

                public String getEntryMinMoney() {
                    return entryMinMoney;
                }

                public void setEntryMinMoney(String entryMinMoney) {
                    this.entryMinMoney = entryMinMoney;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public String getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(String updateTime) {
                    this.updateTime = updateTime;
                }

                public int getDepostType() {
                    return depostType;
                }

                public void setDepostType(int depostType) {
                    this.depostType = depostType;
                }

                public int getSpeakDepostType() {
                    return speakDepostType;
                }

                public void setSpeakDepostType(int speakDepostType) {
                    this.speakDepostType = speakDepostType;
                }

                public int getEnable() {
                    return enable;
                }

                public void setEnable(int enable) {
                    this.enable = enable;
                }

                public String getSpeakRechargeMoney() {
                    return speakRechargeMoney;
                }

                public void setSpeakRechargeMoney(String speakRechargeMoney) {
                    this.speakRechargeMoney = speakRechargeMoney;
                }

                public String getSpeakBetMoney() {
                    return speakBetMoney;
                }

                public void setSpeakBetMoney(String speakBetMoney) {
                    this.speakBetMoney = speakBetMoney;
                }

                public int getPlayBetLimit() {
                    return playBetLimit;
                }

                public void setPlayBetLimit(int playBetLimit) {
                    this.playBetLimit = playBetLimit;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.id);
                    dest.writeString(this.entryLevel);
                    dest.writeString(this.speakingLevel);
                    dest.writeString(this.name);
                    dest.writeString(this.entryMaxMoney);
                    dest.writeString(this.entryMinMoney);
                    dest.writeString(this.createTime);
                    dest.writeString(this.updateTime);
                    dest.writeInt(this.depostType);
                    dest.writeInt(this.speakDepostType);
                    dest.writeInt(this.enable);
                    dest.writeString(this.speakRechargeMoney);
                    dest.writeString(this.speakBetMoney);
                    dest.writeInt(this.playBetLimit);
                }

                public RoomConditionEntityBean() {
                }

                protected RoomConditionEntityBean(Parcel in) {
                    this.id = in.readString();
                    this.entryLevel = in.readString();
                    this.speakingLevel = in.readString();
                    this.name = in.readString();
                    this.entryMaxMoney = in.readString();
                    this.entryMinMoney = in.readString();
                    this.createTime = in.readString();
                    this.updateTime = in.readString();
                    this.depostType = in.readInt();
                    this.speakDepostType = in.readInt();
                    this.enable = in.readInt();
                    this.speakRechargeMoney = in.readString();
                    this.speakBetMoney = in.readString();
                    this.playBetLimit = in.readInt();
                }

                public static final Creator<RoomConditionEntityBean> CREATOR = new Creator<RoomConditionEntityBean>() {
                    @Override
                    public RoomConditionEntityBean createFromParcel(Parcel source) {
                        return new RoomConditionEntityBean(source);
                    }

                    @Override
                    public RoomConditionEntityBean[] newArray(int size) {
                        return new RoomConditionEntityBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.name);
                dest.writeInt(this.index);
                dest.writeString(this.levelCondition);
                dest.writeString(this.remark);
                dest.writeString(this.updateTime);
                dest.writeString(this.createTime);
                dest.writeString(this.stationId);
                dest.writeInt(this.type);
                dest.writeString(this.pcBackGroundImg);
                dest.writeString(this.wapBackGroundImg);
                dest.writeString(this.contentColor);
                dest.writeInt(this.enable);
                dest.writeInt(this.isBanspeak);
                dest.writeString(this.joinCondition);
                dest.writeString(this.roomImg);
                dest.writeString(this.roomKey);
                dest.writeParcelable(this.roomConditionEntity, flags);
                dest.writeInt(this.roomUserCount);
                dest.writeString(this.defaultLotteryCode);
                dest.writeInt(this.fakePerNum);
            }

            public DataBean() {
            }

            protected DataBean(Parcel in) {
                this.id = in.readString();
                this.name = in.readString();
                this.index = in.readInt();
                this.levelCondition = in.readString();
                this.remark = in.readString();
                this.updateTime = in.readString();
                this.createTime = in.readString();
                this.stationId = in.readString();
                this.type = in.readInt();
                this.pcBackGroundImg = in.readString();
                this.wapBackGroundImg = in.readString();
                this.contentColor = in.readString();
                this.enable = in.readInt();
                this.isBanspeak = in.readInt();
                this.joinCondition = in.readString();
                this.roomImg = in.readString();
                this.roomKey = in.readString();
                this.roomConditionEntity = in.readParcelable(RoomConditionEntityBean.class.getClassLoader());
                this.roomUserCount = in.readInt();
                this.defaultLotteryCode = in.readString();
                this.fakePerNum = in.readInt();
            }

            public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
                @Override
                public DataBean createFromParcel(Parcel source) {
                    return new DataBean(source);
                }

                @Override
                public DataBean[] newArray(int size) {
                    return new DataBean[size];
                }
            };
        }
    }
}
