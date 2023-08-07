package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.BaseRecyclerAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.QRCodeDecoder;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BankPay;
import com.yibo.yiboapp.entify.FastPay;
import com.yibo.yiboapp.entify.SubPayBean;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * 新的充值界面---经典风格
 */
public class SubChargeActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    private ConstraintLayout constraintDepositInfo;
    private EditText editDepositAmount;
    private EditText editDepositName;
    private EditText editDepositNote;

    private RecyclerView content;
    public static final int NORMAL_CHILD_ITEM = 0;
    public static final int IMAGE_CHILD_ITEM = 1;
    public static final int BUTTON_CHILD_ITEM = 2;
    public static final int QRCODE_CHILD_ITEM = 3;
    public static final int FIX_AMOUNT_ITEM = 4;
    public static final int AUTOMONEY_ChIlD_ITEM = 4;
    public static final int SUBMIT_PAY = 0x02;
    List<SubPayBean> subPayBeans;

    private long maxFee;//最大充值金额

    private long minFee;//最小充值金额
    String tipStr;

    private boolean isFast; //是否是快速入款
    private BankPay bankPay;
    String money = "";
    private boolean isFastShowQrCode = true; //快速入款是否显示二维码
    private String fixedAmount = "";
    private String mQrCode = ""; //快速入款 二维码地址
    GroupedListAdapter mainAdapter;
    XEditText iputview;
    private String details;
    private boolean isUSDT = false;
    private String usdtUrl;
    private String usdt_rate = "";
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private String preFilledMoney;
    private String preFilledName;
    private String preFilledNote;
    private SysConfig config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_charge_page);
        config = UsualMethod.getConfigFromJson(this);
        initView();
        isFast = getIntent().getBooleanExtra("isFast", true);
        maxFee = getIntent().getLongExtra("maxFee", 0);
        minFee = getIntent().getLongExtra("minFee", 0);
        fixedAmount = getIntent().getStringExtra("fixedAmount");
        details = getIntent().getStringExtra("frontDetails");

        if(isFast){
            showStep2();
        }else {
            if("on".equals(config.getOnoff_new_pay_mode())){
                showStep1();
            }else {
                showStep2();
            }
        }
    }

    /**
     * 如果是使用银行卡充值，要先输入存款人姓名、充值金额、转帐备注等资讯
     */
    private void showStep1() {
        constraintDepositInfo.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    private void showStep2() {
        constraintDepositInfo.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        String chargeData = getIntent().getStringExtra("chargeData");
        subPayBeans = prepareData(chargeData, isFast);

        SysConfig config = UsualMethod.getConfigFromJson(this);
        if (config != null) {
            tipStr = config.getOffline_charge_toast_tip();
        }
        if (isUSDT) {
            tipStr = config.getUsdt_show();
            usdtUrl = config.getUsdt_url();
        }

        mainAdapter = new GroupedListAdapter(this, subPayBeans);

        //该段代码无法跳转到个人资料页面
//        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
//            @Override
//            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
//                                      int groupPosition) {
//                UserCenterActivity.createIntent(SubChargeActivity.this);
//            }
//        });

        content.setAdapter(mainAdapter);
    }

    private List<SubPayBean> prepareData(String chargeData, boolean isFast) {
        List<SubPayBean> list = new ArrayList<>();
        if (isFast) {
            FastPay pay = new Gson().fromJson(chargeData, FastPay.class);
            if (!Utils.isEmptyString(pay.getPayName())) {
                isUSDT = pay.getPayName().equalsIgnoreCase("USDT");
            }
            if (isUSDT) {
                SubPayBean title = new SubPayBean();
                title.setTitle("收款地址");
                title.setContent(pay.getReceiveAccount());
                list.add(title);

                SubPayBean usdt = new SubPayBean();
                usdt.setTitle("收款货币");
                usdt.setContent(pay.getPayName());
                list.add(usdt);

                mQrCode = pay.getQrCodeImg().trim();
                if (config.getOnoff_payment_show_info().equals("on") && !TextUtils.isEmpty(pay.getQrCodeImg())) {
                    SubPayBean qrcode = new SubPayBean();
                    qrcode.setQrcode(pay.getQrCodeImg().trim());
                    list.add(qrcode);
                } else {
                    isFastShowQrCode = false;
                }

                SubPayBean usdtRate = new SubPayBean();
                usdtRate.setTitle("收款汇率");
                if (config != null && !Utils.isEmptyString(config.getUsdt_rate())) {
                    usdt_rate = config.getUsdt_rate().trim();
                }
                usdtRate.setCode("rate");
                usdtRate.setContent(Utils.isEmptyString(usdt_rate) ? "" : Float.parseFloat(usdt_rate) + "");
                list.add(usdtRate);

                SubPayBean inputNum = new SubPayBean();
                inputNum.setTitle("充值数量");
                inputNum.setCode("count");
                inputNum.setHolderText("请输入USDT数量");
                inputNum.setInput(true);
                list.add(inputNum);

                SubPayBean inputMoney = new SubPayBean();
                inputMoney.setTitle("充值金额");
                inputMoney.setCode("money");
                inputMoney.setHolderText("0");
                inputMoney.setInput(true);
                list.add(inputMoney);

                SubPayBean label = new SubPayBean();
                label.setTitle("交易ID/交易号");
                label.setInput(true);
                label.setHolderText("请输入后五位");
                label.setCode("depositor");
                list.add(label);
            } else {
                SubPayBean title = new SubPayBean();
                title.setTitle("收款姓名");
                title.setContent(pay.getReceiveName());
                list.add(title);

                SubPayBean account = new SubPayBean();
                account.setTitle("收款帐号");
                account.setContent(pay.getReceiveAccount());
                list.add(account);
                mQrCode = pay.getQrCodeImg().trim();
//            if (!Utils.isEmptyString(pay.getQrCodeImg())) {
                if (config.getOnoff_payment_show_info().equals("on") && !TextUtils.isEmpty(pay.getQrCodeImg())) {
                    SubPayBean qrcode = new SubPayBean();
                    qrcode.setQrcode(pay.getQrCodeImg().trim());
                    list.add(qrcode);

                } else {
                    isFastShowQrCode = false;
                }
//            }

                if (!TextUtils.isEmpty(fixedAmount)) {
                    SubPayBean autoMoney = new SubPayBean();
                    autoMoney.setTitle("存入金额");
                    autoMoney.setShowAutoMoney(true);
                    autoMoney.setHolderText("请输入充值金额");
                    list.add(autoMoney);
                }
                SubPayBean inputMoney = new SubPayBean();
                inputMoney.setTitle("充值金额");
                inputMoney.setCode("money");
                inputMoney.setHolderText("请输入充值金额");
                inputMoney.setInput(true);
                list.add(inputMoney);

                if (!Utils.isEmptyString(pay.getFrontLabel())) {
                    SubPayBean label = new SubPayBean();
                    label.setTitle(String.format("%s:", !Utils.isEmptyString(pay.getFrontLabel()) ? pay.getFrontLabel() : ""));
                    label.setInput(true);
                    label.setHolderText(String.format("请输入%s", pay.getFrontLabel()));
                    label.setCode("depositor");
                    list.add(label);
                }
            }

            if (config != null) {
                String chargeRemarkSwitch = config.getRemark_field_switch();
                if (chargeRemarkSwitch.equalsIgnoreCase("on")) {
                    SubPayBean remark = new SubPayBean();
                    remark.setTitle("转账备注");
                    remark.setCode("remark");
                    remark.setHolderText("请输入转账备注");
                    remark.setInput(true);
                    list.add(remark);
                }
            }
        } else {
            bankPay = new Gson().fromJson(chargeData, BankPay.class);

//            SubPayBean details = new SubPayBean();
//            details.setTitle("前台说明");
//            details.setFrontDetails(pay.getFrontDetails());
//            list.add(details);

            if("off".equalsIgnoreCase(config.getOnoff_new_pay_mode())){
                //new_pay_mode为on时，代表要提交订单之後才让用户看见帐户资讯，所以不显示下列资讯
                SubPayBean title = new SubPayBean();
                title.setTitle("充值银行");
                title.setContent(bankPay.getPayName());
                list.add(title);

                SubPayBean account = new SubPayBean();
                account.setTitle("收款姓名");
                account.setContent(bankPay.getReceiveName());
                list.add(account);

                SubPayBean cardno = new SubPayBean();
                cardno.setTitle("收款帐号");
                cardno.setContent(bankPay.getBankCard());
                list.add(cardno);

                SubPayBean address = new SubPayBean();
                address.setTitle("开户网点");
                address.setContent(bankPay.getBankAddress());
                list.add(address);
            }

            SubPayBean inputMoney = new SubPayBean();
            inputMoney.setTitle("充值金额");
            inputMoney.setHolderText("请输入金额");
            inputMoney.setContent(preFilledMoney);
            inputMoney.setCode("money");
            inputMoney.setInput(true);
            list.add(inputMoney);

            SubPayBean label = new SubPayBean();
            label.setTitle("存款人姓名");
            label.setHolderText("请填写真实姓名");
            label.setContent(preFilledName);
            label.setInput(true);
            label.setCode("depositor");
            list.add(label);

            if (config != null) {
                String chargeRemarkSwitch = config.getRemark_field_switch();
                if (chargeRemarkSwitch.equalsIgnoreCase("on")) {
                    SubPayBean remark = new SubPayBean();
                    remark.setTitle("转帐备注");
                    remark.setCode("remark");
                    remark.setContent(preFilledNote);
                    remark.setHolderText("请输入转帐备注");
                    remark.setInput(true);
                    list.add(remark);
                }
                if (bankPay.getAliQrcodeStatus() != null && bankPay.getAliQrcodeStatus() == 2) {
                    SubPayBean aliQrcode = new SubPayBean();
                    aliQrcode.setTitle("支付宝转卡");
                    aliQrcode.setContent(preFilledName);
                    aliQrcode.setHolderText("请填写真实姓名");
                    aliQrcode.setInput(true);
                    aliQrcode.setCode("aliqrcode");
                    aliQrcode.setShowButton(true);
                    aliQrcode.setQrLink(bankPay.getAliQrcodeLink());
                    list.add(aliQrcode);

                    //配置了手动二维码图片地址时，则也显示二维码图片

                    if (!Utils.isEmptyString(bankPay.getQrCodeImg())) {
                        SubPayBean matualAliQrcode = new SubPayBean();
                        matualAliQrcode.setTitle("支付二维码");
                        matualAliQrcode.setHolderText("");
                        matualAliQrcode.setInput(true);
                        matualAliQrcode.setCode("payQrcode");
                        matualAliQrcode.setQrcodeLink(bankPay.getQrCodeImg().trim());
                        matualAliQrcode.setShowQrcode(true);
                        list.add(matualAliQrcode);
                    }

                } else {
                    //配置了手动二维码图片地址时，则也显示二维码图片

                    if (!Utils.isEmptyString(bankPay.getQrCodeImg())) {
                        SubPayBean matualAliQrcode = new SubPayBean();
                        matualAliQrcode.setTitle("支付二维码");
                        matualAliQrcode.setHolderText("");
                        matualAliQrcode.setInput(true);
                        matualAliQrcode.setCode("payQrcode");
                        matualAliQrcode.setQrcodeLink(bankPay.getQrCodeImg().trim());
                        matualAliQrcode.setShowQrcode(true);
                        list.add(matualAliQrcode);
                    }

                }
            }
        }
        return list;
    }

    public static void createIntent(Context context, String data, boolean isFast, String account, String balance, long maxFee, long minFee, String details) {
        createIntent(context, data, "", isFast, account, balance, maxFee, minFee, details);
    }

    public static void createIntent(Context context, String data, String fixedAmount, boolean isFast, String account, String balance, long maxFee, long minFee, String details) {
        Intent intent = new Intent(context, SubChargeActivity.class);
        intent.putExtra("chargeData", data);
        intent.putExtra("account", account);
        intent.putExtra("money", balance);
        intent.putExtra("isFast", isFast);
        intent.putExtra("maxFee", maxFee);
        intent.putExtra("minFee", minFee);
        intent.putExtra("fixedAmount", fixedAmount);
        intent.putExtra("frontDetails", details);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        constraintDepositInfo = findViewById(R.id.constraintDepositInfo);
        editDepositAmount = findViewById(R.id.editDepositAmount);
        editDepositName = findViewById(R.id.editDepositName);
        editDepositNote = findViewById(R.id.editDepositNote);
        findViewById(R.id.buttonNext).setOnClickListener(this);

        content = (RecyclerView) findViewById(R.id.content);
        content.setLayoutManager(new LinearLayoutManager(this));
        tvMiddleTitle.setText("支付信息");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText(getString(R.string.charge_record));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_text:
                AccountDetailListActivity.createIntent(this);
                break;
            case R.id.buttonNext:
                String name = editDepositName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showToast("请填写姓名");
                    return;
                }

                String money = editDepositAmount.getText().toString();
                String note = editDepositNote.getText().toString();

                if(isFast && !money.contains(".")){
                    money = randomAmount(money);
                    editDepositAmount.setText(money);
                }

                if (checkFeeRangeValidity(money, maxFee, minFee)) {
                    preFilledMoney = money;
                    preFilledName = name;
                    preFilledNote = note;
                    showStep2();
                }
                break;
        }
    }


    public class GroupedListAdapter extends GroupedRecyclerViewAdapter {

        private List<SubPayBean> mGroups;

        public GroupedListAdapter(Context context, List<SubPayBean> groups) {
            super(context);
            mGroups = groups;
        }

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mGroups == null ? 0 : mGroups.size();
        }

        @Override
        public boolean hasHeader(int groupPosition) {
            return true;
        }

        @Override
        public boolean hasFooter(int groupPosition) {
            return true;
        }

        @Override
        public int getHeaderLayout(int viewType) {
            return R.layout.charge_header;
        }

        @Override
        public int getFooterLayout(int viewType) {
            return R.layout.sub_charge_footer;
        }

        @Override
        public int getChildViewType(int groupPosition, int childPosition) {
            if (childPosition == 2 && isFast && isFastShowQrCode) {
                return IMAGE_CHILD_ITEM;
            }
            SubPayBean bean = mGroups.get(childPosition);
            if (bean.isShowButton()) {
                return BUTTON_CHILD_ITEM;
            } else if (bean.isShowQrcode()) {
                return QRCODE_CHILD_ITEM;
            } else if (bean.isShowAutoMoney()) {
                return AUTOMONEY_ChIlD_ITEM;
            }
            return NORMAL_CHILD_ITEM;
        }

        @Override
        public int getChildLayout(int viewType) {
            if (viewType == IMAGE_CHILD_ITEM) {
                return R.layout.image_pay_infopage;
            } else if (viewType == BUTTON_CHILD_ITEM) {
                return R.layout.sub_page_infopage_button;
            } else if (viewType == QRCODE_CHILD_ITEM) {
                return R.layout.sub_page_info_qrcode;
            } else if (viewType == AUTOMONEY_ChIlD_ITEM) {
                return R.layout.sub_automoney_infopage;
            }
            return R.layout.sub_pay_infopage;
        }

        @Override
        public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
            //赋值帐户名及余额
            String accountName = getIntent().getStringExtra("account");
            String moneyName = getIntent().getStringExtra("money");
            String balanceName = String.format("%s",
                    !Utils.isEmptyString(moneyName) ? moneyName : "0");
            holder.setText(R.id.account, accountName);
            holder.setText(R.id.balance, balanceName);
            holder.get(R.id.online_pay).setVisibility(View.GONE);
            UsualMethod.LoadUserImage(getApplicationContext(), (SimpleDraweeView) holder.get(R.id.header));
            holder.get(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserCenterActivity.createIntent(SubChargeActivity.this);
                }
            });
        }

        @Override
        public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
            TextView tvDetails = holder.get(R.id.footer_front_details);
            if (!TextUtils.isEmpty(details)) {
                tvDetails.setText(details);
                tvDetails.setVisibility(View.VISIBLE);
            }
            TextView tv = holder.get(R.id.footer_tip);
            updatePayTips(tv);
            holder.get(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionCommit(isFast);
                }
            });
        }

        @Override
        public void onBindChildViewHolder(final BaseViewHolder holder, int groupPosition, final int childPosition) {
            final SubPayBean entity = mGroups.get(childPosition);
            if (isFast && childPosition == 2 && isFastShowQrCode) {
                updateImage((ImageView) holder.get(R.id.qrcode), entity.getQrcode());
                ((ImageView) holder.get(R.id.qrcode)).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
//                        saveQrCode(entity.getQrcode());
                        showChooseSaveModeList(entity.getQrcode(), (ImageView) holder.get(R.id.qrcode));
                        return false;
                    }
                });
            } else {
                holder.setText(R.id.title, entity.getTitle());
                if (entity.isShowButton()) {
                    holder.get(R.id.copyBtn).setVisibility(View.GONE);
//                    holder.setText(R.id.content, entity.getContent());
                    holder.get(R.id.content).setVisibility(View.VISIBLE);
//                    holder.get(R.id.content_input).setVisibility(View.GONE);
                    holder.get(R.id.content).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Utils.isEmptyString(entity.getQrLink())) {
                                showToast("支付链接不存在，请联系客服");
                                return;
                            }
                            UsualMethod.viewLink(SubChargeActivity.this, entity.getQrLink());
                        }
                    });
                } else if (entity.isShowQrcode()) {
                    holder.get(R.id.copyBtn).setVisibility(View.GONE);
//                    holder.setText(R.id.content, entity.getContent());
                    holder.get(R.id.qrcode).setVisibility(View.VISIBLE);
                    UsualMethod.updateLocImageWithUrl(SubChargeActivity.this,
                            (ImageView) holder.get(R.id.qrcode), entity.getQrcodeLink(), R.drawable.default_placeholder_picture);
                    holder.get(R.id.qrcode).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showChooseSaveModeListForBankPay(entity.getQrcodeLink(), (ImageView) holder.get(R.id.qrcode));
                            return false;
                        }
                    });
//                    holder.get(R.id.content_input).setVisibility(View.GONE);
                } else if (entity.isInput()) {
                    holder.get(R.id.copyBtn).setVisibility(View.VISIBLE);
                    XEditText view = null;
                    if (entity.getCode().equalsIgnoreCase("money")) {
                        iputview = holder.get(R.id.content_input);
                        iputview.setInputType(InputType.TYPE_CLASS_PHONE);
                        if (!TextUtils.isEmpty(fixedAmount)) {
                            iputview.setEnabled(false);
                        }
                        iputview.setHint(!Utils.isEmptyString(entity.getHolderText()) ? entity.getHolderText() : "请输入");
                        if (isUSDT) {
                            iputview.setOnClickListener(v -> {
                                iputview.setText(money);
                            });
                            iputview.setText(entity.getContent());
                            iputview.setCursorVisible(false);
                            iputview.setFocusable(false);
                            iputview.setFocusableInTouchMode(false);
                        } else {
                            iputview.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (Utils.isEmptyString(s.toString())) {
                                        return;
                                    }
                                    subPayBeans.get(childPosition).setContent(s.toString().trim());
                                }
                            });
                        }
                    } else if (entity.getCode().equalsIgnoreCase("count")) {
                        iputview = holder.get(R.id.content_input);
                        iputview.setInputType(InputType.TYPE_CLASS_NUMBER);
                        iputview.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        iputview.setHint(!Utils.isEmptyString(entity.getHolderText()) ? entity.getHolderText() : "请输入");
                        iputview.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                String trim = s.toString().trim();
                                if (trim.contains(".") || trim.startsWith("0")) {
                                    showToast("请输入正整数");
                                    iputview.setText("");
                                    subPayBeans.get(childPosition).setContent("");
                                    subPayBeans.get(childPosition + 1).setContent("");
                                    return;
                                }

                                if(TextUtils.isEmpty(trim)){
                                    trim = "0";
                                }
                                decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                                double tempMoney = (Double.parseDouble(usdt_rate) * Integer.parseInt(trim));
                                money = decimalFormat.format(tempMoney);
                                subPayBeans.get(childPosition).setContent(trim);
                                subPayBeans.get(childPosition + 1).setContent(money);
                                mainAdapter.notifyChildChanged(groupPosition, childPosition+1);
                            }
                        });
                    } else {
                        view = holder.get(R.id.content_input);
                        view.setInputType(InputType.TYPE_CLASS_TEXT);
                        view.setHint(!Utils.isEmptyString(entity.getHolderText()) ? entity.getHolderText() : "请输入");
                    }
                    if (view != null) {
                        view.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (Utils.isEmptyString(s.toString())) {
                                    return;
                                }
                                subPayBeans.get(childPosition).setContent(s.toString().trim());
                            }
                        });

                    }
                    holder.get(R.id.content).setVisibility(View.GONE);
                    holder.get(R.id.content_input).setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(entity.getContent())) {
                        holder.setText(R.id.content_input, entity.getContent());
                    }
                } else if (isFast && entity.isShowAutoMoney()) {
                    holder.setText(R.id.title, entity.getTitle());
                    RecyclerView recyclerView = holder.get(R.id.rc_content);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
                    String[] a = fixedAmount.split(",");
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < a.length; i++) {
                        list.add(a[i] + "");
                    }
                    recyclerView.setAdapter(new AutoMoneyAdapter(getApplicationContext(), list));
                } else {
                    holder.get(R.id.copyBtn).setVisibility(View.VISIBLE);
                    holder.setText(R.id.content, entity.getContent());
                    holder.get(R.id.content).setVisibility(View.VISIBLE);
                    holder.get(R.id.content_input).setVisibility(View.GONE);
                }

                if (holder.get(R.id.copyBtn) != null)
                    //复制帐号等信息
                    holder.get(R.id.copyBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String content = subPayBeans.get(childPosition).getContent();
                            if (!Utils.isEmptyString(content)) {
                                UsualMethod.copy(content, SubChargeActivity.this);
                                showToast("复制成功");
                            } else {
                                showToast("没有内容，无法复制");
                            }

                        }
                    });
            }

            if (holder.get(R.id.tip_tv) != null) {
                if (!Utils.isEmptyString(tipStr)) {
                    holder.get(R.id.tip_tv).setVisibility(View.VISIBLE);
                    ((TextView) holder.get(R.id.tip_tv)).setText(tipStr);
                } else {
                    holder.get(R.id.tip_tv).setVisibility(View.GONE);
                }
            }

            if (holder.get(R.id.url_tv) != null) {
                if (!Utils.isEmptyString(usdtUrl)) {
                    holder.get(R.id.url_tv).setVisibility(View.VISIBLE);
                    ((TextView) holder.get(R.id.url_tv)).setOnClickListener(v -> {
                        Intent intent = new Intent();
                        //意图的行为，隐式意图，打开浏览器
                        intent.setAction(Intent.ACTION_VIEW);
                        //意图的数据，解析URL
                        intent.setData(Uri.parse(usdtUrl));
                        //启动
                        startActivity(intent);
                    });
                } else {
                    holder.get(R.id.url_tv).setVisibility(View.GONE);
                }
            }
        }
    }

    private void updatePayTips(TextView tv) {
        SysConfig config = UsualMethod.getConfigFromJson(this);
        if (isFast) {
            if (!Utils.isEmptyString(config.getPay_tips_deposit_fast())) {
                try {
                    String html = "<html><head></head><body>" + config.getPay_tips_deposit_fast() + "</body></html>";
                    tv.setText(Html.fromHtml(html, null, null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (!Utils.isEmptyString(config.getPay_tips_deposit_general())) {
                try {
                    String html = "<html><head></head><body>" + config.getPay_tips_deposit_general() + "</body></html>";
                    tv.setText(Html.fromHtml(html, null, null));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //显示保存二维码选择框
    private void showChooseSaveModeList(final String url, final ImageView imageView) {
        String[] stringItems = new String[]{"保存二维码", "识别二维码"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("选择方式");
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                saveQrCode(position == 1, url, imageView);
            }
        });
    }

    //显示保存二维码选择框
    private void showChooseSaveModeListForBankPay(final String url, final ImageView imageView) {
        String[] stringItems = new String[]{"保存到相册", "识别并打开支付"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("选择方式");
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                saveQrCodeForBankPay(position == 1, url, imageView);
            }
        });
    }

    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String, Object> map = (Map<String, Object>) msg.obj;

            if (map == null) {
                return;
            }
            String qrcode = (String) map.get("qrCode");
            boolean openInBrowser = (Boolean) map.get("openInBrowser");
            if (openInBrowser) {
                if (!Utils.isEmptyString(qrcode)) {
                    if (qrcode.contains("www.alipay.com/?appId")) { //直接跳转到支付宝，有些手机不支持支付宝协议
                        if (UsualMethod.hasInstalledAlipayClient(SubChargeActivity.this))
                            UsualMethod.startAlipayClient(SubChargeActivity.this, qrcode);
                        else {
                            Toast.makeText(SubChargeActivity.this, "没有检测到支付宝客户端", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        UsualMethod.viewLink(SubChargeActivity.this, qrcode);
                    }
                } else {
                    Toast.makeText(SubChargeActivity.this, "二维码中支付地址不存在", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (!TextUtils.isEmpty(qrcode)) {
                    if (qrcode.contains("wxp") || qrcode.contains("payapp.weixin.qq.com")) {
                        UsualMethod.viewLink(SubChargeActivity.this, qrcode);
                    } else if (qrcode.contains("ALIPAY") || qrcode.contains("alipay")) {
                        if (UsualMethod.hasInstalledAlipayClient(SubChargeActivity.this))
                            UsualMethod.startAlipayClient(SubChargeActivity.this, qrcode);
                        else {
                            Toast.makeText(SubChargeActivity.this, "没有检测到支付宝客户端", Toast.LENGTH_SHORT).show();
                        }
                    } else if (qrcode.contains("http")) {
                        UsualMethod.viewLink(SubChargeActivity.this, qrcode);
                    } else {
                        Toast.makeText(SubChargeActivity.this, "没有检测到相应的扫码客户端，" +
                                "请打开软件从相册获取二维码手动扫描", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    /**
     * 解析图片中的二维码
     */
    private void decoderQRFromPic(final Drawable drawable, final boolean openInBrowser) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Utils.drawableToBitmap(drawable);
                String qrCode = QRCodeDecoder.syncDecodeQRCode(bitmap);
                Message msg = new Message();
                Map<String, Object> map = new HashMap<>();
                map.put("openInBrowser", openInBrowser);
                map.put("qrCode", qrCode);
                msg.obj = map;
                myhandler.sendMessageDelayed(msg, 1000);
            }
        }).start();
    }

    /**
     * 解析图片中的二维码uri
     */
    private void decoderQRFromPic2(final String uri, final boolean openInBrowser) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String qrCode = QRCodeDecoder.syncDecodeQRCode2(uri, SubChargeActivity.this);
                Message msg = new Message();
                Map<String, Object> map = new HashMap<>();
                map.put("openInBrowser", openInBrowser);
                map.put("qrCode", qrCode);
                msg.obj = map;
                myhandler.sendMessageDelayed(msg, 1000);
            }
        }).start();
    }

    /**
     * @param regAction 是否需要识别并跳转扫码
     */
    private void saveQrCode(final boolean regAction, String url, final ImageView imageView) {
        if (Utils.isEmptyString(url)) {
            showToast("二维码地址为空，无法保存和识别");
            return;
        }

        Glide.with(this).asBitmap().load(url.trim()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                try {
                    if (regAction) {
                        showToast("正在识别...");
                        if (bitmap != null) {
                            String uri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                                    "qrcode", "save_qrcode");
                            decoderQRFromPic(imageView.getDrawable(), false);
                        } else {
                            showToast("保存失败，请重试");
                        }
                    } else {
                        showToast("正在保存二维码...");
                        if (bitmap != null) {
                            String res = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                                    "qrcode", "save_qrcode");
                            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(res));
                            sendBroadcast(scannerIntent);
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

    /**
     * @param regAction 是否需要识别并跳转扫码
     */
    private void saveQrCodeForBankPay(final boolean regAction, String url, final ImageView imageView) {
        if (Utils.isEmptyString(url)) {
            showToast("二维码地址为空，无法保存");
            return;
        }

        Glide.with(this).asBitmap().load(url.trim()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                try {
                    if (regAction) {
                        showToast("正在识别...");
                        if (bitmap != null) {
                            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                                    "qrcode", "save_qrcode");
                            decoderQRFromPic(imageView.getDrawable(), true);
                        } else {
                            showToast("保存失败，请重试");
                        }
                    } else {
                        showToast("正在保存二维码...");
                        if (bitmap != null) {
                            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                                    "qrcode", "save_qrcode");
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

    private void actionCommit(boolean isFastPay) {
        for (SubPayBean bean : this.subPayBeans) {
            if (bean.isInput() && Utils.isEmptyString(bean.getContent())) {
                if (!bean.getCode().equalsIgnoreCase("remark") && !bean.getCode().equalsIgnoreCase("aliqrcode")
                        && !bean.getCode().equalsIgnoreCase("payQrcode")) {
                    if (bean.getCode().equalsIgnoreCase("depositor") && isUSDT){
                    }else {
                        showToast("请输入" + bean.getTitle());
                        return;
                    }
                }
            }
        }

        String depositor = "";
        String remark = "";
        String usdtNum = "";
        for (SubPayBean bean : this.subPayBeans) {
            if (bean.isInput() && !Utils.isEmptyString(bean.getContent())) {
                if (bean.getCode().equalsIgnoreCase("money")) {
                    money = bean.getContent();
                } else if (bean.getCode().equalsIgnoreCase("depositor")) {
                    depositor = bean.getContent();
                } else if (bean.getCode().equalsIgnoreCase("remark")) {
                    remark = bean.getContent();
                } else if (bean.getCode().equalsIgnoreCase("count")) {
                    usdtNum = bean.getContent();
                }
            }
        }

        if (isUSDT) {
            if (Utils.isEmptyString(usdtNum) || Integer.parseInt(usdtNum) <= 0) {
                showToast("USDT充值数量必须大于0");
                return;
            }
        }

        if(money.contains(".") && (money.length()-1 > money.indexOf(".")+2)){
            showToast("金额不能超过小数第二位");
            return;
        }

        if(isFastPay && !money.contains(".")){
            money = randomAmount(money);
        }

        if (!checkFeeRangeValidity(money, maxFee, minFee)) {
            return;
        }

        //检查两次提交间隔是否符合规定
        long time = YiboPreference.instance(SubChargeActivity.this).getConfirmtime();
        String configtime = "0";
        if (config != null && !Utils.isEmptyString(config.getDeposit_interval_times()) && Utils.isInteger(config.getDeposit_interval_times())) {
            configtime = config.getDeposit_interval_times();
        }
        long configtimelong = Long.parseLong(configtime);
        if (System.currentTimeMillis() - time < configtimelong * 1000) {
            showToast("两次充值间隔不能小于" + configtimelong + "秒");
            return;
        }

        //纪录提交时间
        YiboPreference.instance(this).setConfirmtime(System.currentTimeMillis());

        String data = getIntent().getStringExtra("chargeData");
        String account = getIntent().getStringExtra("account");
        FastPay pay = new Gson().fromJson(data, FastPay.class);
        BankPay bank = new Gson().fromJson(data, BankPay.class);
        actionSubmitPay(isFastPay ? BankingManager.PAY_METHOD_FAST : BankingManager.PAY_METHOD_BANK,
                money, depositor, pay, bank, account, remark, usdtNum);
    }

    /**
     * 依据config判断是否需要添加随机小数金额或者随机尾数
     */
    private String randomAmount(String amount) {
        double money = Utils.parseDouble(amount);
        if (money == 0) {
            return "";
        }

        SysConfig config = UsualMethod.getConfigFromJson(this);
        if (config != null && "on".equalsIgnoreCase(config.getFast_deposit_add_random())) {
            String temp = String.valueOf(new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP));
            int last = Integer.parseInt(temp.substring(temp.lastIndexOf(".") + 1));
            if (last == 0) {
                temp = temp.substring(0, temp.lastIndexOf("."));
                temp += "." + ((int) (Math.random() * 99 + 1));
            }

            if (temp.substring(temp.length() - 1).equals("0")) {
                temp = temp.substring(0, temp.length() - 1);
            }

            return temp;
        }

        if (config != null && "1".equalsIgnoreCase(config.getFast_deposit_add_money_select())) {
            int im = (int) money;
            im = im + (int) (Math.random() * 5 + 1);
            return im + "";
        }

        return amount;
    }

    private boolean checkFeeRangeValidity(String money, long maxFee, long minFee) {
        if (Utils.isEmptyString(money)) {
            showToast("请输入金额");
            return false;
        }

        if (!Utils.isNumber(money)) {
            showToast("金额格式不对，请重新输入");
            return false;
        }

        float moneyFloat = Float.parseFloat(money);
        if (moneyFloat <= 0) {
            showToast("充值金额必须大于0");
            return false;
        }

        if(moneyFloat < minFee){
            showToast("充值金額" + money +"须大於" + minFee);
            return false;
        }

        if (maxFee > 0 && moneyFloat > maxFee) {
            showToast("充值金額" + money +"须小於" + maxFee);
            return false;
        }

        return true;
    }

    /**
     * @param payType
     * @param money
     * @param depositor
     * @param usdtNum
     * @param account
     */
    private void actionSubmitPay(int payType, String money, String depositor,
                                 FastPay fastPay, BankPay bankPay, String account, String remark, String usdtNum) {
        StringBuilder url = new StringBuilder();
        try {
            url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SUBMIT_PAY_URL);
            url.append("?payMethod=").append(payType);
            if (payType == BankingManager.PAY_METHOD_FAST) {
                url.append("&money=").append(money);
                if (!Utils.isEmptyString(depositor)) {
                    url.append("&depositor=").append(URLEncoder.encode(depositor, "utf-8"));
                }
                if (!Utils.isEmptyString(account)) {
                    url.append("&account=").append(URLEncoder.encode(account, "utf-8"));
                }
                url.append("&payId=").append(fastPay.getId());
                if (!Utils.isEmptyString(remark)) {
                    url.append("&remark=").append(URLEncoder.encode(remark, "utf-8"));
                }
                if (isUSDT && !Utils.isEmptyString(usdt_rate)) {
                    url.append("&rate=").append(URLEncoder.encode(usdt_rate, "utf-8"));
                }
                if (isUSDT && !Utils.isEmptyString(depositor)) {
                    url.append("&transactionNo=").append(URLEncoder.encode(depositor, "utf-8"));
                }
                if (isUSDT && !Utils.isEmptyString(usdtNum)) {
                    url.append("&ruantity=").append(URLEncoder.encode(usdtNum, "utf-8"));
                }
            } else if (payType == BankingManager.PAY_METHOD_BANK) {
                url.append("&money=").append(money);
                if (!Utils.isEmptyString(depositor)) {
                    url.append("&depositor=").append(URLEncoder.encode(depositor, "utf-8"));
                }
                url.append("&bankId=").append(bankPay.getId());
                if (!Utils.isEmptyString(remark)) {
                    url.append("&remark=").append(URLEncoder.encode(remark, "utf-8"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CrazyRequest<CrazyResult<String>> request = new AbstractCrazyRequest.Builder().
                url(url.toString())
                .seqnumber(SUBMIT_PAY)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .placeholderText(getString(R.string.post_pick_moneying))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(null)
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == SUBMIT_PAY) {
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
            String json = (String) regResult;
            try{
                JSONObject jsonObj = new JSONObject(json);
                if(!jsonObj.optBoolean("success")){
                    showToast(!Utils.isEmptyString(jsonObj.optString("msg")) ? jsonObj.optString("msg") :
                            getString(R.string.post_fail));
                    if (jsonObj.has("code") && jsonObj.getInt("code") == 0) {
                        UsualMethod.loginWhenSessionInvalid(this);
                    }
                    return;
                }
                YiboPreference.instance(this).setToken(jsonObj.optString("accessToken"));
                goConfirmPay();
            }catch (JSONException e){
                showToast(R.string.post_fail);
                e.printStackTrace();
            }
        }
    }

    private void goConfirmPay() {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        if(!isFast){
            //bank
            keys.add("充值银行");
            values.add(bankPay.getPayName());
            keys.add("收款姓名");
            values.add(bankPay.getReceiveName());
            keys.add("收款账号");
            values.add(bankPay.getBankCard());
            keys.add("充值金额");
            values.add(money);

            if(!TextUtils.isEmpty(bankPay.getBankAddress())){
                keys.add("开戶网点");
                values.add(bankPay.getBankAddress());
            }

            SubPayBean beanDepositor = filterSubPayBean("depositor");
            if(beanDepositor != null){
                keys.add("存款人姓名");
                values.add(beanDepositor.getContent());
            }

            SubPayBean beanRemark = filterSubPayBean("remark");
            if(beanRemark != null){
                keys.add("转帐备注");
                values.add(beanRemark.getContent());
            }
        }else if(isUSDT){
            keys.add("收款地址");
            values.add(subPayBeans.get(0).getContent());
            keys.add("收款货币");
            values.add(subPayBeans.get(1).getContent());
            keys.add("充值金额");
            values.add(money);

            SubPayBean beanRate = filterSubPayBean("rate");
            if(beanRate != null){
                keys.add(beanRate.getTitle());
                values.add(beanRate.getContent());
            }

            SubPayBean beanCount = filterSubPayBean("count");
            if(beanCount != null){
                keys.add(beanCount.getTitle());
                values.add(beanCount.getContent());
            }

            SubPayBean beanDepositor = filterSubPayBean("depositor");
            if(beanDepositor != null){
                keys.add(beanDepositor.getTitle());
                values.add(beanDepositor.getContent());
            }
        }else {
            keys.add("收款姓名");
            values.add(subPayBeans.get(0).getContent());
            keys.add("收款账号");
            values.add(subPayBeans.get(1).getContent());
            keys.add("收款金额");
            values.add(money);

            SubPayBean beanDepositor = filterSubPayBean("depositor");
            if(beanDepositor != null){
                keys.add(beanDepositor.getTitle());
                values.add(beanDepositor.getContent());
            }

            SubPayBean beanRemark = filterSubPayBean("remark");
            if(beanRemark != null){
                keys.add(beanRemark.getTitle());
                values.add(beanRemark.getContent());
            }
        }

        String[] keyArray = new String[keys.size()];
        keyArray = keys.toArray(keyArray);
        String[] valueArray = new String[values.size()];
        valueArray = values.toArray(valueArray);

        ShowPayInfoAvtivity.createIntent(SubChargeActivity.this, keyArray, valueArray, mQrCode);
        showToast("订单提交成功");
        finish();
    }

    private SubPayBean filterSubPayBean(String code){
        return filterSubPayBean(code, null);
    }

    private SubPayBean filterSubPayBean(String code, String title){
        for(SubPayBean bean: subPayBeans){
            if(code != null && code.equals(bean.getCode())){
                return bean;
            }

            if(title != null && title.equals(bean.getTitle())){
                return bean;
            }
        }

        return null;
    }

    public void updateImage(final ImageView img, String url) {

        if (Utils.isEmptyString(url)) {
            img.setBackgroundResource(R.drawable.default_placeholder_picture);
            return;
        }
        GlideUrl glideUrl = UsualMethod.getGlide(this, url);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_placeholder_picture)
                .error(R.drawable.default_placeholder_picture);

        Glide.with(this).load(glideUrl)
                .apply(options)
                .into(img);
    }


    public class AutoMoneyAdapter extends BaseRecyclerAdapter<String> {
        Context ctx;
        List<String> list;
        int checkposition;

        public AutoMoneyAdapter(Context ctx, List<String> list) {
            super(ctx, list);
            this.ctx = ctx;
            this.list = list;
            checkposition = list.size();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AutoMoneyAdapter.ViewHolder(mInflater.inflate(R.layout.item_automoney, parent, false));


        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            super.onBindViewHolder(viewHolder, position);
            final ViewHolder holder = (ViewHolder) viewHolder;
            holder.tv_money.setText(getList().get(position));
            if (position == checkposition) {
                holder.tv_money.setChecked(true);
            } else {
                holder.tv_money.setChecked(false);
            }
            holder.tv_money.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_money.setChecked(true);
                    checkposition = position;
                    iputview.setText(list.get(position));
                    notifyDataSetChanged();
                }
            });


        }


        class ViewHolder extends RecyclerView.ViewHolder {
            private RadioButton tv_money;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_money = itemView.findViewById(R.id.tv_money);
            }
        }
    }
}
