package com.example.anuo.immodule.bean;

import java.util.List;

/*
* 用户头像列表
* */
public class ChatPersonPhotoListBean {

    /**
     * msg : 操作成功。
     * code : R7035
     * success : true
     * source : {"items":["https://yj8.me/img/h3eM/iozdKN3wr.jpg","https://yj8.me/img/h3eM/iozdKHwZy.jpg","https://yj8.me/img/h3eM/iozdKaSyp.jpg","https://yj8.me/img/h3eM/iozdKssMt.jpg","https://yj8.me/img/h3eM/iozdKsFK8.png","https://yj8.me/img/h3eM/iozdFtBNm.jpg","https://yj8.me/img/h3eM/iozdF8rpI.jpg","https://yj8.me/img/h3eM/iozdFjh9Z.jpg","https://yj8.me/img/h3eM/iozdFlgtp.jpg","https://yj8.me/img/h3eM/iozdFGnDC.jpg","http://n.sinaimg.cn/sinacn/w640h398/20180208/0362-fyrkuxs1440784.jpg","http://vpic.video.qq.com/94626833/j0349nqvc4k_ori_3.jpg","https://yj8.me/img/h3eM/i5nXT7rgb.jpg","https://yj8.me/img/h3eM/i5nXTjYxH.jpg","https://yj8.me/img/h3eM/i5nX8ImZ1.jpg","https://yj8.me/img/h3eM/i5nX8IV0n.jpg","https://yj8.me/img/h3eM/i5nX8QubV.jpg","https://yj8.me/img/h3eM/i5niHFMyd.jpg","https://yj8.me/img/h3eM/i5niHdj1P.jpg","https://yj8.me/img/h3eM/i5niH0naV.jpg","https://yj8.me/img/h3eM/i5nieHQ7G.jpg","https://yj8.me/img/h3eM/i5nieHJw8.jpg"]}
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
        private List<String> items;

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }
    }
}
