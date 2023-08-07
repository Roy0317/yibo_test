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
public class WebSiteNotificationBean {


    /**
     * code : 11
     * data : {"push":{"code":13,"content":"<spspan>","cp":true,"id":122,"index":true,"modelStatus":2,"mutil":true,"overTime":1540915200000,"recharge":false,"reg":true,"stationId":24,"status":2,"title":"我们要飞了","updateTime":1509465600000}}
     * sort : 2
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
         * push : {"code":13,"content":"<spspan>","cp":true,"id":122,"index":true,"modelStatus":2,"mutil":true,"overTime":1540915200000,"recharge":false,"reg":true,"stationId":24,"status":2,"title":"我们要飞了","updateTime":1509465600000}
         */

        private PushBean push;

        public PushBean getPush() {
            return push;
        }

        public void setPush(PushBean push) {
            this.push = push;
        }

        public static class PushBean {
            /**
             * code : 13
             * content : <spspan>
             * cp : true
             * id : 122
             * index : true
             * modelStatus : 2
             * mutil : true
             * overTime : 1540915200000
             * recharge : false
             * reg : true
             * stationId : 24
             * status : 2
             * title : 我们要飞了
             * updateTime : 1509465600000
             */

            private int code;
            private String content;
            private boolean cp;
            private int id;
            private boolean index;
            private int modelStatus;
            private boolean mutil;
            private long overTime;
            private boolean recharge;
            private boolean reg;
            private int stationId;
            private int status;
            private String title;
            private long updateTime;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isCp() {
                return cp;
            }

            public void setCp(boolean cp) {
                this.cp = cp;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public boolean isIndex() {
                return index;
            }

            public void setIndex(boolean index) {
                this.index = index;
            }

            public int getModelStatus() {
                return modelStatus;
            }

            public void setModelStatus(int modelStatus) {
                this.modelStatus = modelStatus;
            }

            public boolean isMutil() {
                return mutil;
            }

            public void setMutil(boolean mutil) {
                this.mutil = mutil;
            }

            public long getOverTime() {
                return overTime;
            }

            public void setOverTime(long overTime) {
                this.overTime = overTime;
            }

            public boolean isRecharge() {
                return recharge;
            }

            public void setRecharge(boolean recharge) {
                this.recharge = recharge;
            }

            public boolean isReg() {
                return reg;
            }

            public void setReg(boolean reg) {
                this.reg = reg;
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

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
