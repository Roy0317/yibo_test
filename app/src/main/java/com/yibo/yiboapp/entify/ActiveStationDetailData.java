package com.yibo.yiboapp.entify;

import java.util.List;

public class ActiveStationDetailData {
    /**
     * activity : {"activeType":3,"content":"<img src=\"https://yk6.me/img/4dOz/BE5Jquk4C.png\"/>","id":35,"levelIds":",9,86,78,79,80,226,227,228,","overTime":1609344000000,"paiXu":102,"stationId":30,"status":2,"title":"真人送豪礼 优惠一","titleImgUrl":"https://yk6.me/img/4dOz/BEw5ub3rP.png","updateTime":1577808000000}
     * awards : [{"activeId":35,"awardIndex":1,"awardName":"有效投注1000+申请3元","awardType":1,"awardValue":3,"betRate":1,"id":200},{"activeId":35,"awardIndex":2,"awardName":"有效投注5000+申请8元","awardType":1,"awardValue":8,"betRate":1,"id":201},{"activeId":35,"awardIndex":3,"awardName":"有效投注10000+申请18元","awardType":1,"awardValue":18,"betRate":1,"id":202},{"activeId":35,"awardIndex":4,"awardName":"有效投注50000+申请38元","awardType":1,"awardValue":38,"betRate":1,"id":203},{"activeId":35,"awardIndex":5,"awardName":"有效投注100000+申请88元","awardType":1,"awardValue":88,"betRate":1,"id":204},{"activeId":35,"awardIndex":6,"awardName":"有效投注300000+申请188元","awardType":1,"awardValue":188,"betRate":1,"id":205},{"activeId":35,"awardIndex":7,"awardName":"有效投注500000+申请388元","awardType":1,"awardValue":388,"betRate":1,"id":206},{"activeId":35,"awardIndex":8,"awardName":"有效投注1000000+申请888元","awardType":1,"awardValue":888,"betRate":1,"id":207},{"activeId":35,"awardIndex":9,"awardName":"有效投注5000000+申请1888元","awardType":1,"awardValue":1888,"betRate":1,"id":208},{"activeId":35,"awardIndex":10,"awardName":"有效投注50000000+申请18888元","awardType":1,"awardValue":18888,"betRate":1,"id":209}]
     */

    private ActivityBean activity;
    private List<AwardsBean> awards;

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public List<AwardsBean> getAwards() {
        return awards;
    }

    public void setAwards(List<AwardsBean> awards) {
        this.awards = awards;
    }

    public static class ActivityBean {
        /**
         * activeType : 3
         * content : <img src="https://yk6.me/img/4dOz/BE5Jquk4C.png"/>
         * id : 35
         * levelIds : ,9,86,78,79,80,226,227,228,
         * overTime : 1609344000000
         * paiXu : 102
         * stationId : 30
         * status : 2
         * title : 真人送豪礼 优惠一
         * titleImgUrl : https://yk6.me/img/4dOz/BEw5ub3rP.png
         * updateTime : 1577808000000
         */

        private int activeType;
        private String content;
        private int id;
        private String levelIds;
        private long overTime;
        private int paiXu;
        private int stationId;
        private int status;
        private String title;
        private String titleImgUrl;
        private long updateTime;

        public int getActiveType() {
            return activeType;
        }

        public void setActiveType(int activeType) {
            this.activeType = activeType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLevelIds() {
            return levelIds;
        }

        public void setLevelIds(String levelIds) {
            this.levelIds = levelIds;
        }

        public long getOverTime() {
            return overTime;
        }

        public void setOverTime(long overTime) {
            this.overTime = overTime;
        }

        public int getPaiXu() {
            return paiXu;
        }

        public void setPaiXu(int paiXu) {
            this.paiXu = paiXu;
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

        public String getTitleImgUrl() {
            return titleImgUrl;
        }

        public void setTitleImgUrl(String titleImgUrl) {
            this.titleImgUrl = titleImgUrl;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class AwardsBean {
        /**
         * activeId : 35
         * awardIndex : 1
         * awardName : 有效投注1000+申请3元
         * awardType : 1
         * awardValue : 3
         * betRate : 1
         * id : 200
         */

        private int activeId;
        private int awardIndex;
        private String awardName;
        private int awardType;
        private String awardValue;
        private String betRate;
        private int id;
        private boolean ischeck;

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }

        public boolean isIscheck() {
            return ischeck;
        }

        public int getActiveId() {
            return activeId;
        }

        public void setActiveId(int activeId) {
            this.activeId = activeId;
        }

        public int getAwardIndex() {
            return awardIndex;
        }

        public void setAwardIndex(int awardIndex) {
            this.awardIndex = awardIndex;
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

        public String getAwardValue() {
            return awardValue;
        }

        public void setAwardValue(String awardValue) {
            this.awardValue = awardValue;
        }

        public String getBetRate() {
            return betRate;
        }

        public void setBetRate(String betRate) {
            this.betRate = betRate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
