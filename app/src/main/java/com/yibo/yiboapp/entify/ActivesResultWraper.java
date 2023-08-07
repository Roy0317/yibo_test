package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/11/18.
 */

public class ActivesResultWraper {
    /**
     * success : true
     * accessToken : 9882d493-ca92-4a04-b662-618aaaefc244
     * content : [{"typeList":[{"id":1,"stationId":23,"status":2,"typeName":"123"},{"id":2,"stationId":23,"status":2,"typeName":"321321"}],"readFlag":1,"overTime":1601395200000,"updateTime":1569859200000,"id":22,"title":"优惠活动","titleImgUrl":"","content":"优惠活动来了，快充值啊啊 啊啊啊啊啊啊啊啊 啊啊啊啊 啊啊啊 啊啊啊啊<img src=\"https://yt6.me/img/Cr3p/BLB0BDQfp.jpg\" style=\"width:100%\" />"},{"typeList":[{"$ref":"$.content[0].typeList[0]"},{"$ref":"$.content[0].typeList[1]"}],"readFlag":1,"overTime":1612022400000,"updateTime":1580486400000,"id":24,"title":"APP全面更新完毕","titleImgUrl":"http://g1.dfcfw.com/g3/201905/20190513092737.jpg","content":"看得见风大水坑俩房间阿萨德离开房间djf拉速度快积分<img src=\"https://yt6.me/img/3wIS/Znmu6tK0C.jpg\"  style=\"width:100%\"/>"},{"typeList":[{"$ref":"$.content[0].typeList[0]"},{"$ref":"$.content[0].typeList[1]"}],"readFlag":0,"overTime":1622390400000,"updateTime":1590940800000,"id":25,"title":"321321","titleImgUrl":"","content":""},{"typeList":[{"$ref":"$.content[0].typeList[0]"},{"$ref":"$.content[0].typeList[1]"}],"readFlag":0,"overTime":1622390400000,"updateTime":1590940800000,"typeId":1,"id":26,"title":"EventBus框架源码分析","titleImgUrl":"","content":""},{"typeList":[{"$ref":"$.content[0].typeList[0]"},{"$ref":"$.content[0].typeList[1]"}],"readFlag":0,"overTime":1622390400000,"updateTime":1590940800000,"id":27,"title":"Android Dalvik虚拟机Java堆创建过程分析","titleImgUrl":"","content":"3213213"}]
     */

    String msg;
    int code;
    private boolean success;
    private String accessToken;
    private List<ContentBean> content;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * typeList : [{"id":1,"stationId":23,"status":2,"typeName":"123"},{"id":2,"stationId":23,"status":2,"typeName":"321321"}]
         * readFlag : 1
         * overTime : 1601395200000
         * updateTime : 1569859200000
         * id : 22
         * title : 优惠活动
         * titleImgUrl :
         * content : 优惠活动来了，快充值啊啊 啊啊啊啊啊啊啊啊 啊啊啊啊 啊啊啊 啊啊啊啊<img src="https://yt6.me/img/Cr3p/BLB0BDQfp.jpg" style="width:100%" />
         * typeId : 1
         */

        private int readFlag;
        private long overTime;
        private long updateTime;
        private int id;
        private String title;
        private String titleImgUrl;
        private String content;
        private int typeId;
        private boolean showContent;
        private List<TypeListBean> typeList;

        public boolean isShowContent() {
            return showContent;
        }

        public void setShowContent(boolean showContent) {
            this.showContent = showContent;
        }

        public int getReadFlag() {
            return readFlag;
        }

        public void setReadFlag(int readFlag) {
            this.readFlag = readFlag;
        }

        public long getOverTime() {
            return overTime;
        }

        public void setOverTime(long overTime) {
            this.overTime = overTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public List<TypeListBean> getTypeList() {
            return typeList;
        }

        public void setTypeList(List<TypeListBean> typeList) {
            this.typeList = typeList;
        }

        public static class TypeListBean {
            /**
             * id : 1
             * stationId : 23
             * status : 2
             * typeName : 123
             */

            private int id;
            private int stationId;
            private int status;
            private String typeName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }
        }
    }


//    boolean success;
//    String msg;
//    int code;
//    String accessToken;
//    List<ActiveResult> content;
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public List<ActiveResult> getContent() {
//        return content;
//    }
//
//    public void setContent(List<ActiveResult> content) {
//        this.content = content;
//    }
}
