package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BankPay;
import com.yibo.yiboapp.entify.FastPay;
import com.yibo.yiboapp.entify.GroupPayResult;
import com.yibo.yiboapp.entify.OnlinePay;
import com.yibo.yiboapp.entify.PayMethodResult;
import com.yibo.yiboapp.entify.PayMethodWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.ui.OnlinePayWindow;
import com.yibo.yiboapp.ui.PayMethodWindow;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * 新的充值界面---经典风格
 */
public class NewChargeMoneyActivity extends BaseActivity implements
        SessionResponse.Listener<CrazyResult<Object>> {

    private RecyclerView content;
    PayMethodResult payMethodResult;
    List<GroupPayResult> list = new ArrayList<>();
    public static final int LOAD_PAY_METHODS = 0x01;
    public static final int ACCOUNT_HEADER = 1;
    public static final int NORMAL_HEADER = 2;

    public static final int JUMP_CHILD_TYPE = 3;
    public static final int NORMAL_CHILD_TYPE = 4;


    OnlinePayWindow onlinePayWindow;
    SysConfig config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_charge_page);
        initView();
        getPayMethods();

    }

    public static void createIntent(Context context, String accountName, String leftMoneyName) {
        Intent intent = new Intent(context, NewChargeMoneyActivity.class);
        intent.putExtra("account", accountName);
        intent.putExtra("money", leftMoneyName);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        content = (RecyclerView) findViewById(R.id.content);
        content.setLayoutManager(new LinearLayoutManager(this));
        tvMiddleTitle.setText("充值");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText(getString(R.string.charge_record));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.right_text) {
            AccountDetailListActivity.createIntent(this);
        }
    }


    public class GroupedListAdapter extends GroupedRecyclerViewAdapter {

        private List<GroupPayResult> mGroups;

        public GroupedListAdapter(Context context, List<GroupPayResult> groups) {
            super(context);
            mGroups = groups;
        }

        @Override
        public int getGroupCount() {
            return mGroups == null ? 0 : mGroups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            List<GroupPayResult> children = mGroups.get(groupPosition).getChildrens();
            return children == null ? 0 : children.size();
        }

        @Override
        public boolean hasHeader(int groupPosition) {
            return true;
        }

        @Override
        public boolean hasFooter(int groupPosition) {
            return false;
        }

        @Override
        public int getHeaderViewType(int groupPosition) {
            if (groupPosition == 0) {
                return ACCOUNT_HEADER;
            } else {
                return NORMAL_HEADER;
            }
        }

        @Override
        public int getChildViewType(int groupPosition, int childPosition) {
            if (mGroups.get(groupPosition).isClickJump()) {
                return JUMP_CHILD_TYPE;
            } else {
                return NORMAL_CHILD_TYPE;
            }
        }

        @Override
        public int getHeaderLayout(int viewType) {
            if (viewType == ACCOUNT_HEADER) {
                return R.layout.charge_header;
            }
            return R.layout.new_charge_header;
        }

        @Override
        public int getFooterLayout(int viewType) {
            return 0;
        }

        @Override
        public int getChildLayout(int viewType) {
            if (viewType == NORMAL_CHILD_TYPE) {
                return R.layout.pay_method_item;
            } else if (viewType == JUMP_CHILD_TYPE) {
                return R.layout.pay_jump_item;
            }
            return R.layout.pay_method_item;
        }

        @Override
        public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
            if (groupPosition == 0) {
                //赋值帐户名及余额

                final String[] accountName = {getIntent().getStringExtra("account")};
                final String[] moneyName = {getIntent().getStringExtra("money")};
                String balanceName = String.format("%s", !Utils.isEmptyString(moneyName[0]) ? moneyName[0] : "余额:0元");

                //如果是从聊天室进入，从网络重新拉取用户数据
                if (accountName[0] == null && moneyName[0] == null) {
                    UsualMethod.getUserInfo(NewChargeMoneyActivity.this, false, "", false, meminfo -> {
                        moneyName[0] = String.format("%.2f元", Double.parseDouble(meminfo.getBalance()));
                        accountName[0] = !Utils.isEmptyString(meminfo.getAccount()) ? meminfo.getAccount() : "暂无名称";
                        String balanceName1 = String.format("%s", !Utils.isEmptyString(moneyName[0]) ? moneyName[0] : "余额:0元");
                        holder.setText(R.id.account, accountName[0]);
                        holder.setText(R.id.balance, balanceName1);
                    });
                } else {
                    holder.setText(R.id.account, accountName[0]);
                    holder.setText(R.id.balance, balanceName);
                }

                SimpleDraweeView imageView = holder.get(R.id.header);
                UsualMethod.LoadUserImage(NewChargeMoneyActivity.this, imageView);
                holder.get(R.id.item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserCenterActivity.createIntent(NewChargeMoneyActivity.this);
                    }
                });
            }

            GroupPayResult entity = mGroups.get(groupPosition);
            holder.setText(R.id.group_textView, entity.getName());

        }

        @Override
        public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
            //.......
        }

        @Override
        public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
            GroupPayResult entity = mGroups.get(groupPosition).getChildrens().get(childPosition);
            if (!entity.isClickJump()) {
                updateImage((ImageView) holder.get(R.id.icon), entity, (TextView) holder.get(R.id.iconText));
                if (mGroups.get(groupPosition).getPayCategory() == 0 &&
                        UsualMethod.getConfigFromJson(NewChargeMoneyActivity.this).getOnlinepay_name_switch().equals("on")) {
                    ((TextView) holder.get(R.id.pay_name)).setText(Html.fromHtml(getString(R.string.charge_tips_3, entity.getName(), Utils.bigNum(entity.getMinFee()), Utils.bigNum(entity.getMaxFee()))));
                } else {
                    ((TextView) holder.get(R.id.pay_name)).setText(Html.fromHtml(getString(R.string.charge_tips, Utils.bigNum(entity.getMinFee()), Utils.bigNum(entity.getMaxFee()))));
                }
                holder.get(R.id.checkbox).setVisibility(View.GONE);
                holder.get(R.id.more).setVisibility(View.VISIBLE);
                holder.get(R.id.summary).setVisibility(View.GONE);
            } else {
                holder.get(R.id.layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Utils.isEmptyString(entity.getKfUrl())) {
                            String type = UsualMethod.getConfigFromJson(NewChargeMoneyActivity.this).getOnline_service_open_switch();
                            if (!Utils.isEmptyString(type) && type.equalsIgnoreCase("v1")) {
                                UsualMethod.viewLink(NewChargeMoneyActivity.this, entity.getKfUrl());
                            } else {
                                ActiveDetailActivity.createIntent(NewChargeMoneyActivity.this, "", "在线客服", entity.getKfUrl(), false);
                            }
                        } else {
                            showToast("没有客服地址，请联系客服");
                        }
                    }
                });
            }

        }
    }

    public void updateImage(final ImageView img, GroupPayResult result, TextView imgTextIcon) {

        if (result == null) {
            return;
        }
        int payCategory = result.getPayCategory();
        String finalUrl = !Utils.isEmptyString(result.getImgUrl()) ? result.getImgUrl().trim() : "";
        if (payCategory == 0) {
            if (Utils.isEmptyString(result.getImgUrl())) {
                finalUrl = PayMethodWindow.fixPayIconWithPayType(result.getPayType());
            }
            if (Utils.isEmptyString(finalUrl)) {
                if (imgTextIcon != null) {
                    imgTextIcon.setVisibility(View.VISIBLE);
                    img.setVisibility(View.GONE);
                    imgTextIcon.setText(!Utils.isEmptyString(result.getName()) ? result.getName() : "暂无名称");
                }
                return;
            } else {
                img.setVisibility(View.VISIBLE);
                imgTextIcon.setVisibility(View.GONE);
            }
            if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
                finalUrl = String.format("%s%s%s", Urls.BASE_URL, Urls.PORT, finalUrl);
            }
        } else if (payCategory == 1) {
            if (Utils.isEmptyString(result.getImgUrl())) {
                finalUrl = UsualMethod.fixPayIconWithIconCss(result.getIconCss());
            }
            if (Utils.isEmptyString(finalUrl)) {
                if (imgTextIcon != null) {
                    imgTextIcon.setVisibility(View.VISIBLE);
                    img.setVisibility(View.GONE);
                    imgTextIcon.setText(!Utils.isEmptyString(result.getName()) ? result.getName() : "暂无名称");
                }
                return;
            } else {
                img.setVisibility(View.VISIBLE);
                imgTextIcon.setVisibility(View.GONE);
            }
            if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
                finalUrl = String.format("%s%s%s", Urls.BASE_URL, Urls.PORT, finalUrl);
            }
        } else if (payCategory == 2) {
            if (Utils.isEmptyString(result.getImgUrl())) {
                finalUrl = UsualMethod.fixPayIconWithIconCss(result.getIconCss());
                if (Utils.isEmptyString(finalUrl)) {
                    imgTextIcon.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(Utils.bankCardIcon(result.getName()));
                } else {
                    img.setVisibility(View.VISIBLE);
                    imgTextIcon.setVisibility(View.GONE);
                    if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
                        finalUrl = String.format("%s%s%s", Urls.BASE_URL, Urls.PORT, finalUrl);
                    }

                    Glide.with(this).asBitmap().load(finalUrl).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            img.setImageBitmap(resource);
                        }
                    });

                }

            } else {
                Glide.with(this).asBitmap().load(finalUrl).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        img.setImageBitmap(resource);
                    }

                });

            }
            return;
        }

        if (Utils.isEmptyString(finalUrl)) {
            img.setBackgroundResource(Utils.bankCardIcon(result.getName()));
            return;
        }

        Glide.with(this).asBitmap().load(finalUrl).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                img.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                img.setImageResource(R.drawable.icon_charge_fail);
            }
        });
    }


    private int paySection() {
        int section = 0;
        if (payMethodResult == null) {
            return section;
        }
        if (payMethodResult.getOnline() != null) {
            section += 1;
        }
        if (payMethodResult.getFast() != null) {
            section += 1;
        }
        if (payMethodResult.getBank() != null) {
            section += 1;
        }
        return section;
    }

    private List<GroupPayResult> prepareDatas(PayMethodResult result) {
        if (result == null) {
            return null;
        }
        List<GroupPayResult> list = new ArrayList<>();

        GroupPayResult online = new GroupPayResult();
        online.setName(!TextUtils.isEmpty(UsualMethod.getConfigFromJson(this).getOnline_charge_name()) ? UsualMethod.getConfigFromJson(this).getOnline_charge_name() : "线上充值");
        online.setPayCategory(0);
        List<GroupPayResult> ochilds = new ArrayList<>();
        if (result.getOnline() != null && !result.getOnline().isEmpty()) {
            for (OnlinePay onlinePay : result.getOnline()) {
                GroupPayResult g = new GroupPayResult();
                String onlineName = !TextUtils.isEmpty(onlinePay.getOnlinePayAlias()) ? onlinePay.getOnlinePayAlias() : onlinePay.getPayName();
                g.setName(onlineName);
                g.setMinFee(onlinePay.getMinFee());
                g.setMaxFee(onlinePay.getMaxFee());
                g.setImgUrl(onlinePay.getIcon());
                g.setPayType(onlinePay.getPayType());
                g.setIconCss(onlinePay.getIconCss());
                g.setIsFixedAmount(online.getIsFixedAmount());
                g.setFixedAmount(online.getFixedAmount());
                g.setPayCategory(0);
                ochilds.add(g);
            }
        }
        online.setChildrens(ochilds);
//        list.add(online);
//                }else if (num.equalsIgnoreCase("2")) {
        GroupPayResult fast = new GroupPayResult();
        fast.setName(!TextUtils.isEmpty(UsualMethod.getConfigFromJson(this).getFastcharge_charge_name()) ? UsualMethod.getConfigFromJson(this).getFastcharge_charge_name() : "快速充值");
        fast.setPayCategory(1);
        List<GroupPayResult> fchilds = new ArrayList<>();
        if (result.getFast() != null && !result.getFast().isEmpty()) {
            for (FastPay onlinePay : result.getFast()) {
                GroupPayResult g = new GroupPayResult();
                g.setName(onlinePay.getPayName());
                g.setMinFee(onlinePay.getMinFee());
                g.setMaxFee(onlinePay.getMaxFee());
                g.setImgUrl(onlinePay.getIcon());
                g.setIconCss(onlinePay.getIconCss());
                g.setPayCategory(1);
                g.setPayName(onlinePay.getPayName());
                fchilds.add(g);
            }
        }
        fast.setChildrens(fchilds);

        GroupPayResult bank = new GroupPayResult();
        bank.setPayCategory(2);
        bank.setName(!Utils.isEmptyString(UsualMethod.getConfigFromJson(this).getOffline_charge_name()) ?
                UsualMethod.getConfigFromJson(this).getOffline_charge_name() : "公司入款");
        List<GroupPayResult> bchilds = new ArrayList<>();
        if (result.getBank() != null && !result.getBank().isEmpty()) {
            for (BankPay onlinePay : result.getBank()) {
                GroupPayResult g = new GroupPayResult();
                g.setName(onlinePay.getPayName());
                g.setMaxFee(onlinePay.getMaxFee());
                g.setMinFee(onlinePay.getMinFee());
                g.setImgUrl(onlinePay.getIcon());
                g.setPayCategory(2);
                g.setIconCss(onlinePay.getIconCss());
                bchilds.add(g);
            }
        }
        bank.setChildrens(bchilds);

        reSortList(list, online, fast, bank);
        String type = UsualMethod.getConfigFromJson(this).getOnoff_show_pay_custom();
        if (!Utils.isEmptyString(type) && type.equalsIgnoreCase("on")) {
            GroupPayResult mannual = new GroupPayResult();
            mannual.setName("人工入款");
            mannual.setClickJump(true);
            List<GroupPayResult> mannualChild = new ArrayList<>();
            GroupPayResult g = new GroupPayResult();
            g.setName("点击跳转在线客服");
            g.setClickJump(true);
            g.setKfUrl(UsualMethod.getConfigFromJson(this).getOnline_handle_service_url());
            mannualChild.add(g);
            mannual.setChildrens(mannualChild);
            list.add(mannual);
        }

        return list;
    }

    /**
     * 对数据进行重新排序
     *
     * @param list
     * @param online
     * @param fast
     * @param bank
     */
    private void reSortList(List<GroupPayResult> list, GroupPayResult online,
                            GroupPayResult fast, GroupPayResult bank) {
//        充值页按 在线充值，快速充值，线下充值 按开关排序
//        后台开关字段：webpay_group_sort="";//1=在线支付,2=快速入款,3=银行入款(逗号隔开)
//        需判断开关返回的值合法性；不合法则还是按原来写死的排序
        String[] numberCount = {"1", "2", "3"}; //定义一个标准格式的数组
        List<String> newOrderNumber = new ArrayList<>();
        String groupSort = UsualMethod.getConfigFromJson(this).getWebpay_group_sort();

        try {
            if (groupSort.contains("，")) {
                groupSort = groupSort.replaceAll("，", ",");
            }
            for (String temp : groupSort.split(",")) {
                if (/*长度不为0*/temp.length() != 0 &&
                        /*是数字*/crazy_wrapper.Crazy.Utils.Utils.isNumeric(temp) &&
                        /*不包含*/!newOrderNumber.contains(temp) &&
                        /*数据在1和3之间*/(Integer.parseInt(temp) <= 3 && Integer.parseInt(temp) >= 1) &&
                        /*数组长度小于7*/newOrderNumber.size() < 3) {
                    newOrderNumber.add(temp);
                }
            }
            //数组长度小于3的时候才添加
            if (newOrderNumber.size() < 3) {
                for (String s : numberCount) {
                    if (!newOrderNumber.contains(s)) {
                        newOrderNumber.add(s);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            for (int i = 1; i <= 3; i++) {
                newOrderNumber.add(String.valueOf(i));
            }
        }

        for (String s : newOrderNumber) {
            switch (s) {
                case "1":
                    list.add(online);
                    break;
                case "2":
                    list.add(fast);
                    break;
                case "3":
                    list.add(bank);
                    break;
            }
        }
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
            //没有充值方式时，赋空处理
            if (payMethodResult.getOnline() == null) {
                List<OnlinePay> pays = new ArrayList<>();
                payMethodResult.setOnline(pays);
            }
            if (payMethodResult.getFast() == null) {
                List<FastPay> pays = new ArrayList<>();
                payMethodResult.setFast(pays);
            }
            if (payMethodResult.getBank() == null) {
                List<BankPay> pays = new ArrayList<>();
                payMethodResult.setBank(pays);
            }
            List<GroupPayResult> tempList = prepareDatas(payMethodResult);

            for (int i = 0; i < tempList.size(); i++) {
                if (tempList.get(i).getChildrens() != null && tempList.get(i).getChildrens().size() > 0) {
                    list.add(tempList.get(i));
                }
            }

            if (list.size() == 0) {
                showToast("没有充值通道，请联系客服");
            }

            GroupedListAdapter adapter = new GroupedListAdapter(this, list);
//            adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
//                @Override
//                public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
//                                          int groupPosition) {
//
//                }
//            });
            adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
                @Override
                public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                         int groupPosition, int childPosition) {
                    long time = YiboPreference.instance(NewChargeMoneyActivity.this).getConfirmtime();
                    config = UsualMethod.getConfigFromJson(NewChargeMoneyActivity.this);
                    String configtime = "0";
                    if (!Utils.isEmptyString(config.getDeposit_interval_times()) && Utils.isInteger(config.getDeposit_interval_times())) {
                        configtime = config.getDeposit_interval_times();
                    }
                    long configtimelong = Long.parseLong(configtime);
                    if (System.currentTimeMillis() - time < configtimelong * 1000) {
                        showToast("两次充值间隔不能小于" + configtimelong + "秒");
                        return;
                    } else {
                        handleChildClickEvent(groupPosition, childPosition);
                    }
                }
            });
            content.setAdapter(adapter);
        }
    }

    public void showOnlineWindow(final OnlinePay onlinePay) {

        if (onlinePay == null) {
            return;
        }
        if (onlinePayWindow == null) {
            onlinePayWindow = new OnlinePayWindow(this);
            onlinePayWindow.setOnlinePayListener(new OnlinePayWindow.OnlinePayListener() {
                @Override
                public void onPayListener(String money, OnlinePay pay) {
                    if (Utils.isEmptyString(money)) {
                        showToast("请输入充值金额");
                        return;
                    }
                    if (!Utils.isNumber(money)) {
                        showToast("请输入正确格式的金额");
                        return;
                    }

                    if (Float.parseFloat(money) <= 0) {
                        showToast("充值金额必须大于0");
                        return;
                    }

                    float fm = Float.parseFloat(money);
                    if (fm < pay.getMinFee()) {
                        showToast("金额必须大于最小充值金额");
                        return;
                    }
                    if (fm > pay.getMaxFee() && pay.getMaxFee() > 0) {
                        showToast("金额不能大于" + pay.getMaxFee());
                        return;
                    }

                    if (pay.getMaxFee() > 0 && pay.getMaxFee() < pay.getMinFee()) {
                        showToast("充值不在" + pay.getMinFee() + "-" + pay.getMaxFee() + "范围内，请联系客服");
                        return;
                    }
                    if (pay.getMaxFee() == 0 && pay.getMinFee() == 0 && TextUtils.isEmpty(pay.getFixedAmount())) {
                        showToast("充值不在" + pay.getMinFee() + "-" + pay.getMaxFee() + "范围内，请联系客服");
                        return;
                    }

                    boolean isSupPoint = UsualMethod.getConfigFromJson(NewChargeMoneyActivity.this).getOnline_charge_support_decimal_point().equals("on") ? true : false;
                    if (!isSupPoint) { //不支持小数
                        int fmint = 0;
                        if (Utils.isDouble(String.valueOf(money))) {
                            String m = String.valueOf(money);
                            if (m.indexOf(".") > 0) {
                                m = m.substring(0, m.indexOf("."));
                                if (m.length() > 0) {
                                    fmint = Integer.parseInt(m);
                                }
                            }
                            if (fmint == 0) {
                                showToast("充值金额必须大于0");
                                return;
                            }
                            fm = fmint;
                        }

                    }

                    String methodName = pay.getPayName();
                    String payJson = new Gson().toJson(pay, onlinePay.getClass());

                    //在线支付不提交后台订单
//                    actionSubmitPay(selectPayType,selectPosition,money,"","");
                    String accountName = getIntent().getStringExtra("account");

                    ConfirmPayActivity.createIntent(NewChargeMoneyActivity.this, "", accountName,
                            getMoney(fm),
                            methodName,
                            null, null, null, null,
                            null, BankingManager.PAY_METHOD_ONLINE, payJson);
                }
            });
        }
        onlinePayWindow.setData(onlinePay);
        if (!onlinePayWindow.isShowing()) {
            onlinePayWindow.showWindow(findViewById(R.id.item));
        }
    }

    private String getMoney(float m) {
        String money = String.valueOf(m);
        if (money.contains(".")) {
            String[] temp = money.split("\\.");
            if (Integer.parseInt(temp[1]) > 0) {
                return Utils.doubleToString(Double.valueOf(m));
            } else {
                return temp[0] + "";
            }
        } else {
            return money;
        }
    }

    private void goNextPage(BankPay bankPay) {
        String data = new Gson().toJson(bankPay, BankPay.class);
        String account = getIntent().getStringExtra("account");
        String money = getIntent().getStringExtra("money");
        SubChargeActivity.createIntent(this, data, false, account, money, bankPay.getMaxFee(), bankPay.getMinFee(), bankPay.getFrontDetails());
    }

    private void goNextPage(FastPay fastPay) {
        String data = new Gson().toJson(fastPay, FastPay.class);
        String account = getIntent().getStringExtra("account");
        String money = getIntent().getStringExtra("money");
        SubChargeActivity.createIntent(this, data, fastPay.getFixedAmoun(), true, account, money, fastPay.getMaxFee(), fastPay.getMinFee(), fastPay.getFrontDetails());
    }

    private void handleChildClickEvent(int groupPos, int childPos) {
        if (this.list.isEmpty()) {
            return;
        }

        GroupPayResult result = list.get(groupPos).getChildrens().get(childPos);
        List<OnlinePay> online = payMethodResult.getOnline();
        List<BankPay> bank = payMethodResult.getBank();
        List<FastPay> fast = payMethodResult.getFast();

        switch (result.getPayCategory()) {
            //0--在线充值
            //1--快速充值
            //2--线下充值
            case 0:
                showOnlineWindow(online.get(childPos));
                break;
            case 1:
                FastPay pay = fast.get(childPos);
                boolean isUSDT = pay.getPayName().equalsIgnoreCase("USDT");
                if (isUSDT) {
                    SysConfig config = UsualMethod.getConfigFromJson(this);
                    if (TextUtils.isEmpty(config.getUsdt_rate())) {
                        showToast("请联系客服设置汇率");
                        return;
                    }
                }

                goNextPage(pay);
                break;
            case 2:
                goNextPage(bank.get(childPos));
                break;
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


//    @Override
//    protected void onResume() {
//        super.onResume();
//       if(!UsualMethod.checkIsLogin(this)){
//            finish();
//       }
//    }
}
