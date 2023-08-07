package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/9/14.
 */

public class ChargeMethod {

    String imgurl;
    int minMoney;
    boolean isCheck;
    int type;// 0-线上，1-线下

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(int minMoney) {
        this.minMoney = minMoney;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
