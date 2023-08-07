package com.yibo.yiboapp.entify;

import com.yibo.yiboapp.data.PeilvPlayData;

import java.util.List;

/**
 * Created by johnson on 2017/10/24.
 */

public class CalcPeilvOrder {
    double totalMoney;
    int zhushu;
    float peilvWhenMultiselect;
    boolean isMultiSelect;//是否多选模式
    List<PeilvPlayData> selectDatas;

    public List<PeilvPlayData> getSelectDatas() {
        return selectDatas;
    }

    public void setSelectDatas(List<PeilvPlayData> selectDatas) {
        this.selectDatas = selectDatas;
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        isMultiSelect = multiSelect;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getZhushu() {
        return zhushu;
    }

    public void setZhushu(int zhushu) {
        this.zhushu = zhushu;
    }

    public float getPeilvWhenMultiselect() {
        return peilvWhenMultiselect;
    }

    public void setPeilvWhenMultiselect(float peilvWhenMultiselect) {
        this.peilvWhenMultiselect = peilvWhenMultiselect;
    }
}
