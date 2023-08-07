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
 * Date  :09/10/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class BetHistoryBean {

    /**
     * msg : 操作成功。
     * code : R7033
     * success : true
     * source : {"msg":[{"orderId":"C19100900007","icon":"","rollBackStatus":1,"buyMoney":5,"closedTime":"2019-10-09 14:29:30","proxyRollback":1,"canUndo":false,"buyOdds":"5.0000","lotCode":"BJSC","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"fe35a34e4471b17b5551b69318320b4e","id":212242,"oddsCode":"gyjhda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠，亚和]","multiple":1,"updateTime":"2019-10-09 14:23:40","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"gyjh","createTime":"2019-10-09 14:23:40","playType":"北京赛车","qiHao":"739659","sellingTime":"2019-10-09 14:10:00","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900008","icon":"","rollBackStatus":1,"buyMoney":5,"closedTime":"2019-10-09 14:29:30","proxyRollback":1,"canUndo":false,"buyOdds":"1.9500","lotCode":"BJSC","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"03b320a00bb47750b1435a74ef824797","id":212243,"oddsCode":"gjl","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠军]","multiple":1,"updateTime":"2019-10-09 14:23:40","buyZhuShu":1,"haoMa":"1V10龙","cheat":false,"accountId":1194,"playCode":"gj","createTime":"2019-10-09 14:23:40","playType":"北京赛车","qiHao":"739659","sellingTime":"2019-10-09 14:10:00","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900009","icon":"","rollBackStatus":1,"buyMoney":5,"closedTime":"2019-10-09 14:29:30","proxyRollback":1,"canUndo":false,"buyOdds":"1.950000","lotCode":"BJSC","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"cee7483a403a48fda09d8aa68fa34722","id":212244,"oddsCode":"yjda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[亚军]","multiple":1,"updateTime":"2019-10-09 14:23:40","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"yj","createTime":"2019-10-09 14:23:40","playType":"北京赛车","qiHao":"739659","sellingTime":"2019-10-09 14:10:00","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900001","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"5.0000","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"11dccda4d94fcc300868d6fea6391283","id":212236,"oddsCode":"gyjhda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠，亚和]","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"gyjh","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900002","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"1.9500","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"e713c8c677f14c4ad843518c27cbf986","id":212237,"oddsCode":"gjda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠军]","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"gj","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900003","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"1.9500","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"b07a4a82fffa974eb32a4bba4b970c46","id":212238,"oddsCode":"gjh","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠军]","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"1V10虎","cheat":false,"accountId":1194,"playCode":"gj","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900004","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"21.7120","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"75642ea972df495c3c7c2f630ba71a25","id":212239,"oddsCode":"gyh6","stationId":2,"kickback":0,"zhuiHao":1,"playName":"冠亚和值","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"6","cheat":false,"accountId":1194,"playCode":"gyh","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900005","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"14.4750","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"ba543fb6486cb0ac571036233cf13ff8","id":212240,"oddsCode":"gyh7","stationId":2,"kickback":0,"zhuiHao":1,"playName":"冠亚和值","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"7","cheat":false,"accountId":1194,"playCode":"gyh","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"}],"showZhuShu":false,"sourceClient":"yunji","isPrivate":false}
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
         * msg : [{"orderId":"C19100900007","icon":"","rollBackStatus":1,"buyMoney":5,"closedTime":"2019-10-09 14:29:30","proxyRollback":1,"canUndo":false,"buyOdds":"5.0000","lotCode":"BJSC","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"fe35a34e4471b17b5551b69318320b4e","id":212242,"oddsCode":"gyjhda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠，亚和]","multiple":1,"updateTime":"2019-10-09 14:23:40","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"gyjh","createTime":"2019-10-09 14:23:40","playType":"北京赛车","qiHao":"739659","sellingTime":"2019-10-09 14:10:00","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900008","icon":"","rollBackStatus":1,"buyMoney":5,"closedTime":"2019-10-09 14:29:30","proxyRollback":1,"canUndo":false,"buyOdds":"1.9500","lotCode":"BJSC","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"03b320a00bb47750b1435a74ef824797","id":212243,"oddsCode":"gjl","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠军]","multiple":1,"updateTime":"2019-10-09 14:23:40","buyZhuShu":1,"haoMa":"1V10龙","cheat":false,"accountId":1194,"playCode":"gj","createTime":"2019-10-09 14:23:40","playType":"北京赛车","qiHao":"739659","sellingTime":"2019-10-09 14:10:00","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900009","icon":"","rollBackStatus":1,"buyMoney":5,"closedTime":"2019-10-09 14:29:30","proxyRollback":1,"canUndo":false,"buyOdds":"1.950000","lotCode":"BJSC","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"cee7483a403a48fda09d8aa68fa34722","id":212244,"oddsCode":"yjda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[亚军]","multiple":1,"updateTime":"2019-10-09 14:23:40","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"yj","createTime":"2019-10-09 14:23:40","playType":"北京赛车","qiHao":"739659","sellingTime":"2019-10-09 14:10:00","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900001","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"5.0000","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"11dccda4d94fcc300868d6fea6391283","id":212236,"oddsCode":"gyjhda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠，亚和]","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"gyjh","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900002","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"1.9500","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"e713c8c677f14c4ad843518c27cbf986","id":212237,"oddsCode":"gjda","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠军]","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"大","cheat":false,"accountId":1194,"playCode":"gj","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900003","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"1.9500","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"b07a4a82fffa974eb32a4bba4b970c46","id":212238,"oddsCode":"gjh","stationId":2,"kickback":0,"zhuiHao":1,"playName":"整合[冠军]","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"1V10虎","cheat":false,"accountId":1194,"playCode":"gj","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900004","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"21.7120","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"75642ea972df495c3c7c2f630ba71a25","id":212239,"oddsCode":"gyh6","stationId":2,"kickback":0,"zhuiHao":1,"playName":"冠亚和值","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"6","cheat":false,"accountId":1194,"playCode":"gyh","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"},{"orderId":"C19100900005","icon":"","rollBackStatus":1,"buyMoney":10,"closedTime":"2019-10-09 00:12:40","proxyRollback":1,"canUndo":false,"buyOdds":"14.4750","lotCode":"XYFT","buyIp":"125.252.99.42","currentRebate":7.5,"lotVersion":2,"model":1,"signKey":"ba543fb6486cb0ac571036233cf13ff8","id":212240,"oddsCode":"gyh7","stationId":2,"kickback":0,"zhuiHao":1,"playName":"冠亚和值","multiple":1,"updateTime":"2019-10-09 00:09:12","buyZhuShu":1,"haoMa":"7","cheat":false,"accountId":1194,"playCode":"gyh","createTime":"2019-10-09 00:09:12","playType":"幸运飞艇","qiHao":"20191008134","sellingTime":"2019-10-09 00:08:40","lotType":3,"terminalBetType":3,"status":1,"username":"anuo1234"}]
         * showZhuShu : false
         * sourceClient : yunji
         * isPrivate : false
         */

        private boolean showZhuShu;
        private String sourceClient;
        private boolean isPrivate;
        private List<MsgBean> msg;

        public boolean isShowZhuShu() {
            return showZhuShu;
        }

        public void setShowZhuShu(boolean showZhuShu) {
            this.showZhuShu = showZhuShu;
        }

        public String getSourceClient() {
            return sourceClient;
        }

        public void setSourceClient(String sourceClient) {
            this.sourceClient = sourceClient;
        }

        public boolean isIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        public List<MsgBean> getMsg() {
            return msg;
        }

        public void setMsg(List<MsgBean> msg) {
            this.msg = msg;
        }

        public static class MsgBean {
            /**
             * orderId : C19100900007
             * icon :
             * rollBackStatus : 1
             * buyMoney : 5
             * closedTime : 2019-10-09 14:29:30
             * proxyRollback : 1
             * canUndo : false
             * buyOdds : 5.0000
             * lotCode : BJSC
             * buyIp : 125.252.99.42
             * currentRebate : 7.5
             * lotVersion : 2
             * model : 1
             * signKey : fe35a34e4471b17b5551b69318320b4e
             * id : 212242
             * oddsCode : gyjhda
             * stationId : 2
             * kickback : 0
             * zhuiHao : 1
             * playName : 整合[冠，亚和]
             * multiple : 1
             * updateTime : 2019-10-09 14:23:40
             * buyZhuShu : 1
             * haoMa : 大
             * cheat : false
             * accountId : 1194
             * playCode : gyjh
             * createTime : 2019-10-09 14:23:40
             * playType : 北京赛车
             * qiHao : 739659
             * sellingTime : 2019-10-09 14:10:00
             * lotType : 3
             * terminalBetType : 3
             * status : 1
             * username : anuo1234
             */

            private String orderId;
            private String icon;
            private int rollBackStatus;
            private String buyMoney;
            private String closedTime;
            private int proxyRollback;
            private boolean canUndo;
            private String buyOdds;
            private String lotCode;
            private String buyIp;
            private double currentRebate;
            private String lotVersion;
            private int model;
            private String signKey;
            private int id;
            private String oddsCode;
            private int stationId;
            private String  kickback;
            private int zhuiHao;
            private String playName;
            private int multiple;
            private String updateTime;
            private int buyZhuShu;
            private String haoMa;
            private boolean cheat;
            private int accountId;
            private String playCode;
            private String createTime;
            private String playType;
            private String qiHao;
            private String sellingTime;
            private int lotType;
            private int terminalBetType;
            private int status;
            private String username;
            private boolean isChecked;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getRollBackStatus() {
                return rollBackStatus;
            }

            public void setRollBackStatus(int rollBackStatus) {
                this.rollBackStatus = rollBackStatus;
            }

            public String getBuyMoney() {
                return buyMoney;
            }

            public void setBuyMoney(String buyMoney) {
                this.buyMoney = buyMoney;
            }

            public String getClosedTime() {
                return closedTime;
            }

            public void setClosedTime(String closedTime) {
                this.closedTime = closedTime;
            }

            public int getProxyRollback() {
                return proxyRollback;
            }

            public void setProxyRollback(int proxyRollback) {
                this.proxyRollback = proxyRollback;
            }

            public boolean isCanUndo() {
                return canUndo;
            }

            public void setCanUndo(boolean canUndo) {
                this.canUndo = canUndo;
            }

            public String getBuyOdds() {
                return buyOdds;
            }

            public void setBuyOdds(String buyOdds) {
                this.buyOdds = buyOdds;
            }

            public String getLotCode() {
                return lotCode;
            }

            public void setLotCode(String lotCode) {
                this.lotCode = lotCode;
            }

            public String getBuyIp() {
                return buyIp;
            }

            public void setBuyIp(String buyIp) {
                this.buyIp = buyIp;
            }

            public double getCurrentRebate() {
                return currentRebate;
            }

            public void setCurrentRebate(double currentRebate) {
                this.currentRebate = currentRebate;
            }

            public String getLotVersion() {
                return lotVersion;
            }

            public void setLotVersion(String lotVersion) {
                this.lotVersion = lotVersion;
            }

            public int getModel() {
                return model;
            }

            public void setModel(int model) {
                this.model = model;
            }

            public String getSignKey() {
                return signKey;
            }

            public void setSignKey(String signKey) {
                this.signKey = signKey;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOddsCode() {
                return oddsCode;
            }

            public void setOddsCode(String oddsCode) {
                this.oddsCode = oddsCode;
            }

            public int getStationId() {
                return stationId;
            }

            public void setStationId(int stationId) {
                this.stationId = stationId;
            }

            public String  getKickback() {
                return kickback;
            }

            public void setKickback(String  kickback) {
                this.kickback = kickback;
            }

            public int getZhuiHao() {
                return zhuiHao;
            }

            public void setZhuiHao(int zhuiHao) {
                this.zhuiHao = zhuiHao;
            }

            public String getPlayName() {
                return playName;
            }

            public void setPlayName(String playName) {
                this.playName = playName;
            }

            public int getMultiple() {
                return multiple;
            }

            public void setMultiple(int multiple) {
                this.multiple = multiple;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getBuyZhuShu() {
                return buyZhuShu;
            }

            public void setBuyZhuShu(int buyZhuShu) {
                this.buyZhuShu = buyZhuShu;
            }

            public String getHaoMa() {
                return haoMa;
            }

            public void setHaoMa(String haoMa) {
                this.haoMa = haoMa;
            }

            public boolean isCheat() {
                return cheat;
            }

            public void setCheat(boolean cheat) {
                this.cheat = cheat;
            }

            public int getAccountId() {
                return accountId;
            }

            public void setAccountId(int accountId) {
                this.accountId = accountId;
            }

            public String getPlayCode() {
                return playCode;
            }

            public void setPlayCode(String playCode) {
                this.playCode = playCode;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getPlayType() {
                return playType;
            }

            public void setPlayType(String playType) {
                this.playType = playType;
            }

            public String getQiHao() {
                return qiHao;
            }

            public void setQiHao(String qiHao) {
                this.qiHao = qiHao;
            }

            public String getSellingTime() {
                return sellingTime;
            }

            public void setSellingTime(String sellingTime) {
                this.sellingTime = sellingTime;
            }

            public int getLotType() {
                return lotType;
            }

            public void setLotType(int lotType) {
                this.lotType = lotType;
            }

            public int getTerminalBetType() {
                return terminalBetType;
            }

            public void setTerminalBetType(int terminalBetType) {
                this.terminalBetType = terminalBetType;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }
        }
    }
}
