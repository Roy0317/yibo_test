package com.example.anuo.immodule.bean;

import java.util.List;

public class ChatQuickMessageBean {


    /**
     * msg : 操作成功。
     * code : R0011
     * success : true
     * source : [{"createTime":"2019-11-18T13:13:05.303+0000","enable":1,"record":"在开车","updateTime":"2019-11-18T13:13:05.303+0000","id":"33d4a961c97245fabe1108fa99a8c892","stationId":"yjtest1_3"},{"createTime":"2019-11-18T09:07:41.784+0000","enable":1,"record":"fafafafa","updateTime":"2019-11-18T09:07:41.784+0000","id":"0bfc45f9d4d447eba01aa31400a50fb3","stationId":"yjtest1_3"},{"createTime":"2019-11-18T13:12:59.890+0000","enable":1,"record":"好了吗","updateTime":"2019-11-18T13:12:59.890+0000","id":"c37e5cca19394fdf8e6fa77504c7c3cb","stationId":"yjtest1_3"}]
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private String status;
    private List<SourceBean> source;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SourceBean> getSource() {
        return source;
    }

    public void setSource(List<SourceBean> source) {
        this.source = source;
    }

    public static class SourceBean {
        /**
         * createTime : 2019-11-18T13:13:05.303+0000
         * enable : 1
         * record : 在开车
         * updateTime : 2019-11-18T13:13:05.303+0000
         * id : 33d4a961c97245fabe1108fa99a8c892
         * stationId : yjtest1_3
         */

        private String createTime;
        private int enable;
        private String record;
        private String updateTime;
        private String id;
        private String stationId;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public String getRecord() {
            return record;
        }

        public void setRecord(String record) {
            this.record = record;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }
    }
}
