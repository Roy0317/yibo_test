package com.yibo.yiboapp.entify;

import java.util.List;

public class IncomeListData {
    boolean hasNext;
    boolean hasPre;
    String nextPage;
    String pageSize;


    List<IncomDetial> list;

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

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }


    public class IncomDetial {
        public String getStatDate() {
            return statDate;
        }

        public void setStatDate(String statDate) {
            this.statDate = statDate;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        String statDate;
        String income;


    }

    public List<IncomDetial> getList() {
        return list;
    }

    public void setList(List<IncomDetial> list) {
        this.list = list;
    }

}
