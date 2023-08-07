package com.yibo.yiboapp.data;

public class VerifyCheckData {

    /**
     * code : verify_code_error
     * success : false
     * token :
     */

    private String code;
    private boolean success;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
