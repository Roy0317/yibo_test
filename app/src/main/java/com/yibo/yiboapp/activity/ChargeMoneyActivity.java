package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.QRCodeDecoder;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BankPay;
import com.yibo.yiboapp.entify.FastPay;
import com.yibo.yiboapp.entify.OnlinePay;
import com.yibo.yiboapp.entify.PayMethodResult;
import com.yibo.yiboapp.entify.PayMethodWraper;
import com.yibo.yiboapp.entify.SubmitPayResultWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.manager.ManagerFactory;
import com.yibo.yiboapp.ui.PayMethodWindow;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;

import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;

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
 * charge money page
 * 充值界面
 */

public class ChargeMoneyActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {
    public static final int LOAD_PAY_METHODS = 0x01;
    public static final int SUBMIT_PAY = 0x02;

    SimpleDraweeView header;
    TextView accountTV;
    TextView leftMoney;

    TextView bankAddressTV;//银行开户行
    TextView copybankAddressTV;//复制银行开户行
    TextView receiver;//银行收款帐号及卡号
    TextView copy_receiver;//复制收款帐号按钮
    TextView bankPayTip;//银行转帐并提交订单提示
    LinearLayout qrcode_area;//扫二维码支付区域
    ImageView qrcodeImage;//二维码

    private LinearLayout linearUSDT;
    private TextView textUSDTRate;
    private XEditText editUSDTNumber;
    private LinearLayout usdt_clean;
    private TextView textMark;

    XEditText inputMoney;
    XEditText inputSummary;
    Button confirm;
    TextView selectedChargeMethod;
    ImageView selectedChargeMethodImg;
    TextView changeBtn;
    TextView moneyTip;
    SysConfig config;
    LinearLayout moneyClean;
    LinearLayout summaryClean;
    float minmoney;

    PayMethodWindow popWindow;
    private boolean isUSDT;
    private String usdtRate = "0";
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    int selectPayType = BankingManager.PAY_METHOD_ONLINE;//已选择的支付方式
    int selectPosition;//对应支付方式下的支付列表中已选择的位置
    PayMethodResult payMethodResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_money);
        initView();
        getPayMethods();
    }

    public static void createIntent(Context context, String accountName, String leftMoneyName) {
        Intent intent = new Intent(context, ChargeMoneyActivity.class);
        intent.putExtra("account", accountName);
        intent.putExtra("money", leftMoneyName);
        context.startActivity(intent);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == LOAD_PAY_METHODS) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.acquire_pay_method_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.acquire_pay_method_fail));
                return;
            }
            Object regResult = result.result;
            PayMethodWraper reg = (PayMethodWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_pay_method_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新充值方式
            payMethodResult = reg.getContent();
            if (payMethodResult != null) {
                String json = "";
                List<FastPay> fast = payMethodResult.getFast();
                if (fast != null && !fast.isEmpty()) {
                    FastPay fastPay = fast.get(selectPosition);
                    json = new Gson().toJson(fastPay, FastPay.class);
                    selectPayType = BankingManager.PAY_METHOD_FAST;
                }
                List<BankPay> bank = payMethodResult.getBank();
                if (bank != null && !bank.isEmpty()) {
                    BankPay bankPay = bank.get(selectPosition);
                    json = new Gson().toJson(bankPay, BankPay.class);
                    selectPayType = BankingManager.PAY_METHOD_BANK;
                }
                List<OnlinePay> online = payMethodResult.getOnline();
                if (online != null && !online.isEmpty()) {
                    OnlinePay onlinePay = online.get(selectPosition);
                    json = new Gson().toJson(onlinePay, OnlinePay.class);
                    selectPayType = BankingManager.PAY_METHOD_ONLINE;
                }

                updatePayInfo(selectPayType, json);
            }

        } else if (action == SUBMIT_PAY) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.post_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.post_fail));
                return;
            }
            Object regResult = result.result;
            SubmitPayResultWraper reg = (SubmitPayResultWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.post_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            //更新充值方式
            showToast("订单提交成功");

            String orderNo = reg.getContent();
            String accountName = getIntent().getStringExtra("account");
            String chargeMoney = inputMoney.getText().toString().trim();

            inputMoney.setText("");
            inputSummary.setText("");
            editUSDTNumber.setText("");

            if (selectPayType == BankingManager.PAY_METHOD_ONLINE) {
                OnlinePay onlinePay = payMethodResult.getOnline().get(selectPosition);
                String payMethodName = onlinePay.getPayName();

                String payJson = new Gson().toJson(onlinePay, OnlinePay.class);
                ConfirmPayActivity.createIntent(this, "", accountName, chargeMoney, payMethodName,
                        null, null, null, null, null, selectPayType, payJson);

            } else if (selectPayType == BankingManager.PAY_METHOD_FAST) {
                FastPay fastPay = payMethodResult.getFast().get(selectPosition);
                String payMethodName = fastPay.getPayName();
                String dipositorAccount = inputSummary.getText().toString().trim();
                ConfirmPayActivity.createIntent(this, orderNo, accountName, chargeMoney, payMethodName,
                        null, null, null, dipositorAccount, fastPay.getQrCodeImg(), selectPayType, "");
            } else if (selectPayType == BankingManager.PAY_METHOD_BANK) {
                BankPay bankPay = payMethodResult.getBank().get(selectPosition);
                String payMethodName = bankPay.getPayName();
                String receiveName = bankPay.getReceiveName();
                String receiveAccount = bankPay.getBankCard();
                String dipositor = inputSummary.getText().toString().trim();
                ConfirmPayActivity.createIntent(this, orderNo, accountName, chargeMoney,
                        payMethodName, receiveName, receiveAccount, dipositor, null, null, selectPayType, "");
            }
        }
    }

    private void getPayMethods() {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_PAY_METHODS_URL);
        CrazyRequest<CrazyResult<PayMethodWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LOAD_PAY_METHODS)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .placeholderText(getString(R.string.acqurie_pay_ongoing))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<PayMethodWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private void showPayMethodWindow(PayMethodResult result, final int type, final int position) {

        if (popWindow == null) {
            popWindow = new PayMethodWindow(this);
        }
        popWindow.setData(result, type, position);
        popWindow.setPaySelectListener(new PayMethodWindow.PaySelectListener() {
            @Override
            public void onPaySelected(int type, String payJson, int position) {
                if (Utils.isEmptyString(payJson)) {
                    return;
                }
                selectPayType = type;
                selectPosition = position;
                updatePayInfo(type, payJson);
            }
        });
        if (!popWindow.isShowing()) {
            popWindow.showWindow(findViewById(R.id.item));
        }
    }

    //更新界面上对应支付方式的支付信息
    private void updatePayInfo(int type, String json) {
        if (Utils.isEmptyString(json)) {
            return;
        }

        isUSDT = false;
        showUSDTEntries(false);
        if (type == BankingManager.PAY_METHOD_ONLINE) {
            OnlinePay onlinePay = new Gson().fromJson(json, OnlinePay.class);
            moneyTip.setText(String.format(getString(R.string.min_charge_money_format), onlinePay.getMinFee()));
            minmoney = onlinePay.getMinFee();
            moneyTip.setVisibility(View.VISIBLE);
            inputSummary.setHint(getString(R.string.pay_summary_memo));
            //更新确认支付按钮下方的支付方式文字
//            selectedChargeMethod.setText(onlinePay.getPayName());
            PayMethodWindow.updateImage(this, selectedChargeMethodImg, onlinePay.getIcon(), onlinePay.getPayType());

            bankPayTip.setVisibility(View.GONE);
            receiver.setVisibility(View.GONE);
            copy_receiver.setVisibility(View.GONE);
            bankAddressTV.setVisibility(View.GONE);
            copybankAddressTV.setVisibility(View.GONE);
            qrcode_area.setVisibility(View.GONE);

        } else if (type == BankingManager.PAY_METHOD_FAST) {
            FastPay fastPay = new Gson().fromJson(json, FastPay.class);
            isUSDT = "USDT".equalsIgnoreCase(fastPay.getPayName());
            showUSDTEntries(isUSDT);
            moneyTip.setText(String.format(getString(R.string.min_charge_money_format), fastPay.getMinFee()));
            minmoney = fastPay.getMinFee();
            moneyTip.setVisibility(View.VISIBLE);
            inputSummary.setHint(fastPay.getFrontLabel() + "(请输入正确的" + fastPay.getFrontLabel() + "，否则无法到帐)");
            //更新确认支付按钮下方的支付方式文字
//            selectedChargeMethod.setText(fastPay.getPayName());
            PayMethodWindow.updateImage(this, selectedChargeMethodImg, fastPay.getIcon(), "");

            bankPayTip.setVisibility(View.GONE);
            receiver.setVisibility(View.VISIBLE);
            copy_receiver.setVisibility(View.VISIBLE);
            bankAddressTV.setVisibility(View.GONE);
            copybankAddressTV.setVisibility(View.GONE);

            receiver.setText("收款人：" + (!Utils.isEmptyString(fastPay.getReceiveName()) ? fastPay.getReceiveName() : "暂无姓名")
                    + "(" + (!Utils.isEmptyString(fastPay.getReceiveAccount()) ? fastPay.getReceiveAccount() : "暂无帐号") + ")");
            qrcode_area.setVisibility(View.VISIBLE);
            if (!Utils.isEmptyString(fastPay.getQrCodeImg())) {
                GlideUrl glideUrl = UsualMethod.getGlide(this, fastPay.getQrCodeImg().trim());
                RequestOptions options = new RequestOptions().placeholder(R.drawable.default_placeholder_picture)
                        .error(R.drawable.default_placeholder_picture);

                Glide.with(this).load(glideUrl).apply(options)
                        .into(qrcodeImage);
            }

        } else if (type == BankingManager.PAY_METHOD_BANK) {
            BankPay bankPay = new Gson().fromJson(json, BankPay.class);
            moneyTip.setText(String.format(getString(R.string.min_charge_money_format), bankPay.getMinFee()));
            minmoney = bankPay.getMinFee();
            moneyTip.setVisibility(View.VISIBLE);
            inputSummary.setHint("请输入存款人姓名");
            //更新确认支付按钮下方的支付方式文字
//            selectedChargeMethod.setText(bankPay.getPayName());
            PayMethodWindow.updateImage(this, selectedChargeMethodImg, bankPay.getIcon(), "");

            bankPayTip.setVisibility(View.VISIBLE);
            receiver.setVisibility(View.VISIBLE);
            copy_receiver.setVisibility(View.VISIBLE);
            bankAddressTV.setVisibility(View.VISIBLE);
            copybankAddressTV.setVisibility(View.VISIBLE);

            String bank = !Utils.isEmptyString(bankPay.getBankAddress()) ?
                    String.format("开户行：%s(%s)", bankPay.getBankAddress(), bankPay.getPayBankName()) : "开户行：暂无开户行";
            bankAddressTV.setText(bank);

            receiver.setText("收款人：" + (!Utils.isEmptyString(bankPay.getReceiveName()) ? bankPay.getReceiveName() : "暂无姓名")
                    + "(" + (!Utils.isEmptyString(bankPay.getBankCard()) ? bankPay.getBankCard() : "暂无帐号") + ")");
            qrcode_area.setVisibility(View.GONE);
        }

        if (selectPayType == BankingManager.PAY_METHOD_ONLINE) {
            inputSummary.setVisibility(View.GONE);
        } else {
            inputSummary.setVisibility(View.VISIBLE);
        }
    }


    private final class InputContentChangeListener implements TextWatcher {

        EditText editText;

        InputContentChangeListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String name = charSequence.toString().trim();
            if (Utils.isEmptyString(name)) {
                if (editText.getId() == R.id.input_money) {
                    moneyClean.setVisibility(View.GONE);
                } else {
                    summaryClean.setVisibility(View.GONE);
                }
            } else {
                if (editText.getId() == R.id.input_money) {
                    if(!isUSDT){
                        moneyClean.setVisibility(View.VISIBLE);
                    }
                } else {
                    summaryClean.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class USDTInputWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String number = s.toString();
            if(TextUtils.isEmpty(number)){
                usdt_clean.setVisibility(View.GONE);
                inputMoney.setText("0");
            }else {
                usdt_clean.setVisibility(View.VISIBLE);
                double tempMoney = (Double.parseDouble(usdtRate) * Integer.parseInt(number.trim()));
                inputMoney.setText(decimalFormat.format(tempMoney));
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.charge_money));
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText(getString(R.string.charge_record));

        header = (SimpleDraweeView) findViewById(R.id.header);
        header.setOnClickListener(this);
        accountTV = (TextView) findViewById(R.id.name);
        leftMoney = (TextView) findViewById(R.id.left_money);

        inputMoney = (XEditText) findViewById(R.id.input_money);
        inputSummary = (XEditText) findViewById(R.id.summary);

        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        selectedChargeMethod = (TextView) findViewById(R.id.charge_method_txt);
        selectedChargeMethodImg = (ImageView) findViewById(R.id.charge_method_img);
        changeBtn = (TextView) findViewById(R.id.change_method);
        changeBtn.setOnClickListener(this);

        linearUSDT = findViewById(R.id.linearUSDT);
        textUSDTRate = findViewById(R.id.textUSDTRate);
        editUSDTNumber = findViewById(R.id.editUSDTNumber);
        editUSDTNumber.addTextChangedListener(new USDTInputWatcher());
        usdt_clean = findViewById(R.id.usdt_clean);
        usdt_clean.setOnClickListener(this);
        textMark = findViewById(R.id.textMark);

        inputMoney.addTextChangedListener(new InputContentChangeListener(inputMoney));
        inputSummary.addTextChangedListener(new InputContentChangeListener(inputSummary));
        moneyTip = (TextView) findViewById(R.id.money_tip);

        bankAddressTV = (TextView) findViewById(R.id.bank_address);
        copybankAddressTV = (TextView) findViewById(R.id.copy_bank);
        receiver = (TextView) findViewById(R.id.receiver);
        copy_receiver = (TextView) findViewById(R.id.copy_receiver);
        copy_receiver.setOnClickListener(this);
        copybankAddressTV.setOnClickListener(this);
        bankPayTip = (TextView) findViewById(R.id.bank_pay_tip);
        qrcode_area = (LinearLayout) findViewById(R.id.qrcode_area);
        qrcodeImage = (ImageView) qrcode_area.findViewById(R.id.qrcode);
        qrcodeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showChooseSaveModeList(qrcodeImage);
                return false;
            }
        });

        moneyClean = (LinearLayout) findViewById(R.id.money_clean);
        summaryClean = (LinearLayout) findViewById(R.id.summar_clean);
        moneyClean.setOnClickListener(this);
        summaryClean.setOnClickListener(this);

        //赋值帐户名及余额
        String accountName = getIntent().getStringExtra("account");
        String moneyName = getIntent().getStringExtra("money");
        accountTV.setText(accountName);
        leftMoney.setText(String.format(getString(R.string.bottom_money_format), !Utils.isEmptyString(moneyName) ? moneyName : "0"));

        changeBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        changeBtn.getPaint().setAntiAlias(true);

        //点击主界面时，隐藏键盘
        findViewById(R.id.item).setOnClickListener(this);
        findViewById(R.id.top_area).setOnClickListener(this);
        UsualMethod.LoadUserImage(this, header);
    }

    private void showUSDTEntries(boolean isUSDT){
        if(isUSDT){
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            linearUSDT.setVisibility(View.VISIBLE);
            textMark.setText("USDT账号");
            SysConfig config = UsualMethod.getConfigFromJson(this);
            if(TextUtils.isEmpty(config.getUsdt_rate())){
                showToast("请联系客服设置汇率");
            }else {
                usdtRate = config.getUsdt_rate();
                textUSDTRate.setText(config.getUsdt_rate());
            }
            editUSDTNumber.requestFocus();
            inputMoney.setEnabled(false);
        }else {
            linearUSDT.setVisibility(View.GONE);
            textMark.setText("转帐备注");
            inputMoney.setEnabled(true);
        }
    }

    //显示保存二维码选择框
    private void showChooseSaveModeList(final ImageView imageView) {
        String[] stringItems = new String[]{"保存二维码", "识别二维码"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("选择方式");
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                saveQrCode(position == 1, imageView);
            }
        });
    }

    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            String qrcode = (String) msg.obj;
            if (!TextUtils.isEmpty(qrcode)) {
                if (qrcode.contains("wxp")) {
                    UsualMethod.toWeChatScan(ChargeMoneyActivity.this);
                } else if (qrcode.contains("ALIPAY") || qrcode.contains("alipay")) {
                    if (UsualMethod.hasInstalledAlipayClient(ChargeMoneyActivity.this))
                        UsualMethod.startAlipayClient(ChargeMoneyActivity.this, qrcode);
                    else {
                        Toast.makeText(ChargeMoneyActivity.this, "没有检测到支付宝客户端", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChargeMoneyActivity.this, "没有检测到相应的扫码客户端，" +
                            "请打开软件从相册获取二维码手动扫描", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChargeMoneyActivity.this, "没有检测到相应的扫码客户端，" +
                        "请打开软件从相册获取二维码手动扫描", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 解析图片中的二维码
     */
    private void decoderQRFromPic(final Drawable drawable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String qrCode = QRCodeDecoder.syncDecodeQRCode(Utils.drawableToBitmap(drawable));
                Message msg = new Message();
                msg.obj = qrCode;
                myhandler.sendMessage(msg);
            }
        }).start();


    }

    private void actionClean(int viewID) {
        if (viewID == R.id.money_clean) {
            inputMoney.setText("");
        } else {
            inputSummary.setText("");
        }
    }

    /**
     * @param regAction 是否需要识别并跳转扫码
     */
    private void saveQrCode(final boolean regAction, final ImageView imageView) {
        if (payMethodResult == null) {
            return;
        }
        //只有扫码支付时才有将二维码存本地功能
        if (selectPayType != BankingManager.PAY_METHOD_FAST) {
            return;
        }
        List<FastPay> fast = payMethodResult.getFast();
        if (fast == null) {
            return;
        }
        FastPay fastPay = fast.get(selectPosition);
        if (fastPay == null) {
            return;
        }
        String url = fastPay.getQrCodeImg();
        if (Utils.isEmptyString(url)) {
            showToast("二维码地址为空，无法保存");
            return;
        }


        //TODO 待测
//        Glide.with(this).load(url.trim()).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
//            @Override
//            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
//                try {
//                    if (regAction) {
//                        showToast("正在识别...");
//                        boolean saveSuccess = UsualMethod.savaBitmap(Constant.QRCODE_NAME, bytes);
//                        if (saveSuccess) {
//                            decoderQRFromPic(imageView.getDrawable());
//                        }
//                    } else {
//                        showToast("正在保存二维码...");
//                        Bitmap bitmap = Utils.bytes2Bimap(bytes);
//                        if (bitmap != null) {
//                            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "qrcode", "save_qrcode");
//                            showToast("二维码图片已保存,请使用微信,支付宝等进行扫码付款");
//                        } else {
//                            showToast("保存失败，请重试");
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        Glide.with(this).asBitmap().load(url.trim()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                try {
                    if (regAction) {
                        showToast("正在识别...");
//                        boolean saveSuccess = UsualMethod.savaBitmap(Constant.QRCODE_NAME, bytes);
                        boolean saveSuccess = UsualMethod.saveBitmapToSD(BankingManager.QRCODE_NAME, bitmap);
                        if (saveSuccess) {
                            decoderQRFromPic(imageView.getDrawable());
                        }
                    } else {
                        showToast("正在保存二维码...");
                        if (bitmap != null) {
                            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "qrcode", "save_qrcode");
                            showToast("二维码图片已保存,请使用微信,支付宝等进行扫码付款");
                        } else {
                            showToast("保存失败，请重试");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //复制收款帐号
    private void copyReceiver() {

        if (payMethodResult != null) {
            if (selectPayType == BankingManager.PAY_METHOD_FAST) {
                List<FastPay> fast = payMethodResult.getFast();
                if (fast != null && !fast.isEmpty()) {
                    FastPay fastPay = fast.get(selectPosition);
                    if (Utils.isEmptyString(fastPay.getReceiveAccount())) {
                        showToast("没有帐号可以复制");
                        return;
                    }
                    UsualMethod.copy(fastPay.getReceiveAccount(), this);
                    showToast("复制成功");
                }
            } else if (selectPayType == BankingManager.PAY_METHOD_BANK) {
                List<BankPay> bank = payMethodResult.getBank();
                if (bank != null && !bank.isEmpty()) {
                    BankPay bankPay = bank.get(selectPosition);
                    if (Utils.isEmptyString(bankPay.getBankCard())) {
                        showToast("没有帐号可以复制");
                        return;
                    }
                    UsualMethod.copy(String.format("%s(%s)", bankPay.getReceiveName(), bankPay.getBankCard()), this);
                    showToast("复制成功");
                }
            }

        }
    }

    //复制开户行
    private void copybank() {
        if (!TextUtils.isEmpty(bankAddressTV.getText().toString()) && bankAddressTV.getText().toString().contains("开户行：")) {
            UsualMethod.copy(bankAddressTV.getText().toString().replace("开户行：", ""), this);
            showToast("复制成功");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header:
                UserCenterActivity.createIntent(this);
                break;
            case R.id.copy_receiver:
                copyReceiver();
                break;
            case R.id.copy_bank:
                copybank();
                break;
            case R.id.money_clean:
                actionClean(R.id.money_clean);
                break;
            case R.id.summar_clean:
                actionClean(R.id.summar_clean);
                break;
            case R.id.confirm:
                long time = YiboPreference.instance(this).getConfirmtime();
                config = UsualMethod.getConfigFromJson(this);
                String configtime = config.getDeposit_interval_times();
                if(!TextUtils.isEmpty(configtime)){
                    long configtimelong = Long.parseLong(configtime);
                    if (System.currentTimeMillis() - time < configtimelong * 1000) {
                        showToast("两次充值间隔不能小于" + configtimelong + "秒");
                        return;
                    }
                }

                String personValue = inputSummary.getText().toString().trim();
                String money = inputMoney.getText().toString().trim();
                String usdtNumber = editUSDTNumber.getText().toString();

                if(isUSDT){
                    if(usdtRate.equals("0")){
                        showToast("请联系客服设置汇率");
                        return;
                    }else if(TextUtils.isEmpty(usdtNumber)){
                        showToast("请输入USDT数量");
                        return;
                    }else if(TextUtils.isEmpty(personValue)){
                        showToast("请输入USDT账号");
                        return;
                    }
                }

                if (Utils.isEmptyString(money)) {
                    showToast("请输入充值金额");
                    return;
                }
                if (!Utils.isNumber(money)) {
                    showToast("请输入正确格式的金额");
                    return;
                }
                if(money.contains(".") && (money.length()-1 > money.indexOf(".")+2)){
                    showToast("金额不能超过小数第二位");
                    return;
                }

                float fm = Float.parseFloat(money);
                if (fm < minmoney) {
                    showToast("最低充值金额" + minmoney + "元");
                    return;
                }

                if (!isUSDT && config != null) {
                    money = ManagerFactory.INSTANCE.getBankingManager().addMoneyFloat(config.getOnoff_show_pay_quick_addmoney(), money);
                }

                if (selectPayType == BankingManager.PAY_METHOD_FAST) {
                    if(isUSDT){
                        payUSDT(selectPayType, selectPosition, usdtRate, usdtNumber, money, personValue);
                    }else {
                        actionSubmitPay(selectPayType, selectPosition, money, "", personValue);
                    }
                } else if (selectPayType == BankingManager.PAY_METHOD_BANK) {
                    actionSubmitPay(selectPayType, selectPosition, money, personValue, "");
                } else if (selectPayType == BankingManager.PAY_METHOD_ONLINE) {
                    //在线支付不提交后台订单
//                    actionSubmitPay(selectPayType,selectPosition,money,"","");
                    OnlinePay onlinePay = payMethodResult.getOnline().get(selectPosition);
                    String payMethodName = onlinePay.getPayName();
                    String accountName = getIntent().getStringExtra("account");
                    String chargeMoney = inputMoney.getText().toString().trim();
                    String payJson = new Gson().toJson(onlinePay, OnlinePay.class);
                    ConfirmPayActivity.createIntent(this, "", accountName, chargeMoney, payMethodName,
                            null, null, null, null, null, selectPayType, payJson);
                    return;
                }

                break;
            case R.id.right_text:
                goRecords();
                break;
            case R.id.change_method:
                if (payMethodResult != null) {
                    showPayMethodWindow(payMethodResult, selectPayType, selectPosition);
                } else {
                    showToast("暂无支付方式，无法更换");
                }
                break;
            case R.id.item:
            case R.id.top_area:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    private void goRecords() {
        AccountDetailListActivity.createIntent(this);
    }

    /**
     * @param payType
     * @param position
     * @param money
     * @param depositor
     * @param account
     */
    private void actionSubmitPay(int payType, int position, String money, String depositor, String account) {
        if (payMethodResult == null) {
            return;
        }
        StringBuilder url = new StringBuilder();
        try {
            url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SUBMIT_PAY_URL);
            url.append("?payMethod=").append(payType);
            if (payType == BankingManager.PAY_METHOD_ONLINE) {
                List<OnlinePay> online = payMethodResult.getOnline();
                if (online != null && !online.isEmpty() && online.size() > position) {
                    OnlinePay onlinePay = online.get(position);
                    url.append("&money=").append(money);
                    url.append("&payId=").append(onlinePay.getId());
                }
            } else if (payType == BankingManager.PAY_METHOD_FAST) {
                List<FastPay> fast = payMethodResult.getFast();
                if (fast != null && !fast.isEmpty() && fast.size() > position) {
                    FastPay fastPay = fast.get(position);
                    url.append("&money=").append(money);
                    url.append("&account=").append(URLEncoder.encode(account, "utf-8"));
                    url.append("&payId=").append(fastPay.getId());
                }
            } else if (payType == BankingManager.PAY_METHOD_BANK) {
                List<BankPay> banks = payMethodResult.getBank();
                if (banks != null && !banks.isEmpty() && banks.size() > position) {
                    BankPay bankPay = banks.get(position);
                    url.append("&money=").append(money);
                    url.append("&depositor=").append(URLEncoder.encode(depositor, "utf-8"));
                    url.append("&bankId=").append(bankPay.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CrazyRequest<CrazyResult<SubmitPayResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(SUBMIT_PAY)
                .headers(Urls.getHeader(this))
                .shouldCache(true)
                .placeholderText(getString(R.string.post_pick_moneying))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<SubmitPayResultWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private void payUSDT(int payType, int position, String rate, String number, String money, String account){
        if (payMethodResult == null) {
            return;
        }

        StringBuilder url = new StringBuilder();
        try {
            url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SUBMIT_PAY_URL);
            url.append("?payMethod=").append(payType);
            List<FastPay> fast = payMethodResult.getFast();
            if (fast != null && !fast.isEmpty() && fast.size() > position) {
                FastPay fastPay = fast.get(position);
                url.append("&rate=").append(rate);
                url.append("&ruantity=").append(number);
                url.append("&depositor=").append(account);
                url.append("&money=").append(money);
                url.append("&transactionNo=").append(account);
                url.append("&payId=").append(fastPay.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CrazyRequest<CrazyResult<SubmitPayResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(SUBMIT_PAY)
                .headers(Urls.getHeader(this))
                .shouldCache(true)
                .placeholderText(getString(R.string.post_pick_moneying))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<SubmitPayResultWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }
}
