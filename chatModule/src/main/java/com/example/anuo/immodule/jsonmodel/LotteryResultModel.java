package com.example.anuo.immodule.jsonmodel;

/*
* 聊天室彩票列表
* */
public class LotteryResultModel {

    private String code;

    private String stationId;

    private String source;



    public LotteryResultModel(String code, String stationId, String source) {
        this.code = code;
        this.stationId = stationId;
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
