package com.example.anuo.immodule.bean;

import java.util.List;

public class ChatRoomNoticeBean {


    /**
     * msg : 操作成功。
     * code : R7025
     * success : true
     * source : [{"id":"14e676586b844087bc17fdff3bcf6056","title":"我是一个超级英雄","body":"<p><span style=\"color: rgb(77, 77, 77); font-family: Verdana; font-size: 13.3333px; background-color: rgb(255, 255, 255);\">基于这3个方面，我们把Kerberos Authentication进行最大限度的简化：整个过程涉及到Client和Server，他们之间的这个Secret我们用一个Key（<\/span><span style=\"box-sizing: border-box; outline: 0px; margin: 0px; padding: 0px; font-weight: 700; overflow-wrap: break-word; color: rgb(77, 77, 77); font-family: Verdana; font-size: 13.3333px; background-color: rgb(255, 255, 255);\"><span style=\"box-sizing: border-box; outline: 0px; margin: 0px; padding: 0px; font-family: &quot;Microsoft YaHei&quot;, &quot;SF Pro Display&quot;, Roboto, Noto, Arial, &quot;PingFang SC&quot;, sans-serif; overflow-wrap: break-word; font-size: 12pt;\">K<\/span>Server-Client<\/span><span style=\"color: rgb(77, 77, 77); font-family: Verdana; font-size: 13.3333px; background-color: rgb(255, 255, 255);\">）来表示。Client为了让Server对自己进行有效的认证，向对方提供如下两组信息：<\/span><\/p>","enable":1,"updateTime":"2019-09-30T14:55:28.018+0000","createTime":"2019-09-30T14:55:28.018+0000","stationId":"yunt0Chat_2"}]
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
         * id : 14e676586b844087bc17fdff3bcf6056
         * title : 我是一个超级英雄
         * body : <p><span style="color: rgb(77, 77, 77); font-family: Verdana; font-size: 13.3333px; background-color: rgb(255, 255, 255);">基于这3个方面，我们把Kerberos Authentication进行最大限度的简化：整个过程涉及到Client和Server，他们之间的这个Secret我们用一个Key（</span><span style="box-sizing: border-box; outline: 0px; margin: 0px; padding: 0px; font-weight: 700; overflow-wrap: break-word; color: rgb(77, 77, 77); font-family: Verdana; font-size: 13.3333px; background-color: rgb(255, 255, 255);"><span style="box-sizing: border-box; outline: 0px; margin: 0px; padding: 0px; font-family: &quot;Microsoft YaHei&quot;, &quot;SF Pro Display&quot;, Roboto, Noto, Arial, &quot;PingFang SC&quot;, sans-serif; overflow-wrap: break-word; font-size: 12pt;">K</span>Server-Client</span><span style="color: rgb(77, 77, 77); font-family: Verdana; font-size: 13.3333px; background-color: rgb(255, 255, 255);">）来表示。Client为了让Server对自己进行有效的认证，向对方提供如下两组信息：</span></p>
         * enable : 1
         * updateTime : 2019-09-30T14:55:28.018+0000
         * createTime : 2019-09-30T14:55:28.018+0000
         * stationId : yunt0Chat_2
         */

        private String id;
        private String title;
        private String body;
        private int enable;
        private String updateTime;
        private String createTime;
        private String stationId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }
    }
}
