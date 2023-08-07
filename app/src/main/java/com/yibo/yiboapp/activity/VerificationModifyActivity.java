package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.LoginUUIDBean;
import com.yibo.yiboapp.entify.QuestionListResultWrap;
import com.yibo.yiboapp.utils.Utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class VerificationModifyActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {


    Spinner spn_question;
    EditText edt_pwd;
    EditText edt_pwd_agin;
    Button btn_login;
    String questionName;
    private List<String> questionList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    long accountId;
    String userName;
    String passWord;

    public static void createIntent(Context context, long accountId) {
        Intent intent = new Intent(context, VerificationModifyActivity.class);
        intent.putExtra("accountId", accountId);
        context.startActivity(intent);
    }

    public static final int INPUT_REQUEST = 0x01;
    public static final int GET_QUESTION = 0x02;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        accountId = getIntent().getLongExtra("accountId", 0);
        setContentView(R.layout.activity_vervification_set);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("安全问题修改");
        initChildView();
    }

    private void initChildView() {
        spn_question = findViewById(R.id.spn_question);
        edt_pwd = findViewById(R.id.edt_pwd);
        edt_pwd_agin = findViewById(R.id.edt_pwd_agin);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setText("设定");
        btn_login.setOnClickListener(this);
        initTypeFilter();
        actionGetList();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                vertifiyAnswer();

                break;
            default:
                super.onClick(v);
                break;
        }
    }

    void initQuestionList() {
        questionName = questionList.get(0);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questionList);
        spn_question.setAdapter(adapter);
        spn_question.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionName = questionList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    void actionGetList() {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_QUESTION_LIST);

        CrazyRequest<CrazyResult<QuestionListResultWrap>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(GET_QUESTION)
                .headers(Urls.getHeader(this))
                .placeholderText("请稍等")
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<QuestionListResultWrap>() {
                }.getType()))
                .loadMethod(true ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }


    //提交
    void actionInput() {
        //accountId,  question, answer
        try {
            StringBuilder urls = new StringBuilder();
            urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.UPDATE_QUESTION_AND_ANSWER);
//            urls.append("?accountId=").append(accountId);
            urls.append("?question=").append(URLEncoder.encode(questionName, "UTF-8"));
            urls.append("&answer=").append(URLEncoder.encode(edt_pwd_agin.getText().toString().trim(), "UTF-8"));

            CrazyRequest<CrazyResult<QuestionListResultWrap>> request = new AbstractCrazyRequest.Builder().
                    url(urls.toString())
                    .seqnumber(INPUT_REQUEST)
                    .headers(Urls.getHeader(this))
                    .placeholderText("请稍等")
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<QuestionListResultWrap>() {
                    }.getType()))
                    .loadMethod(true ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);
        } catch (Exception e) {
        }
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == GET_QUESTION) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("获取问题列表失败");
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? "获取问题列表失败" : errorString);
                return;
            }
            Object regResult = result.result;
            QuestionListResultWrap reg = (QuestionListResultWrap) regResult;
            if (reg.getQuestionList() == null || reg.getQuestionList().size() <= 0) {
                ToastUtil.showToast(this, "获取问题列表失败");
                return;
            }
            questionList.clear();
            questionList.addAll(reg.getQuestionList());
            initQuestionList();


        } else if(action == INPUT_REQUEST){
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("修改失败");
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? "修改失败" : errorString);
                return;
            }
            Object regResult = result.result;
            QuestionListResultWrap reg = (QuestionListResultWrap) regResult;
            if(!reg.isSuccess()){
                String errorString = Urls.parseResponseResult(reg.getMsg());
                showToast(Utils.isEmptyString(errorString) ? "修改失败" : errorString);
                return;
            }
             showToast("修改安全问题成功");
             finish();
        }

    }



    //验证答案输入
     void vertifiyAnswer(){
         if(TextUtils.isEmpty(edt_pwd.getText()+"")){
             showToast("请输入答案");
             return;
         }
         if(TextUtils.isEmpty(edt_pwd_agin.getText()+"")){
             showToast("请确认答案");
             return;
         }
         if(!(edt_pwd_agin.getText().toString().trim()).equals(edt_pwd.getText().toString().trim())){
             showToast("两次输入不一致");
             return;
         }
         actionInput();
     }


    void initTypeFilter() {


        InputFilter typeFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("[[\\u4e00-\\u9fa5]|[a-zA-Z]|\\d]*");
                Matcher m = p.matcher(source.toString());
                if (!m.matches()) return "";
                return null;
            }
        };
        edt_pwd_agin.setFilters(new InputFilter[]{typeFilter,new InputFilter.LengthFilter(20)});
        edt_pwd.setFilters(new InputFilter[]{typeFilter,new InputFilter.LengthFilter(20)});


    }
}
