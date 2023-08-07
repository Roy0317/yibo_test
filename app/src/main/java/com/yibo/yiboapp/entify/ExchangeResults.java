package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/11/15.
 */

public class ExchangeResults {
    float score;
    List<ExchangeConfig> configs;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<ExchangeConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ExchangeConfig> configs) {
        this.configs = configs;
    }
}
