package com.yibo.yiboapp.Event;

import android.content.Intent;

/**
 * h5 聊天室点击选择本地图片的返回
 */
public class WebChatPhotoEvent {

    int requestCode;
    int resultCode;
    Intent intent;

    public WebChatPhotoEvent(int requestCode, int resultCode, Intent intent) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.intent = intent;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
