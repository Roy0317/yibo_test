package com.yibo.yiboapp.data;

import java.util.HashMap;
import java.util.Map;

public class RequestEventTrack {

    private String uid;
    private String event;
    private String timestamp;
    private String url;
    private Map<String, String> headers = new HashMap<>();
    private String headerString;
    private String statusCode;
    private String response;

    /*需要追踪的接口与对应名称*/
    public static HashMap<String, String> getNeedTrackEventMap() {
        HashMap<String, String> needTrackEventMap = new HashMap<>();
        needTrackEventMap.put(Urls.LOGIN_NEWV2_URL, "登录事件");
        needTrackEventMap.put(Urls.DO_BETS_URL, "投注事件");
        needTrackEventMap.put(Urls.DO_PEILVBETS_URL, "投注事件");
        needTrackEventMap.put(Urls.DO_SIX_MARK_URL, "投注事件");
        needTrackEventMap.put(Urls.SPORT_BETS_URL, "投注事件");
        return needTrackEventMap;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String headerToString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Headers:\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            stringBuilder.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        return stringBuilder.toString();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getHeadersString() { return headerString; }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }
}
