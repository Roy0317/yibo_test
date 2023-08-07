package com.example.anuo.immodule.bean;

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
 * Date  :26/06/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class JoinChatRoomBean {


    /**
     * msg : 操作成功。
     * code : R7022
     * success : true
     * source : {"depositMobileUrl":"https://skytest1.yunji9.com/m/v2/index.do#/myPay","lotteryImageUrl":"https://skytest1.yunji9.com/common/lot/images/gameIcon/","title":"房间1","type":"","speakingFlag":0,"drawUrl":"https://skytest1.yunji9.com/userCenter/finance/withdraw.do","roomId":"77214bf37cba4b8695a0f379218a6e95","permissionObj":{"SEND_BETTING":"1","SEND_EXPRESSION":"1","ENTER_ROOM":"1","SEND_TEXT":"1","RECEIVE_RED_PACKET":"1","SEND_RED_PACKET":"1","SEND_IMAGE":"1","FAST_TALK":"1"},"score":"0","mobileUrl":"https://skytest1.yunji9.com/m/v2/index.do","levelIcon":"","roomPassWord":"","dailyMoney":{"depositAmount":"0","sbSportsWinAmount":"0","realRebateAmount":"0","sportsRebateAmount":"0","realWinAmount":"0","sbSportsRebateAmount":"0","realBetAmount":"0","chessWinAmount":"0","chessRebateAmount":"0","egameRebateAmount":"0","lotteryWinAmount":"0","sbSportsBetAmount":"0","chessBetAmount":"0","egameWinAmount":"0","sportsWinAmount":"0","sportsBetAmount":"0","lotteryRebateAmount":"0","depositArtificial":"0","egameBetAmount":"0","lotteryBetAmount":"0","withdrawAmount":0,"proxyRebateAmount":0},"level":"渣渣会员","nickName":"","accountType":1,"privatePermission":false,"signDay":0,"signed":0,"avatar":"https://yj8.me/img/h3eM/i5nXT7rgb.jpg","isBanSpeak":0,"depositUrl":"https://skytest1.yunji9.com/userCenter/finance/recharge/.do","backGround":"http://img2.imgtn.bdimg.com/it/u=3114044930,303817530&fm=26&gp=0.jpg","drawMobileUrl":"https://skytest1.yunji9.com/m/v2/index.do#/withdrawDep","roomKey":"0","userType":1,"account":"anuo1234"}
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
         * depositMobileUrl : https://skytest1.yunji9.com/m/v2/index.do#/myPay
         * lotteryImageUrl : https://skytest1.yunji9.com/common/lot/images/gameIcon/
         * title : 房间1
         * type :
         * speakingFlag : 0
         * drawUrl : https://skytest1.yunji9.com/userCenter/finance/withdraw.do
         * roomId : 77214bf37cba4b8695a0f379218a6e95
         * permissionObj : {"SEND_BETTING":"1","SEND_EXPRESSION":"1","ENTER_ROOM":"1","SEND_TEXT":"1","RECEIVE_RED_PACKET":"1","SEND_RED_PACKET":"1","SEND_IMAGE":"1","FAST_TALK":"1"}
         * score : 0
         * mobileUrl : https://skytest1.yunji9.com/m/v2/index.do
         * levelIcon :
         * roomPassWord :
         * dailyMoney : {"depositAmount":"0","sbSportsWinAmount":"0","realRebateAmount":"0","sportsRebateAmount":"0","realWinAmount":"0","sbSportsRebateAmount":"0","realBetAmount":"0","chessWinAmount":"0","chessRebateAmount":"0","egameRebateAmount":"0","lotteryWinAmount":"0","sbSportsBetAmount":"0","chessBetAmount":"0","egameWinAmount":"0","sportsWinAmount":"0","sportsBetAmount":"0","lotteryRebateAmount":"0","depositArtificial":"0","egameBetAmount":"0","lotteryBetAmount":"0","withdrawAmount":0,"proxyRebateAmount":0}
         * level : 渣渣会员
         * nickName :
         * accountType : 1
         * privatePermission : false
         * signDay : 0
         * signed : 0
         * avatar : https://yj8.me/img/h3eM/i5nXT7rgb.jpg
         * isBanSpeak : 0
         * depositUrl : https://skytest1.yunji9.com/userCenter/finance/recharge/.do
         * backGround : http://img2.imgtn.bdimg.com/it/u=3114044930,303817530&fm=26&gp=0.jpg
         * drawMobileUrl : https://skytest1.yunji9.com/m/v2/index.do#/withdrawDep
         * roomKey : 0
         * userType : 1
         * account : anuo1234
         */

        private String depositMobileUrl;
        private String lotteryImageUrl;
        private String title;
        private String type;
        //是否可以说话；1--不可以 0--可以
        private int speakingFlag;
        private String drawUrl;
        private String roomId;
        private PermissionObjBean permissionObj;
        private String score;
        private String mobileUrl;
        private String levelIcon;
        private String roomPassWord;
        private DailyMoneyBean dailyMoney;
        private String level;
        private String nickName;
        private int accountType;
        private boolean privatePermission;
        private int signDay;
        private int signed;
        private String avatar;
        //房间是否被禁言 1--禁言 0-不禁言
        private int isBanSpeak;
        private String depositUrl;
        private String backGround;
        private String drawMobileUrl;
        private String roomKey;
        private int userType; //1:普通玩家(默认)，2：后台管理员 ,3:机器人，4：前台管理员,5:前台普通用户(后台试玩 暂时没用到)
        private String account;
        // 被禁言的原因
        private String banSpeakRemark;
        private String agentRoomHost;
        private String mentorName;
        //是否是计划员
        private int planUser;
        private List<String> toolPermission;

        public List<String> getToolPermission() {
            return toolPermission;
        }

        public void setToolPermission(List<String> toolPermission) {
            this.toolPermission = toolPermission;
        }


        public String getDepositMobileUrl() {
            return depositMobileUrl;
        }

        public void setDepositMobileUrl(String depositMobileUrl) {
            this.depositMobileUrl = depositMobileUrl;
        }

        public String getLotteryImageUrl() {
            return lotteryImageUrl;
        }

        public void setLotteryImageUrl(String lotteryImageUrl) {
            this.lotteryImageUrl = lotteryImageUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSpeakingFlag() {
            return speakingFlag;
        }

        public void setSpeakingFlag(int speakingFlag) {
            this.speakingFlag = speakingFlag;
        }

        public String getDrawUrl() {
            return drawUrl;
        }

        public void setDrawUrl(String drawUrl) {
            this.drawUrl = drawUrl;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public PermissionObjBean getPermissionObj() {
            return permissionObj;
        }

        public void setPermissionObj(PermissionObjBean permissionObj) {
            this.permissionObj = permissionObj;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getMobileUrl() {
            return mobileUrl;
        }

        public void setMobileUrl(String mobileUrl) {
            this.mobileUrl = mobileUrl;
        }

        public String getLevelIcon() {
            return levelIcon;
        }

        public void setLevelIcon(String levelIcon) {
            this.levelIcon = levelIcon;
        }

        public String getRoomPassWord() {
            return roomPassWord;
        }

        public void setRoomPassWord(String roomPassWord) {
            this.roomPassWord = roomPassWord;
        }

        public DailyMoneyBean getDailyMoney() {
            return dailyMoney;
        }

        public void setDailyMoney(DailyMoneyBean dailyMoney) {
            this.dailyMoney = dailyMoney;
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

        public int getSignDay() {
            return signDay;
        }

        public void setSignDay(int signDay) {
            this.signDay = signDay;
        }

        public int getSigned() {
            return signed;
        }

        public void setSigned(int signed) {
            this.signed = signed;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getIsBanSpeak() {
            return isBanSpeak;
        }

        public void setIsBanSpeak(int isBanSpeak) {
            this.isBanSpeak = isBanSpeak;
        }

        public String getDepositUrl() {
            return depositUrl;
        }

        public void setDepositUrl(String depositUrl) {
            this.depositUrl = depositUrl;
        }

        public String getBackGround() {
            return backGround;
        }

        public void setBackGround(String backGround) {
            this.backGround = backGround;
        }

        public String getDrawMobileUrl() {
            return drawMobileUrl;
        }

        public void setDrawMobileUrl(String drawMobileUrl) {
            this.drawMobileUrl = drawMobileUrl;
        }

        public String getRoomKey() {
            return roomKey;
        }

        public void setRoomKey(String roomKey) {
            this.roomKey = roomKey;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getBanSpeakRemark() {
            return banSpeakRemark;
        }

        public void setBanSpeakRemark(String banSpeakRemark) {
            this.banSpeakRemark = banSpeakRemark;
        }

        public String getAgentRoomHost() {
            return agentRoomHost;
        }

        public void setAgentRoomHost(String agentRoomHost) {
            this.agentRoomHost = agentRoomHost;
        }

        public int getPlanUser() {
            return planUser;
        }

        public void setPlanUser(int planUser) {
            this.planUser = planUser;
        }

        public String getMentorName() {
            return mentorName;
        }

        public void setMentorName(String mentorName) {
            this.mentorName = mentorName;
        }

        public static class PermissionObjBean {
            /**
             * SEND_BETTING : 1
             * SEND_EXPRESSION : 1
             * ENTER_ROOM : 1
             * SEND_TEXT : 1
             * RECEIVE_RED_PACKET : 1
             * SEND_RED_PACKET : 1
             * SEND_IMAGE : 1
             * FAST_TALK : 1
             */

            private String SEND_BETTING;
            private String SEND_EXPRESSION;
            private String ENTER_ROOM;
            private String SEND_TEXT;
            private String RECEIVE_RED_PACKET;
            private String SEND_RED_PACKET;
            private String SEND_IMAGE;
            private String FAST_TALK;
            private String SEND_AUDIO;

            public String getSEND_BETTING() {
                return SEND_BETTING;
            }

            public void setSEND_BETTING(String SEND_BETTING) {
                this.SEND_BETTING = SEND_BETTING;
            }

            public String getSEND_EXPRESSION() {
                return SEND_EXPRESSION;
            }

            public void setSEND_EXPRESSION(String SEND_EXPRESSION) {
                this.SEND_EXPRESSION = SEND_EXPRESSION;
            }

            public String getENTER_ROOM() {
                return ENTER_ROOM;
            }

            public void setENTER_ROOM(String ENTER_ROOM) {
                this.ENTER_ROOM = ENTER_ROOM;
            }

            public String getSEND_TEXT() {
                return SEND_TEXT;
            }

            public void setSEND_TEXT(String SEND_TEXT) {
                this.SEND_TEXT = SEND_TEXT;
            }

            public String getRECEIVE_RED_PACKET() {
                return RECEIVE_RED_PACKET;
            }

            public void setRECEIVE_RED_PACKET(String RECEIVE_RED_PACKET) {
                this.RECEIVE_RED_PACKET = RECEIVE_RED_PACKET;
            }

            public String getSEND_RED_PACKET() {
                return SEND_RED_PACKET;
            }

            public void setSEND_RED_PACKET(String SEND_RED_PACKET) {
                this.SEND_RED_PACKET = SEND_RED_PACKET;
            }

            public String getSEND_IMAGE() {
                return SEND_IMAGE;
            }

            public void setSEND_IMAGE(String SEND_IMAGE) {
                this.SEND_IMAGE = SEND_IMAGE;
            }

            public String getFAST_TALK() {
                return FAST_TALK;
            }

            public void setFAST_TALK(String FAST_TALK) {
                this.FAST_TALK = FAST_TALK;
            }

            public String getSEND_AUDIO() {
                return SEND_AUDIO;
            }

            public void setSEND_AUDIO(String SEND_AUDIO) {
                this.SEND_AUDIO = SEND_AUDIO;
            }
        }

        public static class DailyMoneyBean {
            /**
             * depositAmount : 0
             * sbSportsWinAmount : 0
             * realRebateAmount : 0
             * sportsRebateAmount : 0
             * realWinAmount : 0
             * sbSportsRebateAmount : 0
             * realBetAmount : 0
             * chessWinAmount : 0
             * chessRebateAmount : 0
             * egameRebateAmount : 0
             * lotteryWinAmount : 0
             * sbSportsBetAmount : 0
             * chessBetAmount : 0
             * egameWinAmount : 0
             * sportsWinAmount : 0
             * sportsBetAmount : 0
             * lotteryRebateAmount : 0
             * depositArtificial : 0
             * egameBetAmount : 0
             * lotteryBetAmount : 0
             * withdrawAmount : 0
             * proxyRebateAmount : 0
             */

            private String depositAmount;
            private String sbSportsWinAmount;
            private String realRebateAmount;
            private String sportsRebateAmount;
            private String realWinAmount;
            private String sbSportsRebateAmount;
            private String realBetAmount;
            private String chessWinAmount;
            private String chessRebateAmount;
            private String egameRebateAmount;
            private String lotteryWinAmount;
            private String sbSportsBetAmount;
            private String chessBetAmount;
            private String egameWinAmount;
            private String sportsWinAmount;
            private String sportsBetAmount;
            private String lotteryRebateAmount;
            private String depositArtificial;
            private String egameBetAmount;
            private String lotteryBetAmount;
            private int withdrawAmount;
            private int proxyRebateAmount;

            public String getDepositAmount() {
                return depositAmount;
            }

            public void setDepositAmount(String depositAmount) {
                this.depositAmount = depositAmount;
            }

            public String getSbSportsWinAmount() {
                return sbSportsWinAmount;
            }

            public void setSbSportsWinAmount(String sbSportsWinAmount) {
                this.sbSportsWinAmount = sbSportsWinAmount;
            }

            public String getRealRebateAmount() {
                return realRebateAmount;
            }

            public void setRealRebateAmount(String realRebateAmount) {
                this.realRebateAmount = realRebateAmount;
            }

            public String getSportsRebateAmount() {
                return sportsRebateAmount;
            }

            public void setSportsRebateAmount(String sportsRebateAmount) {
                this.sportsRebateAmount = sportsRebateAmount;
            }

            public String getRealWinAmount() {
                return realWinAmount;
            }

            public void setRealWinAmount(String realWinAmount) {
                this.realWinAmount = realWinAmount;
            }

            public String getSbSportsRebateAmount() {
                return sbSportsRebateAmount;
            }

            public void setSbSportsRebateAmount(String sbSportsRebateAmount) {
                this.sbSportsRebateAmount = sbSportsRebateAmount;
            }

            public String getRealBetAmount() {
                return realBetAmount;
            }

            public void setRealBetAmount(String realBetAmount) {
                this.realBetAmount = realBetAmount;
            }

            public String getChessWinAmount() {
                return chessWinAmount;
            }

            public void setChessWinAmount(String chessWinAmount) {
                this.chessWinAmount = chessWinAmount;
            }

            public String getChessRebateAmount() {
                return chessRebateAmount;
            }

            public void setChessRebateAmount(String chessRebateAmount) {
                this.chessRebateAmount = chessRebateAmount;
            }

            public String getEgameRebateAmount() {
                return egameRebateAmount;
            }

            public void setEgameRebateAmount(String egameRebateAmount) {
                this.egameRebateAmount = egameRebateAmount;
            }

            public String getLotteryWinAmount() {
                return lotteryWinAmount;
            }

            public void setLotteryWinAmount(String lotteryWinAmount) {
                this.lotteryWinAmount = lotteryWinAmount;
            }

            public String getSbSportsBetAmount() {
                return sbSportsBetAmount;
            }

            public void setSbSportsBetAmount(String sbSportsBetAmount) {
                this.sbSportsBetAmount = sbSportsBetAmount;
            }

            public String getChessBetAmount() {
                return chessBetAmount;
            }

            public void setChessBetAmount(String chessBetAmount) {
                this.chessBetAmount = chessBetAmount;
            }

            public String getEgameWinAmount() {
                return egameWinAmount;
            }

            public void setEgameWinAmount(String egameWinAmount) {
                this.egameWinAmount = egameWinAmount;
            }

            public String getSportsWinAmount() {
                return sportsWinAmount;
            }

            public void setSportsWinAmount(String sportsWinAmount) {
                this.sportsWinAmount = sportsWinAmount;
            }

            public String getSportsBetAmount() {
                return sportsBetAmount;
            }

            public void setSportsBetAmount(String sportsBetAmount) {
                this.sportsBetAmount = sportsBetAmount;
            }

            public String getLotteryRebateAmount() {
                return lotteryRebateAmount;
            }

            public void setLotteryRebateAmount(String lotteryRebateAmount) {
                this.lotteryRebateAmount = lotteryRebateAmount;
            }

            public String getDepositArtificial() {
                return depositArtificial;
            }

            public void setDepositArtificial(String depositArtificial) {
                this.depositArtificial = depositArtificial;
            }

            public String getEgameBetAmount() {
                return egameBetAmount;
            }

            public void setEgameBetAmount(String egameBetAmount) {
                this.egameBetAmount = egameBetAmount;
            }

            public String getLotteryBetAmount() {
                return lotteryBetAmount;
            }

            public void setLotteryBetAmount(String lotteryBetAmount) {
                this.lotteryBetAmount = lotteryBetAmount;
            }

            public int getWithdrawAmount() {
                return withdrawAmount;
            }

            public void setWithdrawAmount(int withdrawAmount) {
                this.withdrawAmount = withdrawAmount;
            }

            public int getProxyRebateAmount() {
                return proxyRebateAmount;
            }

            public void setProxyRebateAmount(int proxyRebateAmount) {
                this.proxyRebateAmount = proxyRebateAmount;
            }
        }
    }
}
