package com.yibo.yiboapp.entify;

import java.util.List;

/*
* 首页弹窗时间间隔
* */
public class MainPopupTime {

    private List<MainPopupTimeDetail> data;

    public List<MainPopupTimeDetail> getData() {
        return data;
    }

    public void setData(List<MainPopupTimeDetail> data) {
        this.data = data;
    }

   public static class MainPopupTimeDetail{

        private String userName;

        private long spaceTime; //间隔时间

        private long lastTime; //上次弹窗时间

       public MainPopupTimeDetail(String userName, long spaceTime, long lastTime){
           this.userName = userName;
           this.spaceTime = spaceTime;
           this.lastTime = lastTime;
       }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public long getSpaceTime() {
            return spaceTime;
        }

        public void setSpaceTime(long spaceTime) {
            this.spaceTime = spaceTime;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }
    }

}
