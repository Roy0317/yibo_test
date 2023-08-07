package com.yibo.yiboapp.webview.js;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * 暴露给给JavaScript方法都需要加上@JavascriptInterface注解
 */
public class AndroidInterface {
    private static final String TAG = "AndroidInterface";
    private JSCallback mJSCallback;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    /**
     * 访客当前的状态
     */
    private int mChatStatus = JSCallback.CHAT_STATUS_UNKNOWN;

    public AndroidInterface() {

    }

    public AndroidInterface(JSCallback jsCallback) {
        if (jsCallback == null) {
            throw new IllegalArgumentException("jsCallback is empty.");
        }
        this.mJSCallback = jsCallback;
    }

    /**
     * 获取访客当前状态
     *
     * @return CHAT_STATUS_UNKNOWN:未知状态
     * CHAT_STATUS_WAITING_HUMAN:等待接通人工对话
     * CHAT_STATUS_HUMAN:已接通人工对话
     * CHAT_STATUS_END_HUMAN:人工对话结束
     * CHAT_STATUS_ROBOT:机器人状态
     * CHAT_STATUS_LEAVE_MESSAGE:留言状态
     */
    public int getChatStatus() {
        return mChatStatus;
    }

    public void setJSCallback(JSCallback jsCallback) {
        if (jsCallback == null) {
            throw new IllegalArgumentException("jsCallback is empty.");
        }
        this.mJSCallback = jsCallback;
    }

    /**
     * 访客状态改变时,Live800的H5页面会调用当前方法
     * <p>
     *
     * @param chatStatus 访客当前的状态
     *                   W:等待接通人工对话
     *                   C:已接通人工对话
     *                   E:人工对话结束
     *                   R:机器人状态
     *                   L:留言状态
     */
    @JavascriptInterface
    public void changeChatStatus(final String chatStatus) {
        Log.d(TAG, "changeChatStatus:W等待接通人工对话,C已接通人工对话,E人工对话结束,R机器人状态,L未知状态 chatStatus=" + chatStatus);
        if (mJSCallback == null) {
            return;
        }
        if ("W".equalsIgnoreCase(chatStatus)) {
            // 等待接通人工对话
            mChatStatus = JSCallback.CHAT_STATUS_WAITING_HUMAN;
        } else if ("C".equalsIgnoreCase(chatStatus)) {
            // 已接通人工对话
            mChatStatus = JSCallback.CHAT_STATUS_HUMAN;
        } else if ("E".equalsIgnoreCase(chatStatus)) {
            // 人工对话结束
            mChatStatus = JSCallback.CHAT_STATUS_END_HUMAN;
        } else if ("R".equalsIgnoreCase(chatStatus)) {
            // 机器人状态
            mChatStatus = JSCallback.CHAT_STATUS_ROBOT;
        } else if ("L".equalsIgnoreCase(chatStatus)) {
            // 机器人状态
            mChatStatus = JSCallback.CHAT_STATUS_LEAVE_MESSAGE;
        } else {
            mChatStatus = JSCallback.CHAT_STATUS_UNKNOWN;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mJSCallback.changeChatStatus(mChatStatus);
            }
        });
    }

    /**
     * @param msg 目前只会传null或空字符串
     */
    @JavascriptInterface
    public void newMsg(final String msg) {
        Log.d(TAG, "newMsg: msg=" + msg);
        if (mJSCallback == null) {
            return;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mJSCallback.newMsg(msg);
            }
        });
    }

    /**
     * 通知App,对话已被关闭
     *
     * @param closedCode 1：访客主动关闭;
     *                   2：长时间未回复关;
     *                   3：客服关闭对话
     */
    @JavascriptInterface
    public void closeChat(final String closedCode) {
        Log.d(TAG, "closeChat: 1访客主动关闭,2长时间未回复关闭,3客服关闭对话 closedCode=" + closedCode);
        if (mJSCallback == null) {
            return;
        }
        final int code;
        if ("1".equalsIgnoreCase(closedCode)) {
            // 访客主动关闭
            code = JSCallback.CHAT_CLOSED_VISITOR;
        } else if ("2".equalsIgnoreCase(closedCode)) {
            // 长时间未回复关闭
            code = JSCallback.CHAT_CLOSED_TIMEOUT;
        } else if ("3".equalsIgnoreCase(closedCode)) {
            // 客服关闭对话
            code = JSCallback.CHAT_CLOSED_OPERATOR;
        } else {
            code = JSCallback.CHAT_CLOSED_UNKNOWN;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mJSCallback.chatClosed(code);
            }
        });
    }

    /**
     * 通知App,H5页面的对话评估状态
     *
     * @param evalStatus 0：取消评估;
     *                   1：已评估
     */
    @JavascriptInterface
    public void changeEvalStatus(final String evalStatus) {
        Log.d(TAG, "changeEvalStatus:0取消评估,1已评估 evalStatus=" + evalStatus);
        if (mJSCallback == null) {
            return;
        }
        final int status;
        if ("0".equalsIgnoreCase(evalStatus)) {
            // 取消评估
            status = JSCallback.CHAT_EVALUATION_STATUS_CANCEL;
        } else if ("1".equalsIgnoreCase(evalStatus)) {
            // 已评估
            status = JSCallback.CHAT_EVALUATION_STATUS_COMPLETE;
        } else {
            status = JSCallback.CHAT_EVALUATION_STATUS_UNKNOWN;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mJSCallback.evalStatusChanged(status);
            }
        });
    }

    /**
     * H5通知App,H5导航栏位置中title的文本
     *
     * @param title H5页面的title
     */
    @JavascriptInterface
    public void changeTitle(final String title) {
        Log.d(TAG, "changeTitle:导航栏title=" + title);
        if (mJSCallback == null) {
            return;
        }
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mJSCallback.changeTitle(title);
            }
        });
    }

}
