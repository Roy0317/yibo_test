package com.yibo.yiboapp.entify;

import com.yibo.yiboapp.data.LotteryData;

import java.util.List;

public class UpdateAllGameEvent {

    private String lotteryJson;

    public UpdateAllGameEvent(String lotteryJson) {
        this.lotteryJson = lotteryJson;
    }

    public String getLotteryJson() {
        return lotteryJson;
    }

    public void setLotteryJson(String lotteryJson) {
        this.lotteryJson = lotteryJson;
    }
}
