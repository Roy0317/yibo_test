package com.example.anuo.immodule.bean.base;

import com.example.anuo.immodule.bean.MsgType;

import java.io.Serializable;

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
 * Date  :2019/6/6
 * Desc  :com.example.anuo.immodule.bean
 */
public class MsgBody implements Serializable {
    private MsgType localMsgType; //
    private String msgUUID; //
    private String code;
    private String status;
    private String channelId;
    private String userId; //
    private String source;
    private String sentTime;
    //1.快捷发言 2.自由发言 3.发送表情 4.投注消息 5.@其它用户的消息 6.快捷图片
    private String speakType;
    //1 代表当前帐号是当前所在的代理房间的主人  0 不是
    private String agentRoomHost;
    private int stopTalkType;
    private String msgId;
    private FromUserBean fromUser;


    //用于私聊
    private String passiveUserId;//被邀请的userId
    private String getRemark;
    private Integer start;
    private Integer msgType;//1.文本 2.图片 4.分享今日输赢 5.分享注单
    private String userMessageId;
    public static final int PRIVATE_TXT = 1 ;
    public static final int PRIVATE_IMAGE = 2 ;
    public static final int PRIVATE_WIN_LOST = 4 ;
    public static final int PRIVATE_SHARE_BET = 5 ;

    public String getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(String userMessageId) {
        this.userMessageId = userMessageId;
    }

    public String getPassiveUserId() {
        return passiveUserId;
    }

    public void setPassiveUserId(String passiveUserId) {
        this.passiveUserId = passiveUserId;
    }

    public String getGetRemark() {
        return getRemark;
    }

    public void setGetRemark(String getRemark) {
        this.getRemark = getRemark;
    }


    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getStopTalkType() {
        return stopTalkType;
    }

    public void setStopTalkType(int stopTalkType) {
        this.stopTalkType = stopTalkType;
    }


    public String getMsgUUID() {
        return msgUUID;
    }

    public void setMsgUUID(String msgUUID) {
        this.msgUUID = msgUUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MsgType getLocalMsgType() {
        return localMsgType;
    }

    public void setLocalMsgType(MsgType localMsgType) {
        this.localMsgType = localMsgType;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getSpeakType() {
        return speakType;
    }

    public void setSpeakType(String speakType) {
        this.speakType = speakType;
    }

    public String getAgentRoomHost() {
        return agentRoomHost;
    }

    public void setAgentRoomHost(String agentRoomHost) {
        this.agentRoomHost = agentRoomHost;
    }

    public FromUserBean getFromUser() {
        return fromUser;
    }

    public void setFromUser(FromUserBean fromUser) {
        this.fromUser = fromUser;
    }

    public class FromUserBean {

        /**
         * robotImage : http://img4.imgtn.bdimg.com/it/u=3822054330,2180592158&fm=11&gp=0.jpg
         * createTime : 1573824626014
         * enable : 1
         * levelId : 140
         * name : testman
         * banSpeakSend : 0
         * levelName : 天王会员
         * id : a6008ce65a2b4f70af406156efc8e6dc
         * stationId : yjtest1_3
         */

        private String robotImage;
        private String createTime;
        private String enable;
        private String levelId;
        private String name;
        private String banSpeakSend;
        private String levelName;
        private String id;
        private String stationId;

        public String getRobotImage() {
            return robotImage;
        }

        public void setRobotImage(String robotImage) {
            this.robotImage = robotImage;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public String getLevelId() {
            return levelId;
        }

        public void setLevelId(String levelId) {
            this.levelId = levelId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBanSpeakSend() {
            return banSpeakSend;
        }

        public void setBanSpeakSend(String banSpeakSend) {
            this.banSpeakSend = banSpeakSend;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }
    }

}
