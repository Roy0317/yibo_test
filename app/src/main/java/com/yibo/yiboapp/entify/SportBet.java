package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/31.
 */

public class SportBet {
    long gid;
    long mid;
    float odds;
    String type;
    String project;
    String scoreH;//主队比分(针对滚球情况)
    String scoreC;//客队比分(针对滚球情况)

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

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public float getOdds() {
        return odds;
    }

    public void setOdds(float odds) {
        this.odds = odds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
