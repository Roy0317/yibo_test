package com.yibo.yiboapp.entify;

import java.util.List;

public class PickAccountResponse {


    /**
     * success : true
     * accessToken : d4e14a21-f572-4788-96b5-6e99e6d33ca9
     * content : {"drawFlag":"投注量未达标，不能提款","minPickMoney":"10","checkBetNum":8188.46,"canDraw":false,"bankName":"北京农商银行","userName":"**uo","bankAddress":null,"cardNo":"************1234","accountStatus":0,"curWnum":0,"enablePick":false,"wnum":-1,"pickAccounts":[{"usdtAddress":null,"alipayName":null,"bankName":"北京农商银行","type":"1","userName":"**uo","alipayAccount":null,"bankAddress":null,"cardNo":"************1234"},{"usdtAddress":null,"alipayName":null,"bankName":null,"type":"2","userName":null,"alipayAccount":"******7890","bankAddress":null,"cardNo":null},{"usdtAddress":"********1010","alipayName":null,"bankName":null,"type":"3","userName":null,"alipayAccount":null,"bankAddress":null,"cardNo":null}],"startTime":"00:00","validBetMoney":160,"endTime":"23:59","accountBalance":13683.948,"strategy":null,"maxPickMoney":"1000000","needUpdCardNo":false}
     */
    private boolean success;
    private String msg;
    private int code;
    private String accessToken;
    private ContentEntity content;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setContent(ContentEntity content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public ContentEntity getContent() {
        return content;
    }

    public class ContentEntity {
        /**
         * drawFlag : 投注量未达标，不能提款
         * minPickMoney : 10
         * checkBetNum : 8188.46
         * canDraw : false
         * bankName : 北京农商银行
         * userName : **uo
         * bankAddress : null
         * cardNo : ************1234
         * accountStatus : 0
         * curWnum : 0
         * enablePick : false
         * wnum : -1
         * pickAccounts : [{"usdtAddress":null,"alipayName":null,"bankName":"北京农商银行","type":"1","userName":"**uo","alipayAccount":null,"bankAddress":null,"cardNo":"************1234"},{"usdtAddress":null,"alipayName":null,"bankName":null,"type":"2","userName":null,"alipayAccount":"******7890","bankAddress":null,"cardNo":null},{"usdtAddress":"********1010","alipayName":null,"bankName":null,"type":"3","userName":null,"alipayAccount":null,"bankAddress":null,"cardNo":null}]
         * startTime : 00:00
         * validBetMoney : 160.0
         * endTime : 23:59
         * accountBalance : 13683.948
         * strategy : null
         * maxPickMoney : 1000000
         * needUpdCardNo : false
         */
        private String drawFlag;
        private String minPickMoney;
        private double checkBetNum;
        private boolean canDraw;
        private String bankName;
        private String userName;
        private String bankAddress;
        private String cardNo;
        private int accountStatus;
        private int curWnum;
        private boolean enablePick;
        private int wnum;
        private List<PickAccount> pickAccounts;
        private String startTime;
        private double validBetMoney;
        private String endTime;
        private double accountBalance;
        private PickMoneyData.Strategy strategy;
        private String maxPickMoney;
        private boolean needUpdCardNo;

        public void setDrawFlag(String drawFlag) {
            this.drawFlag = drawFlag;
        }

        public void setMinPickMoney(String minPickMoney) {
            this.minPickMoney = minPickMoney;
        }

        public void setCheckBetNum(double checkBetNum) {
            this.checkBetNum = checkBetNum;
        }

        public void setCanDraw(boolean canDraw) {
            this.canDraw = canDraw;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setBankAddress(String bankAddress) {
            this.bankAddress = bankAddress;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public void setAccountStatus(int accountStatus) {
            this.accountStatus = accountStatus;
        }

        public void setCurWnum(int curWnum) {
            this.curWnum = curWnum;
        }

        public void setEnablePick(boolean enablePick) {
            this.enablePick = enablePick;
        }

        public void setWnum(int wnum) {
            this.wnum = wnum;
        }

        public void setPickAccounts(List<PickAccount> pickAccounts) {
            this.pickAccounts = pickAccounts;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setValidBetMoney(double validBetMoney) {
            this.validBetMoney = validBetMoney;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setAccountBalance(double accountBalance) {
            this.accountBalance = accountBalance;
        }

        public void setStrategy(PickMoneyData.Strategy strategy) {
            this.strategy = strategy;
        }

        public void setMaxPickMoney(String maxPickMoney) {
            this.maxPickMoney = maxPickMoney;
        }

        public void setNeedUpdCardNo(boolean needUpdCardNo) {
            this.needUpdCardNo = needUpdCardNo;
        }

        public String getDrawFlag() {
            return drawFlag;
        }

        public String getMinPickMoney() {
            return minPickMoney;
        }

        public double getCheckBetNum() {
            return checkBetNum;
        }

        public boolean isCanDraw() {
            return canDraw;
        }

        public String getBankName() {
            return bankName;
        }

        public String getUserName() {
            return userName;
        }

        public String getBankAddress() {
            return bankAddress;
        }

        public String getCardNo() {
            return cardNo;
        }

        public int getAccountStatus() {
            return accountStatus;
        }

        public int getCurWnum() {
            return curWnum;
        }

        public boolean isEnablePick() {
            return enablePick;
        }

        public int getWnum() {
            return wnum;
        }

        public List<PickAccount> getPickAccounts() {
            return pickAccounts;
        }

        public String getStartTime() {
            return startTime;
        }

        public double getValidBetMoney() {
            return validBetMoney;
        }

        public String getEndTime() {
            return endTime;
        }

        public double getAccountBalance() {
            return accountBalance;
        }

        public PickMoneyData.Strategy getStrategy() {
            return strategy;
        }

        public String getMaxPickMoney() {
            return maxPickMoney;
        }

        public boolean isNeedUpdCardNo() {
            return needUpdCardNo;
        }

        public class PickAccount {
            /**
             * usdtAddress : null
             * alipayName : null
             * bankName : 北京农商银行
             * type : 1
             * userName : **uo
             * alipayAccount : null
             * bankAddress : null
             * cardNo : ************1234
             */
            private String usdtAddress;
            private String alipayName;
            private String bankName;
            private int type;
            private String userName;
            private String alipayAccount;
            private String bankAddress;
            private String cardNo;
            private String gopayName;
            private String goapayaccount;
            private String okpayName;
            private String okapayaccount;
            private String topayName;
            private String topayaccount;
            private String vpayName;
            private String vpayaccount;

            public void setUsdtAddress(String usdtAddress) {
                this.usdtAddress = usdtAddress;
            }

            public void setAlipayName(String alipayName) {
                this.alipayName = alipayName;
            }

            public void setBankName(String bankName) {
                this.bankName = bankName;
            }

            public void setType(int type) {
                this.type = type;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public void setAlipayAccount(String alipayAccount) {
                this.alipayAccount = alipayAccount;
            }

            public void setBankAddress(String bankAddress) {
                this.bankAddress = bankAddress;
            }

            public void setCardNo(String cardNo) {
                this.cardNo = cardNo;
            }

            public String getUsdtAddress() {
                return usdtAddress;
            }

            public String getAlipayName() {
                return alipayName;
            }

            public String getBankName() {
                return bankName;
            }

            public int getType() {
                return type;
            }

            public String getUserName() {
                return userName;
            }

            public String getAlipayAccount() {
                return alipayAccount;
            }

            public String getBankAddress() {
                return bankAddress;
            }

            public String getCardNo() {
                return cardNo;
            }

            public String getGopayName() {
                return gopayName;
            }
            public String getGopayAccount() {
                return goapayaccount;
            }

            public String getOkpayName() {
                return okpayName;
            }
            public String getOkpayAccount() {
                return okapayaccount;
            }

            public String getTopayName() { return topayName; }
            public String getTopayaccount() {
                return topayaccount;
            }

            public String getVpayName() {return vpayName;}
            public String getVpayaccount() {return vpayaccount;}
        }
    }
}
