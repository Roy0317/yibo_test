package com.yibo.yiboapp.entify;

import androidx.annotation.Nullable;

/**
 * Author: Ray
 * created on 2018/10/15
 * description : 我的推荐信息诗句实体类
 */
public class MyRecommendInfoBean {

    /**
     * success : true
     * accessToken : 0059c38a-bca2-45ed-9744-b731551754ac
     * content : {"data":{"cpRolling":2,"egameRolling":2,"id":46,"linkKey":"22840","linkUrl":"https://skyy002.yb876.com/registersFixedAlone.do?init=22840","linkUrlEn":"https://skyy002.yb876.com/vote_topic_22840.do","realRolling":2,"sbSportRolling":2,"sportRolling":2,"stationId":24,"status":2,"type":2,"userAccount":"testray","userId":1597},"username":"testray"}
     */

    private boolean success;
    private String accessToken;
    private ContentBean content;

    @Nullable
     String msg;
    @Nullable
     int code;

    @Nullable
    public String getMsg() {
        return msg;
    }

    public void setMsg(@Nullable String msg) {
        this.msg = msg;
    }

    @Nullable
    public int getCode() {
        return code;
    }

    public void setCode(@Nullable int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * data : {"cpRolling":2,"egameRolling":2,"id":46,"linkKey":"22840","linkUrl":"https://skyy002.yb876.com/registersFixedAlone.do?init=22840","linkUrlEn":"https://skyy002.yb876.com/vote_topic_22840.do","realRolling":2,"sbSportRolling":2,"sportRolling":2,"stationId":24,"status":2,"type":2,"userAccount":"testray","userId":1597}
         * username : testray
         */

        private DataBean data;
        private String username;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public static class DataBean {
            /**
             * cpRolling : 2.0
             * egameRolling : 2.0
             * id : 46
             * linkKey : 22840
             * linkUrl : https://skyy002.yb876.com/registersFixedAlone.do?init=22840
             * linkUrlEn : https://skyy002.yb876.com/vote_topic_22840.do
             * realRolling : 2.0
             * sbSportRolling : 2.0
             * sportRolling : 2.0
             * stationId : 24
             * status : 2
             * type : 2
             * userAccount : testray
             * userId : 1597
             */

            private double cpRolling;
            private double egameRolling;
            private double chessRolling;
            private double lhcRolling;
            private int id;
            private String linkKey;
            private String linkUrl;//推荐地址:
            private String linkUrlEn;//加密推荐地址
            private double realRolling;
            private double sbSportRolling;
            private double sportRolling;
            private double thirdSportRolling;
            private int stationId;
            private int status;
            private int type;
            private String userAccount;
            private int userId;

            public double getThirdSportRolling() {
                return thirdSportRolling;
            }

            public void setThirdSportRolling(double thirdSportRolling) {
                this.thirdSportRolling = thirdSportRolling;
            }

            public double getChessRolling() {
                return chessRolling;
            }

            public void setChessRolling(double chessRolling) {
                this.chessRolling = chessRolling;
            }

            public double getLhcRolling() {
                return lhcRolling;
            }

            public void setLhcRolling(double lhcRolling) {
                this.lhcRolling = lhcRolling;
            }

            public double getCpRolling() {
                return cpRolling;
            }

            public void setCpRolling(double cpRolling) {
                this.cpRolling = cpRolling;
            }

            public double getEgameRolling() {
                return egameRolling;
            }

            public void setEgameRolling(double egameRolling) {
                this.egameRolling = egameRolling;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLinkKey() {
                return linkKey;
            }

            public void setLinkKey(String linkKey) {
                this.linkKey = linkKey;
            }

            public String getLinkUrl() {
                return linkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
            }

            public String getLinkUrlEn() {
                return linkUrlEn;
            }

            public void setLinkUrlEn(String linkUrlEn) {
                this.linkUrlEn = linkUrlEn;
            }

            public double getRealRolling() {
                return realRolling;
            }

            public void setRealRolling(double realRolling) {
                this.realRolling = realRolling;
            }

            public double getSbSportRolling() {
                return sbSportRolling;
            }

            public void setSbSportRolling(double sbSportRolling) {
                this.sbSportRolling = sbSportRolling;
            }

            public double getSportRolling() {
                return sportRolling;
            }

            public void setSportRolling(double sportRolling) {
                this.sportRolling = sportRolling;
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

            public String getUserAccount() {
                return userAccount;
            }

            public void setUserAccount(String userAccount) {
                this.userAccount = userAccount;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
