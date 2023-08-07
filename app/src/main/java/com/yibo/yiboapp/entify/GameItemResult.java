package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/9.
 */

public class GameItemResult {
    String ButtonImagePath;
    String DisplayName;
    int typeid;
    String LapisId;
    int single;

    public String getButtonImagePath() {
        return ButtonImagePath;
    }

    public void setButtonImagePath(String buttonImagePath) {
        ButtonImagePath = buttonImagePath;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getLapisId() {
        return LapisId;
    }

    public void setLapisId(String lapisId) {
        LapisId = lapisId;
    }

    public int getSingle() {
        return single;
    }

    public void setSingle(int single) {
        this.single = single;
    }
}
