package com.yibo.yiboapp.entify;

import java.util.List;

public class SBResult {
    AggsResult aggsData;
    int currentPageNo;
    boolean hasNext;
    boolean hasPre;
    List<SBSportOrder> list;
    int nextPage;
    int pageSize;
    int prePage;

    public AggsResult getAggsData() {
        return aggsData;
    }

    public void setAggsData(AggsResult aggsData) {
        this.aggsData = aggsData;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }

    public List<SBSportOrder> getList() {
        return list;
    }

    public void setList(List<SBSportOrder> list) {
        this.list = list;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }
}
