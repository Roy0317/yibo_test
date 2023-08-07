package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2018/1/26.
 */

public class BigWheelData {

    String userScore;//用户积分
    long activeId;
    List<String> colors;//大转盘颜色
    List<String> awardNames;//奖项名称集
    int costScore;//消耗积分
    String activity_rule;//活动规则
    String activity_condition;//抽奖资格
    String activity_notices;//活动声明
    private boolean activePay;

    public boolean isActivePay() {
        return activePay;
    }

    public void setActivePay(boolean activePay) {
        this.activePay = activePay;
    }

    public String getActivity_rule() {
        return activity_rule;
    }

    public void setActivity_rule(String activity_rule) {
        this.activity_rule = activity_rule;
    }

    public String getActivity_condition() {
        return activity_condition;
    }

    public void setActivity_condition(String activity_condition) {
        this.activity_condition = activity_condition;
    }

    public String getActivity_notices() {
        return activity_notices;
    }

    public void setActivity_notices(String activity_notices) {
        this.activity_notices = activity_notices;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public long getActiveId() {
        return activeId;
    }

    public void setActiveId(long activeId) {
        this.activeId = activeId;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getAwardNames() {
        return awardNames;
    }

    public void setAwardNames(List<String> awardNames) {
        this.awardNames = awardNames;
    }

    public int getCostScore() {
        return costScore;
    }

    public void setCostScore(int costScore) {
        this.costScore = costScore;
    }
}
