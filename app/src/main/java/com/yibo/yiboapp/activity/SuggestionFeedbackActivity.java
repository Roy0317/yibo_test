package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.utils.LogUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LongLonngBean;
import com.yibo.yiboapp.entify.UnreadMsgCountWraper;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONObject;

import java.net.URLEncoder;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * 意见反馈
 */
public class SuggestionFeedbackActivity extends BaseActivity{

    private static final int SEND_FEEDBACK = 1;

    private static final int TYPE_SUGGESTION = 1;
    private static final int TYPE_COMPLAINT = 2;

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, SuggestionFeedbackActivity.class);
        context.startActivity(intent);
    }


    private LinearLayout linearActions;
    private LinearLayout linearFeedback;
    private EditText editFeedback;
    private int feedbackType;//（1表示建议，2表示投诉）

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.suggestion_feedback_layout);
        initView();
        showActionsLayout();
    }

    protected void initView() {
        super.initView();
        linearActions = findViewById(R.id.linearActions);
        linearFeedback = findViewById(R.id.linearFeedback);
        editFeedback = findViewById(R.id.editFeedback);

//        findViewById(R.id.buttonCustomerService).setOnClickListener(this);
        findViewById(R.id.buttonSuggestion).setOnClickListener(this);
        findViewById(R.id.buttonComplaint).setOnClickListener(this);
        findViewById(R.id.buttonSend).setOnClickListener(this);
    }

    private boolean isShowingActions(){
        return linearActions.getVisibility() == View.VISIBLE;
    }

    private void showActionsLayout(){
        linearFeedback.setVisibility(View.GONE);
        linearActions.setVisibility(View.VISIBLE);
        tvMiddleTitle.setText(getString(R.string.feedback_title));
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText(R.string.feedback_record);
    }

    private void showFeedbackLayout(String title){
        linearActions.setVisibility(View.GONE);
        linearFeedback.setVisibility(View.VISIBLE);
        tvMiddleTitle.setText(title);
        tvRightText.setVisibility(View.GONE);
    }

    private void sendFeedback() {
        if (!Utils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_invalid));
            return;
        }

        String feedback = editFeedback.getText().toString().trim();
        if (Utils.isEmptyString(feedback)) {
            showToast(getString(R.string.suggestion_hint));
            return;
        }

        ApiParams params = new ApiParams();
        params.put("content", feedback);
        params.put("sendType", feedbackType);
        HttpUtil.get(this, Urls.SEND_FEEDBACK, params, true, "", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (isFinishing()) {
                    return;
                }

                try{
                    String content = result.getContent();
                    if (content == null || content.length() == 0) {
                        showToast(getString(R.string.request_fail));
                    }else {
                        LogUtil.d(TAG, "feedback result = " + content);
                        JSONObject obj = new JSONObject(content);
                        if(obj.getBoolean("success")){
                            showToast(getString(R.string.feedback_uploaded));
                            editFeedback.setText("");
                        }else {
                            showToast(getString(R.string.request_fail));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    showToast(getString(R.string.request_fail));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_text:
                onBackPressed();
                break;
            case R.id.right_text:
                //反饋紀錄
                Intent intent = SuggestionRecordActivity.createIntent(this);
                startActivity(intent);
                break;
//            case R.id.buttonCustomerService:
//                showToast("期请期待...");
//                break;
            case R.id.buttonSuggestion:
                feedbackType = TYPE_SUGGESTION;
                showFeedbackLayout(getString(R.string.suggestion_title));
                break;
            case R.id.buttonComplaint:
                feedbackType = TYPE_COMPLAINT;
                showFeedbackLayout(getString(R.string.complaint_title));
                break;
            case R.id.buttonSend:
                sendFeedback();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(isShowingActions()){
            hideKeyboard(this, tvBackText);
            super.onBackPressed();
        }else {
            hideKeyboard(this, tvBackText);
            showActionsLayout();
        }
    }
}
