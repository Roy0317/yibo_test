package com.yibo.yiboapp.entify;

import java.util.List;

public class TeamOverViewWraper {

    List<TeamViewBean> teamAccount;
    TeamMap teamMaps;

    public List<TeamViewBean> getTeamAccount() {
        return teamAccount;
    }

    public void setTeamAccount(List<TeamViewBean> teamAccount) {
        this.teamAccount = teamAccount;
    }

    public TeamMap getTeamMaps() {
        return teamMaps;
    }

    public void setTeamMaps(TeamMap teamMaps) {
        this.teamMaps = teamMaps;
    }
}
