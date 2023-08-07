package com.yibo.yiboapp.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.yibo.yiboapp.activity.MainActivity;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.views.WebChatRoomDialog;

/*
* 聊天室的方法
* */
public class ChatRoomUtils {

    private Activity act;

    private WebChatRoomDialog chatMainDialog;

    public ChatRoomUtils(Activity act){
        this.act = act;
        showDialog();
    }

    public void showDialog(){
        if (chatMainDialog == null) {
            getChatUrl();
        } else {
            if (!chatMainDialog.isShowing()) {
                chatMainDialog.show();
            }
        }
    }

    private void getChatUrl() {
        HttpUtil.get(act, Urls.CHAT_ROOM_URL, null, true, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    String url = result.getContent();
                    if (!TextUtils.isEmpty(url)) {
                        if (chatMainDialog == null) {
                            chatMainDialog = new WebChatRoomDialog(act, url);
                            chatMainDialog.initDialog();
                        } else {
                            if (!chatMainDialog.isShowing()) {
                                chatMainDialog.show();
                            }
//                            chatMainDialog = new WebChatRoomDialog(ctx, chatUrl);
//                            chatMainDialog.initDialog().show();
//                            chatMainDialog.initDialog().hide();
                        }
                    } else {
                        Toast.makeText(act, "获取聊天室链接为空", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
