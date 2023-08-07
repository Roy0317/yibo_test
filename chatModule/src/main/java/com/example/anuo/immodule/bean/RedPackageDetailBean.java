package com.example.anuo.immodule.bean;

import java.util.List;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :03/10/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class RedPackageDetailBean {

    /**
     * msg : 操作成功。
     * code : R7032
     * success : true
     * source : {"parent":{"balance":"16.72","countNum":2,"hasNum":1,"remark":"恭喜发财，大吉大利！","countMoney":"21"},"redStatus":1,"other":[{"money":"4.28","nickName":"","levelName":"会员王者","userType":4,"avatar":"0","id":"99814d93b34d42d9a1aea819a8c75b3e","account":"test1","levelIcon":"","onLine":false}],"c":{"nickName":"","levelName":"会员王者","avatar":"0","totalCount":0,"type":2,"userId":"99814d93b34d42d9a1aea819a8c75b3e","parentId":"82195ea2cbbb416681fac2ed47dccd9c","remaining":"4.28","money":"4.28","createTime":1570081968602,"userType":4,"id":"a2d97e4659894ad5aaf9d1c2a1da69eb","account":"test1","levelIcon":"","stationId":"yunt0Chat_2","status":1}}
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

    public static class SourceBean {
        /**
         * parent : {"balance":"16.72","countNum":2,"hasNum":1,"remark":"恭喜发财，大吉大利！","countMoney":"21"}
         * redStatus : 1
         * other : [{"money":"4.28","nickName":"","levelName":"会员王者","userType":4,"avatar":"0","id":"99814d93b34d42d9a1aea819a8c75b3e","account":"test1","levelIcon":"","onLine":false}]
         * c : {"nickName":"","levelName":"会员王者","avatar":"0","totalCount":0,"type":2,"userId":"99814d93b34d42d9a1aea819a8c75b3e","parentId":"82195ea2cbbb416681fac2ed47dccd9c","remaining":"4.28","money":"4.28","createTime":1570081968602,"userType":4,"id":"a2d97e4659894ad5aaf9d1c2a1da69eb","account":"test1","levelIcon":"","stationId":"yunt0Chat_2","status":1}
         */

        private ParentBean parent;
        private int redStatus;
        private CBean c;
        private List<OtherBean> other;

        public ParentBean getParent() {
            return parent;
        }

        public void setParent(ParentBean parent) {
            this.parent = parent;
        }

        public int getRedStatus() {
            return redStatus;
        }

        public void setRedStatus(int redStatus) {
            this.redStatus = redStatus;
        }

        public CBean getC() {
            return c;
        }

        public void setC(CBean c) {
            this.c = c;
        }

        public List<OtherBean> getOther() {
            return other;
        }

        public void setOther(List<OtherBean> other) {
            this.other = other;
        }

        public static class ParentBean {
            /**
             * balance : 16.72
             * countNum : 2
             * hasNum : 1
             * remark : 恭喜发财，大吉大利！
             * countMoney : 21
             */

            private String balance;
            private int countNum;
            private int hasNum;
            private String remark;
            private String countMoney;

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public int getCountNum() {
                return countNum;
            }

            public void setCountNum(int countNum) {
                this.countNum = countNum;
            }

            public int getHasNum() {
                return hasNum;
            }

            public void setHasNum(int hasNum) {
                this.hasNum = hasNum;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getCountMoney() {
                return countMoney;
            }

            public void setCountMoney(String countMoney) {
                this.countMoney = countMoney;
            }
        }

        public static class CBean {
            /**
             * nickName :
             * levelName : 会员王者
             * avatar : 0
             * totalCount : 0
             * type : 2
             * userId : 99814d93b34d42d9a1aea819a8c75b3e
             * parentId : 82195ea2cbbb416681fac2ed47dccd9c
             * remaining : 4.28
             * money : 4.28
             * createTime : 1570081968602
             * userType : 4
             * id : a2d97e4659894ad5aaf9d1c2a1da69eb
             * account : test1
             * levelIcon :
             * stationId : yunt0Chat_2
             * status : 1
             */

            private String nickName;
            private String levelName;
            private String avatar;
            private int totalCount;
            private int type;
            private String userId;
            private String parentId;
            private String remaining;
            private String money;
            private long createTime;
            private int userType;
            private String id;
            private String account;
            private String levelIcon;
            private String stationId;
            private int status;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getLevelName() {
                return levelName;
            }

            public void setLevelName(String levelName) {
                this.levelName = levelName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getRemaining() {
                return remaining;
            }

            public void setRemaining(String remaining) {
                this.remaining = remaining;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getStationId() {
                return stationId;
            }

            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class OtherBean {
            /**
             * money : 4.28
             * nickName :
             * levelName : 会员王者
             * userType : 4
             * avatar : 0
             * id : 99814d93b34d42d9a1aea819a8c75b3e
             * account : test1
             * levelIcon :
             * onLine : false
             */

            private String money;
            private String nickName;
            private String levelName;
            private int userType;
            private String avatar;
            private String id;
            private String account;
            private String levelIcon;
            private boolean onLine;
            private String nativeAccount;


            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getLevelName() {
                return levelName;
            }

            public void setLevelName(String levelName) {
                this.levelName = levelName;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public boolean isOnLine() {
                return onLine;
            }

            public void setOnLine(boolean onLine) {
                this.onLine = onLine;
            }

            public String getNativeAccount() {
                return nativeAccount;
            }

            public void setNativeAccount(String nativeAccount) {
                this.nativeAccount = nativeAccount;
            }
        }
    }
}
