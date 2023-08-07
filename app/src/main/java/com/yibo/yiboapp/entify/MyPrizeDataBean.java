package com.yibo.yiboapp.entify;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: game
 * @package: com.yibo.yiboapp.entify
 * @description: ${DESP}
 * @date: 2019/9/12
 * @time: 3:59 PM
 */
public class MyPrizeDataBean {

    /**
     * account : king888
     * accountId : 519
     * activeId : 4
     * awardType : 1
     * createDatetime : 2019-09-11 23:25:58
     * dataVersion : 0
     * haoMa : [6,4,4,5,3,4](三红)
     * id : 154
     * productName :
     * stationId : 3
     * status : 1
     */

    private String account;
    private int accountId;
    private int activeId;
    private int awardType;
    private String createDatetime;
    private int dataVersion;
    private String haoMa;
    private int id;
    private String productName;
    private int awardValue;
    private int stationId;
    private int status;

    public int getAwardValue() {
        return awardValue;
    }

    public void setAwardValue(int awardValue) {
        this.awardValue = awardValue;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public int getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getHaoMa() {
        return haoMa;
    }

    public void setHaoMa(String haoMa) {
        this.haoMa = haoMa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
