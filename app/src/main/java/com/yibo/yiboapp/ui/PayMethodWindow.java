package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.BankPay;
import com.yibo.yiboapp.entify.FastPay;
import com.yibo.yiboapp.entify.OnlinePay;
import com.yibo.yiboapp.entify.PayMethodResult;
import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;

/**
 * Created by johnson on 2017/9/21.
 */

public class PayMethodWindow extends PopupWindow{

    private Context mContext;
    ListView onlineListView;
    ListView fastListView;
    ListView bankListView;
    TextView onlineText;
    TextView fastText;
    TextView offlineText;
    int height;

    int payType = BankingManager.PAY_METHOD_ONLINE;
    PaySelectListener paySelectListener;

    public PayMethodWindow(final Context mContext) {

        this.mContext = mContext;
        View content = LayoutInflater.from(mContext).inflate(R.layout.switch_pay_method_window, null);
        onlineListView = (ListView) content.findViewById(R.id.onlines);
        fastListView = (ListView) content.findViewById(R.id.fast);
        bankListView = (ListView) content.findViewById(R.id.bank);

        onlineText = (TextView) content.findViewById(R.id.online_text);
        fastText = (TextView) content.findViewById(R.id.fast_text);
        offlineText = (TextView) content.findViewById(R.id.offline_text);

        DisplayMetrics dm = Utils.screenInfo(mContext);
        height = dm.heightPixels;

        content.isFocusableInTouchMode();
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
        setOutsideTouchable(true);
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
    }

    public interface PaySelectListener{
        void onPaySelected(int type, String payJson, int position);
    }

    public void setPaySelectListener(PaySelectListener paySelectListener) {
        this.paySelectListener = paySelectListener;
    }

    /**
     * @param payMethodResult 支付方式数据
     * @param type 方式类型 0-在线支付 1-快捷支付 2-银行支付
     * @param position 支付方式列表中选择过的position
     */
    public void setData(PayMethodResult payMethodResult,int type,int position) {

        if (payMethodResult == null) {
            return;
        }
        //在线充值
        List<OnlinePay> online = payMethodResult.getOnline();
        if (online != null && !online.isEmpty()) {
            OnlinePayMethodAdapter onlineAdapter = new OnlinePayMethodAdapter(mContext,
                    online, R.layout.pay_method_item);
            onlineAdapter.setSelectPostion(type,position);
            onlineListView.setAdapter(onlineAdapter);
            onlineText.setVisibility(View.VISIBLE);
            Utils.setListViewHeight(onlineListView);
        }else{
            onlineText.setVisibility(View.GONE);
            onlineListView.setVisibility(View.GONE);
        }

        //快速充值
        List<FastPay> fastDatas = payMethodResult.getFast();
        if (fastDatas != null && !fastDatas.isEmpty()) {
            FastPayMethodAdapter fastAdapter = new FastPayMethodAdapter(mContext,
                    fastDatas, R.layout.pay_method_item);
            fastAdapter.setSelectPostion(type,position);
            fastListView.setAdapter(fastAdapter);
            fastText.setVisibility(View.VISIBLE);
            fastListView.setVisibility(View.VISIBLE);
            Utils.setListViewHeight(fastListView);
        }else{
            fastText.setVisibility(View.GONE);
            fastListView.setVisibility(View.GONE);
        }

        //银行充值
        List<BankPay> bankDatas = payMethodResult.getBank();
        if (bankDatas != null && !bankDatas.isEmpty()) {
            BankPayMethodAdapter bankAdapter = new BankPayMethodAdapter(mContext,
                    bankDatas, R.layout.pay_method_item);
            bankAdapter.setSelectPostion(type,position);
            bankListView.setAdapter(bankAdapter);
            offlineText.setVisibility(View.VISIBLE);
            bankListView.setVisibility(View.VISIBLE);
            Utils.setListViewHeight(bankListView);
        }else{
            offlineText.setVisibility(View.GONE);
            bankListView.setVisibility(View.GONE);
        }

    }

    public void showWindow(View attach) {
        if (!(mContext instanceof Activity)){
            throw new IllegalStateException("attach window is not in activity");
        }
        final Activity activity = (Activity) mContext;
        showAtLocation(attach, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
    }

    public static void updateImage(final Context mContext, final ImageView img, String url, String payType) {

        Utils.LOG("aaa","the pay image url == "+url);
        String finalUrl = url;
        if (Utils.isEmptyString(url)) {
            finalUrl = fixPayIconWithPayType(payType);
        }
        if (Utils.isEmptyString(finalUrl)) {
            img.setBackgroundResource(R.drawable.default_pay_icon);
            return;
        }
        if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
            finalUrl = String.format("%s%s%s", Urls.BASE_URL, Urls.PORT, finalUrl);
        }

        Glide.with(mContext).asBitmap().load(finalUrl.trim()).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();

                int height = Utils.dip2px(mContext,60) * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = img.getLayoutParams();
                para.height = height;
                para.width = Utils.dip2px(mContext,60);
                img.setImageBitmap(resource);
            }
        });
    }

    public static String fixPayIconWithPayType(String payType) {
        if (Utils.isEmptyString(payType)) {
            return "";
        }
        if (payType.equals("3")) {
            return "/native/resources/images/weixin.jpg";
        } else if (payType.equals("4")) {
            return "/native/resources/images/zhifubao.jpg";
        } else if (payType.equals("5")) {
            return "/native/resources/images/qqpay.png";
        } else if (payType.equals("6")) {
            return "/native/resources/images/jdpay.jpg";
        } else if (payType.equals("7")) {
            return "/native/resources/images/baidu.jpg";
        } else if (payType.equals("8")) {
            return "/native/resources/images/union.jpg";
        } else if (payType.equals("1") || payType.equals("2") ) {
            return "/native/resources/images/shouyintai.png";
        } else if (payType.equals("10")) {
            return "/native/resources/images/meituan.png";
        } else if (payType.equals("9")) {
            return "/native/resources/images/kuaijie.png";
        }
        return "";
    }

    //在线充值方式adapter
    public final class OnlinePayMethodAdapter extends LAdapter<OnlinePay> {

        Context context;
        int selectPostion;
        int type;
        public OnlinePayMethodAdapter(Context mContext, List<OnlinePay> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        public void setSelectPostion(int type,int selectPostion) {
            this.selectPostion = selectPostion;
            this.type = type;
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final OnlinePay item) {
            ImageView icon = holder.getView(R.id.icon);
            TextView payName = holder.getView(R.id.pay_name);
            TextView summaryTV = holder.getView(R.id.summary);
            CheckBox checkBox = holder.getView(R.id.checkbox);
            updateImage(context,icon,item.getIcon(),item.getPayType());
            String txt = "";
//            if (item.getMinFee() > 0) {
                txt += "(最小充值金额" + item.getMinFee() + "元)";
//            }
            if (selectPostion == position && type == BankingManager.PAY_METHOD_ONLINE) {
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            payName.setText(txt);
            summaryTV.setVisibility(View.GONE);
            RelativeLayout layout = holder.getView(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paySelectListener != null) {
                        String onlineJson = new Gson().toJson(item, OnlinePay.class);
                        paySelectListener.onPaySelected(BankingManager.PAY_METHOD_ONLINE,onlineJson,position);
                    }
                    dismiss();
                }
            });
        }
    }

    //快速充值方式adapter
    public final class FastPayMethodAdapter extends LAdapter<FastPay> {

        Context context;
        int selectPostion;
        int type;
        public FastPayMethodAdapter(Context mContext, List<FastPay> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        public void setSelectPostion(int type,int selectPostion) {
            this.selectPostion = selectPostion;
            this.type = type;
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final FastPay item) {
            ImageView icon = holder.getView(R.id.icon);
            TextView payName = holder.getView(R.id.pay_name);
            TextView summaryTV = holder.getView(R.id.summary);
            CheckBox checkBox = holder.getView(R.id.checkbox);
            updateImage(context,icon,item.getIcon(),"");
            String txt = item.getPayName();
            if (item.getMinFee() > 0) {
                txt += "(最小充值金额" + item.getMinFee() + "元)";
            }
            if (selectPostion == position && type == BankingManager.PAY_METHOD_FAST) {
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            payName.setText(txt);
            summaryTV.setVisibility(View.GONE);
            RelativeLayout layout = holder.getView(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paySelectListener != null) {
                        String fastJson = new Gson().toJson(item, FastPay.class);
                        paySelectListener.onPaySelected(BankingManager.PAY_METHOD_FAST,fastJson,position);
                    }
                    dismiss();
                }
            });
        }
    }

    //银行充值方式adapter
    public final class BankPayMethodAdapter extends LAdapter<BankPay> {

        Context context;
        int selectPostion;
        int type;
        public BankPayMethodAdapter(Context mContext, List<BankPay> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        public void setSelectPostion(int type,int selectPostion) {
            this.selectPostion = selectPostion;
            this.type = type;
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final BankPay item) {
            ImageView icon = holder.getView(R.id.icon);
            TextView payName = holder.getView(R.id.pay_name);
            TextView summaryTV = holder.getView(R.id.summary);
            CheckBox checkBox = holder.getView(R.id.checkbox);
            updateImage(context,icon,item.getIcon(),"");
            String txt = item.getPayName();
            if (item.getMinFee() > 0) {
                txt += "(最小充值金额" + item.getMinFee() + "元)";
            }
            if (selectPostion == position && type == BankingManager.PAY_METHOD_BANK) {
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            payName.setText(txt);
            summaryTV.setVisibility(View.GONE);
            RelativeLayout layout = holder.getView(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paySelectListener != null) {
                        String bankJson = new Gson().toJson(item, BankPay.class);
                        paySelectListener.onPaySelected(BankingManager.PAY_METHOD_BANK,bankJson,position);
                    }
                    dismiss();
                }
            });
        }
    }

}
