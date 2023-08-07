package com.example.anuo.immodule.jsonmodel;

/*
* 违禁词
* */
public class ChatViolateWordsModel {

    private String code;
    private String statid;
    private String stationId;
    private String source;

    public ChatViolateWordsModel(String code, String statid, String source) {
        this.code = code;
        this.statid = statid;
        this.stationId = statid;
        this.source = source;
    }

}
