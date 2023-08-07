package com.example.anuo.immodule.jsonmodel;



public class ChatMuteJsonModel extends BaseJsonModel {

    private String roomId ;
    private int speakingClose;//0解除禁言 1禁言
    private String speakingCloseId;
    private String operatorAcount ;
    private String message ;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getSpeakingClose() {
        return speakingClose;
    }

    public void setSpeakingClose(int speakingClose) {
        this.speakingClose = speakingClose;
    }

    public String getSpeakingCloseId() {
        return speakingCloseId;
    }

    public void setSpeakingCloseId(String speakingCloseId) {
        this.speakingCloseId = speakingCloseId;
    }

    public String getOperatorAcount() {
        return operatorAcount;
    }

    public void setOperatorAcount(String operatorAcount) {
        this.operatorAcount = operatorAcount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
