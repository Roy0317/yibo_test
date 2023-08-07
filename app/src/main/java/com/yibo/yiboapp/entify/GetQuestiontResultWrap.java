package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/5.
 */

public class GetQuestiontResultWrap {

    boolean success;
    String msg;
    String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
