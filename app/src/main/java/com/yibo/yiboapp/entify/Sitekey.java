package com.yibo.yiboapp.entify;

/**
 * {"siteKey":"6Lc76KAfAAAAABWQO9jRF0j8kahaXzB8Nphr2JD6","success":true,"googleRobotOnOff":true}
 */
public class Sitekey {
    String siteKey;
    boolean success;
    boolean googleRobotOnOff;

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isGoogleRobotOnOff() {
        return googleRobotOnOff;
    }

    public void setGoogleRobotOnOff(boolean googleRobotOnOff) {
        this.googleRobotOnOff = googleRobotOnOff;
    }
}
