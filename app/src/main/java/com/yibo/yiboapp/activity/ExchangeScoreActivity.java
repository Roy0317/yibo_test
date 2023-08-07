package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.ExchangeConfig;
import com.yibo.yiboapp.entify.ExchangeConfigResultWraper;
import com.yibo.yiboapp.entify.ExchangeWraper;
import com.yibo.yiboapp.entify.MemInfoWraper;
import com.yibo.yiboapp.ui.CircleImageView;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 * 积分兑换
 */
public class ExchangeScoreActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    public static final String TAG = ExchangeScoreActivity.class.getSimpleName();
    public static final int EXCHANGE_CONFIGS_REQUEST = 0x01;
    public static final int EXCHANGE_POST_REQUEST = 0x02;
    public static final int ACCOUNT_REQUEST = 0x03;

    TextView userName;
    TextView balanceTV;
    TextView scoreTV;
    TextView exchangeSummaryTV;

    XEditText input_money;
    Button exchangeBtn;
    TextView exchangeTypeTV;
    TextView switchTypeTV;
    SimpleDraweeView header;

    List<ExchangeConfig> configs = null;
    long typeId;//当前兑换类型的ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_score);
        initView();

        configs = new ArrayList<>();
        String account = getIntent().getStringExtra("account");
        String leftMoney = getIntent().getStringExtra("leftMoney");
        updateAccount(account, leftMoney);
        actionData(true);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.score_exchange_str));
        userName = (TextView) findViewById(R.id.name);
        balanceTV = (TextView) findViewById(R.id.left_money);
        scoreTV = (TextView) findViewById(R.id.scores);
        input_money = (XEditText) findViewById(R.id.input_money);
        exchangeSummaryTV = (TextView) findViewById(R.id.exchange_summary);
        exchangeBtn = (Button) findViewById(R.id.confirm);
        exchangeBtn.setOnClickListener(this);
        exchangeTypeTV = (TextView) findViewById(R.id.charge_method_txt);
        switchTypeTV = (TextView) findViewById(R.id.change_method);
        header = (SimpleDraweeView) findViewById(R.id.header);
        switchTypeTV.setOnClickListener(this);
        UsualMethod.LoadUserImage(this, header);
    }

    private void updateAccount(String account, String leftMoney) {
        userName.setText(account);
        if (!Utils.isEmptyString(leftMoney)) {
            String balance = String.format(Locale.CHINA,"余额:%.2f元", Double.parseDouble(leftMoney));
            balanceTV.setText(balance);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.confirm:
                String inputMoney = input_money.getText().toString().trim();
                if (Utils.isEmptyString(inputMoney)) {
                    showToast(R.string.input_money_or_score);
                    return;
                }
                if (!Utils.isNumeric(inputMoney)) {
                    showToast(R.string.input_money_must_be_zs);
                    return;
                }
                postExchange(typeId, inputMoney);
                break;
            case R.id.change_method:
                showConvertList(this);
                break;
        }
    }

    private void postExchange(long typeId, String amount) {
        StringBuilder lunboUrl = new StringBuilder();
        lunboUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.EXCHANGE_URL);
        lunboUrl.append("?typeId=" + typeId);
        lunboUrl.append("&amount=" + amount);
        CrazyRequest<CrazyResult<ExchangeWraper>> request = new AbstractCrazyRequest.Builder().
                url(lunboUrl.toString())
                .seqnumber(EXCHANGE_POST_REQUEST)
                .shouldCache(false)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.post_exchange_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ExchangeWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    public static void createIntent(Context context, String account, String leftMoney) {
        Intent intent = new Intent(context, ExchangeScoreActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("leftMoney", leftMoney);
        context.startActivity(intent);
    }

    private void actionData(boolean showDialog) {
        //获取列表数据
        StringBuilder lunboUrl = new StringBuilder();
        lunboUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.EXCHANGE_CONFIG_URL);
        CrazyRequest<CrazyResult<ExchangeConfigResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(lunboUrl.toString())
                .seqnumber(EXCHANGE_CONFIGS_REQUEST)
                .shouldCache(false)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.change_config_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ExchangeConfigResultWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() : CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();

//        //获取一些主页显示的后台数据
//        StringBuilder accountUrls = new StringBuilder();
//        accountUrls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MEMINFO_URL);
//        CrazyRequest<CrazyResult<MemInfoWraper>> accountRequest = new AbstractCrazyRequest.Builder().
//                url(accountUrls.toString())
//                .seqnumber(ACCOUNT_REQUEST)
//                .listener(this)
//                .headers(Urls.getHeader(this))
//                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<MemInfoWraper>() {
//                }.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private void actionAccountData() {
        //获取一些主页显示的后台数据
        StringBuilder accountUrls = new StringBuilder();
        accountUrls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MEMINFO_URL);
        CrazyRequest<CrazyResult<MemInfoWraper>> accountRequest = new AbstractCrazyRequest.Builder().
                url(accountUrls.toString())
                .seqnumber(ACCOUNT_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MemInfoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, accountRequest);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == EXCHANGE_CONFIGS_REQUEST) {

            actionAccountData();//获取配置结束后 再获取一下帐户信息
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.acquire_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            ExchangeConfigResultWraper reg = (ExchangeConfigResultWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                scoreTV.setText("积分：" + reg.getContent().getScore());
                List<ExchangeConfig> results = reg.getContent().getConfigs();
                if (results != null) {
                    if (results.isEmpty()) {
                        exchangeTypeTV.setText("兑换类型：暂无兑换类型");
                        showToast("没有兑换类型，请在后台配置兑换类型后重试！");
                        return;
                    }
                    configs.clear();
                    for (int i = 0; i < results.size(); i++) {
                        if (results.get(i).getType() == 1) {
                            configs.add(results.get(i));
                            break;
                        }
                    }
                    for (int i = 0; i < results.size(); i++) {
                        if (results.get(i).getType() == 2) {
                            configs.add(results.get(i));
                            break;
                        }
                    }
                    if (configs.isEmpty()) {
                        return;
                    } else {
                        if (configs.size() == 2) {
                            stringItems = new String[]{"现金兑换积分", "积分兑换现金"};
                        } else if (configs.size() == 1){
                            if (configs.get(0).getType() == 1) {
                                stringItems = new String[]{"现金兑换积分"};
                            } else {
                                stringItems = new String[]{"积分兑换现金"};
                            }
                        }
                    }
                    if (typeId == 0) {
                        updateExchangeInfoView(configs.get(0));
                    } else {
                        for (ExchangeConfig config : configs) {
                            if (config.getType() == typeId) {
                                updateExchangeInfoView(config);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (action == EXCHANGE_POST_REQUEST) {

            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.exchange_fail);
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.exchange_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            ExchangeWraper reg = (ExchangeWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.exchange_fail));
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            showToast(R.string.exchange_success);
            actionData(false);
        } else if (action == ACCOUNT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            MemInfoWraper reg = (MemInfoWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            updateAccount(reg.getContent().getAccount(), reg.getContent().getBalance());
        }
    }

    private void updateExchangeInfoView(ExchangeConfig config) {
        int type = (int) config.getType();
        exchangeSummaryTV.setVisibility(View.VISIBLE);
        if (type == 1) {
            exchangeSummaryTV.setText("(" + config.getNumerator() + "现金可兑换" + config.getDenominator() + "积分)");
            exchangeTypeTV.setText("兑换类型：现金兑换积分");
        } else if (type == 2) {
            exchangeSummaryTV.setText("(" + config.getNumerator() + "积分可兑换" + config.getDenominator() + "现金)");
            exchangeTypeTV.setText("兑换类型：积分兑换现金");
        }
        typeId = config.getId();
    }

    String[] stringItems = null;

    private void showConvertList(final Context context) {

        if (configs == null || configs.isEmpty()) {
            return;
        }

        if (stringItems.length == 0) {
            return;
        }
        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
        dialog.title("兑换");
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (configs.isEmpty()) {
                    return;
                }
                updateExchangeInfoView(configs.get(position));
                //showToast("暂不支持此兑换类型，可在管理后台添加此兑换类型");
            }
        });
    }
}
