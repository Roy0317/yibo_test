package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.ZhudangAdapter;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.utils.Utils;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 2017/9/21.
 */

public class ZhudangDetailPopWindow extends PopupWindow{

    private Context mContext;
    TextView beishuTV;
    TextView qihaoTV;
    TextView totalMoneyTV;
    SwipeMenuListView listView;

    ZhudangAdapter menuAdapter;
    int height;

    public ZhudangDetailPopWindow(final Context mContext) {

        this.mContext = mContext;
        View content = LayoutInflater.from(mContext).inflate(R.layout.zhudang_detail_popwindow, null);
        beishuTV = (TextView) content.findViewById(R.id.beishu);
        qihaoTV = (TextView) content.findViewById(R.id.qihao);
        totalMoneyTV = (TextView) content.findViewById(R.id.total_money);
        listView = (SwipeMenuListView) content.findViewById(R.id.cListview);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.order_footer, null);
        listView.addFooterView(view);

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
        this.setHeight((height*3)/5);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.adjust_window_anim);
    }

    public void setData(String json, int beishu, String qihao,float totalMoney) {

        Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {}.getType();
        List<OrderDataInfo> list = new Gson().fromJson(json, listType);

        menuAdapter = new ZhudangAdapter(mContext, list, R.layout.order_list_item);
        listView.setAdapter(menuAdapter);
        beishuTV.setText("追号倍数："+beishu+"倍");
        qihaoTV.setText("当前期号："+qihao+"");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String money = decimalFormat.format(totalMoney);
        totalMoneyTV.setText(String.format(mContext.getString(R.string.money_zuihao_format),money));
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

}
