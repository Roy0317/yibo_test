package com.example.anuo.immodule.jsonmodel;

public class ChatJoinPrivateRoomMsg {


    /**
     * success : true
     * type : join
     * roomId : 076e68fbd83040b78f173573c8bbeead
     */

    private boolean success;
    private String type;
    private String roomId;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
