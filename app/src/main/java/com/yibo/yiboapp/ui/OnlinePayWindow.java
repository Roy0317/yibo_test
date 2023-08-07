package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.FuncResult;
import com.yibo.yiboapp.entify.OnlinePay;
import com.yibo.yiboapp.entify.SportBean;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johnson on 2017/9/19.
 */

public class OnlinePayWindow extends PopupWindow implements View.OnClickListener{

    private Context mContext;
    TextView payNameTV;
    TextView minFeeTV;
    XEditText input_money;
    GridView fixMoneys;
    Button cancelBtn;
    TextView winMoneyTV;
    TextView frontDetailTV;
    Button okBtn;
    OnlinePayListener onlinePayListener;
    String inputMoney;
    OnlinePay onlinePay;
    FixMoneyAdapter adapter;
    List<String> fixMoneyDatas = new ArrayList<>();
    int selectPos;

    String showPayNameSwitch = "off";//是否显示支付名

    public OnlinePayWindow(final Context mContext) {

        this.mContext = mContext;
        View content = LayoutInflater.from(mContext).inflate(R.layout.online_pay_window, null);
        content.isFocusableInTouchMode();
        cancelBtn = (Button) content.findViewById(R.id.cancel);
        okBtn = (Button) content.findViewById(R.id.ok);
        cancelBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
        payNameTV = (TextView) content.findViewById(R.id.payName);
        minFeeTV = (TextView) content.findViewById(R.id.min_fee);
        input_money = (XEditText) content.findViewById(R.id.input_money);
        fixMoneys = (GridView) content.findViewById(R.id.fix_moneys);
        winMoneyTV = (TextView) content.findViewById(R.id.win_money);
        frontDetailTV = (TextView) content.findViewById(R.id.front_details);
        adapter = new FixMoneyAdapter(mContext, fixMoneyDatas, R.layout.fix_money_item);
        fixMoneys.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(fixMoneys,4);
        input_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputMoney = charSequence.toString();
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
                if (!(mContext instanceof Activity)){
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
        //更新在线支付配置说明
        updatePayIntroduce();
        //更新在线支付前台支付说明


    }

    private void updateFrontDetails(String details) {
        if (!Utils.isEmptyString(details)) {
            frontDetailTV.setVisibility(View.VISIBLE);
            frontDetailTV.setText(details);
        }else{
            frontDetailTV.setVisibility(View.GONE);
        }
    }

    private void updatePayIntroduce() {
        SysConfig config = UsualMethod.getConfigFromJson(mContext);
        if (!Utils.isEmptyString(config.getPay_tips_deposit_third())) {
            try {
                String html = "<html><head></head><body>" + config.getPay_tips_deposit_third() + "</body></html>";
                winMoneyTV.setText(Html.fromHtml(html, null, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class FixMoneyAdapter extends LAdapter<String> {

        Context context;

        public FixMoneyAdapter(Context mContext, List<String> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final String item) {

            final TextView itemLayout = holder.getView(R.id.item);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isEmptyString(item)) {
                        Toast.makeText(mContext, "没有金额，请联系客服",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    selectPos = position;
                    inputMoney = item;
                    adapter.notifyDataSetChanged();
                }
            });
            itemLayout.setText(item);
            if (selectPos == -1) {
                itemLayout.setBackgroundResource(R.drawable.grey_corner_btn_bg);
                itemLayout.setTextColor(mContext.getResources().getColor(R.color.black_semi_transparent));
            }else{
                if (selectPos == position) {
                    itemLayout.setBackgroundResource(R.drawable.red_corner_btn_bg);
                    itemLayout.setTextColor(mContext.getResources().getColor(R.color.white));
                }else{
                    itemLayout.setBackgroundResource(R.drawable.grey_corner_btn_bg);
                    itemLayout.setTextColor(mContext.getResources().getColor(R.color.black_semi_transparent));
                }
            }
        }
    }

    public void setData(OnlinePay onlinePay) {

        if (onlinePay == null) {
            return;
        }
        selectPos = -1;
        this.onlinePay = onlinePay;

        SysConfig config = UsualMethod.getConfigFromJson(mContext);
        if (config.getOnlinepay_name_switch().equalsIgnoreCase("on")) {
            payNameTV.setText(!Utils.isEmptyString(onlinePay.getPayName()) ?
                    onlinePay.getPayName() : "在线充值");
        }else{
            payNameTV.setText("在线支付");
        }
        minFeeTV.setText(String.format("最小充值金额:%d元",onlinePay.getMinFee()));
        updateFrontDetails("操作说明: " + onlinePay.getFrontDetails());
        if (onlinePay != null) {
            if (!Utils.isEmptyString(onlinePay.getFixedAmount())&&
                    onlinePay.getFixedAmount().indexOf(",") > 0){
                String[] moneys = onlinePay.getFixedAmount().split(",");
                if (moneys.length > 0) {
                    fixMoneyDatas.clear();
                    fixMoneyDatas.addAll(Arrays.asList(moneys));
                    fixMoneys.setVisibility(View.VISIBLE);
                    input_money.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    Utils.setListViewHeightBasedOnChildren(fixMoneys, 4,5);
                    return;
                }
            }
        }
        fixMoneys.setVisibility(View.GONE);
        input_money.setVisibility(View.VISIBLE);
    }

    public void showWindow(View attach) {
        input_money.setText("");
        if (!(mContext instanceof Activity)) {
            throw new IllegalStateException("attach window is not in activity");
        }
        final Activity activity = (Activity) mContext;
        showAtLocation(attach, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
    }


    public interface OnlinePayListener{
        void onPayListener(String money,OnlinePay pay);
    }

    public void setOnlinePayListener(OnlinePayListener onlinePayListener) {
        this.onlinePayListener = onlinePayListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                inputMoney = "";
                selectPos = -1;
                dismiss();
                break;
            case R.id.ok:
                if (Utils.isEmptyString(inputMoney)) {
                    Toast.makeText(mContext, "请先选择金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                dismiss();
                if (onlinePayListener != null) {
                    onlinePayListener.onPayListener(inputMoney,onlinePay);
                }
                break;
        }
    }
}