package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/14.
 */

/**
 * total : 8
 * aggsData : {"bettingMoneyCount":16462,"realBettingMoneyCount":16462,"winMoneyCount":14806}
 * rows : [{"accountId":14973,"bettingContent":"发炮数量:214 JP预抽=2.14","bettingMoney":2140,"bettingTime":1588492879000,"createDatetime":1588492928261,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45196,"jackpot":0,"md5Str":"39574ac682bc2eba61e686d607df3186","orderId":"bg_fish_200503040119000003","parentIds":",367,","platformType":"BGFISH","realBettingMoney":2140,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":1490},{"accountId":14973,"bettingContent":"发炮数量:276 JP预抽=2.26","bettingMoney":2260,"bettingTime":1588492830000,"createDatetime":1588492867969,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45176,"jackpot":0,"md5Str":"f500df93feef12c982b09ab873ec1beb","orderId":"bg_fish_200503040030000005","parentIds":",367,","platformType":"BGFISH","realBettingMoney":2260,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":2050},{"accountId":14973,"bettingContent":"发炮数量:178 JP预抽=1.78","bettingMoney":1780,"bettingTime":1588492770000,"createDatetime":1588492807828,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45153,"jackpot":0,"md5Str":"ec2a9125fc06f866847a2eed27b32039","orderId":"bg_fish_200503035930000006","parentIds":",367,","platformType":"BGFISH","realBettingMoney":1780,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":820},{"accountId":14973,"bettingContent":"发炮数量:195 JP预抽=1.733","bettingMoney":1733,"bettingTime":1588492710000,"createDatetime":1588492747837,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45097,"jackpot":0,"md5Str":"40c853772bb9a1a102698d73e7717edc","orderId":"bg_fish_200503035830000007","parentIds":",367,","platformType":"BGFISH","realBettingMoney":1733,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":1556},{"accountId":14973,"bettingContent":"发炮数量:204 JP预抽=2.019","bettingMoney":2019,"bettingTime":1588492650000,"createDatetime":1588492687858,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45084,"jackpot":0,"md5Str":"e867c04525440f75338f936b8e812a0c","orderId":"bg_fish_200503035730000008","parentIds":",367,","platformType":"BGFISH","realBettingMoney":2019,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":1410},{"accountId":14973,"bettingContent":"发炮数量:184 JP预抽=1.84","bettingMoney":1840,"bettingTime":1588492590000,"createDatetime":1588492627845,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45068,"jackpot":0,"md5Str":"a36f0568bdeb795e772440ac31a2fa26","orderId":"bg_fish_200503035630000006","parentIds":",367,","platformType":"BGFISH","realBettingMoney":1840,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":2070},{"accountId":14973,"bettingContent":"发炮数量:318 JP预抽=2.69","bettingMoney":2690,"bettingTime":1588492530000,"createDatetime":1588492567706,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45045,"jackpot":0,"md5Str":"1084ace1d3a308a993691770182080b1","orderId":"bg_fish_200503035530000006","parentIds":",367,","platformType":"BGFISH","realBettingMoney":2690,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":2910},{"accountId":14973,"bettingContent":"发炮数量:200 JP预抽=2","bettingMoney":2000,"bettingTime":1588492470000,"createDatetime":1588492508110,"deviceType":"1","gameCode":"105","gameName":"BG捕鱼大师","id":45029,"jackpot":0,"md5Str":"ff5c06560f8185f59b580f97fe2c5110","orderId":"bg_fish_200503035430000006","parentIds":",367,","platformType":"BGFISH","realBettingMoney":2000,"sceneId":"2020050300058965","serverId":228,"staId":624,"stationId":23,"thirdMemberId":4748923,"thirdUsername":"yb6cn9xpj200","type":20,"username":"xpj200","winMoney":2500}]
 */
public class HunterGameData {

    private int total;
    private AggsDataBean aggsData;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public AggsDataBean getAggsData() {
        return aggsData;
    }

    public void setAggsData(AggsDataBean aggsData) {
        this.aggsData = aggsData;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class AggsDataBean {
        /**
         * bettingMoneyCount : 16462.0
         * realBettingMoneyCount : 16462.0
         * winMoneyCount : 14806.0
         */

        private float bettingMoneyCount;
        private float realBettingMoneyCount;
        private float winMoneyCount;

        public float getBettingMoneyCount() {
            return bettingMoneyCount;
        }

        public void setBettingMoneyCount(float bettingMoneyCount) {
            this.bettingMoneyCount = bettingMoneyCount;
        }

        public float getRealBettingMoneyCount() {
            return realBettingMoneyCount;
        }

        public void setRealBettingMoneyCount(float realBettingMoneyCount) {
            this.realBettingMoneyCount = realBettingMoneyCount;
        }

        public float getWinMoneyCount() {
            return winMoneyCount;
        }

        public void setWinMoneyCount(float winMoneyCount) {
            this.winMoneyCount = winMoneyCount;
        }
    }

    public static class RowsBean {
        /**
         * accountId : 14973
         * bettingContent : 发炮数量:214 JP预抽=2.14
         * bettingMoney : 2140.0
         * bettingTime : 1588492879000
         * createDatetime : 1588492928261
         * deviceType : 1
         * gameCode : 105
         * gameName : BG捕鱼大师
         * id : 45196
         * jackpot : 0.0
         * md5Str : 39574ac682bc2eba61e686d607df3186
         * orderId : bg_fish_200503040119000003
         * parentIds : ,367,
         * platformType : BGFISH
         * realBettingMoney : 2140.0
         * sceneId : 2020050300058965
         * serverId : 228
         * staId : 624
         * stationId : 23
         * thirdMemberId : 4748923
         * thirdUsername : yb6cn9xpj200
         * type : 20
         * username : xpj200
         * winMoney : 1490.0
         */

        private int accountId;
        private String bettingContent;
        private double bettingMoney;
        private long bettingTime;
        private long createDatetime;
        private String deviceType;
        private String gameCode;
        private String gameName;
        private int id;
        private double jackpot;
        private String md5Str;
        private String orderId;
        private String parentIds;
        private String platformType;
        private double realBettingMoney;
        private String sceneId;
        private int serverId;
        private int staId;
        private int stationId;
        private int thirdMemberId;
        private String thirdUsername;
        private int type;
        private String username;
        private double winMoney;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getBettingContent() {
            return bettingContent;
        }

        public void setBettingContent(String bettingContent) {
            this.bettingContent = bettingContent;
        }

        public double getBettingMoney() {
            return bettingMoney;
        }

        public void setBettingMoney(double bettingMoney) {
            this.bettingMoney = bettingMoney;
        }

        public long getBettingTime() {
            return bettingTime;
        }

        public void setBettingTime(long bettingTime) {
            this.bettingTime = bettingTime;
        }

        public long getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(long createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getGameCode() {
            return gameCode;
        }

        public void setGameCode(String gameCode) {
            this.gameCode = gameCode;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getJackpot() {
            return jackpot;
        }

        public void setJackpot(double jackpot) {
            this.jackpot = jackpot;
        }

        public String getMd5Str() {
            return md5Str;
        }

        public void setMd5Str(String md5Str) {
            this.md5Str = md5Str;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getParentIds() {
            return parentIds;
        }

        public void setParentIds(String parentIds) {
            this.parentIds = parentIds;
        }

        public String getPlatformType() {
            return platformType;
        }

        public void setPlatformType(String platformType) {
            this.platformType = platformType;
        }

        public double getRealBettingMoney() {
            return realBettingMoney;
        }

        public void setRealBettingMoney(double realBettingMoney) {
            this.realBettingMoney = realBettingMoney;
        }

        public String getSceneId() {
            return sceneId;
        }

        public void setSceneId(String sceneId) {
            this.sceneId = sceneId;
        }

        public int getServerId() {
            return serverId;
        }

        public void setServerId(int serverId) {
            this.serverId = serverId;
        }

        public int getStaId() {
            return staId;
        }

        public void setStaId(int staId) {
            this.staId = staId;
        }

        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }

        public int getThirdMemberId() {
            return thirdMemberId;
        }

        public void setThirdMemberId(int thirdMemberId) {
            this.thirdMemberId = thirdMemberId;
        }

        public String getThirdUsername() {
            return thirdUsername;
        }

        public void setThirdUsername(String thirdUsername) {
            this.thirdUsername = thirdUsername;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public double getWinMoney() {
            return winMoney;
        }

        public void setWinMoney(double winMoney) {
            this.winMoney = winMoney;
        }
    }
}
