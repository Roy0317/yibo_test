package com.example.anuo.immodule.jsonmodel;

public class JoinPrivateRoomJsonModel extends BaseJsonModel {
    private String stationId;
    private String type;
    private String roomId;
    private String userId;
    private String passiveUserId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassiveUserId() {
        return passiveUserId;
    }

    public void setPassiveUserId(String passiveUserId) {
        this.passiveUserId = passiveUserId;
    }


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
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
}
