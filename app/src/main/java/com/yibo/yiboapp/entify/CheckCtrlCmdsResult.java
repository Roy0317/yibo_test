package com.yibo.yiboapp.entify;

import java.io.Serializable;
import java.util.List;

public class CheckCtrlCmdsResult implements Serializable {

    /**
     * success : true
     * content : [{"content":"https://www.google.com","createTime":1615861984245,"deadlineTime":1615897977000,"id":23,"interval":10,"stationCode":"d401","status":2,"taskNo":"kiDQLVzvC43dIHV","type":3},{"content":"330943.com","createTime":1615862028946,"deadlineTime":1615897997000,"id":24,"interval":20,"stationCode":"d402","status":2,"taskNo":"WusGp5ak2AadOUh","type":1},{"content":"433243.com","createTime":1615862056389,"deadlineTime":1615898049000,"id":25,"interval":30,"stationCode":"d403","status":2,"taskNo":"Ret6GBP46S5VHo2","type":2}]
     */

    private Boolean success;
    private List<CmdsBean> content;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<CmdsBean> getContent() {
        return content;
    }

    public void setContent(List<CmdsBean> content) {
        this.content = content;
    }

    public static class CmdsBean implements Serializable{
        /**
         * content : https://www.google.com
         * clientIp : 112.198.218.192
         * createTime : 1615861984245
         * deadlineTime : 1615897977000
         * id : 23
         * interval : 10
         * stationCode : d401
         * status : 2
         * taskNo : kiDQLVzvC43dIHV
         * type : 3
         */

        private String clientIp;
        private String content;
        private Long createTime;
        private Long deadlineTime;
        private Integer id;
        private Integer interval;
        private String stationCode;
        private Integer status;
        private String taskNo;
        private Integer type;

        public String getClientIp() {
            return clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getDeadlineTime() {
            return deadlineTime;
        }

        public void setDeadlineTime(Long deadlineTime) {
            this.deadlineTime = deadlineTime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getInterval() {
            return interval;
        }

        public void setInterval(Integer interval) {
            this.interval = interval;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getTaskNo() {
            return taskNo;
        }

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

    }
}
