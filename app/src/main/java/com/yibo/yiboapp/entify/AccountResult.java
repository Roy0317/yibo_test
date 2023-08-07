package com.yibo.yiboapp.entify;

/**
 * Created by johnson on 2017/11/13.
 */

public class AccountResult {
    /**
     * account : lvy2018
     * bankAddress : 湖南省
     * bankName : 工商银行
     * betNum : 0.0
     * cardNo : ************5655
     * city :
     * email :
     * levelInfo : {"allMoney":0,"diffBetNum":0,"allBetNum":0,"levelName":"vip","nextLevel":{"betNum":0,"createDatetime":1503656096240,"depositMoney":1000,"icon":"","id":42,"levelDefault":1,"levelName":"vip1","levelType":1,"memberCount":5149,"remark":"","stationId":34,"status":2},"diffMoney":1000,"hideLevel":false}
     * phone :
     * province : 湖南省
     * qq :
     * sex : 0
     * userName : 薇
     * wechat :
     */

    private String account;
    private String bankAddress;
    private String bankName;
    private double betNum;
    private String cardNo;
    private String city;
    private String email;
    private LevelInfoBean levelInfo;
    private String phone;
    private String province;
    private String qq;
    private int sex;
    private String userName;
    private String wechat;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public double getBetNum() {
        return betNum;
    }

    public void setBetNum(double betNum) {
        this.betNum = betNum;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LevelInfoBean getLevelInfo() {
        return levelInfo;
    }

    public void setLevelInfo(LevelInfoBean levelInfo) {
        this.levelInfo = levelInfo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public static class LevelInfoBean {
        /**
         * allMoney : 0
         * diffBetNum : 0.0
         * allBetNum : 0.0
         * levelName : vip
         * nextLevel : {"betNum":0,"createDatetime":1503656096240,"depositMoney":1000,"icon":"","id":42,"levelDefault":1,"levelName":"vip1","levelType":1,"memberCount":5149,"remark":"","stationId":34,"status":2}
         * diffMoney : 1000
         * hideLevel : false
         */

        private String allMoney;
        private double diffBetNum;
        private double allBetNum;
        private String levelName;
        private NextLevelBean nextLevel;
        private String diffMoney;
        private boolean hideLevel;

        public String getAllMoney() {
            return allMoney;
        }

        public void setAllMoney(String allMoney) {
            this.allMoney = allMoney;
        }

        public double getDiffBetNum() {
            return diffBetNum;
        }

        public void setDiffBetNum(double diffBetNum) {
            this.diffBetNum = diffBetNum;
        }

        public double getAllBetNum() {
            return allBetNum;
        }

        public void setAllBetNum(double allBetNum) {
            this.allBetNum = allBetNum;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public NextLevelBean getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(NextLevelBean nextLevel) {
            this.nextLevel = nextLevel;
        }

        public String getDiffMoney() {
            return diffMoney;
        }

        public void setDiffMoney(String diffMoney) {
            this.diffMoney = diffMoney;
        }

        public boolean isHideLevel() {
            return hideLevel;
        }

        public void setHideLevel(boolean hideLevel) {
            this.hideLevel = hideLevel;
        }

        public static class NextLevelBean {
            /**
             * betNum : 0
             * createDatetime : 1503656096240
             * depositMoney : 1000
             * icon :
             * id : 42
             * levelDefault : 1
             * levelName : vip1
             * levelType : 1
             * memberCount : 5149
             * remark :
             * stationId : 34
             * status : 2
             */

            private int betNum;
            private long createDatetime;
            private String depositMoney;
            private String icon;
            private int id;
            private int levelDefault;
            private String levelName;
            private int levelType;
            private int memberCount;
            private String remark;
            private int stationId;
            private int status;

            public int getBetNum() {
                return betNum;
            }

            public void setBetNum(int betNum) {
                this.betNum = betNum;
            }

            public long getCreateDatetime() {
                return createDatetime;
            }

            public void setCreateDatetime(long createDatetime) {
                this.createDatetime = createDatetime;
            }

            public String getDepositMoney() {
                return depositMoney;
            }

            public void setDepositMoney(String depositMoney) {
                this.depositMoney = depositMoney;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLevelDefault() {
                return levelDefault;
            }

            public void setLevelDefault(int levelDefault) {
                this.levelDefault = levelDefault;
            }

            public String getLevelName() {
                return levelName;
            }

            public void setLevelName(String levelName) {
                this.levelName = levelName;
            }

            public int getLevelType() {
                return levelType;
            }

            public void setLevelType(int levelType) {
                this.levelType = levelType;
            }

            public int getMemberCount() {
                return memberCount;
            }

            public void setMemberCount(int memberCount) {
                this.memberCount = memberCount;
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
        }
    }
}

