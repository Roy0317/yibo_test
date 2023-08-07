package com.yibo.yiboapp.entify;

public class CheckUpdatePasswordResponse {

    private boolean success = false;
    private boolean needUpgrade = false;
    private boolean need_update_member_login_password = false;
    private String msg;
    private int code;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(boolean needUpgrade) {
        this.needUpgrade = needUpgrade;
    }

    public boolean isNeed_update_member_login_password() {
        return need_update_member_login_password;
    }

    public void setNeed_update_member_login_password(boolean need_update_member_login_password) {
        this.need_update_member_login_password = need_update_member_login_password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
