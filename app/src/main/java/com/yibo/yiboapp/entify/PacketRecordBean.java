package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/12.
 */

public class PacketRecordBean {
    // 未处理
    public static int STATUS_UNTREATED = 1;
    // 处理成功
    public static int STATUS_SUCCESS = 2;
    // 处理失败
    public static int STATUS_FAILED = 3;
    // 已取消
    public static int STATUS_CANCELED = 4;

    private long id;

    private long accountId;

    private String account;

    private float money;

    private long createDatetime;

    private long redPacketId;

    private int status;

    private String remark;
    private String redPacketName;
    private String ip;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public long getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(long redPacketId) {
        this.redPacketId = redPacketId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRedPacketName() {
        return redPacketName;
    }

    public void setRedPacketName(String redPacketName) {
        this.redPacketName = redPacketName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
