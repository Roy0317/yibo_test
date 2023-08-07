package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/28.
 */

public class SportBean {
    String gid;//赛事ID
    String txt;//显示结果
    String peilv;//显示赔率
    String peilvKey;//赔率KEY
    String mid;//matchId
    boolean fakeItem;//占位显示项
    boolean isHeader;//是否不及格头部
    String project;//結果內容

    String lianSaiName;//联赛名称
    String teamNames;//球队名称
    String gameCategoryName;//比赛类型，全场-波胆

    String scores;//两队比分
    String gameRealTime;//比赛进行到的时间，针对足球
    String halfName;//上半场，下半场，针对足球
    String nowSession;//当前比赛节数，OT代表超时，针对篮球

    String scoreH;//主队比分值，针对足球滚球
    String scoreC;//客队比分值，针对足球滚球
    String home;
    String client;
    String betTeamName;//下注时提示的球队名称(主队，客队，和局),波胆时值为比分值

    public String getBetTeamName() {
        return betTeamName;
    }

    public void setBetTeamName(String betTeamName) {
        this.betTeamName = betTeamName;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getScoreH() {
        return scoreH;
    }

    public void setScoreH(String scoreH) {
        this.scoreH = scoreH;
    }

    public String getScoreC() {
        return scoreC;
    }

    public void setScoreC(String scoreC) {
        this.scoreC = scoreC;
    }

    public String getNowSession() {
        return nowSession;
    }

    public void setNowSession(String nowSession) {
        this.nowSession = nowSession;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public String getGameRealTime() {
        return gameRealTime;
    }

    public void setGameRealTime(String gameRealTime) {
        this.gameRealTime = gameRealTime;
    }

    public String getHalfName() {
        return halfName;
    }

    public void setHalfName(String halfName) {
        this.halfName = halfName;
    }

    public String getLianSaiName() {
        return lianSaiName;
    }

    public void setLianSaiName(String lianSaiName) {
        this.lianSaiName = lianSaiName;
    }

    public String getTeamNames() {
        return teamNames;
    }

    public void setTeamNames(String teamNames) {
        this.teamNames = teamNames;
    }

    public String getGameCategoryName() {
        return gameCategoryName;
    }

    public void setGameCategoryName(String gameCategoryName) {
        this.gameCategoryName = gameCategoryName;
    }

    public String getPeilvKey() {
        return peilvKey;
    }

    public void setPeilvKey(String peilvKey) {
        this.peilvKey = peilvKey;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isFakeItem() {
        return fakeItem;
    }

    public void setFakeItem(boolean fakeItem) {
        this.fakeItem = fakeItem;
    }


    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getPeilv() {
        return peilv;
    }

    public void setPeilv(String peilv) {
        this.peilv = peilv;
    }
}
