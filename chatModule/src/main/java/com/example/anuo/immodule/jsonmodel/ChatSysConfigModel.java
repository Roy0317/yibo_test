package com.example.anuo.immodule.jsonmodel;

import java.util.UUID;

/*
* 聊天室配置
* */
public class ChatSysConfigModel {

    String code;
    String userId;
    String stationId;
    int userType;
    String source;
    String msgUUID;

    public ChatSysConfigModel(String code, String userId, String stationId, int userType, String source,String msgUUID) {
        this.code = code;
        this.userId = userId;
        this.stationId = stationId;
        this.userType = userType;
        this.source = source;
        this.msgUUID = msgUUID;
    }
}
