package com.yibo.yiboapp.entify;

import java.util.List;

public class RechargeCardRecordBean {


    /**
     * currentPageNo : 1
     * hasNext : false
     * hasPre : false
     * list : [{"betNum":1,"cardNo":"19074737530764","cardPassWord":"***","createDatetime":1582792815067,"createUserid":0,"createUsername":"***","denomination":10,"id":1,"stationId":23,"status":2,"useDatetime":1582803237479,"useUserid":409,"useUsername":"testray"}]
     * nextPage : 1
     * pageSize : 20
     * prePage : 1
     * results : [{"$ref":"$.content.list[0]"}]
     * set : [{"$ref":"$.content.list[0]"}]
     * start : 0
     * totalCount : 1
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
         * betNum : 1
         * cardNo : 19074737530764
         * cardPassWord : ***
         * createDatetime : 1582792815067
         * createUserid : 0
         * createUsername : ***
         * denomination : 10
         * id : 1
         * stationId : 23
         * status : 2
         * useDatetime : 1582803237479
         * useUserid : 409
         * useUsername : testray
         */

        private int betNum;
        private String cardNo;
        private String cardPassWord;
        private long createDatetime;
        private int createUserid;
        private String createUsername;
        private int denomination;
        private int id;
        private int stationId;
        private int status;
        private long useDatetime;
        private int useUserid;
        private String useUsername;

        public int getBetNum() {
            return betNum;
        }

        public void setBetNum(int betNum) {
            this.betNum = betNum;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCardPassWord() {
            return cardPassWord;
        }

        public void setCardPassWord(String cardPassWord) {
            this.cardPassWord = cardPassWord;
        }

        public long getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(long createDatetime) {
            this.createDatetime = createDatetime;
        }

        public int getCreateUserid() {
            return createUserid;
        }

        public void setCreateUserid(int createUserid) {
            this.createUserid = createUserid;
        }

        public String getCreateUsername() {
            return createUsername;
        }

        public void setCreateUsername(String createUsername) {
            this.createUsername = createUsername;
        }

        public int getDenomination() {
            return denomination;
        }

        public void setDenomination(int denomination) {
            this.denomination = denomination;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public long getUseDatetime() {
            return useDatetime;
        }

        public void setUseDatetime(long useDatetime) {
            this.useDatetime = useDatetime;
        }

        public int getUseUserid() {
            return useUserid;
        }

        public void setUseUserid(int useUserid) {
            this.useUserid = useUserid;
        }

        public String getUseUsername() {
            return useUsername;
        }

        public void setUseUsername(String useUsername) {
            this.useUsername = useUsername;
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
