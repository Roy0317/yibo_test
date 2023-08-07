package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/6.
 */

public class RegisterResult {

    int accountType;//用户帐户类型模式 1-会员 6-游客，试玩帐号
    String cpVersion;//当前彩票版本号
    String account;

    public String getCpVersion() {
        return cpVersion;
    }

    public void setCpVersion(String cpVersion) {
        this.cpVersion = cpVersion;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
