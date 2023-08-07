package com.yibo.yiboapp.webview.js;

public abstract class JSCallback {
    /**
     * 访客状态:未知状态
     */
    public static final int CHAT_STATUS_UNKNOWN = 0;
    /**
     * 访客状态:等待接通人工对话
     */
    public static final int CHAT_STATUS_WAITING_HUMAN = 1;
    /**
     * 访客状态:已接通人工对话
     */
    public static final int CHAT_STATUS_HUMAN = 2;
    /**
     * 访客状态:人工对话结束
     */
    public static final int CHAT_STATUS_END_HUMAN = 3;
    /**
     * 访客状态:机器人状态
     */
    public static final int CHAT_STATUS_ROBOT = 4;
    /**
     * 访客状态:留言状态
     */
    public static final int CHAT_STATUS_LEAVE_MESSAGE = 5;


    /**
     * 对话已被关闭:未知原因
     */
    public static final int CHAT_CLOSED_UNKNOWN = 10;
    /**
     * 对话已被关闭:访客主动关闭
     */
    public static final int CHAT_CLOSED_VISITOR = 11;
    /**
     * 对话已被关闭:长时间未回复
     */
    public static final int CHAT_CLOSED_TIMEOUT = 12;
    /**
     * 对话已被关闭:客服关闭对话
     */
    public static final int CHAT_CLOSED_OPERATOR = 13;


    /**
     * 对话评估状态已改变:未知原因
     */
    public static final int CHAT_EVALUATION_STATUS_UNKNOWN = 20;
    /**
     * 对话评估状态已改变:取消评估
     */
    public static final int CHAT_EVALUATION_STATUS_CANCEL = 21;
    /**
     * 对话评估状态已改变:已评估
     */
    public static final int CHAT_EVALUATION_STATUS_COMPLETE = 22;

    /**
     * 访客状态改变时,Live800的H5页面会调用当前方法
     * <p>
     *
     * @param chatStatus 访客当前的状态
     *                   CHAT_STATUS_UNKNOWN:未知状态
     *                   CHAT_STATUS_WAITING_HUMAN:等待接通人工对话
     *                   CHAT_STATUS_HUMAN:已接通人工对话
     *                   CHAT_STATUS_END_HUMAN:人工对话结束
     *                   CHAT_STATUS_ROBOT:机器人状态
     *                   CHAT_STATUS_LEAVE_MESSAGE:留言状态
     */
    public void changeChatStatus(int chatStatus) {

    }

    /**
     * @param msg 目前只会传null或空字符串
     */
    public void newMsg(String msg) {

    }

    /**
     * 通知App,对话已被关闭
     *
     * @param code CHAT_CLOSED_UNKNOWN：未知原因;
     *             CHAT_CLOSED_VISITOR：访客主动关闭;
     *             CHAT_CLOSED_TIMEOUT：长时间未回复;
     *             CHAT_CLOSED_OPERATOR：客服关闭对话
     */
    public void chatClosed(int code) {

    }

    /**
     * 对话评估状态已改变
     *
     * @param evaluationStatus CHAT_EVALUATION_STATUS_UNKNOWN：未知原因
     *                         CHAT_EVALUATION_STATUS_CANCEL：取消评估
     *                         CHAT_EVALUATION_STATUS_COMPLETE：已评估
     */
    public void evalStatusChanged(int evaluationStatus) {

    }

    /**
     * H5通知App,H5导航栏位置中title的文本
     *
     * @param title H5页面的title
     */
    public void changeTitle(String title) {

    }
}
