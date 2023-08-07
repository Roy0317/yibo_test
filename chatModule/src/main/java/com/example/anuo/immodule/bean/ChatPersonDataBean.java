package com.example.anuo.immodule.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatPersonDataBean implements Parcelable {

    /**
     * msg : 操作成功。
     * code : R7038
     * success : true
     * source : {"depositMobileUrl":"https://skytest1.yunji9.com/m/v2/index.do#/myPay","lotteryImageUrl":"https://skytest1.yunji9.com/common/lot/images/gameIcon/","winLost":{"allBetZhuShu":0,"sumDepost":0,"sumDepostToday":0,"allWinZhuShu":0,"allWinAmount":0,"yingkuiAmount":0,"betNum":0,"success":true,"winPer":0,"allBetAmount":0},"level":"渣渣会员","nickName":null,"accountType":1,"avatar":"https://yj8.me/img/h3eM/i5nXTjYxH.jpg","drawUrl":"https://skytest1.yunji9.com/userCenter/finance/withdraw.do","depositUrl":"https://skytest1.yunji9.com/userCenter/finance/recharge/.do","drawMobileUrl":"https://skytest1.yunji9.com/m/v2/index.do#/withdrawDep","mobileUrl":"https://skytest1.yunji9.com/m/v2/index.do","account":"btest01","levelIcon":""}
     * status : b1
     */

    private String msg;
    private String code;
    private boolean success;
    private SourceBean source;
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class SourceBean implements Parcelable {
        /**
         * depositMobileUrl : https://skytest1.yunji9.com/m/v2/index.do#/myPay
         * lotteryImageUrl : https://skytest1.yunji9.com/common/lot/images/gameIcon/
         * winLost : {"allBetZhuShu":0,"sumDepost":0,"sumDepostToday":0,"allWinZhuShu":0,"allWinAmount":0,"yingkuiAmount":0,"betNum":0,"success":true,"winPer":0,"allBetAmount":0}
         * level : 渣渣会员
         * nickName : null
         * accountType : 1
         * avatar : https://yj8.me/img/h3eM/i5nXTjYxH.jpg
         * drawUrl : https://skytest1.yunji9.com/userCenter/finance/withdraw.do
         * depositUrl : https://skytest1.yunji9.com/userCenter/finance/recharge/.do
         * drawMobileUrl : https://skytest1.yunji9.com/m/v2/index.do#/withdrawDep
         * mobileUrl : https://skytest1.yunji9.com/m/v2/index.do
         * account : btest01
         * levelIcon :
         */

        private String depositMobileUrl;
        private String lotteryImageUrl;
        private WinLostBean winLost;
        private String level;
        private String nickName;
        private int accountType;
        private String avatar;
        private String drawUrl;
        private String depositUrl;
        private String drawMobileUrl;
        private String mobileUrl;
        private String account;
        private String levelIcon;

        public String getDepositMobileUrl() {
            return depositMobileUrl;
        }

        public void setDepositMobileUrl(String depositMobileUrl) {
            this.depositMobileUrl = depositMobileUrl;
        }

        public String getLotteryImageUrl() {
            return lotteryImageUrl;
        }

        public void setLotteryImageUrl(String lotteryImageUrl) {
            this.lotteryImageUrl = lotteryImageUrl;
        }

        public WinLostBean getWinLost() {
            return winLost;
        }

        public void setWinLost(WinLostBean winLost) {
            this.winLost = winLost;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDrawUrl() {
            return drawUrl;
        }

        public void setDrawUrl(String drawUrl) {
            this.drawUrl = drawUrl;
        }

        public String getDepositUrl() {
            return depositUrl;
        }

        public void setDepositUrl(String depositUrl) {
            this.depositUrl = depositUrl;
        }

        public String getDrawMobileUrl() {
            return drawMobileUrl;
        }

        public void setDrawMobileUrl(String drawMobileUrl) {
            this.drawMobileUrl = drawMobileUrl;
        }

        public String getMobileUrl() {
            return mobileUrl;
        }

        public void setMobileUrl(String mobileUrl) {
            this.mobileUrl = mobileUrl;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getLevelIcon() {
            return levelIcon;
        }

        public void setLevelIcon(String levelIcon) {
            this.levelIcon = levelIcon;
        }

        public static class WinLostBean implements Parcelable {
            /**
             * allBetZhuShu : 0.0
             * sumDepost : 0.0
             * sumDepostToday : 0.0
             * allWinZhuShu : 0.0
             * allWinAmount : 0.0
             * yingkuiAmount : 0.0
             * betNum : 0.0
             * success : true
             * winPer : 0.0
             * allBetAmount : 0.0
             */

            private double allBetZhuShu;
            private double sumDepost;
            private double sumDepostToday;
            private double allWinZhuShu;
            private double allWinAmount;
            private double yingkuiAmount;
            private double betNum;
            private boolean success;
            private double winPer;
            private double allBetAmount;

            public double getAllBetZhuShu() {
                return allBetZhuShu;
            }

            public void setAllBetZhuShu(double allBetZhuShu) {
                this.allBetZhuShu = allBetZhuShu;
            }

            public double getSumDepost() {
                return sumDepost;
            }

            public void setSumDepost(double sumDepost) {
                this.sumDepost = sumDepost;
            }

            public double getSumDepostToday() {
                return sumDepostToday;
            }

            public void setSumDepostToday(double sumDepostToday) {
                this.sumDepostToday = sumDepostToday;
            }

            public double getAllWinZhuShu() {
                return allWinZhuShu;
            }

            public void setAllWinZhuShu(double allWinZhuShu) {
                this.allWinZhuShu = allWinZhuShu;
            }

            public double getAllWinAmount() {
                return allWinAmount;
            }

            public void setAllWinAmount(double allWinAmount) {
                this.allWinAmount = allWinAmount;
            }

            public double getYingkuiAmount() {
                return yingkuiAmount;
            }

            public void setYingkuiAmount(double yingkuiAmount) {
                this.yingkuiAmount = yingkuiAmount;
            }

            public double getBetNum() {
                return betNum;
            }

            public void setBetNum(double betNum) {
                this.betNum = betNum;
            }

            public boolean isSuccess() {
                return success;
            }

            public void setSuccess(boolean success) {
                this.success = success;
            }

            public double getWinPer() {
                return winPer;
            }

            public void setWinPer(double winPer) {
                this.winPer = winPer;
            }

            public double getAllBetAmount() {
                return allBetAmount;
            }

            public void setAllBetAmount(double allBetAmount) {
                this.allBetAmount = allBetAmount;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeDouble(this.allBetZhuShu);
                dest.writeDouble(this.sumDepost);
                dest.writeDouble(this.sumDepostToday);
                dest.writeDouble(this.allWinZhuShu);
                dest.writeDouble(this.allWinAmount);
                dest.writeDouble(this.yingkuiAmount);
                dest.writeDouble(this.betNum);
                dest.writeByte(this.success ? (byte) 1 : (byte) 0);
                dest.writeDouble(this.winPer);
                dest.writeDouble(this.allBetAmount);
            }

            public WinLostBean() {
            }

            protected WinLostBean(Parcel in) {
                this.allBetZhuShu = in.readDouble();
                this.sumDepost = in.readDouble();
                this.sumDepostToday = in.readDouble();
                this.allWinZhuShu = in.readDouble();
                this.allWinAmount = in.readDouble();
                this.yingkuiAmount = in.readDouble();
                this.betNum = in.readDouble();
                this.success = in.readByte() != 0;
                this.winPer = in.readDouble();
                this.allBetAmount = in.readDouble();
            }

            public static final Creator<WinLostBean> CREATOR = new Creator<WinLostBean>() {
                @Override
                public WinLostBean createFromParcel(Parcel source) {
                    return new WinLostBean(source);
                }

                @Override
                public WinLostBean[] newArray(int size) {
                    return new WinLostBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.depositMobileUrl);
            dest.writeString(this.lotteryImageUrl);
            dest.writeParcelable(this.winLost, flags);
            dest.writeString(this.level);
            dest.writeString(this.nickName);
            dest.writeInt(this.accountType);
            dest.writeString(this.avatar);
            dest.writeString(this.drawUrl);
            dest.writeString(this.depositUrl);
            dest.writeString(this.drawMobileUrl);
            dest.writeString(this.mobileUrl);
            dest.writeString(this.account);
            dest.writeString(this.levelIcon);
        }

        public SourceBean() {
        }

        protected SourceBean(Parcel in) {
            this.depositMobileUrl = in.readString();
            this.lotteryImageUrl = in.readString();
            this.winLost = in.readParcelable(WinLostBean.class.getClassLoader());
            this.level = in.readString();
            this.nickName = in.readString();
            this.accountType = in.readInt();
            this.avatar = in.readString();
            this.drawUrl = in.readString();
            this.depositUrl = in.readString();
            this.drawMobileUrl = in.readString();
            this.mobileUrl = in.readString();
            this.account = in.readString();
            this.levelIcon = in.readString();
        }

        public static final Creator<SourceBean> CREATOR = new Creator<SourceBean>() {
            @Override
            public SourceBean createFromParcel(Parcel source) {
                return new SourceBean(source);
            }

            @Override
            public SourceBean[] newArray(int size) {
                return new SourceBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg);
        dest.writeString(this.code);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.source, flags);
        dest.writeString(this.status);
    }

    public ChatPersonDataBean() {
    }

    protected ChatPersonDataBean(Parcel in) {
        this.msg = in.readString();
        this.code = in.readString();
        this.success = in.readByte() != 0;
        this.source = in.readParcelable(SourceBean.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Creator<ChatPersonDataBean> CREATOR = new Creator<ChatPersonDataBean>() {
        @Override
        public ChatPersonDataBean createFromParcel(Parcel source) {
            return new ChatPersonDataBean(source);
        }

        @Override
        public ChatPersonDataBean[] newArray(int size) {
            return new ChatPersonDataBean[size];
        }
    };
}
