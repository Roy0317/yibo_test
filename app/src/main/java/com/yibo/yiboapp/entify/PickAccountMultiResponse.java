package com.yibo.yiboapp.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PickAccountMultiResponse {


    /**
     * USDTLimit : 3
     * BankLimit : 5
     * bankCardTips : 绑定银行卡界面提示语
     * usdtDraw : true
     * cardsList : [{"accountId":93513,"bankAddress":"","bankName":"北京农商银行","cardNo":"123***1234","city":"","createTime":1608797907924,"id":131,"isPrimary":1,"province":"","realName":"***uo","stationId":270,"status":1,"type":1,"userName":"anuo1"},{"accountId":93513,"bankAddress":"","bankName":"北京农商银行","cardNo":"123***1234","city":"","createTime":1608843986597,"id":814,"isPrimary":2,"province":"","realName":"***uo","stationId":270,"status":1,"type":1,"userName":"anuo1"},{"accountId":93513,"bankAddress":"","bankName":"北京农商银行","cardNo":"123***1234","city":"","createTime":1608891929080,"id":1501,"isPrimary":2,"province":"","realName":"***uo","stationId":270,"status":1,"type":1,"userName":"anuo1"},{"accountId":93513,"cardNo":"101***1010","createTime":1608797907939,"id":648,"isPrimary":2,"stationId":270,"type":2,"userName":"anuo1"},{"accountId":93513,"cardNo":"101***1010","createTime":1608843986613,"id":1335,"isPrimary":2,"stationId":270,"type":2,"userName":"anuo1"},{"accountId":93513,"cardNo":"101***1010","createTime":1608891929094,"id":2025,"isPrimary":2,"stationId":270,"type":2,"userName":"anuo1"}]
     */

    private String USDTLimit;
    private String BankLimit;
    private String GOPAYLimit;
    private String OKPAYLimit;
    private String TOPAYLimit;
    private String VPAYLimit;
    private String bankCardTips;
    private boolean usdtDraw;
    private boolean gopayDraw;
    private boolean okpayDraw;
    private List<AccountBean> cardsList;
    private List<AccountBean> alipayList;
    private List<AccountBean> gopayList;
    private List<AccountBean> okpayList;
    private List<AccountBean> topayList;
    private List<AccountBean> vpayList;

    public String getUSDTLimit() {
        return USDTLimit;
    }

    public void setUSDTLimit(String USDTLimit) {
        this.USDTLimit = USDTLimit;
    }

    public String getBankLimit() {
        return BankLimit;
    }

    public void setBankLimit(String BankLimit) {
        this.BankLimit = BankLimit;
    }

    public String getGOPAYLimit() {
        return GOPAYLimit;
    }

    public void setGOPAYLimit(String GOPAYLimit) {
        this.GOPAYLimit = GOPAYLimit;
    }

    public String getOKPAYLimit() {
        return OKPAYLimit;
    }

    public void setOKPAYLimit(String OKPAYLimit) {
        this.OKPAYLimit = OKPAYLimit;
    }

    public String getTOPAYLimit() {
        return TOPAYLimit;
    }

    public void setTOPAYLimit(String TOPAYLimit) {
        this.TOPAYLimit = TOPAYLimit;
    }

    public String getVPAYLimit() {
        return VPAYLimit;
    }

    public void setVPAYLimit(String VPAYLimit) {
        this.VPAYLimit = VPAYLimit;
    }

    public String getBankCardTips() {
        return bankCardTips;
    }

    public void setBankCardTips(String bankCardTips) {
        this.bankCardTips = bankCardTips;
    }

    public boolean isUsdtDraw() {
        return usdtDraw;
    }

    public void setUsdtDraw(boolean usdtDraw) {
        this.usdtDraw = usdtDraw;
    }

    public boolean isGopayDraw() {
        return gopayDraw;
    }

    public void setGopayDraw(boolean gopayDraw) {
        this.gopayDraw = gopayDraw;
    }

    public boolean isOkpayDraw() {
        return okpayDraw;
    }

    public void setOkpayDraw(boolean okpayDraw) {
        this.okpayDraw = okpayDraw;
    }

    public List<AccountBean> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<AccountBean> cardsList) {
        this.cardsList = cardsList;
    }

    public List<AccountBean> getAlipayList() {
        return alipayList;
    }

    public List<AccountBean> getGopayList() {
        return gopayList;
    }

    public void setGopayList(List<AccountBean> gopayList) {
        this.gopayList = gopayList;
    }

    public List<AccountBean> getOkpayList() {
        return okpayList;
    }

    public void setOkpayList(List<AccountBean> okpayList) {
        this.okpayList = okpayList;
    }

    public static class AccountBean {
        /**
         * accountId : 93513
         * bankAddress :
         * bankName : 北京农商银行
         * cardNo : 123***1234
         * city :
         * createTime : 1608797907924
         * id : 131
         * isPrimary : 1
         * province :
         * realName : ***uo
         * stationId : 270
         * status : 1
         * type : 1
         * userName : anuo1
         */

        private Long accountId;
        private String bankAddress;
        private String bankName;
        private String cardNo;
        private String city;
        private long createTime;
        private Long id;
        private int isPrimary;
        private String province;
        private String realName;
        private int stationId;
        private int status;
        private int type;
        private String userName;

        public Long getAccountId() {
            return accountId;
        }

        public void setAccountId(Long accountId) {
            this.accountId = accountId;
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

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Long getID() {
            return id;
        }

        public void setID(Long id) {
            this.id = id;
        }

        public int getIsPrimary() {
            return isPrimary;
        }

        public void setIsPrimary(int isPrimary) {
            this.isPrimary = isPrimary;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String toString() {
            return "AccountBean{" +
                    "accountId=" + accountId +
                    ", bankAddress='" + bankAddress + '\'' +
                    ", bankName='" + bankName + '\'' +
                    ", cardNo='" + cardNo + '\'' +
                    ", city='" + city + '\'' +
                    ", createTime=" + createTime +
                    ", id=" + id +
                    ", isPrimary=" + isPrimary +
                    ", province='" + province + '\'' +
                    ", realName='" + realName + '\'' +
                    ", stationId=" + stationId +
                    ", status=" + status +
                    ", type=" + type +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }
}
