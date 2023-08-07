package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/5.
 */

public class RegConfig {

    int requiredVal;//是否必须 2-是，1-否
    int showVal;//是否显示，1-不显示 2-显示
    String regex;
    String name;
    String key;
    int validateVal;//是否验证此字段
    int status;//启用状态，1-禁用 2-启用
    int uniqueVal;//是否唯一 1-否 2-是

    public int getRequiredVal() {
        return requiredVal;
    }

    public void setRequiredVal(int requiredVal) {
        this.requiredVal = requiredVal;
    }

    public int getShowVal() {
        return showVal;
    }

    public void setShowVal(int showVal) {
        this.showVal = showVal;
    }

    public int getValidateVal() {
        return validateVal;
    }

    public void setValidateVal(int validateVal) {
        this.validateVal = validateVal;
    }

    public int getUniqueVal() {
        return uniqueVal;
    }

    public void setUniqueVal(int uniqueVal) {
        this.uniqueVal = uniqueVal;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
