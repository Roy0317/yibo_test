package com.yibo.yiboapp.network;


import android.text.TextUtils;

import com.google.gson.annotations.Expose;

/**
 * 网络请求结果基类
 */
public class NetworkResult {

    @Expose
    private boolean success = false;  //请求状态
    @Expose
    private String msg = "";  //请求结果提示

    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
    //所以此接口当code == 0时表示帐号被踢，或登录超时
    @Expose
    private int code;
    @Expose
    private String accessToken;
    @Expose(deserialize = false)
    private String content = "";  //内容

    private String url;

    public NetworkResult(String msg) {
        this.msg = msg;
    }

    public NetworkResult() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsg(String errorMsg) {
        if (TextUtils.isEmpty(msg)) {
            return errorMsg;
        } else {
            return msg;
        }
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
