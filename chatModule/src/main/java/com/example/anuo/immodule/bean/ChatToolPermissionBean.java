package com.example.anuo.immodule.bean;

import java.util.List;

public class ChatToolPermissionBean {


    /**
     * msg : 操作成功。
     * code : R0008
     * success : true
     * source : {"toolPermission":[{"toolCode":"R0009","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"cb168c3da75c42938d435bfc1270d403","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"禁言/解禁言"},{"toolCode":"R0102","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"ccabe6385cc84869a05aef0a932f853f","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"撤回"},{"toolCode":"R0500","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"77c2382bc2494c12beda7f244f9ecae7","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"私聊"},{"toolCode":"R0013","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"85d443134c664074b57b876974924efb","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"进入所有公共房间"},{"toolCode":"R00022","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"85eec9d5868b4375a159316bddaeacbd","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"进入所有代理房间"},{"toolCode":"R0029","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"f861b7baf65c496983f577aef97e1eff","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"全体禁言/解禁言"},{"toolCode":"R0024","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"bca71010daba4661bd3c5ca90eb49fcd","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"发消息自定义机器人& #40;仅限文本& #41;"},{"toolCode":"R0001s","createTime":"2020-01-23T06:26:47.908+0000","enable":1,"updateTime":"2020-01-23T06:26:47.908+0000","id":"13ba77f0ac9b4abba5d05bb73a604489","userId":"97f7c8d22e074c7c836a05bd4b1d7afe","toolName":"消息群发房间& #40;仅限文本& #41;"}]}
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private SourceBean source;
    private String status;

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

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class SourceBean {
        private List<ToolPermissionBean> toolPermission;

        public List<ToolPermissionBean> getToolPermission() {
            return toolPermission;
        }

        public void setToolPermission(List<ToolPermissionBean> toolPermission) {
            this.toolPermission = toolPermission;
        }

        public static class ToolPermissionBean {
            /**
             * toolCode : R0009
             * createTime : 2020-01-23T06:26:47.908+0000
             * enable : 1
             * updateTime : 2020-01-23T06:26:47.908+0000
             * id : cb168c3da75c42938d435bfc1270d403
             * userId : 97f7c8d22e074c7c836a05bd4b1d7afe
             * toolName : 禁言/解禁言
             */

            private String toolCode;
            private String createTime;
            private int enable;
            private String updateTime;
            private String id;
            private String userId;
            private String toolName;

            public String getToolCode() {
                return toolCode;
            }

            public void setToolCode(String toolCode) {
                this.toolCode = toolCode;
            }

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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getToolName() {
                return toolName;
            }

            public void setToolName(String toolName) {
                this.toolName = toolName;
            }
        }
    }
}
