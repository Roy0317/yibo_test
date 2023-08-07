package com.yibo.yiboapp.entify;

import java.util.List;

public class PointExchangeReocrdWraper {


    /**
     * currentPageNo : 1
     * hasNext : false
     * hasPre : false
     * list : [{"createDatetime":1581422314862,"score":-200,"accountId":608,"afterScore":1380,"remark":"积分兑换现金","id":44,"type":4,"account":"test900","beforeScore":1580,"stationId":23},{"createDatetime":1581422311329,"score":-20,"accountId":608,"afterScore":1580,"remark":"积分兑换现金","id":43,"type":4,"account":"test900","beforeScore":1600,"stationId":23},{"createDatetime":1581421551237,"score":140,"accountId":608,"afterScore":1600,"remark":"现金兑换积分","id":42,"type":3,"account":"test900","beforeScore":1460,"stationId":23},{"createDatetime":1581421548947,"score":130,"accountId":608,"afterScore":1460,"remark":"现金兑换积分","id":41,"type":3,"account":"test900","beforeScore":1330,"stationId":23},{"createDatetime":1581421545981,"score":120,"accountId":608,"afterScore":1330,"remark":"现金兑换积分","id":40,"type":3,"account":"test900","beforeScore":1210,"stationId":23},{"createDatetime":1581421544083,"score":10,"accountId":608,"afterScore":1210,"remark":"现金兑换积分","id":39,"type":3,"account":"test900","beforeScore":1200,"stationId":23},{"createDatetime":1581410164554,"score":990,"accountId":608,"afterScore":1200,"remark":"现金兑换积分","id":38,"type":3,"account":"test900","beforeScore":210,"stationId":23},{"createDatetime":1581410152321,"score":110,"accountId":608,"afterScore":210,"remark":"现金兑换积分","id":37,"type":3,"account":"test900","beforeScore":100,"stationId":23},{"createDatetime":1581410072586,"score":100,"accountId":608,"afterScore":100,"remark":"现金兑换积分","id":36,"type":3,"account":"test900","beforeScore":0,"stationId":23}]
     * nextPage : 1
     * pageSize : 20
     * prePage : 1
     * results : [{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[1]"},{"$ref":"$.content.list[2]"},{"$ref":"$.content.list[3]"},{"$ref":"$.content.list[4]"},{"$ref":"$.content.list[5]"},{"$ref":"$.content.list[6]"},{"$ref":"$.content.list[7]"},{"$ref":"$.content.list[8]"}]
     * set : [{"$ref":"$.content.list[1]"},{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[5]"},{"$ref":"$.content.list[6]"},{"$ref":"$.content.list[2]"},{"$ref":"$.content.list[8]"},{"$ref":"$.content.list[7]"},{"$ref":"$.content.list[4]"},{"$ref":"$.content.list[3]"}]
     * start : 0
     * totalCount : 9
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
         * createDatetime : 1581422314862
         * score : -200
         * accountId : 608
         * afterScore : 1380
         * remark : 积分兑换现金
         * id : 44
         * type : 4
         * account : test900
         * beforeScore : 1580
         * stationId : 23
         */

        private long createDatetime;
        private int score;
        private int accountId;
        private int afterScore;
        private String remark;
        private int id;
        private int type;
        private String account;
        private int beforeScore;
        private int stationId;

        public long getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(long createDatetime) {
            this.createDatetime = createDatetime;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public int getAfterScore() {
            return afterScore;
        }

        public void setAfterScore(int afterScore) {
            this.afterScore = afterScore;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getBeforeScore() {
            return beforeScore;
        }

        public void setBeforeScore(int beforeScore) {
            this.beforeScore = beforeScore;
        }

        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
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
         * $ref : $.content.list[1]
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
