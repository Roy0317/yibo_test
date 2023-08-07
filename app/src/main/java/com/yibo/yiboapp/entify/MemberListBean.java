package com.yibo.yiboapp.entify;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Author: Ray
 * created on 2018/10/17
 * description :用户列表
 */
public class MemberListBean {


    /**
     * success : true
     * accessToken : fd62612b-c768-4def-ae02-3e2ea5442c05
     * content : {"currentPageNo":1,"hasNext":false,"hasPre":false,"list":[{"createDatetime":1539614075328,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1617,"account":"123456q","sportScale":2},{"createDatetime":1539611351208,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1616,"account":"qqq1234","sportScale":2},{"createDatetime":1539611302423,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1615,"account":"123123a","sportScale":2},{"createDatetime":1539611214641,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1614,"account":"qqq12345","sportScale":2},{"createDatetime":1539611077957,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1613,"account":"qqq123","sportScale":2},{"createDatetime":1539610870611,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1609,"account":"qqq123456","sportScale":2},{"createDatetime":1539610767366,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1607,"account":"111qqqqqq","sportScale":2},{"createDatetime":1539610741226,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1606,"account":"111qqqq","sportScale":2},{"createDatetime":1539607887630,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1604,"account":"12312321321321321","sportScale":2},{"createDatetime":1539607879115,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1603,"account":"1rewrew","sportScale":2},{"createDatetime":1539607861561,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1602,"account":"11111","sportScale":2},{"createDatetime":1539607850288,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1601,"account":"3213213","sportScale":2},{"createDatetime":1539606545688,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1600,"account":"testray2","sportScale":2},{"createDatetime":1539606343609,"accountStatus":2,"lastLoginDatetime":1539608255000,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1599,"account":"testray1","sportScale":2}],"nextPage":1,"pageSize":20,"prePage":1,"results":[{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[1]"},{"$ref":"$.content.list[2]"},{"$ref":"$.content.list[3]"},{"$ref":"$.content.list[4]"},{"$ref":"$.content.list[5]"},{"$ref":"$.content.list[6]"},{"$ref":"$.content.list[7]"},{"$ref":"$.content.list[8]"},{"$ref":"$.content.list[9]"},{"$ref":"$.content.list[10]"},{"$ref":"$.content.list[11]"},{"$ref":"$.content.list[12]"},{"$ref":"$.content.list[13]"}],"set":[{"$ref":"$.content.list[13]"},{"$ref":"$.content.list[11]"},{"$ref":"$.content.list[3]"},{"$ref":"$.content.list[10]"},{"$ref":"$.content.list[2]"},{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[6]"},{"$ref":"$.content.list[5]"},{"$ref":"$.content.list[8]"},{"$ref":"$.content.list[7]"},{"$ref":"$.content.list[9]"},{"$ref":"$.content.list[12]"},{"$ref":"$.content.list[1]"},{"$ref":"$.content.list[4]"}],"start":0,"totalCount":14,"totalPageCount":1}
     */

    private boolean success;
    private String accessToken;
    private ContentBean content;
    @Nullable
    String msg;
    @Nullable
    int code;

    @Nullable
    public String getMsg() {
        return msg;
    }

    public void setMsg(@Nullable String msg) {
        this.msg = msg;
    }

    @Nullable
    public int getCode() {
        return code;
    }

    public void setCode(@Nullable int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * currentPageNo : 1
         * hasNext : false
         * hasPre : false
         * list : [{"createDatetime":1539614075328,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1617,"account":"123456q","sportScale":2},{"createDatetime":1539611351208,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1616,"account":"qqq1234","sportScale":2},{"createDatetime":1539611302423,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1615,"account":"123123a","sportScale":2},{"createDatetime":1539611214641,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1614,"account":"qqq12345","sportScale":2},{"createDatetime":1539611077957,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1613,"account":"qqq123","sportScale":2},{"createDatetime":1539610870611,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1609,"account":"qqq123456","sportScale":2},{"createDatetime":1539610767366,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1607,"account":"111qqqqqq","sportScale":2},{"createDatetime":1539610741226,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1606,"account":"111qqqq","sportScale":2},{"createDatetime":1539607887630,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1604,"account":"12312321321321321","sportScale":2},{"createDatetime":1539607879115,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1603,"account":"1rewrew","sportScale":2},{"createDatetime":1539607861561,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1602,"account":"11111","sportScale":2},{"createDatetime":1539607850288,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1601,"account":"3213213","sportScale":2},{"createDatetime":1539606545688,"accountStatus":2,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1600,"account":"testray2","sportScale":2},{"createDatetime":1539606343609,"accountStatus":2,"lastLoginDatetime":1539608255000,"money":0,"realScale":2,"shabaSportScale":2,"egameScale":2,"accountType":1,"cpScale":2,"id":1599,"account":"testray1","sportScale":2}]
         * nextPage : 1
         * pageSize : 20
         * prePage : 1
         * results : [{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[1]"},{"$ref":"$.content.list[2]"},{"$ref":"$.content.list[3]"},{"$ref":"$.content.list[4]"},{"$ref":"$.content.list[5]"},{"$ref":"$.content.list[6]"},{"$ref":"$.content.list[7]"},{"$ref":"$.content.list[8]"},{"$ref":"$.content.list[9]"},{"$ref":"$.content.list[10]"},{"$ref":"$.content.list[11]"},{"$ref":"$.content.list[12]"},{"$ref":"$.content.list[13]"}]
         * set : [{"$ref":"$.content.list[13]"},{"$ref":"$.content.list[11]"},{"$ref":"$.content.list[3]"},{"$ref":"$.content.list[10]"},{"$ref":"$.content.list[2]"},{"$ref":"$.content.list[0]"},{"$ref":"$.content.list[6]"},{"$ref":"$.content.list[5]"},{"$ref":"$.content.list[8]"},{"$ref":"$.content.list[7]"},{"$ref":"$.content.list[9]"},{"$ref":"$.content.list[12]"},{"$ref":"$.content.list[1]"},{"$ref":"$.content.list[4]"}]
         * start : 0
         * totalCount : 14
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
             * createDatetime : 1539614075328
             * accountStatus : 2
             * money : 0
             * realScale : 2
             * shabaSportScale : 2
             * egameScale : 2
             * accountType : 1
             * cpScale : 2
             * id : 1617
             * account : 123456q
             * sportScale : 2
             * lastLoginDatetime : 1539608255000
             */

            private long createDatetime;
            private float accountStatus;
            private float money;
            private float realScale;
            private float shabaSportScale;
            private float egameScale;
            private float accountType;
            private float cpScale;
            private float id;
            private String account;
            private float sportScale;
            private long lastLoginDatetime;

            public long getCreateDatetime() {
                return createDatetime;
            }

            public void setCreateDatetime(long createDatetime) {
                this.createDatetime = createDatetime;
            }

            public float getAccountStatus() {
                return accountStatus;
            }

            public void setAccountStatus(int accountStatus) {
                this.accountStatus = accountStatus;
            }

            public float getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public float getRealScale() {
                return realScale;
            }

            public void setRealScale(int realScale) {
                this.realScale = realScale;
            }

            public float getShabaSportScale() {
                return shabaSportScale;
            }

            public void setShabaSportScale(int shabaSportScale) {
                this.shabaSportScale = shabaSportScale;
            }

            public float getEgameScale() {
                return egameScale;
            }

            public void setEgameScale(int egameScale) {
                this.egameScale = egameScale;
            }

            public float getAccountType() {
                return accountType;
            }

            public void setAccountType(int accountType) {
                this.accountType = accountType;
            }

            public float getCpScale() {
                return cpScale;
            }

            public void setCpScale(int cpScale) {
                this.cpScale = cpScale;
            }

            public float getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public float getSportScale() {
                return sportScale;
            }

            public void setSportScale(int sportScale) {
                this.sportScale = sportScale;
            }

            public long getLastLoginDatetime() {
                return lastLoginDatetime;
            }

            public void setLastLoginDatetime(long lastLoginDatetime) {
                this.lastLoginDatetime = lastLoginDatetime;
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
             * $ref : $.content.list[13]
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
}
