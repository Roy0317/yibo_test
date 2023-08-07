package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2018/3/16.
 */

public class SavedGameData {

    public static final int LOT_GAME_MODULE = 0;
    public static final int SPORT_GAME_MODULE = 1;
    public static final int REALMAN_GAME_MODULE = 2;
    public static final int DIANZI_GAME_MODULE = 3;

    int gameModuleCode = LOT_GAME_MODULE;
    String user;
    String lotName;
    String lotCode;
    String lotType;
    String playName;
    String playCode;
    String subPlayName;
    String subPlayCode;
    String cpVersion;
    long duration;//开奖间隔时间

    String ballType;
    String gameType;
    String ftPlayCode;
    String bkPlayCode;

    String zhenrenImgUrl;
    String zrPlayCode;

    String dzImgUrl;
    String dzPlayCode;
    long addTime;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCpVersion() {
        return cpVersion;
    }

    public void setCpVersion(String cpVersion) {
        this.cpVersion = cpVersion;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getGameModuleCode() {
        return gameModuleCode;
    }

    public void setGameModuleCode(int gameModuleCode) {
        this.gameModuleCode = gameModuleCode;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getPlayCode() {
        return playCode;
    }

    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }

    public String getSubPlayName() {
        return subPlayName;
    }

    public void setSubPlayName(String subPlayName) {
        this.subPlayName = subPlayName;
    }

    public String getSubPlayCode() {
        return subPlayCode;
    }

    public void setSubPlayCode(String subPlayCode) {
        this.subPlayCode = subPlayCode;
    }

    public String getBallType() {
        return ballType;
    }

    public void setBallType(String ballType) {
        this.ballType = ballType;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getFtPlayCode() {
        return ftPlayCode;
    }

    public void setFtPlayCode(String ftPlayCode) {
        this.ftPlayCode = ftPlayCode;
    }

    public String getBkPlayCode() {
        return bkPlayCode;
    }

    public void setBkPlayCode(String bkPlayCode) {
        this.bkPlayCode = bkPlayCode;
    }

    public String getZhenrenImgUrl() {
        return zhenrenImgUrl;
    }

    public void setZhenrenImgUrl(String zhenrenImgUrl) {
        this.zhenrenImgUrl = zhenrenImgUrl;
    }

    public String getZrPlayCode() {
        return zrPlayCode;
    }

    public void setZrPlayCode(String zrPlayCode) {
        this.zrPlayCode = zrPlayCode;
    }

    public String getDzImgUrl() {
        return dzImgUrl;
    }

    public void setDzImgUrl(String dzImgUrl) {
        this.dzImgUrl = dzImgUrl;
    }

    public String getDzPlayCode() {
        return dzPlayCode;
    }

    public void setDzPlayCode(String dzPlayCode) {
        this.dzPlayCode = dzPlayCode;
    }
}
