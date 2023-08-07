package com.yibo.yiboapp.entify;

import java.util.List;

/**
 * Created by johnson on 2017/10/5.
 */

public class QuestionListResultWrap {

    boolean success;
    String msg;
    String accessToken;
    List<String> questionList;


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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<String> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<String> questionList) {
        this.questionList = questionList;
    }
}
