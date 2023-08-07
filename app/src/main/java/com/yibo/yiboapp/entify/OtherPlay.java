package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/26.
 */

public class OtherPlay {
    String title;
    String imgUrl;
    String openUrl;//点击后跳转到的链接
    int dataCode;
    float balance;
    String playCode;//游戏代号
    int isListGame;//是否有游戏列表
    String feeConvertUrl;//额度转换链接
    String forwardUrl;//跳转链接
    String gameType;//充值代号

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getPlayCode() {
        return playCode;
    }

    public String getFeeConvertUrl() {
        return feeConvertUrl;
    }

    public void setFeeConvertUrl(String feeConvertUrl) {
        this.feeConvertUrl = feeConvertUrl;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getIsListGame() {
        return isListGame;
    }

    public void setIsListGame(int isListGame) {
        this.isListGame = isListGame;
    }

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }

    public int getDataCode() {
        return dataCode;
    }

    public void setDataCode(int dataCode) {
        this.dataCode = dataCode;
    }
}
