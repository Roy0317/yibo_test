package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/11.
 */

public class GameRecordResult {
    int pageSize;
    List<BcLotteryOrder> results;
    int totalCount;
    int totalPageCount;
    int currentPageNo;
    float sumWinMoney;
    float sumBuyMoney;
    float subBuyMoney;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<BcLotteryOrder> getResults() {
        return results;
    }

    public void setResults(List<BcLotteryOrder> results) {
        this.results = results;
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

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public float getSumWinMoney() {
        return sumWinMoney;
    }

    public void setSumWinMoney(float sumWinMoney) {
        this.sumWinMoney = sumWinMoney;
    }

    public float getSumBuyMoney() {
        return sumBuyMoney;
    }

    public void setSumBuyMoney(float sumBuyMoney) {
        this.sumBuyMoney = sumBuyMoney;
    }

    public float getSubBuyMoney() {
        return subBuyMoney;
    }

    public void setSubBuyMoney(float subBuyMoney) {
        this.subBuyMoney = subBuyMoney;
    }
}
