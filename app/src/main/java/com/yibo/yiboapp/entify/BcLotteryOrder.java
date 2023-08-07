package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/11.
 */

public class BcLotteryOrder {
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 会员账号名
     */
    private String account;
    private Long accountId;
    /**
     * 彩票编号
     */
    private String lotCode;
    /**
     * 彩种名称
     *
     * @return
     */
    private String lotName;


    /**
     * 购买的号码
     */
    private String haoMa;

    /**
     * 购买时间
     */
    private long createTime;

    //购买时间字符串
    private String createTimeStr;

    /**
     * 购买金额
     */
    private float buyMoney;

    /**
     * 中奖金额
     */
    private float winMoney;

    /**
     * 状态 1等待开奖 2已中奖 3未中奖 4撤单 5派奖回滚成功 6回滚异常的 7开奖异常
     */
    private Integer status;


    /**
     * 购买注数
     */
    private Integer buyZhuShu;

    /**
     * 中奖注数
     */
    private Integer winZhuShu;

    /**
     * 购买倍数
     */
    private Integer multiple;

    /**
     * 彩票期号
     */
    private String qiHao;
    /**
     * 玩法大类名称
     */
    public String groupName;// 大类名字
    /**
     * 玩法小类名称
     *
     * @return
     */
    private String playName;

    /**
     * 玩法小类code
     */
    private String playCode;

    /**
     * 赔率
     */
    private String peilv;// 赔率,不存库

    /**
     * 彩种奖金
     *
     * @return
     */
    private float minBonusOdds;

    /**
     * 盈亏输赢
     *
     * @return
     */
    private float yingKui;

    /**
     * 模式 1元 10角 100分
     */
    private Integer model;
    private String openHaoma;

    public String getOpenHaoma() {
        return openHaoma;
    }

    public void setOpenHaoma(String openHaoma) {
        this.openHaoma = openHaoma;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Integer getBuyZhuShu() {
        return buyZhuShu;
    }

    public void setBuyZhuShu(Integer buyZhuShu) {
        this.buyZhuShu = buyZhuShu;
    }

    public Integer getWinZhuShu() {
        return winZhuShu;
    }

    public void setWinZhuShu(Integer winZhuShu) {
        this.winZhuShu = winZhuShu;
    }

    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public String getQiHao() {
        return qiHao;
    }

    public void setQiHao(String qiHao) {
        this.qiHao = qiHao;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getPeilv() {
        return peilv;
    }

    public void setPeilv(String peilv) {
        this.peilv = peilv;
    }

    public float getMinBonusOdds() {
        return minBonusOdds;
    }

    public void setMinBonusOdds(float minBonusOdds) {
        this.minBonusOdds = minBonusOdds;
    }

    public float getYingKui() {
        return yingKui;
    }

    public void setYingKui(float yingKui) {
        this.yingKui = yingKui;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getHaoMa() {
        return haoMa;
    }

    public void setHaoMa(String haoMa) {
        this.haoMa = haoMa;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public float getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(float buyMoney) {
        this.buyMoney = buyMoney;
    }

    public float getWinMoney() {
        return winMoney;
    }

    public void setWinMoney(float winMoney) {
        this.winMoney = winMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getPlayCode() {
        return playCode;
    }

    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }
}
