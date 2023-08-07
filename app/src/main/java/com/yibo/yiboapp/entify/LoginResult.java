package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/9/25.
 */

public class LoginResult {

    int accountType;//用户模式 1-会员 6-游客
    String cpVersion;//当前游戏版本号
    String account;//当前帐号
    int accountId;//当前帐号ID


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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int account) {
        this.accountId = account;
    }
}
