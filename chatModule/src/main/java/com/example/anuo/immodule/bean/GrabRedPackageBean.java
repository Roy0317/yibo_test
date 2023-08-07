package com.example.anuo.immodule.bean;

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
 * Date  :02/10/2019
 * Desc  :com.example.anuo.immodule.bean
 */
public class GrabRedPackageBean {

    /**
     * msg : 操作成功。
     * code : R7027
     * success : true
     * source : {"pickData":{"newPay":{"id":"865d4470c57c48e6bdab0d83c67278a4","userId":"99814d93b34d42d9a1aea819a8c75b3e","type":2,"money":12,"createTime":"2019-10-02T08:07:51.989+0000","parentId":"9ec7a068cf3a496193159e756010bdfc","remaining":12,"remark":"","status":"","stationId":"yunt0Chat_2","name":"","totalCount":0,"remarkId":"","sysPayId":"","payConfigId":""},"success":true,"payId":"9ec7a068cf3a496193159e756010bdfc"}}
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
         * pickData : {"newPay":{"id":"865d4470c57c48e6bdab0d83c67278a4","userId":"99814d93b34d42d9a1aea819a8c75b3e","type":2,"money":12,"createTime":"2019-10-02T08:07:51.989+0000","parentId":"9ec7a068cf3a496193159e756010bdfc","remaining":12,"remark":"","status":"","stationId":"yunt0Chat_2","name":"","totalCount":0,"remarkId":"","sysPayId":"","payConfigId":""},"success":true,"payId":"9ec7a068cf3a496193159e756010bdfc"}
         */

        private PickDataBean pickData;

        private String payId;

        private String status;

        private PickDataBean.NewPayBean newPay;

        public PickDataBean.NewPayBean getNewPay() {
            return newPay;
        }

        public void setNewPay(PickDataBean.NewPayBean newPay) {
            this.newPay = newPay;
        }

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public PickDataBean getPickData() {
            return pickData;
        }

        public void setPickData(PickDataBean pickData) {
            this.pickData = pickData;
        }

        public static class PickDataBean {
            /**
             * newPay : {"id":"865d4470c57c48e6bdab0d83c67278a4","userId":"99814d93b34d42d9a1aea819a8c75b3e","type":2,"money":12,"createTime":"2019-10-02T08:07:51.989+0000","parentId":"9ec7a068cf3a496193159e756010bdfc","remaining":12,"remark":"","status":"","stationId":"yunt0Chat_2","name":"","totalCount":0,"remarkId":"","sysPayId":"","payConfigId":""}
             * success : true
             * payId : 9ec7a068cf3a496193159e756010bdfc
             */

            private NewPayBean newPay;
            private boolean success;
            private String payId;

            public NewPayBean getNewPay() {
                return newPay;
            }

            public void setNewPay(NewPayBean newPay) {
                this.newPay = newPay;
            }

            public boolean isSuccess() {
                return success;
            }

            public void setSuccess(boolean success) {
                this.success = success;
            }

            public String getPayId() {
                return payId;
            }

            public void setPayId(String payId) {
                this.payId = payId;
            }

            public static class NewPayBean {
                /**
                 * id : 865d4470c57c48e6bdab0d83c67278a4
                 * userId : 99814d93b34d42d9a1aea819a8c75b3e
                 * type : 2
                 * money : 12
                 * createTime : 2019-10-02T08:07:51.989+0000
                 * parentId : 9ec7a068cf3a496193159e756010bdfc
                 * remaining : 12
                 * remark :
                 * status :
                 * stationId : yunt0Chat_2
                 * name :
                 * totalCount : 0
                 * remarkId :
                 * sysPayId :
                 * payConfigId :
                 */


                private String id;
                private String userId;
                private int type;
                private String  money;
                private String createTime;
                private String parentId;
                private String  remaining;
                private String remark;
                private String status;
                private String stationId;
                private String name;
                private String  totalCount;
                private String remarkId;
                private String sysPayId;
                private String payConfigId;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getMoney() {
                    return money;
                }

                public void setMoney(String money) {
                    this.money = money;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
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

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getStationId() {
                    return stationId;
                }

                public void setStationId(String stationId) {
                    this.stationId = stationId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTotalCount() {
                    return totalCount;
                }

                public void setTotalCount(String totalCount) {
                    this.totalCount = totalCount;
                }

                public String getRemarkId() {
                    return remarkId;
                }

                public void setRemarkId(String remarkId) {
                    this.remarkId = remarkId;
                }

                public String getSysPayId() {
                    return sysPayId;
                }

                public void setSysPayId(String sysPayId) {
                    this.sysPayId = sysPayId;
                }

                public String getPayConfigId() {
                    return payConfigId;
                }

                public void setPayConfigId(String payConfigId) {
                    this.payConfigId = payConfigId;
                }
            }
        }
    }
}
