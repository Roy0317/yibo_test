package com.yibo.yiboapp.entify;


import com.google.gson.JsonArray;

/**
 * Created by johnson on 2017/10/27.
 */

public class SportData {
    int pageCount;
    JsonArray games;
    JsonArray headers;
    SportGameCount gameCount;

    public SportGameCount getGameCount() {
        return gameCount;
    }

    public void setGameCount(SportGameCount gameCount) {
        this.gameCount = gameCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public JsonArray getGames() {
        return games;
    }

    public void setGames(JsonArray games) {
        this.games = games;
    }

    public JsonArray getHeaders() {
        return headers;
    }

    public void setHeaders(JsonArray headers) {
        this.headers = headers;
    }
}
