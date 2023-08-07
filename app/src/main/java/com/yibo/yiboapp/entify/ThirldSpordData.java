package com.yibo.yiboapp.entify;

import java.io.Serializable;
import java.util.List;

/**
 * Created by johnson on 2017/10/14.
 */

/**
 * total : 1
 * aggsData : {"bettingMoneyCount":25,"realBettingMoneyCount":25,"winMoneyCount":25}
 * rows : [{"accountId":604566,"bettingContent":"美式足球<br>------<br>Merw FK<br>亚洲盘<br>-0.50<br>0.64<br>Turkmenistan Yokary Liga<br>Merw FK vs Nebitci FT<br>------<br>","bettingMoney":25,"bettingTime":1588598168022,"buyTime":1588564800000,"createDatetime":1588598229443,"id":37086,"ip":"67.211.64.91","md5Str":"505f73ecaeaab9d88a5f4e76c930a7ad","odds":"0.640","oddsType":"HongKong odds","orderId":"4078213","parentIds":",13033,391,","platformType":"SBO","realBettingMoney":25,"serverId":177,"staId":447,"stationId":35,"status":"running","thirdMemberId":9842789,"thirdUsername":"cfzpg9002","type":32,"username":"g9002","winMoney":25}]
 */
public class ThirldSpordData implements Serializable {
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

    public static class AggsDataBean implements Serializable{
        /**
         * bettingMoneyCount : 25.0
         * realBettingMoneyCount : 25.0
         * winMoneyCount : 25.0
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

    public static class RowsBean implements Serializable{
        /**
         * accountId : 604566
         * bettingContent : 美式足球<br>------<br>Merw FK<br>亚洲盘<br>-0.50<br>0.64<br>Turkmenistan Yokary Liga<br>Merw FK vs Nebitci FT<br>------<br>
         * bettingMoney : 25.0
         * bettingTime : 1588598168022
         * buyTime : 1588564800000
         * createDatetime : 1588598229443
         * id : 37086
         * ip : 67.211.64.91
         * md5Str : 505f73ecaeaab9d88a5f4e76c930a7ad
         * odds : 0.640
         * oddsType : HongKong odds
         * orderId : 4078213
         * parentIds : ,13033,391,
         * platformType : SBO
         * realBettingMoney : 25.0
         * serverId : 177
         * staId : 447
         * stationId : 35
         * status : running
         * thirdMemberId : 9842789
         * thirdUsername : cfzpg9002
         * type : 32
         * username : g9002
         * winMoney : 25.0
         */

        private int accountId;
        private String bettingContent;
        private double bettingMoney;
        private long bettingTime;
        private long buyTime;
        private long createDatetime;
        private int id;
        private String ip;
        private String md5Str;
        private String odds;
        private String oddsType;
        private String orderId;
        private String parentIds;
        private String platformType;
        private double realBettingMoney;
        private int serverId;
        private int staId;
        private int stationId;
        private String status;
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

        public long getBuyTime() {
            return buyTime;
        }

        public void setBuyTime(long buyTime) {
            this.buyTime = buyTime;
        }

        public long getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(long createDatetime) {
            this.createDatetime = createDatetime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getMd5Str() {
            return md5Str;
        }

        public void setMd5Str(String md5Str) {
            this.md5Str = md5Str;
        }

        public String getOdds() {
            return odds;
        }

        public void setOdds(String odds) {
            this.odds = odds;
        }

        public String getOddsType() {
            return oddsType;
        }

        public void setOddsType(String oddsType) {
            this.oddsType = oddsType;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
