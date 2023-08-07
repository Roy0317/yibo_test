package com.example.anuo.immodule.jsonmodel;

public class ChatMessageHistoryModel {

    String code;
    String source;
    String roomId;
    int start; //页数
    int count; //条数

    public ChatMessageHistoryModel(String code,String source ,String roomId, int start, int count) {
        this.code = code;
        this.source = source;
        this.roomId = roomId;
        this.start = start;
        this.count = count;
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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
