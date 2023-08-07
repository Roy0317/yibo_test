package com.yibo.yiboapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestionRecordResponse {

    /**
     * set : [{"$ref":"$.list[0]"}]
     * prePage : 1
     * nextPage : 1
     * start : 0
     * pageSize : 20
     * totalPageCount : 1
     * hasNext : false
     * list : [{"sendId":94063,"createTime":"2020-09-07 14:52:12","sendType":1,"sendAccount":"anuo2","id":62,"content":"feedback testtttttt tttt","finalTime":"2020-09-07 14:52:12","stationId":270,"status":1}]
     * totalCount : 1
     * hasPre : false
     * results : [{"$ref":"$.list[0]"}]
     * currentPageNo : 1
     */
    private int prePage;
    private int nextPage;
    private int start;
    private int pageSize;
    private int totalPageCount;
    private boolean hasNext;
    @SerializedName("list") private List<SuggestionRecord> records;
    private int totalCount;
    private boolean hasPre;
    private int currentPageNo;


    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setRecords(List<SuggestionRecord> records) {
        this.records = records;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }


    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public int getPrePage() {
        return prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getStart() {
        return start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public List<SuggestionRecord> getRecords() {
        return records;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public static class SuggestionRecord {
        /**
         * sendId : 94063
         * createTime : 2020-09-07 14:52:12
         * sendType : 1
         * sendAccount : anuo2
         * id : 62
         * content : feedback testtttttt tttt
         * finalTime : 2020-09-07 14:52:12
         * stationId : 270
         * status : 1
         */
        private int sendId;
        private String createTime;
        private int sendType;
        private String sendAccount;
        private int id;
        private String content;
        private String finalTime;
        private int stationId;
        private int status;

        public void setSendId(int sendId) {
            this.sendId = sendId;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setSendType(int sendType) {
            this.sendType = sendType;
        }

        public void setSendAccount(String sendAccount) {
            this.sendAccount = sendAccount;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setFinalTime(String finalTime) {
            this.finalTime = finalTime;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSendId() {
            return sendId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getSendType() {
            return sendType;
        }

        public String getSendAccount() {
            return sendAccount;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getFinalTime() {
            return finalTime;
        }

        public int getStationId() {
            return stationId;
        }

        public int getStatus() {
            return status;
        }
    }
}
