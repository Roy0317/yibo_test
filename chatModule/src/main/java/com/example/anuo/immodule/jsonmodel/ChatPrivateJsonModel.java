package com.example.anuo.immodule.jsonmodel;

public class ChatPrivateJsonModel extends BaseJsonModel {

    /**
     * 1.初始化私聊
     * 2.拉取私聊列表
     * 3.刷新消息状态
     * 4.私聊备注名
     */
    private String type;
    private String roomId;
    private String passiveUserId;

    private String stationId;
    private String record;
    private int userType;
    private String pUserType;
    private String pNickName;
    private String remarks;//备注名
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getStationId() {

        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getpUserType() {
        return pUserType;
    }

    public void setpUserType(String pUserType) {
        this.pUserType = pUserType;
    }

    public String getpNickName() {
        return pNickName;
    }

    public void setpNickName(String pNickName) {
        this.pNickName = pNickName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPassiveUserId() {
        return passiveUserId;
    }

    public void setPassiveUserId(String passiveUserId) {
        this.passiveUserId = passiveUserId;
    }
}
