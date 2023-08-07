package com.yibo.yiboapp.entify;

public class IncomeData {

//    {"scaleStr":0,"money":934512.61,"incomeEnd":0,"scaleEnd":0,"incomeMon":0,"startTime":"2019-03-19","endTime":"2019-03-26","incomeStr":0,"incomeYear":0}

    private String scaleStr;
    private String money;//总金额
    private String incomeEnd;
    private String scaleEnd;//万份收益
    private String incomeMon;//预计月收益
    private String startTime;
    private String endTime;
    private String incomeStr;

    private String hisIncome;//累计收益
    private String sevendayRate;//七日年化利率
    private String yesterdayRecord;//昨日收益
    private String incomeYear;//预计年收益


    public String getSevendayRate() {
        return sevendayRate;
    }

    public void setSevendayRate(String sevendayRate) {
        this.sevendayRate = sevendayRate;
    }



    public String getHisIncome() {
        return hisIncome;
    }

    public void setHisIncome(String hisIncome) {
        this.hisIncome = hisIncome;
    }

    public String getScaleStr() {
        return scaleStr;
    }

    public void setScaleStr(String scaleStr) {
        this.scaleStr = scaleStr;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIncomeEnd() {
        return incomeEnd;
    }

    public void setIncomeEnd(String incomeEnd) {
        this.incomeEnd = incomeEnd;
    }

    public String getScaleEnd() {
        return scaleEnd;
    }

    public void setScaleEnd(String scaleEnd) {
        this.scaleEnd = scaleEnd;
    }

    public String getIncomeMon() {
        return incomeMon;
    }

    public void setIncomeMon(String incomeMon) {
        this.incomeMon = incomeMon;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIncomeStr() {
        return incomeStr;
    }

    public void setIncomeStr(String incomeStr) {
        this.incomeStr = incomeStr;
    }

    public String getIncomeYear() {
        return incomeYear;
    }

    public void setIncomeYear(String incomeYear) {
        this.incomeYear = incomeYear;
    }


    public String getYesterdayRecord() {
        return yesterdayRecord;
    }

    public void setYesterdayRecord(String yesterdayRecord) {
        this.yesterdayRecord = yesterdayRecord;
    }
}
