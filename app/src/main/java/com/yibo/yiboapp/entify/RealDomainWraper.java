package com.yibo.yiboapp.entify;

import java.util.ArrayList;
import java.util.List;

public class RealDomainWraper {
    /**
     * success : true
     * content : [{"createDatetime":1617349551780,"domain":"https://fdakfda333333.com","id":476,"ip":"","ipWhite":"","routeType":1,"source":1,"ssl":2,"stationCode":"w000","status":2},{"createDatetime":1617349551780,"domain":"https://fdakfda333333.com","id":476,"ip":"","ipWhite":"","routeType":1,"source":1,"ssl":2,"stationCode":"w000","status":2},{"createDatetime":1617349450907,"domain":"https://78ioidsc.com","id":475,"ip":"","ipWhite":"","routeType":0,"source":1,"ssl":2,"stationCode":"w000","status":2},{"createDatetime":1617190713608,"domain":"skyw000t0.yb876.com","id":27,"ip":"https://103.68.174.100","ipWhite":"","routeType":1,"source":1,"ssl":2,"stationCode":"w000","status":2}]
     */

    private boolean success;
    private ArrayList<ContentBean> content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<ContentBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * createDatetime : 1617349551780
         * domain : https://fdakfda333333.com
         * id : 476
         * ip :
         * ipWhite :
         * routeType : 1
         * source : 1
         * ssl : 2
         * stationCode : w000
         * status : 2
         */

        private long createDatetime;
        private String domain;
        private int id;
        private String ip;
        private String ipWhite;
        private int routeType;
        private int source;
        private int ssl;
        private String stationCode;
        private int status;
        private long delay;
        private boolean choosed = false;
        /**
         * name : 节能线路56
         * remark : 易速微信支付 扫码后订单生效
         * sortNo : 0
         */

        private String name;
        private String remark;
        private int sortNo;

        public long getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(long createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
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

        public String getIpWhite() {
            return ipWhite;
        }

        public void setIpWhite(String ipWhite) {
            this.ipWhite = ipWhite;
        }

        public int getRouteType() {
            return routeType;
        }

        public void setRouteType(int routeType) {
            this.routeType = routeType;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getSsl() {
            return ssl;
        }

        public void setSsl(int ssl) {
            this.ssl = ssl;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public boolean isChoosed() {
            return choosed;
        }

        public void setChoosed(boolean choosed) {
            this.choosed = choosed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getSortNo() {
            return sortNo;
        }

        public void setSortNo(int sortNo) {
            this.sortNo = sortNo;
        }
    }
}
