package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/18.
 */

public class PickBankAccount {
    String userName = "";//提款人
    String bankName = "";//提款银行
    String bankAddress = "";//开户行
    String cardNo = "";//卡号
    String repPwd = "";//提款密码

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRepPwd() {
        return repPwd;
    }

    public void setRepPwd(String repPwd) {
        this.repPwd = repPwd;
    }
}
