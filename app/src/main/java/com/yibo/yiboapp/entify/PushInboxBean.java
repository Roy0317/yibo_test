package com.yibo.yiboapp.entify;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.entify
 * @description: ${DESP}
 * @date: 2019/2/18
 * @time: 3:44 PM
 */
public class PushInboxBean {

    /**
     * code : 10
     * data : {"count":22,"push":{"createDatetime":1548417290491,"id":69,"message":"恭喜，您充值订单2019012519504500002已审核通过。","title":"恭喜，您的充值记录已批准","type":1,"userMessageId":10450,"stationId":24,"status":1}}
     * sort : 1
     */

    private int code;
    private DataBean data;
    private int sort;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public static class DataBean {
        /**
         * count : 22
         * push : {"createDatetime":1548417290491,"id":69,"message":"恭喜，您充值订单2019012519504500002已审核通过。","title":"恭喜，您的充值记录已批准","type":1,"userMessageId":10450,"stationId":24,"status":1}
         */

        private int count;
        private PushBean push;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public PushBean getPush() {
            return push;
        }

        public void setPush(PushBean push) {
            this.push = push;
        }

        public static class PushBean {
            /**
             * createDatetime : 1548417290491
             * id : 69
             * message : 恭喜，您充值订单2019012519504500002已审核通过。
             * title : 恭喜，您的充值记录已批准
             * type : 1
             * userMessageId : 10450
             * stationId : 24
             * status : 1
             */

            private long createDatetime;
            private int id;
            private String message;
            private String title;
            private int type;
            private int userMessageId;
            private int stationId;
            private int status;

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

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserMessageId() {
                return userMessageId;
            }

            public void setUserMessageId(int userMessageId) {
                this.userMessageId = userMessageId;
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
        }
    }
}
