package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * 投注球玩法信息
 * 需要展示最全数据，包括位数，功能VIEW的选择情况
 * @author johnson
 */
public class BallonRules {

    String playCode;
    String ruleCode;
    String nums;//号码串
    String postNums;//投注时提交的号码串
    boolean showWeiShuView;//是否显示位数view
    boolean showFuncView;//是否显示便捷筛选号码view
    String ruleTxt;//玩法显示文字
    List<BallListItemInfo> weishuInfo;
    List<BallListItemInfo> funcInfo;
    List<BallListItemInfo> ballonsInfo;

    public List<BallListItemInfo> getBallonsInfo() {
        return ballonsInfo;
    }

    public void setBallonsInfo(List<BallListItemInfo> ballonsInfo) {
        this.ballonsInfo = ballonsInfo;
    }

    public List<BallListItemInfo> getWeishuInfo() {
        return weishuInfo;
    }

    public void setWeishuInfo(List<BallListItemInfo> weishuInfo) {
        this.weishuInfo = weishuInfo;
    }

    public List<BallListItemInfo> getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(List<BallListItemInfo> funcInfo) {
        this.funcInfo = funcInfo;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getPostNums() {
        return postNums;
    }

    public void setPostNums(String postNums) {
        this.postNums = postNums;
    }

    public boolean isShowWeiShuView() {
        return showWeiShuView;
    }

    public void setShowWeiShuView(boolean showWeiShuView) {
        this.showWeiShuView = showWeiShuView;
    }

    public boolean isShowFuncView() {
        return showFuncView;
    }

    public void setShowFuncView(boolean showFuncView) {
        this.showFuncView = showFuncView;
    }

    public String getRuleTxt() {
        return ruleTxt;
    }

    public void setRuleTxt(String ruleTxt) {
        this.ruleTxt = ruleTxt;
    }

    public String getPlayCode() {
        return playCode;
    }

    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

}
