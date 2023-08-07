package com.yibo.yiboapp.entify;

import java.util.List;

public class NewSportOrder {

    SportAggsData aggsData;
    List<NewSportOrderBean> list;
    int currentPageNo;
    int totalCount;
    int totalPageCount;

    public SportAggsData getAggsData() {
        return aggsData;
    }

    public void setAggsData(SportAggsData aggsData) {
        this.aggsData = aggsData;
    }

    public List<NewSportOrderBean> getList() {
        return list;
    }

    public void setList(List<NewSportOrderBean> list) {
        this.list = list;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
