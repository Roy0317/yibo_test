package com.example.anuo.immodule.jsonmodel;



public class ChatMuteAllJsonModel extends BaseJsonModel {

    private String roomId ;
    private int isBanSpeak;//0解除禁言 1禁言
    private String stationId ;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getIsBanSpeak() {
        return isBanSpeak;
    }

    public void setIsBanSpeak(int isBanSpeak) {
        this.isBanSpeak = isBanSpeak;
    }


    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    ;


}
