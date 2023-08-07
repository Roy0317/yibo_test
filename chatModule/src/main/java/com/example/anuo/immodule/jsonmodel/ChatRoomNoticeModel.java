package com.example.anuo.immodule.jsonmodel;

public class ChatRoomNoticeModel { //房间公告

    String code;
    String roomId;
    String stationId;
    String source;

    public ChatRoomNoticeModel(String code, String source, String stationId, String roomId) {
        this.code = code;
        this.roomId = roomId;
        this.source = source;
        this.stationId = stationId;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
