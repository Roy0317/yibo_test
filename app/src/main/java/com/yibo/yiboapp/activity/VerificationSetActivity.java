package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.Event.LoginEvent;
import com.yibo.yiboapp.Event.VervificationSuccessEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.CheckUpdateBean;
import com.yibo.yiboapp.entify.CheckUpdateWraper;
import com.yibo.yiboapp.entify.LoginOutWraper;
import com.yibo.yiboapp.entify.LoginResult;
import com.yibo.yiboapp.entify.LoginResultWrap;
import com.yibo.yiboapp.entify.LoginUUIDBean;
import com.yibo.yiboapp.entify.MainPopupTime;
import com.yibo.yiboapp.entify.QuestionListResultWrap;
import com.yibo.yiboapp.entify.RegisterResultWrapper;
import com.yibo.yiboapp.entify.SerQuestionWrap;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity;
import com.yibo.yiboapp.utils.DateUtils;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.RequestQueue;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class VerificationSetActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {


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
    boolean islogin;

    public static void createIntent(Context context, long accountId, String userName, String passWord, boolean islogin) {
        Intent intent = new Intent(context, VerificationSetActivity.class);
        intent.putExtra("accountId", accountId);
        intent.putExtra("userName", userName);
        intent.putExtra("passWord", passWord);
        intent.putExtra("islogin", islogin);
        context.startActivity(intent);
    }

    public static final int INPUT_REQUEST = 0x01;
    public static final int GET_QUESTION = 0x02;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        accountId = getIntent().getLongExtra("accountId", 0);
        userName = getIntent().getStringExtra("userName");
        passWord = getIntent().getStringExtra("passWord");
        islogin = getIntent().getBooleanExtra("islogin", false);
        setContentView(R.layout.activity_vervification_set);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("安全问题设定");
        initChildView();
    }

    private void initChildView() {
        spn_question = findViewById(R.id.spn_question);
        edt_pwd = findViewById(R.id.edt_pwd);
        edt_pwd_agin = findViewById(R.id.edt_pwd_agin);
        btn_login = findViewById(R.id.btn_login);
        findViewById(R.id.tv_info).setVisibility(View.VISIBLE);
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
            urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SET_QUESTION_AND_ANSWER);
            urls.append("?accountId=").append(accountId);
            urls.append("&question=").append(URLEncoder.encode(questionName, "UTF-8"));
            urls.append("&answer=").append(URLEncoder.encode(edt_pwd_agin.getText().toString().trim(), "UTF-8"));

            CrazyRequest<CrazyResult<SerQuestionWrap>> request = new AbstractCrazyRequest.Builder().
                    url(urls.toString())
                    .seqnumber(INPUT_REQUEST)
                    .headers(Urls.getHeader(this))
                    .placeholderText("请稍等")
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(GsonConverterFactory.create(new TypeToken<SerQuestionWrap>() {
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


        } else if (action == INPUT_REQUEST) {
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
            SerQuestionWrap reg = (SerQuestionWrap) regResult;
            if (!reg.isSuccess()) {
                String errorString = Urls.parseResponseResult(reg.getMsg());
                showToast(Utils.isEmptyString(errorString) ? "设定失败" : errorString);
                return;
            }
            setLoginInfo(reg.getUuid(), reg.getAccessToken());
        }

    }


    /**
     * 设置uuid 存储登陆状态 账户密码
     */
    void setLoginInfo(String uuid, String token) {
        //从sp文件内取出已经存储的账号uuid信息
        //由于存在单个设备登陆多个账号的情况 采用集合的形式来保存，用username作为识别
        String uuidjson = YiboPreference.instance(this).getLoginUuid();
        List<LoginUUIDBean> list;
        if (!TextUtils.isEmpty(uuidjson)) {
            list = new Gson().fromJson(uuidjson, new TypeToken<List<LoginUUIDBean>>() {
            }.getType());

        } else {
            //如果为空就新建一个
            list = new ArrayList<LoginUUIDBean>();
        }

        if (list.size() > 0) {
            //如果uuid存在变动 会覆盖修改以前的存储而不是添加新的条目，避免重复
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().equals(userName)) {
                    list.get(i).setUuid(uuid);
                    break;
                } else {
                    //如果没有存在相等且是集合最后一个对象，说明这个账号没有被记录过，新建个对象加入集合中
                    if (i == list.size() - 1) {
                        LoginUUIDBean uuidBean = new LoginUUIDBean();
                        uuidBean.setUsername(userName);
                        uuidBean.setUuid(uuid);
                        uuidBean.setAccountId(accountId);
                        list.add(uuidBean);
                    }
                }
            }
        } else {
            LoginUUIDBean uuidBean = new LoginUUIDBean();
            uuidBean.setUsername(userName);
            uuidBean.setUuid(uuid);
            uuidBean.setAccountId(accountId);
            list.add(uuidBean);
        }
        //把集合转为json字符串保存在sp文件内
        uuidjson = new Gson().toJson(list, new TypeToken<List<LoginUUIDBean>>() {
        }.getType());
        YiboPreference.instance(this).setLogin_uuid(uuidjson);
        VervificationSuccessEvent event = new VervificationSuccessEvent();
        event.setPassword(passWord);
        event.setUsername(userName);
        event.setAccessToken(token);
        event.setIslogin(islogin);
        EventBus.getDefault().post(event);
        finish();

    }


    //验证答案输入
    void vertifiyAnswer() {

        if (TextUtils.isEmpty(edt_pwd.getText() + "")) {
            showToast("请输入答案");
            return;
        }
        if (TextUtils.isEmpty(edt_pwd_agin.getText() + "")) {
            showToast("请确认答案");
            return;
        }
        if (!(edt_pwd_agin.getText().toString().trim()).equals(edt_pwd.getText().toString().trim())) {
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
