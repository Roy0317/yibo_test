package com.yibo.yiboapp.entify;

public class VIPCenterBean {

    private String name;
    private int resId; //图片id
    private String code;
    private int canChange = 1;  //0，不可更改  1.可更改取消显示在浮动菜单
    private int showMenu = 0; //0 不显示 1.显示在浮动菜单
    private int showConfig = 1; //后台配置是否显示  0 不显示，1.显示
    private String account; //用户账号
    private String righttopData;

    public VIPCenterBean() {

    }

    public VIPCenterBean(String name, int resId, String code) {
        this.name = name;
        this.resId = resId;
        this.code = code;
    }

    public VIPCenterBean(String name, int resId, String code, int showConfig) {
        this.name = name;
        this.resId = resId;
        this.code = code;
        this.showConfig = showConfig;
    }

    public String getRighttopData() {
        return righttopData;
    }

    public void setRighttopData(String righttopData) {
        this.righttopData = righttopData;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getCanChange() {
        return canChange;
    }

    public void setCanChange(int canChange) {
        this.canChange = canChange;
    }

    public int getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(int showMenu) {
        this.showMenu = showMenu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getShowConfig() {
        return showConfig;
    }

    public void setShowConfig(int showConfig) {
        this.showConfig = showConfig;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
