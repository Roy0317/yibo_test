package com.example.anuo.immodule.jsonmodel;

import com.google.gson.annotations.SerializedName;

public class UploadAvatarToDBResponse {


    /**
     * msg : 操作成功。
     * code : R7020
     * success : true
     * source : {"avatar":"https://testfile.yibochat.com/read_file/2009cc91de77c5d842eea1034859659e376c"}
     * status : b1
     */
    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private String code;
    @SerializedName("success")
    private boolean success;
    @SerializedName("source")
    private SourceEntity source;
    @SerializedName("status")
    private String status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setSource(SourceEntity source) {
        this.source = source;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }

    public SourceEntity getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public class SourceEntity {
        /**
         * avatar : https://testfile.yibochat.com/read_file/2009cc91de77c5d842eea1034859659e376c
         */
        @SerializedName("avatar")
        private String avatar;

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
