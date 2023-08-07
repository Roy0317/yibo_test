package com.example.anuo.immodule.jsonmodel;

/*
* 头像列表
* */
public class PhotoListModel {

    private String code;
    private String source;
    private String stationId;
    private String userId;

    public PhotoListModel(String code, String source, String stationId, String userId) {
        this.code = code;
        this.source = source;
        this.stationId = stationId;
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
