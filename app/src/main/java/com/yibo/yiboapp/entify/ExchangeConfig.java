package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/15.
 */

public class ExchangeConfig {
    float numerator;//兑换比例中的分子数
    float denominator;//兑换比例中的分母数
    float maxVal;//最大兑换值
    float minVal;//最小兑换值
    long status;//禁用与否的状态
    long id;
    long type;//兑换类型 1--现金换积分 2-积分换现金

    public float getNumerator() {
        return numerator;
    }

    public void setNumerator(float numerator) {
        this.numerator = numerator;
    }

    public float getDenominator() {
        return denominator;
    }

    public void setDenominator(float denominator) {
        this.denominator = denominator;
    }

    public float getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(float maxVal) {
        this.maxVal = maxVal;
    }

    public float getMinVal() {
        return minVal;
    }

    public void setMinVal(float minVal) {
        this.minVal = minVal;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
