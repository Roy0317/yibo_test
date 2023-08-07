package com.yibo.yiboapp.entify;

import java.util.List;

public class SBSportOrder {
    public static final Integer mix_1 = 1;
    public static final Integer mix_2 = 2;
    public static final Integer mix_3 = 3;

    private String transId;	//注单号
    private String account;	//平台会员号
    private String leagueName;	//联盟名称
    private String homeName;	//主队名称
    private String awayName;	//客队名称
    private String sportTypeName;	//投注种类名称
    private String betTypeName;	//下注类型名称
    private float betOdds;	//注单赔率
    private float accountBetMoney;	//会员投注金额
    private long transactionTime;	//投注时间
    /**
     * Half WON,Half LOSE,WON,LOSE,VOID==>已结算
     * running,Waiting ==>未结算
     * DRAW,Reject,Refund ==>已失效
     */
    private String ticketStatus;	//	注单状态  ※Half WON/Half LOSE/WON/LOSE/VOID/running/DRAW/Reject/Refund/waiting
    private Double homeHdp;	//主队让球
    private Double awayHdp;	//客队让球;
    private Double hdp;	//让球
    private String betTeamName;	//下注类型
    private String betOddsTypeName;	//赔率类型名称
    private float winLostMoney;	//中奖金额
    private long matchTime;	//比赛开球时间
    private String htResultTeam;	//半场比分
    private String resultTeam;	//比赛结果

    private String liveResultTeam;	//滚球比分

    private Integer isLive;	//是否是滚球


    private String parlayId;	//混合过关Id

    private long bjTime;	//北京时间
    /**
     * 混合--子单集合
     */
    private List<SBSportOrder> childrens;
    /**
     * 1，单注，2混合注单-主单，3混合注单-子单
     */
    private Integer mix;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public String getSportTypeName() {
        return sportTypeName;
    }

    public void setSportTypeName(String sportTypeName) {
        this.sportTypeName = sportTypeName;
    }

    public String getBetTypeName() {
        return betTypeName;
    }

    public void setBetTypeName(String betTypeName) {
        this.betTypeName = betTypeName;
    }

    public float getBetOdds() {
        return betOdds;
    }

    public void setBetOdds(float betOdds) {
        this.betOdds = betOdds;
    }

    public float getAccountBetMoney() {
        return accountBetMoney;
    }

    public void setAccountBetMoney(float accountBetMoney) {
        this.accountBetMoney = accountBetMoney;
    }

    public long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Double getHomeHdp() {
        return homeHdp;
    }

    public void setHomeHdp(Double homeHdp) {
        this.homeHdp = homeHdp;
    }

    public Double getAwayHdp() {
        return awayHdp;
    }

    public void setAwayHdp(Double awayHdp) {
        this.awayHdp = awayHdp;
    }

    public Double getHdp() {
        return hdp;
    }

    public void setHdp(Double hdp) {
        this.hdp = hdp;
    }

    public String getBetTeamName() {
        return betTeamName;
    }

    public void setBetTeamName(String betTeamName) {
        this.betTeamName = betTeamName;
    }

    public String getBetOddsTypeName() {
        return betOddsTypeName;
    }

    public void setBetOddsTypeName(String betOddsTypeName) {
        this.betOddsTypeName = betOddsTypeName;
    }

    public float getWinLostMoney() {
        return winLostMoney;
    }

    public void setWinLostMoney(float winLostMoney) {
        this.winLostMoney = winLostMoney;
    }

    public long getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(long matchTime) {
        this.matchTime = matchTime;
    }

    public String getHtResultTeam() {
        return htResultTeam;
    }

    public void setHtResultTeam(String htResultTeam) {
        this.htResultTeam = htResultTeam;
    }

    public String getResultTeam() {
        return resultTeam;
    }

    public void setResultTeam(String resultTeam) {
        this.resultTeam = resultTeam;
    }

    public String getLiveResultTeam() {
        return liveResultTeam;
    }

    public void setLiveResultTeam(String liveResultTeam) {
        this.liveResultTeam = liveResultTeam;
    }

    public Integer getIsLive() {
        return isLive;
    }

    public void setIsLive(Integer isLive) {
        this.isLive = isLive;
    }

    public String getParlayId() {
        return parlayId;
    }

    public void setParlayId(String parlayId) {
        this.parlayId = parlayId;
    }

    public long getBjTime() {
        return bjTime;
    }

    public void setBjTime(long bjTime) {
        this.bjTime = bjTime;
    }

    public List<SBSportOrder> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<SBSportOrder> childrens) {
        this.childrens = childrens;
    }

    public Integer getMix() {
        return mix;
    }

    public void setMix(Integer mix) {
        this.mix = mix;
    }
}
