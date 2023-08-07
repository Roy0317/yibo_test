package com.example.anuo.immodule.bean;

import com.example.anuo.immodule.bean.base.BaseBean;

import java.util.List;

public class ChatWinningListBean extends BaseBean {


    /**
     * source : {"winningList":[{"userLevelName":"砖石会员","prizeDate":"2019-11-23T22:27:14.000+0000","prizeType":"2","prizeMoney":794.77,"id":"d24f89ee910242cbb6ef83365f39163d","userName":"***d5jt","prizeProject":"疯狂赛车","stationId":"yjtest1_3"},{"userLevelName":"帝尊会员","prizeDate":"2019-11-27T09:14:31.000+0000","prizeType":"2","prizeMoney":557.32,"id":"52b042e1542f419188cf2c48f43ed867","userName":"***um16","prizeProject":"十分彩","stationId":"yjtest1_3"},{"userLevelName":"帝尊会员","prizeDate":"2019-11-23T12:04:23.000+0000","prizeType":"2","prizeMoney":334,"id":"8474d281ded343b8b72d15b4629ce1e8","userName":"vmi***91nz","prizeProject":"香港极速彩","stationId":"yjtest1_3"},{"userLevelName":"砖石会员","prizeDate":"2019-11-22T22:55:46.000+0000","prizeType":"2","prizeMoney":256.59,"id":"917c7d4eba0b4a67b80da3adbfa70efc","userName":"***wrdu","prizeProject":"十分赛车","stationId":"yjtest1_3"},{"userLevelName":"渣渣会员","prizeDate":"2019-11-24T22:58:42.000+0000","prizeType":"2","prizeMoney":240.49,"id":"f52c1a64b91b424a8f9ca3660dd717d2","userName":"s4a***viuq","prizeProject":"香港极速彩","stationId":"yjtest1_3"},{"userLevelName":"王者会员","prizeDate":"2019-11-29T01:24:33.000+0000","prizeType":"2","prizeMoney":217.71,"id":"206109bc6fbe41b8b5fd3a76e1621b99","userName":"***0vp1","prizeProject":"澳洲飞艇","stationId":"yjtest1_3"},{"userLevelName":"王者会员","prizeDate":"2019-11-29T01:22:08.000+0000","prizeType":"2","prizeMoney":209.38,"id":"2cd7b8669e2f4eee9829fdd17a516297","userName":"***1g2i","prizeProject":"幸运农场","stationId":"yjtest1_3"},{"userLevelName":"帝尊会员","prizeDate":"2019-11-26T15:41:28.000+0000","prizeType":"2","prizeMoney":159.69,"id":"18c4c5fc55764c7ea9bb5f11921662d8","userName":"***7pei","prizeProject":"疯狂赛车","stationId":"yjtest1_3"},{"userLevelName":"天王会员","prizeDate":"2019-11-27T02:21:13.000+0000","prizeType":"2","prizeMoney":134.44,"id":"8c0df6e7064548718d52650c08603843","userName":"***4c4f","prizeProject":"加拿大28","stationId":"yjtest1_3"},{"userLevelName":"渣渣会员","prizeDate":"2019-11-26T18:29:20.000+0000","prizeType":"2","prizeMoney":60.14,"id":"c5e8cdbea39e4288970037b09eb73043","userName":"***34i3","prizeProject":"广东11选5","stationId":"yjtest1_3"}]}
     */

    private SourceBean source;

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public static class SourceBean {
        private List<WinningListBean> winningList;

        public List<WinningListBean> getWinningList() {
            return winningList;
        }

        public void setWinningList(List<WinningListBean> winningList) {
            this.winningList = winningList;
        }

        public static class WinningListBean {
            /**
             * userLevelName : 砖石会员
             * prizeDate : 2019-11-23T22:27:14.000+0000
             * prizeType : 2
             * prizeMoney : 794.77
             * id : d24f89ee910242cbb6ef83365f39163d
             * userName : ***d5jt
             * prizeProject : 疯狂赛车
             * stationId : yjtest1_3
             */

            private int signalLogo;
            private String imageUrl;
            private String userLevelName;
            private String prizeDate;
            private String prizeType;
            private double prizeMoney;
            private String id;
            private String userName;
            private String prizeProject;
            private String stationId;

            public void setSignalLogo(int signalLogo) {
                this.signalLogo = signalLogo;
            }

            public int getSignalLogo() {
                return signalLogo;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getUserLevelName() {
                return userLevelName;
            }

            public void setUserLevelName(String userLevelName) {
                this.userLevelName = userLevelName;
            }

            public String getPrizeDate() {
                return prizeDate;
            }

            public void setPrizeDate(String prizeDate) {
                this.prizeDate = prizeDate;
            }

            public String getPrizeType() {
                return prizeType;
            }

            public void setPrizeType(String prizeType) {
                this.prizeType = prizeType;
            }

            public double getPrizeMoney() {
                return prizeMoney;
            }

            public void setPrizeMoney(double prizeMoney) {
                this.prizeMoney = prizeMoney;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getPrizeProject() {
                return prizeProject;
            }

            public void setPrizeProject(String prizeProject) {
                this.prizeProject = prizeProject;
            }

            public String getStationId() {
                return stationId;
            }

            public void setStationId(String stationId) {
                this.stationId = stationId;
            }
        }
    }
}
