package com.example.anuo.immodule.jsonmodel;

public class ChatWinningListJsonModel extends BaseJsonModel {

    private String prizeType;
    private String stationId;//站点ID

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
}