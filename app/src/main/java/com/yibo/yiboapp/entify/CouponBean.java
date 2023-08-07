package com.yibo.yiboapp.entify;

import java.util.List;

public class CouponBean {


    /**
     * currentPageNo : 1
     * hasNext : false
     * hasPre : false
     * list : [{"cid":35,"couponsName":"猴子","couponsTypeStr":"平台回馈","createDatetime":1582897772184,"denomination":100,"id":225,"maxAmount":1000,"minAmount":1,"stationId":270,"status":1,"useType":3,"userId":87057,"userName":"testray"},{"cid":33,"couponsName":"2-3-2","couponsTypeStr":"平台回馈","createDatetime":1582897763044,"denomination":180,"id":224,"maxAmount":10000,"minAmount":100,"stationId":270,"status":1,"useEndDate":1582905600000,"useStartDate":1582732800000,"useType":1,"userId":87057,"userName":"testray"}]
     * nextPage : 1
     * pageSize : 20
     * prePage : 1
     * results : [{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[1]"}]
     * set : [{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[1]"}]
     * start : 0
     * totalCount : 2
     * totalPageCount : 1
     */

    private int currentPageNo;
    private boolean hasNext;
    private boolean hasPre;
    private int nextPage;
    private int pageSize;
    private int prePage;
    private int start;
    private int totalCount;
    private int totalPageCount;
    private List<ListBean> list;
    private List<ResultsBean> results;
    private List<SetBean> set;

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public List<SetBean> getSet() {
        return set;
    }

    public void setSet(List<SetBean> set) {
        this.set = set;
    }

    public static class ListBean {
        /**
         * cid : 35
         * couponsName : 猴子
         * couponsTypeStr : 平台回馈
         * createDatetime : 1582897772184
         * denomination : 100
         * id : 225
         * maxAmount : 1000
         * minAmount : 1
         * stationId : 270
         * status : 1
         * useType : 3
         * userId : 87057
         * userName : testray
         * useEndDate : 1582905600000
         * useStartDate : 1582732800000
         */

        private int cid;
        private String couponsName;
        private String couponsTypeStr;
        private long createDatetime;
        private float denomination;
        private int id;
        private int maxAmount;
        private int minAmount;
        private int stationId;
        private int status;
        private int useType;
        private int userId;
        private String userName;
        private long useEndDate;
        private long useStartDate;

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getCouponsName() {
            return couponsName;
        }

        public void setCouponsName(String couponsName) {
            this.couponsName = couponsName;
        }

        public String getCouponsTypeStr() {
            return couponsTypeStr;
        }

        public void setCouponsTypeStr(String couponsTypeStr) {
            this.couponsTypeStr = couponsTypeStr;
        }

        public long getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(long createDatetime) {
            this.createDatetime = createDatetime;
        }

        public float getDenomination() {
            return denomination;
        }

        public void setDenomination(float denomination) {
            this.denomination = denomination;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(int minAmount) {
            this.minAmount = minAmount;
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

        public int getUseType() {
            return useType;
        }

        public void setUseType(int useType) {
            this.useType = useType;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public long getUseEndDate() {
            return useEndDate;
        }

        public void setUseEndDate(long useEndDate) {
            this.useEndDate = useEndDate;
        }

        public long getUseStartDate() {
            return useStartDate;
        }

        public void setUseStartDate(long useStartDate) {
            this.useStartDate = useStartDate;
        }
    }

    public static class ResultsBean {
        /**
         * $ref : $.content.list[0]
         */

        private String $ref;

        public String get$ref() {
            return $ref;
        }

        public void set$ref(String $ref) {
            this.$ref = $ref;
        }
    }

    public static class SetBean {
        /**
         * $ref : $.content.list[0]
         */

        private String $ref;

        public String get$ref() {
            return $ref;
        }

        public void set$ref(String $ref) {
            this.$ref = $ref;
        }
    }
}
