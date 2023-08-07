package com.yibo.yiboapp.entify;

import java.util.List;

public class ChessBetResultWraper {

    ChessAggsData aggsData;
    List<ChessBetBean> rows;
    int total;

    public ChessAggsData getAggsData() {
        return aggsData;
    }

    public void setAggsData(ChessAggsData aggsData) {
        this.aggsData = aggsData;
    }

    public List<ChessBetBean> getRows() {
        return rows;
    }

    public void setRows(List<ChessBetBean> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
