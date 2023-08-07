package com.yibo.yiboapp.entify;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.entify
 * @description: ${DESP}
 * @date: 2019/2/18
 * @time: 3:52 PM
 */
public class DiscountNotificationBean {


    /**
     * code : 12
     * data : {"push":{"content":"爱你呦","id":30,"modelStatus":2,"overTime":1575043200000,"readFlag":1,"stationId":24,"status":2,"title":"小妹妹","titleImgUrl":"http://a.hiphotos.baidu.com/zhidao/pic/item/d788d43f8794a4c2bd77dd030cf41bd5ad6e3945.jpg","updateTime":1543593600000}}
     * sort : 3
     */

    private int code;
    private DataBean data;
    private int sort;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public static class DataBean {
        /**
         * push : {"content":"爱你呦","id":30,"modelStatus":2,"overTime":1575043200000,"readFlag":1,"stationId":24,"status":2,"title":"小妹妹","titleImgUrl":"http://a.hiphotos.baidu.com/zhidao/pic/item/d788d43f8794a4c2bd77dd030cf41bd5ad6e3945.jpg","updateTime":1543593600000}
         */

        private PushBean push;

        public PushBean getPush() {
            return push;
        }

        public void setPush(PushBean push) {
            this.push = push;
        }

        public static class PushBean {
            /**
             * content : 爱你呦
             * id : 30
             * modelStatus : 2
             * overTime : 1575043200000
             * readFlag : 1
             * stationId : 24
             * status : 2
             * title : 小妹妹
             * titleImgUrl : http://a.hiphotos.baidu.com/zhidao/pic/item/d788d43f8794a4c2bd77dd030cf41bd5ad6e3945.jpg
             * updateTime : 1543593600000
             */

            private String content;
            private int id;
            private int modelStatus;
            private long overTime;
            private int readFlag;
            private int stationId;
            private int status;
            private String title;
            private String titleImgUrl;
            private long updateTime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getModelStatus() {
                return modelStatus;
            }

            public void setModelStatus(int modelStatus) {
                this.modelStatus = modelStatus;
            }

            public long getOverTime() {
                return overTime;
            }

            public void setOverTime(long overTime) {
                this.overTime = overTime;
            }

            public int getReadFlag() {
                return readFlag;
            }

            public void setReadFlag(int readFlag) {
                this.readFlag = readFlag;
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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitleImgUrl() {
                return titleImgUrl;
            }

            public void setTitleImgUrl(String titleImgUrl) {
                this.titleImgUrl = titleImgUrl;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
