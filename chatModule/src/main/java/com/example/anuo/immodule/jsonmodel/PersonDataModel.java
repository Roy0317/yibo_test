package com.example.anuo.immodule.jsonmodel;

import java.util.UUID;

/*
* 获取用户资料
* */
public class PersonDataModel {

    private String code;
    private String userId;
    private String source;
    private String roomId;
    private String msgUUID = UUID.randomUUID().toString();

    public PersonDataModel(String code, String source, String userId, String roomId){
        this.code = code;
        this.source = source;
        this.userId = userId;
        this.roomId = roomId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMsgUUID() {
        return msgUUID;
    }

    public void setMsgUUID(String msgUUID) {
        this.msgUUID = msgUUID;
    }
}
