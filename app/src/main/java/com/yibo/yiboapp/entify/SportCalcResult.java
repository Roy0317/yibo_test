package com.yibo.yiboapp.entify;

import java.util.List;
import java.util.Map;

/**
 * Created by johnson on 2017/11/10.
 */

public class SportCalcResult {
    List<List<SportBean>> lists;
    List<Map> subSports;//每个主联赛下的所有赛事数据集
    int selectPos;

    public List<List<SportBean>> getLists() {
        return lists;
    }

    public void setLists(List<List<SportBean>> lists) {
        this.lists = lists;
    }

    public List<Map> getSubSports() {
        return subSports;
    }

    public void setSubSports(List<Map> subSports) {
        this.subSports = subSports;
    }

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }
}
