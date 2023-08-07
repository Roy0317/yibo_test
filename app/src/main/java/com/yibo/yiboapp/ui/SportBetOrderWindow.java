package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.Meminfo;
import com.yibo.yiboapp.entify.SportBean;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 2017/9/19.
 * 体育投注球赛信息及投注信息
 */

public class SportBetOrderWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    SwipeMenuListView orders;
    XEditText input_money;
    TextView win_money;
    TextView min_money;
    CheckBox accept_good_peilv;
    Button cancelBtn;
    Button okBtn;
    List<SportBean> sportBetData;
    SportBetListener sportBetListener;
    String inputMoney;
    boolean acceptBestPeilv;
    OrderAdapter orderAdapter;
    DecimalFormat decimalFormat;
    CheckBox popEverytime;
    private TextView tvAccountBalances;
    boolean autoPop = true;


    public int MIN_FEE = 10;
    public int MAX_FEE = 200000;
    private boolean isMix = false;


    public SportBetOrderWindow(final Context mContext) {
        this.mContext = mContext;
        String minBetMoney = UsualMethod.getConfigFromJson(mContext).getSports_min_bet_money();
        String maxBetMoney = UsualMethod.getConfigFromJson(mContext).getSports_max_bet_money();
        MIN_FEE = Integer.parseInt(minBetMoney);
        MAX_FEE = Integer.parseInt(maxBetMoney);
        View content = LayoutInflater.from(mContext).inflate(R.layout.sport_order_detail, null);
        content.isFocusableInTouchMode();
        cancelBtn = (Button) content.findViewById(R.id.cancel);
        okBtn = (Button) content.findViewById(R.id.ok);
        cancelBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        orders = (SwipeMenuListView) content.findViewById(R.id.xlistview);
        orders.setMenuCreator(creator);
        orders.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        if (!sportBetData.isEmpty()) {
                            sportBetData.remove(position);
                            orderAdapter.notifyDataSetChanged();
                        }
                        calcTotalWinMoneyWhenAdjustInputMoney(input_money.getText().toString().trim());
                        break;
                }
                return false;
            }
        });
        input_money = (XEditText) content.findViewById(R.id.input_money);
        win_money = (TextView) content.findViewById(R.id.win_money);
        min_money = (TextView) content.findViewById(R.id.min_money);
        accept_good_peilv = (CheckBox) content.findViewById(R.id.accept_good_peilv);
        accept_good_peilv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acceptBestPeilv = isChecked;
            }
        });

        tvAccountBalances = content.findViewById(R.id.tv_account_balance);
        popEverytime = (CheckBox) content.findViewById(R.id.pop_everytime);
        popEverytime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoPop = false;
                } else {
                    autoPop = true;
                }
            }
        });

        input_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputMoney = charSequence.toString();
                calcTotalWinMoneyWhenAdjustInputMoney(inputMoney);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 设置外部可点击
        this.setOutsideTouchable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!(mContext instanceof Activity)) {
                    throw new IllegalStateException("attach window is not in activity");
                }
                final Activity activity = (Activity) mContext;
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1.0f;
                activity.getWindow().setAttributes(params);
            }
        });

        this.setContentView(content);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.adjust_window_anim);

        sportBetData = new ArrayList<>();
        decimalFormat = new DecimalFormat("0.00");
    }


    /**
     * 获取用户账号余额
     */
    public void getAccountBalances(){

        HttpUtil.get(mContext, Urls.MEMINFO_URL, null, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()){
                    Meminfo meminfo = new Gson().fromJson(result.getContent(),Meminfo.class);

                    if (!Utils.isEmptyString(meminfo.getBalance())) {
                        String format = String.format("余额:%.2f元", Double.parseDouble(meminfo.getBalance()));
                        tvAccountBalances.setText(format);
                    }

                }
            }
        });
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext.getApplicationContext());
            deleteItem.setBackground(R.color.colorPrimary);
            deleteItem.setWidth(Utils.dip2px(mContext, 90));
            deleteItem.setTitle(R.string.delete_string);
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(18);
//            deleteItem.setIcon(R.drawable.ic_delete);
            menu.addMenuItem(deleteItem);
        }
    };

    private void calcTotalWinMoneyWhenAdjustInputMoney(String inputMoney) {
        if (sportBetData == null) {
            return;
        }
        int money = !Utils.isEmptyString(inputMoney) ? Integer.parseInt(inputMoney) : 0;
        if (money == 0) {
            win_money.setText(String.format(mContext.getString(R.string.win_money_format), "0"));
            return;
        }
        float totalPeilv = 0;
        for (SportBean sb : sportBetData) {
            if (sb != null) {
                float peilv = !Utils.isEmptyString(sb.getPeilv()) ? Float.parseFloat(sb.getPeilv()) : 0;
                if (peilv != 0) {
                    if (!isMix) {
                        if (sb.getGameCategoryName().equals("全场-让球") || sb.getGameCategoryName().equals("半场-让球")
                                || sb.getGameCategoryName().equals("全场-大小") || sb.getGameCategoryName().equals("半场-大小")) {
                            peilv += 1;
                        }
                    }
                    if (totalPeilv == 0)
                        totalPeilv += peilv;
                    else
                        totalPeilv *= peilv;
                } else {
                    totalPeilv += peilv;
                }

            }
        }

        String moneValue = decimalFormat.format(totalPeilv * money - money < 0 ? 0 : totalPeilv * money - money);
        win_money.setText(String.format(mContext.getString(R.string.win_money_format), moneValue));
    }

    public void setData(List<SportBean> betData, boolean isMix) {
        this.isMix = isMix;
        if (betData == null) {
            return;
        }
        if (sportBetData != null) {
            sportBetData.clear();
            sportBetData.addAll(betData);
        }
        if (orderAdapter == null) {
            orderAdapter = new OrderAdapter(mContext, sportBetData,
                    R.layout.sport_popwinow_listitem);
            orders.setAdapter(orderAdapter);
        } else {
            orderAdapter.notifyDataSetChanged();
        }
        min_money.setText("单注最低：" + MIN_FEE + "元");
        calcTotalWinMoneyWhenAdjustInputMoney(input_money.getText().toString().trim());
    }

    public void showWindow(View attach, boolean shoudong) {
        if (!(mContext instanceof Activity)) {
            throw new IllegalStateException("attach window is not in activity");
        }
        if (!shoudong) {
            if (!autoPop) {
                return;
            }
        }
        final Activity activity = (Activity) mContext;
        showAtLocation(attach, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.7f;
        activity.getWindow().setAttributes(params);
    }


    public interface SportBetListener {
        void onSportResult(List<SportBean> data, String moneyValue, boolean appcetBestPeilv);

        void onSportCancel(List<SportBean> sportBetData, String moneyValue, boolean appcetBestPeilv);
    }

    public void setSportResultListener(SportBetListener adjustListener) {
        this.sportBetListener = adjustListener;
    }

    public class OrderAdapter extends LAdapter<SportBean> {

        Context context;

        public OrderAdapter(Context mContext, List<SportBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final SportBean item) {
            final TextView league = holder.getView(R.id.liansai_name);
            final TextView teams = holder.getView(R.id.team_names);
            final TextView peilvTV = holder.getView(R.id.peilv_names);
            final TextView playCategoryTV = holder.getView(R.id.play_category);
            league.setText(!Utils.isEmptyString(item.getLianSaiName()) ? item.getLianSaiName() : "暂无联赛名称");
            teams.setText(!Utils.isEmptyString(item.getTeamNames()) ? item.getTeamNames() : "没有球队名称");
            if (!Utils.isEmptyString(item.getBetTeamName())) {
                peilvTV.setText(item.getBetTeamName() + " @ " + item.getPeilv());
            } else {
                peilvTV.setText((!Utils.isEmptyString(item.getTxt()) ? item.getTxt() : "--") + "  @  " +
                        (!Utils.isEmptyString(item.getPeilv()) ? item.getPeilv() : "暂无赔率"));
            }
            playCategoryTV.setText(item.getGameCategoryName());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                if (sportBetListener != null) {
                    sportBetListener.onSportCancel(sportBetData, inputMoney, acceptBestPeilv);
                }
                break;
            case R.id.ok:
                if (!Utils.isEmptyString(inputMoney) && Integer.parseInt(inputMoney) < MIN_FEE) {
                    ToastUtil.showToast(mContext, String.format("最低下注金额%d元", MIN_FEE));
                    return;
                }
                if (!Utils.isEmptyString(inputMoney) && Integer.parseInt(inputMoney) > MAX_FEE) {
                    ToastUtil.showToast(mContext, String.format("最高下注金额%d元", MAX_FEE));
                    return;
                }
                dismiss();
                if (sportBetListener != null) {
                    sportBetListener.onSportResult(sportBetData, inputMoney, acceptBestPeilv);
                }
                break;
        }
    }
}