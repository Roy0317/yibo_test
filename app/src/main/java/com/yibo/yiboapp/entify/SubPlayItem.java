package com.yibo.yiboapp.entify;

public class SubPlayItem {

    String name;
    String code;
    boolean isActivated = false;
    int randomCount;
    int lotType;//彩种类型
    int status;//开关状态
    String detailDesc;//详细介绍
    String winExample;//中奖示例
    String playMethod;//玩法介绍
    long palyId;

    float maxBounsOdds;//最大中奖金额
    float minBonusOdds;//最小中奖金额
    float minRakeback;//最低返水
    int maxNumber;//最高注数
    float maxRakeback;//最高返水

    int maxBetNum;//maxBetNum最大投注

    public int getMaxBetNum() {
        return maxBetNum;
    }

    public void setMaxBetNum(int maxBetNum) {
        this.maxBetNum = maxBetNum;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
    public float getMaxBounsOdds() {
        return maxBounsOdds;
    }

    public void setMaxBounsOdds(float maxBounsOdds) {
        this.maxBounsOdds = maxBounsOdds;
    }

    public float getMinBonusOdds() {
        return minBonusOdds;
    }

    public void setMinBonusOdds(float minBonusOdds) {
        this.minBonusOdds = minBonusOdds;
    }

    public float getMinRakeback() {
        return minRakeback;
    }

    public void setMinRakeback(float minRakeback) {
        this.minRakeback = minRakeback;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public float getMaxRakeback() {
        return maxRakeback;
    }

    public void setMaxRakeback(float maxRakeback) {
        this.maxRakeback = maxRakeback;
    }

    public int getLotType() {
        return lotType;
    }

    public void setLotType(int lotType) {
        this.lotType = lotType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getWinExample() {
        return winExample;
    }

    public void setWinExample(String winExample) {
        this.winExample = winExample;
    }

    public String getPlayMethod() {
        return playMethod;
    }

    public void setPlayMethod(String playMethod) {
        this.playMethod = playMethod;
    }

    public long getPalyId() {
        return palyId;
    }

    public void setPalyId(long palyId) {
        this.palyId = palyId;
    }

    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
