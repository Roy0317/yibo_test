package com.example.anuo.immodule.jsonmodel;

public class UploadAvatarModel {

    private String code;
    private String source;
    private String stationId;
    private String userId;
    private String fileString;

    public UploadAvatarModel(String code, String source, String stationId, String userId, String fileString) {
        this.code = code;
        this.source = source;
        this.stationId = stationId;
        this.userId = userId;
        this.fileString = fileString;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileString() {
        return fileString;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
