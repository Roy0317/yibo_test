package com.example.anuo.immodule.jsonmodel;

public class ChatQuickMessageJsonModel extends BaseJsonModel {
    private String roomId;
    private String stationId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
