package com.yibo.yiboapp.entify;

import java.util.List;

public class MidAutumnActiveBean {


    /**
     * activeRemark : <img src="	https://yb6.me/img/RpXJ/igMeRuw9C.jpg">
     * activeRole : <img src="https://yb6.me/img/RpXJ/igMKm50lt.jpg">
     * awardList : [{"activeId":6,"awardName":"谢谢参与","awardType":1,"chance":50,"code":"bzj","codeStr":"不中奖","id":1840,"productImg":"","productRemark":""},{"activeId":6,"awardName":"谢谢参与哟","awardType":1,"chance":50,"code":"yx","codeStr":"一秀","id":1841,"productImg":"","productRemark":""},{"activeId":6,"awardName":"小小小小打赏","awardType":4,"awardValue":5,"chance":40,"code":"ej","codeStr":"二举","id":1842,"productImg":"","productRemark":""},{"activeId":6,"awardName":"如花一晚","awardType":3,"chance":78,"code":"sh","codeStr":"三红","id":1843,"productImg":"http://www.orgs.one/images/image/114/11430378.jpg?t=1523185802","productRemark":"恭喜您"},{"activeId":6,"awardName":"百万富翁","awardType":1,"awardValue":1000000,"betNum":1,"chance":40,"code":"sj","codeStr":"四进","id":1844,"productImg":"","productRemark":""},{"activeId":6,"awardName":"苹果手机","awardType":3,"chance":56,"code":"dt","codeStr":"对堂","id":1845,"productImg":"","productRemark":""},{"activeId":6,"awardName":"华为P30","awardType":3,"chance":56,"code":"zy","codeStr":"状元","id":1846,"productImg":"","productRemark":""},{"activeId":6,"awardName":"龅牙个亲一口","awardType":3,"chance":60,"code":"wzdk","codeStr":"五子登科","id":1847,"productImg":"https://yb6.me/img/aUVO/icFeqWsWG.png","productRemark":"恭喜您"},{"activeId":6,"awardName":"苹果","awardType":3,"chance":99,"code":"wh","codeStr":"五红","id":1848,"productImg":"https://cn1.bestapples.com/wp-content/uploads/2015/10/red-delicious.jpg","productRemark":"苹果一颗"},{"activeId":6,"awardName":"加油","awardType":1,"chance":60,"code":"mdb","codeStr":"满地榜","id":1849,"productImg":"","productRemark":""},{"activeId":6,"awardName":"差一步大奖","awardType":1,"chance":50,"code":"lbh","codeStr":"六杯红","id":1850,"productImg":"","productRemark":""},{"activeId":6,"awardName":"哇 不抢会后悔","awardType":2,"awardValue":1.0E8,"betNum":10000,"chance":0,"code":"zycjh","codeStr":"状元插金花","id":1851,"productImg":"","productRemark":""}]
     * beginDatetime : 1568217600000
     * countType : 2
     * createDatetime : 1568207822092
     * endDatetime : 1568649599000
     * forgeryDataList : [{"account":"***razy","id":22448,"productName":"状元","sortNum":0,"stationId":270,"winDate":1568217600000,"winMoney":10000,"winType":"4"},{"account":"***e733","id":22418,"productName":"状元","sortNum":1,"stationId":270,"winDate":1568299347440,"winMoney":4777,"winType":"4"},{"account":"***t152","id":22420,"productName":"状元","sortNum":2,"stationId":270,"winDate":1568302204592,"winMoney":3987,"winType":"4"},{"account":"***a127","id":22422,"productName":"状元","sortNum":3,"stationId":270,"winDate":1568300719012,"winMoney":1213,"winType":"4"},{"account":"***p683","id":22424,"productName":"状元","sortNum":4,"stationId":270,"winDate":1568297649453,"winMoney":1129,"winType":"4"}]
     * id : 6
     * imgPath : https://yb6.me/img/RpXJ/iMOmIW4T8.jpg
     * joinType : 1
     * levelIds : ,485,625,574,549,488,624,491,494,512,555,569,587,612,
     * playConfig : [{"min":"100","max":"999","num":"2"},{"min":"1000","max":"9999","num":"5"},{"min":"10000","max":"99999","num":"10"},{"min":"100000","max":"999999","num":"20"},{"min":"1000000","max":"9999999","num":"50"},{"min":"10000000","max":"99999999","num":"100"},{"min":"100000000","max":"999999999","num":"500"}]
     * playNum : 498
     * playVal : 3.4862067221E8
     * stationId : 270
     * status : 2
     * title : 删除是傻逼
     */

    private String activeRemark;
    private String activeRole;
    private long beginDatetime;
    private int countType;
    private long createDatetime;
    private long endDatetime;
    private int id;
    private String imgPath;
    private int joinType;
    private String levelIds;
    private String playConfig;
    private int playNum;
    private double playVal;
    private int stationId;
    private int status;
    private String title;
    private List<AwardListBean> awardList;
    private List<ForgeryDataListBean> forgeryDataList;

    public String getActiveRemark() {
        return activeRemark;
    }

    public void setActiveRemark(String activeRemark) {
        this.activeRemark = activeRemark;
    }

    public String getActiveRole() {
        return activeRole;
    }

    public void setActiveRole(String activeRole) {
        this.activeRole = activeRole;
    }

    public long getBeginDatetime() {
        return beginDatetime;
    }

    public void setBeginDatetime(long beginDatetime) {
        this.beginDatetime = beginDatetime;
    }

    public int getCountType() {
        return countType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public long getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(long endDatetime) {
        this.endDatetime = endDatetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    public String getLevelIds() {
        return levelIds;
    }

    public void setLevelIds(String levelIds) {
        this.levelIds = levelIds;
    }

    public String getPlayConfig() {
        return playConfig;
    }

    public void setPlayConfig(String playConfig) {
        this.playConfig = playConfig;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public double getPlayVal() {
        return playVal;
    }

    public void setPlayVal(double playVal) {
        this.playVal = playVal;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AwardListBean> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<AwardListBean> awardList) {
        this.awardList = awardList;
    }

    public List<ForgeryDataListBean> getForgeryDataList() {
        return forgeryDataList;
    }

    public void setForgeryDataList(List<ForgeryDataListBean> forgeryDataList) {
        this.forgeryDataList = forgeryDataList;
    }

    public static class AwardListBean {
        /**
         * activeId : 6
         * awardName : 谢谢参与
         * awardType : 1
         * chance : 50.0
         * code : bzj
         * codeStr : 不中奖
         * id : 1840
         * productImg :
         * productRemark :
         * awardValue : 5.0
         * betNum : 1
         */

        private int activeId;
        private String awardName;
        private int awardType;
        private double chance;
        private String code;
        private String codeStr;
        private int id;
        private String productImg;
        private String productRemark;
        private double awardValue;
        private int betNum;

        public int getActiveId() {
            return activeId;
        }

        public void setActiveId(int activeId) {
            this.activeId = activeId;
        }

        public String getAwardName() {
            return awardName;
        }

        public void setAwardName(String awardName) {
            this.awardName = awardName;
        }

        public int getAwardType() {
            return awardType;
        }

        public void setAwardType(int awardType) {
            this.awardType = awardType;
        }

        public double getChance() {
            return chance;
        }

        public void setChance(double chance) {
            this.chance = chance;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodeStr() {
            return codeStr;
        }

        public void setCodeStr(String codeStr) {
            this.codeStr = codeStr;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getProductRemark() {
            return productRemark;
        }

        public void setProductRemark(String productRemark) {
            this.productRemark = productRemark;
        }

        public double getAwardValue() {
            return awardValue;
        }

        public void setAwardValue(double awardValue) {
            this.awardValue = awardValue;
        }

        public int getBetNum() {
            return betNum;
        }

        public void setBetNum(int betNum) {
            this.betNum = betNum;
        }
    }

    public static class ForgeryDataListBean {
        /**
         * account : ***razy
         * id : 22448
         * productName : 状元
         * sortNum : 0
         * stationId : 270
         * winDate : 1568217600000
         * winMoney : 10000.0
         * winType : 4
         */

        private String account;
        private int id;
        private String productName;
        private int sortNum;
        private int stationId;
        private long winDate;
        private double winMoney;
        private String winType;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getSortNum() {
            return sortNum;
        }

        public void setSortNum(int sortNum) {
            this.sortNum = sortNum;
        }

        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }

        public long getWinDate() {
            return winDate;
        }

        public void setWinDate(long winDate) {
            this.winDate = winDate;
        }

        public double getWinMoney() {
            return winMoney;
        }

        public void setWinMoney(double winMoney) {
            this.winMoney = winMoney;
        }

        public String getWinType() {
            return winType;
        }

        public void setWinType(String winType) {
            this.winType = winType;
        }
    }
}
