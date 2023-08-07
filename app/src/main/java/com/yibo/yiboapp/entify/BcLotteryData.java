package com.yibo.yiboapp.entify;


public class BcLotteryData {
    private Long id;

    /**
     * 彩票编码
     */
    private String lotCode;

    /**
     * 期号
     */
    private String qiHao;
    /**
     * 号码
     */
    private String haoMa;
    /**
     * 开奖状态{1未开奖,2未派奖,3已派奖, 4未派奖完，5已经取消,6已经回滚}
     */
    private Integer openStatus;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLotCode() {
        return this.lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getQiHao() {
        return this.qiHao;
    }

    public void setQiHao(String qiHao) {
        this.qiHao = qiHao;
    }

    public String getHaoMa() {
        return this.haoMa;
    }

    public void setHaoMa(String haoMa) {
        this.haoMa = haoMa;
    }


    public Integer getOpenStatus() {
        return this.openStatus;
    }

    public void setOpenStatus(Integer openStatus) {
        this.openStatus = openStatus;
    }


}
