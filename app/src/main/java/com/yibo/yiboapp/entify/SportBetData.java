package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/31.
 */

public class SportBetData {
    String plate;
    String gameType;
    List<SportBet> items;
    int money;
    boolean acceptBestOdds;//是否接受最佳赔率

    public boolean isAcceptBestOdds() {
        return acceptBestOdds;
    }

    public void setAcceptBestOdds(boolean acceptBestOdds) {
        this.acceptBestOdds = acceptBestOdds;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public List<SportBet> getItems() {
        return items;
    }

    public void setItems(List<SportBet> items) {
        this.items = items;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
