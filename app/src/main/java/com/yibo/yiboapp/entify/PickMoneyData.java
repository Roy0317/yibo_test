package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/10/18.
 */

public class PickMoneyData {
    int curWnum;//已提款次数
    int wnum;//今日还可提款几次,-1表示不限制
    boolean enablePick=true;//是否能提款
    String drawFlag;//不可提款的原因，可提款时值为"是"
    String startTime;//今日提款有效开始时间
    String endTime;//今天提款有效结束时间
    String minPickMoney;//最小提款额度
    String maxPickMoney;//最大提款额度
    private DrawLimitMap drawLimitMap;//支付通道
    String cardNo;//提款卡号
    String userName;//提款帐户名
    float validBetMoney;//有效投注金额
    int accountStatus;//帐号启用状态
    String bankAddress;//开户行
    String bankName;//银行名称
    float accountBalance;//帐户余额
    float checkBetNum;//出款需达到的投注量,-1表示不限投注量
    boolean needUpdCardNo;//是否需要更新银行卡信息

    Strategy strategy;
    String arrivalTime;//提款到账时间

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void seStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public int getCurWnum() {
        return curWnum;
    }

    public void setCurWnum(int curWnum) {
        this.curWnum = curWnum;
    }

    public int getWnum() {
        return wnum;
    }

    public void setWnum(int wnum) {
        this.wnum = wnum;
    }

    public boolean isEnablePick() {
        return enablePick;
    }

    public void setEnablePick(boolean enablePick) {
        this.enablePick = enablePick;
    }

    public String getDrawFlag() {
        return drawFlag;
    }

    public void setDrawFlag(String drawFlag) {
        this.drawFlag = drawFlag;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMinPickMoney() {
        return minPickMoney;
    }

    public void setMinPickMoney(String minPickMoney) {
        this.minPickMoney = minPickMoney;
    }

    public String getMaxPickMoney() {
        return maxPickMoney;
    }

    public void setMaxPickMoney(String maxPickMoney) {
        this.maxPickMoney = maxPickMoney;
    }

    public DrawLimitMap getDrawLimitMap() {
        return drawLimitMap;
    }

    public void setDrawLimitMap(DrawLimitMap drawLimitMap) {
        this.drawLimitMap = drawLimitMap;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getValidBetMoney() {
        return validBetMoney;
    }

    public void setValidBetMoney(float validBetMoney) {
        this.validBetMoney = validBetMoney;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public float getCheckBetNum() {
        return checkBetNum;
    }

    public void setCheckBetNum(float checkBetNum) {
        this.checkBetNum = checkBetNum;
    }

    public boolean isNeedUpdCardNo() {
        return needUpdCardNo;
    }

    public void setNeedUpdCardNo(boolean needUpdCardNo) {
        this.needUpdCardNo = needUpdCardNo;
    }

    public class Strategy {
        private int drawNum;
        private int feeType;
        private float feeValue;
        private int id;
        private String levels;
        private String lowerLimit;
        private String remark;
        private int stationId;
        private int status;
        private String upperLimit;

        public int getDrawNum() {
            return drawNum;
        }

        public void setDrawNum(int drawNum) {
            this.drawNum = drawNum;
        }

        public int getFeeType() {
            return feeType;
        }

        public void setFeeType(int feeType) {
            this.feeType = feeType;
        }

        public float getFeeValue() {
            return feeValue;
        }

        public void setFeeValue(float feeValue) {
            this.feeValue = feeValue;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLevels() {
            return levels;
        }

        public void setLevels(String levels) {
            this.levels = levels;
        }

        public String getLowerLimit() {
            return lowerLimit;
        }

        public void setLowerLimit(String lowerLimit) {
            this.lowerLimit = lowerLimit;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getUpperLimit() {
            return upperLimit;
        }

        public void setUpperLimit(String upperLimit) {
            this.upperLimit = upperLimit;
        }
    }

    public static class DrawLimitMap {
        //    銀行卡
        private String withdraw_min_money_bank;
        private String withdraw_max_money_bank;
        //    支付寶
        private String withdraw_min_money_alipay;
        private String withdraw_max_money_alipay;
        //    USDT
        private String withdraw_min_money_usdt;
        private String withdraw_max_money_usdt;
        //    GOPAY
        private String withdraw_min_money_gopay;
        private String withdraw_max_money_gopay;
        //    OKPAY
        private String withdraw_min_money_okpay;
        private String withdraw_max_money_okpay;
        //    VPAY
        private String withdraw_min_money_vpay;
        private String withdraw_max_money_vpay;

        private String withdraw_min_money_topay; //(通道限制金額topay最低)
        private String withdraw_max_money_topay; //(通道限制金額topay最高)

        public String getWithdraw_min_money_bank() {
            return withdraw_min_money_bank;
        }

        public void setWithdraw_min_money_bank(String withdraw_min_money_bank) {
            this.withdraw_min_money_bank = withdraw_min_money_bank;
        }

        public String getWithdraw_max_money_usdt() {
            return withdraw_max_money_usdt;
        }

        public void setWithdraw_max_money_usdt(String withdraw_max_money_usdt) {
            this.withdraw_max_money_usdt = withdraw_max_money_usdt;
        }

        public String getWithdraw_max_money_gopay() {
            return withdraw_max_money_gopay;
        }

        public String getWithdraw_max_money_okpay() {
            return withdraw_max_money_okpay;
        }

        public String getWithdraw_max_money_vpay(){ return  withdraw_max_money_vpay;}

        public void setWithdraw_max_money_gopay(String withdraw_max_money_gopay) {
            this.withdraw_max_money_gopay = withdraw_max_money_gopay;
        }

        public String getWithdraw_min_money_usdt() {
            return withdraw_min_money_usdt;
        }

        public void setWithdraw_min_money_usdt(String withdraw_min_money_usdt) {
            this.withdraw_min_money_usdt = withdraw_min_money_usdt;
        }

        public String getWithdraw_min_money_gopay() {
            return withdraw_min_money_gopay;
        }

        public String getWithdraw_min_money_okpay() {
            return withdraw_min_money_okpay;
        }

        public String getWithdraw_min_money_vpay(){ return  withdraw_min_money_vpay;}

        public void setWithdraw_min_money_gopay(String withdraw_min_money_gopay) {
            this.withdraw_min_money_gopay = withdraw_min_money_gopay;
        }

        public String getWithdraw_min_money_topay() {
            return withdraw_min_money_topay;
        }

        public void setWithdraw_min_money_topay(String withdraw_min_money_topay) {
            this.withdraw_min_money_topay = withdraw_min_money_topay;
        }

        public String getWithdraw_max_money_topay() {
            return withdraw_max_money_topay;
        }

        public void setWithdraw_max_money_topay(String withdraw_max_money_topay) {
            this.withdraw_max_money_topay = withdraw_max_money_topay;
        }

        public String getWithdraw_min_money_alipay() {
            return withdraw_min_money_alipay;
        }

        public void setWithdraw_min_money_alipay(String withdraw_min_money_alipay) {
            this.withdraw_min_money_alipay = withdraw_min_money_alipay;
        }

        public String getWithdraw_max_money_bank() {
            return withdraw_max_money_bank;
        }

        public void setWithdraw_max_money_bank(String withdraw_max_money_bank) {
            this.withdraw_max_money_bank = withdraw_max_money_bank;
        }

        public String getWithdraw_max_money_alipay() {
            return withdraw_max_money_alipay;
        }

        public void setWithdraw_max_money_alipay(String withdraw_max_money_alipay) {
            this.withdraw_max_money_alipay = withdraw_max_money_alipay;
        }
    }

}
