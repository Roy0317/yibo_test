package com.yibo.yiboapp.entify;

/**
 * Created by Administrator on 2017/2/6.
 */
public class BallListItemInfo {

    String num;
    String postNum;
    boolean isSelected;
    boolean clickOn;//是否点击了

    public boolean isClickOn() {
        return clickOn;
    }

    public void setClickOn(boolean clickOn) {
        this.clickOn = clickOn;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
