package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/13.
 */

public class AccountChangeResult {
    int pageSize;
    long totalCount;
    List<AccountRecord> results;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<AccountRecord> getResults() {
        return results;
    }

    public void setResults(List<AccountRecord> results) {
        this.results = results;
    }
}
