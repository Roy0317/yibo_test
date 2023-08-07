package com.yibo.yiboapp.entify;

public class BoundsBean {


    /**
     * data : {"bonusOdds":4.442,"detailDesc":"对冠军、亚军两个号码总和的\u201c大（大于11）小（小于11）、单（单数）双（双数）\u201d（注：开出11为和局，退回所有投注金额）型态进行购买，所选号码的型态与开奖号码的型态相同，即为中奖。","showRakeback":false,"winExample":"投注方案：大；开奖号码前两位：0708，总和15，即中总和大。","playMethod":"从和值的\u201c大、小、单、双\u201d中至少选一个组成一注。"}
     * success : true
     */

    private DataBean data;
    private boolean success;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * bonusOdds : 4.442
         * detailDesc : 对冠军、亚军两个号码总和的“大（大于11）小（小于11）、单（单数）双（双数）”（注：开出11为和局，退回所有投注金额）型态进行购买，所选号码的型态与开奖号码的型态相同，即为中奖。
         * showRakeback : false
         * winExample : 投注方案：大；开奖号码前两位：0708，总和15，即中总和大。
         * playMethod : 从和值的“大、小、单、双”中至少选一个组成一注。
         */

        private double bonusOdds;
        private String detailDesc;
        private boolean showRakeback;
        private String winExample;
        private String playMethod;

        public double getBonusOdds() {
            return bonusOdds;
        }

        public void setBonusOdds(double bonusOdds) {
            this.bonusOdds = bonusOdds;
        }

        public String getDetailDesc() {
            return detailDesc;
        }

        public void setDetailDesc(String detailDesc) {
            this.detailDesc = detailDesc;
        }

        public boolean isShowRakeback() {
            return showRakeback;
        }

        public void setShowRakeback(boolean showRakeback) {
            this.showRakeback = showRakeback;
        }

        public String getWinExample() {
            return winExample;
        }

        public void setWinExample(String winExample) {
            this.winExample = winExample;
        }

        public String getPlayMethod() {
            return playMethod;
        }

        public void setPlayMethod(String playMethod) {
            this.playMethod = playMethod;
        }
    }
}
