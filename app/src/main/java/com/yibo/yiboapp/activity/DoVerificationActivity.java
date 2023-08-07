package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.Event.VervificationSuccessEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.CheckAnswerWrap;
import com.yibo.yiboapp.entify.GetQuestiontResultWrap;
import com.yibo.yiboapp.entify.LoginUUIDBean;
import com.yibo.yiboapp.entify.QuestionListResultWrap;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class DoVerificationActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {


    TextView tv_question;
    TextView tv_forget_answer;
    TextView tv_count;
    EditText edt_pwd;
    Button btn_login;
    String questionName;
    private List<String> questionList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    long accountId;
    String userName;
    String passWord;
     boolean islogin=false;

    public static void createIntent(Context context, long accountId, String userName, String passWord,boolean islogin) {
        Intent intent = new Intent(context, DoVerificationActivity.class);
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
        islogin = getIntent().getBooleanExtra("islogin",false);
        setContentView(R.layout.activity_do_vervification);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("安全问题设定");
        initChildView();
    }

    private void initChildView() {
        tv_question = findViewById(R.id.tv_question);
        tv_forget_answer = findViewById(R.id.tv_forget_answer);
        edt_pwd = findViewById(R.id.edt_pwd);
        tv_count = findViewById(R.id.tv_count);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_forget_answer.setOnClickListener(this);
        actionGetQuestion();
        tv_forget_answer.setText(Html.fromHtml("<u>" + "忘记答案" + "</u>"));
        initTypeFilter();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                vertifiyAnswer();
                break;

                case R.id.tv_forget_answer:
                    SysConfig config = UsualMethod.getConfigFromJson(this);
                    String version = config.getOnline_service_open_switch();
                    if (!version.isEmpty()) {
                        boolean success = UsualMethod.viewService(this, version);
                        if (!success) {
                            showToast("没有在线客服链接地址，无法打开");
                        }
                    }
                break;
            default:
                super.onClick(v);
                break;
        }
    }


    //根据id获得安全问题
    void actionGetQuestion() {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_QUESTION_BY_MEMBER);
        urls.append("?accountId=").append(accountId);
        CrazyRequest<CrazyResult<GetQuestiontResultWrap>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(GET_QUESTION)
                .headers(Urls.getHeader(this))
                .placeholderText("请稍等")
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<GetQuestiontResultWrap>() {
                }.getType()))
                .loadMethod(true ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }


    //提交验证
    void actionInput() {
        //accountId,  question, answer
        try {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.CHECK_ANSWER);
        urls.append("?accountId=").append(accountId);
        urls.append("&answer=").append(URLEncoder.encode(edt_pwd.getText() + "", "UTF-8"));

        CrazyRequest<CrazyResult<CheckAnswerWrap>> request = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(INPUT_REQUEST)
                .headers(Urls.getHeader(this))
                .placeholderText("请稍等")
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<CheckAnswerWrap>() {
                }.getType()))
                .loadMethod(true ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
        }catch (Exception e){

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
                showToast("获取问题失败");
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? "获取问题失败" : errorString);
                return;
            }
            Object regResult = result.result;
            GetQuestiontResultWrap reg = (GetQuestiontResultWrap) regResult;
            if (!TextUtils.isEmpty(reg.getQuestion())) {
                tv_question.setText(reg.getQuestion());
            }
        } else if (action == INPUT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("提交失败");
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? "提交失败" : errorString);
                Object regResult = result.result;
                CheckAnswerWrap reg = (CheckAnswerWrap) regResult;
                if(!TextUtils.isEmpty(reg.getFailCountQuota())){
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText("答案不正确还可以尝试"+reg.getFailCountQuota()+"次");
                }else {

                }
                return;
            }

            Object regResult = result.result;
            CheckAnswerWrap reg = (CheckAnswerWrap) regResult;
            if(!reg.isSuccess()){
                String errorString = Urls.parseResponseResult(reg.getMsg());
                showToast(Utils.isEmptyString(errorString) ? "提交失败" : errorString);
                if(!TextUtils.isEmpty(reg.getFailCountQuota())){
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText("答案不正确还可以尝试"+reg.getFailCountQuota()+"次");
                    if("0".equals(reg.getFailCountQuota())){
                        showContentDialog("温馨提示","因您的错误次数已达上限，为了您的账户安全已先锁定保护，解锁请联系客服。");
                    }
                }else {

                }
                return;
            }
            setLoginInfo(reg.getUuid(),reg.getAccessToken());

        }

    }


    /**
     * 设置uuid 存储登陆状态 账户密码
     */
    void setLoginInfo(String uuid,String token) {
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

        if(list.size()>0){
            //如果uuid存在变动 会覆盖修改以前的存储而不是添加新的条目，避免重复
            for(int i=0;i<list.size();i++){
                if(list.get(i).getUsername().equals(userName)){
                    list.get(i).setUuid(uuid);
                    break;
                }else{
                    //如果没有存在相等且是集合最后一个对象，说明这个账号没有被记录过，新建个对象加入集合中
                    if(i==list.size()-1){
                        LoginUUIDBean uuidBean = new LoginUUIDBean();
                        uuidBean.setUsername(userName);
                        uuidBean.setUuid(uuid);
                        uuidBean.setAccountId(accountId);
                        list.add(uuidBean);
                    }
                }
            }
        }else {
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
        VervificationSuccessEvent event=new VervificationSuccessEvent();
        event.setPassword(passWord);
        event.setUsername(userName);
        event.setAccessToken(token);
        event.setIslogin(islogin);
        EventBus.getDefault().post(event);
        finish();

    }

    //验证答案输入
    void vertifiyAnswer(){
        if(TextUtils.isEmpty(edt_pwd.getText()+"")){
            showToast("请输入答案");
            return;
        }
        actionInput();
    }


    private void showContentDialog(String title, String content) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        ccd.setTitle(title);
        ccd.setContent(content);
        ccd.setMiddleBtnText("确定");
        if (content.contains("</") || content.contains("<img")) {
            ccd.setHtmlContent(true);
        } else {
            ccd.setHtmlContent(false);
        }
//        ccd.setToastShow(true);
        ccd.setBaseUrl(Urls.BASE_URL);
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
                finish();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
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
        edt_pwd.setFilters(new InputFilter[]{typeFilter,new InputFilter.LengthFilter(20)});

    }
}
