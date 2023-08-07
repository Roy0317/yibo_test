package com.yibo.yiboapp.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.OnlinePay;
import com.yibo.yiboapp.entify.OnlinePayResult;
import com.yibo.yiboapp.entify.OnlinePayResultWraper;
import com.yibo.yiboapp.entify.PaySysBean;
import com.yibo.yiboapp.entify.PaySysMethodWraper;
import com.yibo.yiboapp.entify.QrcodePayResultWraper;
import com.yibo.yiboapp.entify.ScanQRCodeWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.ui.ChoseBrowserDialog;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


/**
 * 再次确认充值订单提交后 订单及收款，付款帐号信息，及扫码二维码信息
 * //在线充值方式时，需要在此页面确认跳转收银台支付等操作
 */
public class ConfirmPayActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>>, ChoseBrowserDialog.OnSelectBrowserLinsenner {

    TextView orderno;
    TextView account;
    TextView chargeMoney;
    TextView payMethodName;
    TextView receiveName;
    TextView receiveAccount;
    TextView dipositor;
    TextView dipositAccount;
    ChoseBrowserDialog mDialog;

    LinearLayout qrcode_area;
    ImageView qrcodeImage;
    Button sendBtn;

    public static final int PAY_REQUEST = 0x01;
    public static final int PAY_QRCODE_REQUEST = 0x02;
    public static final int PAY_ONLINE_REQUEST = 0x03;
    public static final int SYNC_PAY_METHOD_CODE = 0x04;
    public static final int PAY_QRCODE_FROM_WEB_REQUEST = 0x05;
    public static final int PAY_FROM_WEB_REQUEST_POST = 0x06;

    String[] shunpayFilterArr = null;
    String[] scanpayFilterArr = null;
    String[] wappayFilterArr = null;
    String[] straightFilterArr = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay);
        initView();
        orderno.setText("订单号码：" + getIntent().getStringExtra("orderno"));
        account.setText("会员帐号：" + getIntent().getStringExtra("account"));
        chargeMoney.setText("充值金额：" + getIntent().getStringExtra("money") + "元");
        payMethodName.setText("支付方式：" + getIntent().getStringExtra("payMethodName"));
        payMethodName.setVisibility(View.GONE);
        String rname = getIntent().getStringExtra("receiveName");
        if (!Utils.isEmptyString(rname)) {
            receiveName.setText("收款姓名：" + rname);
            receiveName.setVisibility(View.VISIBLE);
        } else {
            receiveName.setVisibility(View.GONE);
        }
        String raccount = getIntent().getStringExtra("reeiveAccount");
        if (!Utils.isEmptyString(raccount)) {
            receiveAccount.setText("收款帐号：" + raccount);
            receiveAccount.setVisibility(View.VISIBLE);
        } else {
            receiveAccount.setVisibility(View.GONE);
        }
        String dipositorName = getIntent().getStringExtra("dipositor");
        if (!Utils.isEmptyString(dipositorName)) {
            dipositor.setText("存款人名：" + dipositorName);
            dipositor.setVisibility(View.VISIBLE);
        } else {
            dipositor.setVisibility(View.GONE);
        }
        String daccount = getIntent().getStringExtra("dipositorAccount");
        if (!Utils.isEmptyString(daccount)) {
            dipositAccount.setText("存款帐号：" + daccount);
            dipositAccount.setVisibility(View.VISIBLE);
        } else {
            dipositAccount.setVisibility(View.GONE);
        }

        int payType = getIntent().getIntExtra("payType", BankingManager.PAY_METHOD_ONLINE);
        if (payType == BankingManager.PAY_METHOD_ONLINE) {
            sendBtn.setVisibility(View.VISIBLE);
            orderno.setVisibility(View.GONE);
        } else {
            sendBtn.setVisibility(View.GONE);
            orderno.setVisibility(View.VISIBLE);
        }

        shunpayFilterArr = Constant.shunpayFilterArr;
        scanpayFilterArr = Constant.scanpayFilterArr;
        wappayFilterArr = Constant.wappayFilterArr;
        straightFilterArr = Constant.straightFilterArr;

        //进入时必需先同步最新的支付方式
        UsualMethod.syncPayMethod(this, SYNC_PAY_METHOD_CODE, true);

        mDialog = new ChoseBrowserDialog(this, this);

        String qrcodeUrl = getIntent().getStringExtra("qrcodeUrl");
        if (!Utils.isEmptyString(qrcodeUrl)) {
            qrcode_area.setVisibility(View.VISIBLE);
            GlideUrl gu = UsualMethod.getGlide(this, qrcodeUrl);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.qrcode)
                    .error(R.drawable.qrcode);
            Glide.with(this).load(gu)
                    .apply(options)
                    .into(qrcodeImage);
        } else {
            qrcode_area.setVisibility(View.GONE);
        }

    }

    public static void createIntent(Context context, String orderNo, String accountName,
                                    String chargeMoney, String payMethodName, String receiveName,
                                    String receiveAccount, String dipositor, String dipositorAccount,
                                    String qrcodeUrl, int payType, String payJson) {
        Intent intent = new Intent(context, ConfirmPayActivity.class);
        intent.putExtra("orderno", orderNo);
        intent.putExtra("account", accountName);
        intent.putExtra("money", chargeMoney);
        intent.putExtra("payMethodName", payMethodName);
        intent.putExtra("receiveName", receiveName);
        intent.putExtra("reeiveAccount", receiveAccount);
        intent.putExtra("dipositor", dipositor);
        intent.putExtra("dipositorAccount", dipositorAccount);
        intent.putExtra("qrcodeUrl", qrcodeUrl);
        intent.putExtra("payType", payType);
        intent.putExtra("payJson", payJson);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.confirm_order));
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("支付帮助");

        orderno = (TextView) findViewById(R.id.orderno);
        account = (TextView) findViewById(R.id.account);
        chargeMoney = (TextView) findViewById(R.id.charge_money);
        payMethodName = (TextView) findViewById(R.id.pay_method_name);
        receiveName = (TextView) findViewById(R.id.receive_name);
        receiveAccount = (TextView) findViewById(R.id.receive_account);
        dipositor = (TextView) findViewById(R.id.dipositor);
        dipositAccount = (TextView) findViewById(R.id.diposit_account);

        qrcode_area = (LinearLayout) findViewById(R.id.qrcode_area);
        qrcodeImage = (ImageView) qrcode_area.findViewById(R.id.qrcode);
        sendBtn = (Button) findViewById(R.id.confirm);
        sendBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.confirm) {
            YiboPreference.instance(this).setConfirmtime(System.currentTimeMillis());

            if (Utils.shiwanFromMobile(this)) {
                showToast("操作权限不足，请联系客服！");
                return;
            }

            SharedPreferences sp = getSharedPreferences("browser", 0);
            String browser = sp.getString("browser", "no");
            SysConfig config = UsualMethod.getConfigFromJson(this);
            String on_off = config.getMulti_broswer();
            if ("off".equals(on_off)) {
                actionSend(YiboPreference.instance(this).getUsername(), "0");
                return;
            }
            if (browser.isEmpty() || "no".equals(browser)) {
                mDialog.show();
            } else {
                actionSend(YiboPreference.instance(this).getUsername(), browser);
            }


            return;
        } else if (v.getId() == R.id.right_text) {
            openPayHelp();
        }
    }

    /**
     * 当发起支付时，若用户之前没有选择一个浏览器会弹出手机中所有安装的浏览器
     * 若用户已经选择了一个浏览器，下次想再弹框换一个浏览器时，需要进入设置--应用程序--选择之前选择后的
     * 浏览器程序如（uc浏览器）"--设置为默认"--清除默认值
     * 之后再发起支付时便可弹框选择浏览器
     */
    private void openPayHelp() {
        String url = Urls.BASE_URL + Urls.PORT + "/native/resources/pay/pay_help.jsp";
        Utils.LOG("a", "the pay help url = " + url);
        UsualMethod.viewLink(this, url);
    }

    private void viewLinInternal(String url) {
        Intent intent = new Intent(this, KefuActivity.class);
        intent.putExtra("title", "在线支付");
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void actionSend(String account, String browsercode) {
        String payJson = getIntent().getStringExtra("payJson");
        if (Utils.isEmptyString(payJson)) {
            showToast("支付信息不完整，无法支付");
            return;
        }

        OnlinePay onlinePay = new Gson().fromJson(payJson, OnlinePay.class);
        if (onlinePay == null) {
            showToast("支付信息不完整，无法支付");
            return;
        }

        // payType 支付方式： 1-收银台，2-银行直连，3-单微信，4-单支付宝
        String iconCss = onlinePay.getIconCss();
        String payType = onlinePay.getPayType();
        String chargeMoney = getIntent().getStringExtra("money");
        int payId = onlinePay.getId();

        if (Arrays.asList(shunpayFilterArr).contains(iconCss) &&
                !payType.equals("3") && !payType.equals("4") && !payType.equals("5") && !payType.equals("6")
                && !payType.equals("7") && !payType.equals("8") && !payType.equals("9") && !payType.equals("10")) {
            String url = Urls.BASE_URL + Urls.PORT + "/shunpay/pay.do?amount=" + chargeMoney + "&payId="
                    + payId + "&verifyCode=undefined";
            if (!Utils.isEmptyString(account)) {
                url += "&account=" + account;
            }

            viewLinInternal(url);
//            UsualMethod.viewLink(this,url,browsercode);
            finish();
        } else if (Arrays.asList(scanpayFilterArr).contains(iconCss) &&
                (payType.equals("3") || payType.equals("4") || payType.equals("5") || payType.equals("6")
                        || payType.equals("7") || payType.equals("8") || payType.equals("10"))) {

            //某些扫码付需要请求跳转到商户提供的地址，而不是直接访问
//            if (Arrays.asList(Constant.scanpanWithRedirectFilterArr).contains(iconCss)) {
//
//            }else{
            String url = Urls.BASE_URL + Urls.PORT + "/scanpay/pay.do?amount=" + chargeMoney + "&payId="
                    + payId + "";
            if (!Utils.isEmptyString(account)) {
                url += "&account=" + account;
            }

//                UsualMethod.viewLink(this,url,browsercode);
            ActiveDetailActivity.createIntent(this, "", "在线支付", url);
            //viewLinInternal(url);
            finish();
//            }
        } else {

            String bankCode = "";
            if (payType.equals("3")) {
                bankCode = "WEIXIN";
            } else if (payType.equals("4")) {
                bankCode = "ALIPAY";
            } else if (payType.equals("5")) {
                bankCode = "QQPAY";
            } else if (payType.equals("6")) {
                bankCode = "JDPAY";
            } else if (payType.equals("7")) {
                bankCode = "BAIDU";
            } else if (payType.equals("8")) {
                bankCode = "UNION";
                if (iconCss.equals("wh5zhifu")) {
                    straightFilterArr = Arrays.copyOf(straightFilterArr, straightFilterArr.length + 1);
                    straightFilterArr[straightFilterArr.length] = "wh5zhifu";
                }
            } else if (payType.equals("9")) {
                bankCode = "QUICKPAY";
            } else if (Arrays.asList(wappayFilterArr).contains(iconCss)) {
                showToast("此支付不支持网银版本");
                return;
            }
            postPay(chargeMoney, String.valueOf(payId), "", bankCode);
        }


    }

    private void postPay(String money, String payId, String verifyCode, String bankCode) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ONLINE_PAY_URL);
        configUrl.append("?amount=").append(money);
        configUrl.append("&payId=").append(payId);
        configUrl.append("&verifyCode=").append(verifyCode);
        configUrl.append("&bankcode=").append(bankCode);

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(PAY_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .placeholderText(getString(R.string.pay_jumping))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private String payReferer = "";

    /**
     * 发起获取支付二维码
     *
     * @param iconCss
     * @param redirectUrl
     * @param payId
     * @param redirectParams
     * @param payReferer
     */
    private void postQRCode(String iconCss, String redirectUrl, int payId, String redirectParams,
                            String payReferer, String synchronousReturn) {

        try {
            StringBuilder configUrl = new StringBuilder();
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.PAY_QRCODE_URL);
            configUrl.append("?iconCss=").append(iconCss);
            if (!Utils.isEmptyString(redirectUrl)) {
                configUrl.append("&redirectUrl=").append(URLEncoder.encode(redirectUrl, "utf-8"));
            }
            if (!Utils.isEmptyString(synchronousReturn)) {
                configUrl.append("&synchronousReturn=").append(URLEncoder.encode(synchronousReturn, "utf-8"));
            }
//            else {
//                configUrl.append("&synchronousReturn=");
//            }
            configUrl.append("&payId=").append(payId);
            configUrl.append("&redirectParams=").append(URLEncoder.encode(redirectParams, "utf-8"));
            //configUrl.append("&redirectParams=").append(Utils.Utf8Text(redirectParams));
            configUrl.append("&payReferer=").append(URLEncoder.encode(payReferer, "utf-8"));
            this.payReferer = payReferer;
//            String s = configUrl.toString();
            CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                    url(configUrl.toString())
                    .seqnumber(PAY_QRCODE_REQUEST)
                    .headers(Urls.getHeader(this))
                    .shouldCache(false)
                    .placeholderText(getString(R.string.scan_qrcode_generating))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(null)
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == PAY_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.online_pay_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.online_pay_fail));
                return;
            }
            Object regResult = result.result;
            String payResult = (String) regResult;
            Utils.LOG(TAG, "the pay result = " + payResult);

            if (Utils.isEmptyString(payResult)) {
                showToast("在线支付请求失败");
                return;
            }

            OnlinePayResultWraper resultWraper = new Gson().fromJson(payResult, OnlinePayResultWraper.class);
            if (resultWraper == null) {
                showToast("在线支付请求失败");
                return;
            }

            if (!resultWraper.isSuccess()) {
                showToast(!Utils.isEmptyString(resultWraper.getMsg()) ? resultWraper.getMsg() : "在线支付请求失败");
                return;
            }

            String payJson = getIntent().getStringExtra("payJson");
            if (Utils.isEmptyString(payJson)) {
                return;
            }
            OnlinePay onlinePay = new Gson().fromJson(payJson, OnlinePay.class);
            if (onlinePay == null) {
                return;
            }
            String iconCss = onlinePay.getIconCss();
            //若是网银直连本地网银时，需要跳转第三方网银地址（第三方提供收银台，收银台需要我们后台再发起表单请求生成并返回收银台地址）
            if (Arrays.asList(straightFilterArr).contains(iconCss)) {
                //组装请求参数，请求支付二维码接口
                //如果是公司内部默认域名访问则本地请求跳转地址方式获取
                //if (Urls.BASE_URL.endsWith("yb876.com")) {
                //组装请求参数，请求支付二维码接口
                OnlinePayResult data = resultWraper.getData();
                if (data != null) {
                    String redirectUrl = data.getFormParams().getRedirectUrl();
                    String payReferer = data.getPayReferer();
                    String synchronousReturn = "";
                    String paramsJson = "";
                    try {
                        JSONObject jsonObj = new JSONObject(payResult);
                        if (!jsonObj.isNull("data")) {
                            JSONObject dataObj = jsonObj.getJSONObject("data");
                            if (dataObj != null) {
                                if (!dataObj.isNull("formParams")) {
                                    JSONObject formParamsObj = dataObj.getJSONObject("formParams");
                                    if (formParamsObj != null && !formParamsObj.isNull("redirectUrl")) {
                                        formParamsObj.remove("redirectUrl");
                                    }
                                    if (formParamsObj != null && !formParamsObj.isNull("synchronous_return")) {
                                        synchronousReturn = formParamsObj.getString("synchronous_return");
                                        formParamsObj.remove("synchronous_return");
                                    }
                                    paramsJson = formParamsObj.toString();
                                    Utils.LOG(TAG, "the paramsjson = " + paramsJson);
                                    Utils.LOG(TAG, "the params url = " + redirectUrl);

                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("在线支付请求失败");
                        return;
                    }
                    int payId = onlinePay.getId();
                    //发起获取扫码的二维码请求
                    postQRCode(iconCss, redirectUrl, payId, paramsJson, payReferer, synchronousReturn);
                }
//                }else {//如果是客户解析的自定义正式域名
//                    postQRCodeFromWeb(payResult,onlinePay.getId(),iconCss,Urls.BASE_URL+Urls.PORT);
//                }
            } else {
                try {
                    JSONObject jsonObj = new JSONObject(payResult);
                    if (!jsonObj.isNull("data")) {
                        JSONObject dataObj = jsonObj.getJSONObject("data");
                        if (dataObj != null) {
                            if (!dataObj.isNull("formAction")) {
                                String formActionStr = dataObj.getString("formAction");
                                if (!dataObj.isNull("formParams")) {
                                    this.formParams = dataObj.getJSONObject("formParams");
                                    this.iconCss = iconCss;
                                    this.formActionStr = formActionStr;
                                    String postOrGet = "post"; //post或者是get方法
                                    if (!dataObj.isNull("form_s_method")) {
                                        postOrGet = dataObj.getString("form_s_method");
                                    }
                                    if (iconCss.equals("gaotongv2") || iconCss.equals("chengyizhifuh5") || iconCss.equals("myPaylah5") || iconCss.equals("quanxintong") || iconCss.equals("wanneng") || iconCss.contains("wanglianpay") || iconCss.contains("zhuolinzhifu")) {
                                        goToViewLink();
                                    } else {
                                        if (postOrGet.equals("post")) { //post方法
                                            postWeb(formActionStr, formParams.toString());
                                            return;
                                        } else { //get方法
                                            goToViewLink();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("在线支付请求失败");
                    return;
                }
            }
        } else if (action == PAY_QRCODE_FROM_WEB_REQUEST) {
            CrazyResult<Object> resultQR = response.result;
            if (resultQR == null) {
                showToast(getString(R.string.acquire_qrcode_fail));
                return;
            }
            if (!resultQR.crazySuccess) {
                showToast(getString(R.string.acquire_qrcode_fail));
                return;
            }

            QrcodePayResultWraper resultWraper = (QrcodePayResultWraper) resultQR.result;
            if (!resultWraper.isSuccess()) {
                if (!payReferer.equals("pay6.hyxjcl.com")) {
                    showToast(!Utils.isEmptyString(resultWraper.getMsg()) ? resultWraper.getMsg() :
                            getString(R.string.acquire_qrcode_fail));
                    return;
                } else { //
                    if (TextUtils.isEmpty(resultWraper.getContent())) {
                        String msg = resultWraper.getMsg();
                        int sPos = msg.indexOf("http");
                        int ePos = msg.indexOf("';");
                        String url = msg.substring(sPos, ePos);
                        UsualMethod.viewLink(this, url);
                        return;
                    }
                }
            }

            String qrCodeUrl = resultWraper.getContent();
            //当商户返回的链接是Html内容时，要将内容传到后台做一次转发，并返回地址
            if (qrCodeUrl.indexOf("<html") == 0 || qrCodeUrl.indexOf("<!DOCTYPE") == 0 ||
                    qrCodeUrl.indexOf("<form") == 0 || qrCodeUrl.indexOf("<meta") == 0) {
                ActiveDetailActivity.createIntent(ConfirmPayActivity.this, qrCodeUrl, "在线充值", false);
            } else if (qrCodeUrl.startsWith("<script")) {
                if (qrCodeUrl.contains("window.location=")) {
                    String url = qrCodeUrl.substring(qrCodeUrl.indexOf("window.location="), qrCodeUrl.lastIndexOf("\""));
                    url = url.substring(url.indexOf("\"") + 1, url.length());
                    Utils.LOG(TAG, "qrcode url === " + url);
                    UsualMethod.viewLink(this, url);
                } else if (qrCodeUrl.contains("window.location.href=") || qrCodeUrl.contains("location.href=")) {
                    if (qrCodeUrl.contains("\"")) {
                        String url = qrCodeUrl.substring(qrCodeUrl.indexOf("window.location.href="), qrCodeUrl.lastIndexOf("\""));
                        url = url.substring(url.indexOf("\"") + 1, url.length());
                        Utils.LOG(TAG, "qrcode url === " + url);
                        UsualMethod.viewLink(this, url);
                    } else if (qrCodeUrl.contains("'<")) {
                        String url = qrCodeUrl.substring(qrCodeUrl.indexOf("http"), qrCodeUrl.lastIndexOf("'<"));
                        Utils.LOG(TAG, "qrcode url === " + url);
                        UsualMethod.viewLink(this, url);
                    }
                } else if (qrCodeUrl.contains("window.location.assign")) {
                    int sPos = qrCodeUrl.indexOf("http");
                    int ePos = qrCodeUrl.indexOf("\")");
                    String url = qrCodeUrl.substring(sPos, ePos);
                    UsualMethod.viewLink(this, url);
                } else {
                    showToast("解析支付跳转地址失败");
                    return;
                }
            } else {
                UsualMethod.viewLink(this, qrCodeUrl);
            }
        } else if (action == PAY_QRCODE_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.acquire_qrcode_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.acquire_qrcode_fail));
                return;
            }
            Object regResult = result.result;
            String payResult = (String) regResult;
            Utils.LOG(TAG, "the scan qrcode result = " + payResult);

            if (Utils.isEmptyString(payResult)) {
                showToast(getString(R.string.acquire_qrcode_fail));
                return;
            }

            ScanQRCodeWraper resultWraper = new Gson().fromJson(payResult, ScanQRCodeWraper.class);
            if (resultWraper == null) {
                showToast(getString(R.string.acquire_qrcode_fail));
                return;
            }

            if (!resultWraper.isSuccess()) {
                if (!payReferer.equals("pay6.hyxjcl.com")) {
                    showToast(!Utils.isEmptyString(resultWraper.getMsg()) ? resultWraper.getMsg() :
                            getString(R.string.acquire_qrcode_fail));
                    return;
                } else { //
                    if (TextUtils.isEmpty(resultWraper.getQrcodeUrl())) {
                        String msg = resultWraper.getMsg();
                        int sPos = msg.indexOf("http");
                        int ePos = msg.indexOf("';");
                        String url = msg.substring(sPos, ePos);
                        UsualMethod.viewLink(this, url);
                        return;
                    }
                }
            }

            if (TextUtils.isEmpty(resultWraper.getQrcodeUrl())) {
                showToast(getString(R.string.acquire_qrcode_fail));
                return;
            }
            String qrCodeUrl = resultWraper.getQrcodeUrl();
            Utils.LOG(TAG, "scan qr = " + qrCodeUrl);
            //当商户返回的链接是Html内容时，要将内容传到后台做一次转发，并返回地址
            if (qrCodeUrl.indexOf("<html") == 0 || qrCodeUrl.indexOf("<!DOCTYPE") == 0 ||
                    qrCodeUrl.indexOf("<form") == 0 || qrCodeUrl.indexOf("<meta") == 0) {
                ActiveDetailActivity.createIntent(ConfirmPayActivity.this, qrCodeUrl, "在线充值", false);
            } else if (qrCodeUrl.startsWith("<script")) {
                if (qrCodeUrl.contains("window.location=")) {
                    String url = qrCodeUrl.substring(qrCodeUrl.indexOf("window.location="), qrCodeUrl.lastIndexOf("\""));
                    url = url.substring(url.indexOf("\"") + 1, url.length());
                    Utils.LOG(TAG, "qrcode url === " + url);
                    UsualMethod.viewLink(this, url);
                } else if (qrCodeUrl.contains("window.location.href=") || qrCodeUrl.contains("location.href=")) {
                    if (qrCodeUrl.contains("\"")) {
                        int temp = qrCodeUrl.indexOf("window.location.href=");
                        if (temp < 0) {
                            temp = qrCodeUrl.indexOf("location.href=");
                        }
                        String url = qrCodeUrl.substring(temp, qrCodeUrl.lastIndexOf("\""));
                        url = url.substring(url.indexOf("\"") + 1, url.length());
                        Utils.LOG(TAG, "qrcode url === " + url);
                        UsualMethod.viewLink(this, url);
                    } else if (qrCodeUrl.contains("'<")) {
                        String url = qrCodeUrl.substring(qrCodeUrl.indexOf("http"), qrCodeUrl.lastIndexOf("'<"));
                        Utils.LOG(TAG, "qrcode url === " + url);
                        UsualMethod.viewLink(this, url);
                    }
                } else if (qrCodeUrl.contains("window.location.assign")) {
                    int sPos = qrCodeUrl.indexOf("http");
                    int ePos = qrCodeUrl.indexOf("\")");
                    String url = qrCodeUrl.substring(sPos, ePos);
                    UsualMethod.viewLink(this, url);
                } else {
                    showToast("解析支付跳转地址失败");
                    return;
                }

            } else {
                if (qrCodeUrl.startsWith("http://") || qrCodeUrl.startsWith("https://") ||
                        qrCodeUrl.startsWith("HTTP://") || qrCodeUrl.startsWith("HTTPS://")) {
                    UsualMethod.viewLink(this, qrCodeUrl);
                } else if (qrCodeUrl.contains("alipays://platformapi/startapp")) { //支付宝协议，直接打开支付宝app
                    try {
                        Intent intent = Intent.parseUri(qrCodeUrl, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        intent.setSelector(null);
                        this.startActivity(intent);
                    } catch (Exception E) {
                        showToast("跳转到支付宝APP失败");
                    }
                } else if (qrCodeUrl.contains("weixin")) { //微信
                    // UsualMethod.viewLink(this, qrCodeUrl);
                    try {
                        Uri uri = Uri.parse(qrCodeUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        this.startActivity(intent);
                    } catch (Exception e) {
                        showToast("请确保手机默认浏览器为自带浏览器和安装微信");
                        e.printStackTrace();
                    }
                } else if (qrCodeUrl.contains("wxp")) { //微信收款码
                    Intent intent = new Intent(this, QrCodeImgActivity.class);
                    intent.putExtra("qrCode", qrCodeUrl);
                    startActivity(intent);
                } else {
                    ActiveDetailActivity.createIntent(this, qrCodeUrl, "支付", false);
                }
            }
        } else if (action == PAY_ONLINE_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("跳转支付失败");
                return;
            }
            if (!result.crazySuccess) {
                showToast("跳转支付失败");
                return;
            }
        } else if (action == SYNC_PAY_METHOD_CODE) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.sync_pay_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.sync_pay_fail));
                return;
            }
            Object regResult = result.result;
            PaySysMethodWraper reg = (PaySysMethodWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            syncPay(reg.getContent());
        } else if (action == PAY_ONLINE_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast("跳转支付失败");
                return;
            }
            if (!result.crazySuccess) {
                showToast("跳转支付失败");
                return;
            }
        } else if (action == PAY_FROM_WEB_REQUEST_POST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.sync_pay_fail));
                return;
            }
            if (!result.crazySuccess) {
                //showToast(getString(R.string.sync_pay_fail));
                goToViewLink();
                return;
            }
            if (result.result.toString().contains("<form")) { //后台返回表单
                ActiveDetailActivity.createIntent(this, result.result.toString(), "在线支付", false);
            } else { //暂时没有遇到不是返回表单的
                goToViewLink();
            }

        }
    }

    private JSONObject formParams;
    private String iconCss;
    private String formActionStr;

    private void goToViewLink() { //用浏览器打开
        try {
            Iterator<String> keys = formParams.keys();
            StringBuilder params = new StringBuilder();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!UsualMethod.explosedKeys(key)) {
                    String value = formParams.getString(key);
                    if (value.contains("/") && !value.contains("http")) {
                        String value2 = Utils.Utf8Text(value);
                        params.append(key).append("=").append(value2).append("&");
                    } else {
                        String value2 = URLEncoder.encode(value, "utf-8");
                        params.append(key).append("=").append(value2).append("&");
                    }
                } else {
                    if (formParams.getString(key).contains("/") || formParams.getString(key).contains("=")) {
                        String value2 = Utils.Utf8Text(formParams.getString(key));
                        params.append(key).append("=").append(value2).append("&");
                    } else {
                        params.append(key).append("=").append(formParams.getString(key)).append("&");
                    }

                }
            }
            if (params.length() > 0) {
                params.append("onlinepayType").append("=").append(iconCss).append("&");
                //Utils.LOG(TAG, "pvalue = " + params.toString());
                params = params.deleteCharAt(params.length() - 1);

                String url = formActionStr + "?" + params.toString();
                Utils.LOG(TAG, "the final open address = " + url);
                //得到跳转地址后，使用浏览器打开,但这里打开将会是以get请求形式打开，
                // 若此支付跳转请求不支持get请求则打开链接会失败
                //所以需要请求后台，获取form表单形式的url
                UsualMethod.viewLink(this, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("在线支付请求失败");
            return;
        }

    }

    /**
     * post formAction 这个地址，之前是用get直接url请求
     */
    private void postWeb(String url, String formParams) {

        try {
            StringBuilder configUrl = new StringBuilder();
            configUrl.append(url);
            JSONObject obj = new JSONObject(formParams);
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                // if (!key.equals("redirectUrl")) {
                if (configUrl.toString().contains("?")) {
                    configUrl.append("&" + key + "=").append(URLEncoder.encode(obj.getString(key), "UTF-8"));
                } else {
                    configUrl.append("?" + key + "=").append(URLEncoder.encode(obj.getString(key), "UTF-8"));
                }
                //  }
            }
            CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                    url(configUrl.toString())
                    .seqnumber(PAY_FROM_WEB_REQUEST_POST)
                    .headers(Urls.getHeader(this))
                    .shouldCache(false)
                    .placeholderText(getString(R.string.pay_jumping))
                    .priority(CrazyRequest.Priority.HIGH.ordinal())
                    .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                    .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                    .convertFactory(null)
                    .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                    .create();
            RequestManager.getInstance().startRequest(this, request);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void syncPay(PaySysBean bean) {
        if (bean == null) {
            return;
        }
        shunpayFilterArr = bean.getShunpay().getContent();
        scanpayFilterArr = bean.getScanpay().getContent();
        wappayFilterArr = bean.getWappay().getContent();
        straightFilterArr = bean.getStraightpay().getContent();
    }

    @Override
    public void ChoseAway(int position) {
        mDialog.dismiss();
        SharedPreferences sp = getSharedPreferences("browser", 0);
        sp.edit().putString("browser", position + "").commit();
        actionSend(YiboPreference.instance(this).getUsername(), position + "");
    }

    @Override
    public void ChoseOnly(int position) {
        mDialog.dismiss();
        actionSend(YiboPreference.instance(this).getUsername(), position + "");

    }
}
