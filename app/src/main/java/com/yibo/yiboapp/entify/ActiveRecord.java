package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2018/1/26.
 */

public class ActiveRecord {

    // 未处理
    public static long STATUS_UNTREATED = 1l;
    // 处理成功
    public static long STATUS_SUCCESS = 2l;
    // 处理失败
    public static long STATUS_FAILED = 3l;
    // 已取消
    public static long STATUS_CANCELED = 4l;

    private long id;

    /**
     * 活动ID
     */
    private long activeId;

    /**
     * 站点ID
     */
    private long stationId;

    /**
     * 用户ID
     */
    private long accountId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 奖项类型
     */
    private long awardType;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 奖项面值
     */
    private float awardValue;

    /**
     * 中奖时间
     */
    private long createDatetime;

    /**
     * 商品ID
     */
    private long productId;

    /**
     * 处理状态
     */
    private long status;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 乐观锁版本号
     */
    private Long dataVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActiveId() {
        return activeId;
    }

    public void setActiveId(long activeId) {
        this.activeId = activeId;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getAwardType() {
        return awardType;
    }

    public void setAwardType(long awardType) {
        this.awardType = awardType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getAwardValue() {
        return awardValue;
    }

    public void setAwardValue(float awardValue) {
        this.awardValue = awardValue;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }
}
