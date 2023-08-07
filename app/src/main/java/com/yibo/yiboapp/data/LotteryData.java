package com.yibo.yiboapp.data;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.yibo.yiboapp.entify.PlayItem;

import java.util.List;

/**
 * Created by johnson on 2017/9/16.
 */

public class LotteryData {
    String czCode;//彩票类型代号
    String name;
    int ballonNums;
    long duration;
    boolean isSys;
    String pinLv;
    String gameType;
    String forwardUrl;
    String forwardAction;
    int isListGame;

    /**
     * 状态，1=关闭，2=开启
     */
    int status;
    String code;//彩票编码
    List<PlayItem> rules;

    String imgUrl;//体育，真人，电子的图标相对地址
    int moduleCode = 3;//彩票，体育，真人，电子模块代号,3代表彩票
    boolean isSelect = false;
    private String lotteryIcon;//彩票图标地址键入消息
    private String dynamicIcon;//彩种图标动态效果

    public static final int SPORT_MODULE = 0;
    public static final int REALMAN_MODULE = 1;
    public static final int DIANZI_MODULE = 2;
    public static final int CAIPIAO_MODULE = 3;
    public static final int CHESS_MODULE = 4;
    public static final int ESPORT_MODULE = 6;
    public static final int BUYU_MODULE = 7;

    /**
     * 开奖时间跟封盘时间差，单位秒
     */
    private Integer ago;

    public LotteryData() {
    }

    protected LotteryData(Parcel in) {
        czCode = in.readString();
        name = in.readString();
        ballonNums = in.readInt();
        duration = in.readLong();
        isSys = in.readByte() != 0;
        pinLv = in.readString();
        status = in.readInt();
        code = in.readString();
        imgUrl = in.readString();
        moduleCode = in.readInt();


        if (in.readByte() == 0) {
            ago = null;
        } else {
            ago = in.readInt();
        }
    }

    public String getForwardAction() {

        return forwardAction;
    }

    public void setForwardAction(String forwardAction) {
        this.forwardAction = forwardAction;
    }

    public String getLotteryIcon() {
        return lotteryIcon;
    }

    public void setLotteryIcon(String lotteryIcon) {
        this.lotteryIcon = lotteryIcon;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public int getIsListGame() {
        return isListGame;
    }

    public void setIsListGame(int isListGame) {
        this.isListGame = isListGame;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Integer getAgo() {
        return ago;
    }

    public void setAgo(Integer ago) {
        this.ago = ago;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCzCode() {
        return czCode;
    }

    public void setCzCode(String czCode) {
        this.czCode = czCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBallonNums() {
        return ballonNums;
    }

    public void setBallonNums(int ballonNums) {
        this.ballonNums = ballonNums;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    //    public int getQiShu() {
//        return qiShu;
//    }
//
//    public void setQiShu(int qiShu) {
//        this.qiShu = qiShu;
//    }

    public boolean isSys() {
        return isSys;
    }

    public void setSys(boolean sys) {
        isSys = sys;
    }

    public String getPinLv() {
        return pinLv;
    }

    public void setPinLv(String pinLv) {
        this.pinLv = pinLv;
    }

    public List<PlayItem> getRules() {
        return rules;
    }

    public void setRules(List<PlayItem> rules) {
        this.rules = rules;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getDynamicIcon() {
        return dynamicIcon;
    }

    public void setDynamicIcon(String dynamicIcon) {
        this.dynamicIcon = dynamicIcon;
    }

    @Override
    public String toString() {
        return "LotteryData{" +
                "czCode='" + czCode + '\'' +
                ", name='" + name + '\'' +
                ", forwardUrl='" + forwardUrl + '\'' +
                ", forwardAction='" + forwardAction + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
