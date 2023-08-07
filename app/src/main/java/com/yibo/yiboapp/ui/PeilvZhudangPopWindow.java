package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
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

public class PeilvZhudangPopWindow extends PopupWindow {

    private Context mContext;
    TextView qihaoTV;
    TextView totalMoneyTV;
    SwipeMenuListView listView;
    TextView title;

    ZhudangAdapter menuAdapter;
    int height;

    List<OrderDataInfo> list;
    PeilvWindowListener peilvWindowListener;


    public PeilvZhudangPopWindow(final Context mContext) {

        this.mContext = mContext;
        View content = LayoutInflater.from(mContext).inflate(R.layout.peilv_zhudang_pop_layout, null);
        qihaoTV = (TextView) content.findViewById(R.id.qihao);
        totalMoneyTV = (TextView) content.findViewById(R.id.total_money);
        listView = (SwipeMenuListView) content.findViewById(R.id.cListview);
        title = (TextView) content.findViewById(R.id.title);
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

                if (list != null && !list.isEmpty()) {
                    Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {}.getType();
                    String json = new Gson().toJson(list, listType);
                    if (peilvWindowListener != null) {
                        peilvWindowListener.onWindowDismiss(json);
                    }
                }
            }
        });
        setOutsideTouchable(true);
        this.setContentView(content);
        this.setHeight(height*2/3);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.adjust_window_anim);
    }

    public interface PeilvWindowListener{
        void onWindowDismiss(String json);
    }

    public void setPeilvWindowListener(PeilvWindowListener peilvWindowListener) {
        this.peilvWindowListener = peilvWindowListener;
    }

    public void setData(List<OrderDataInfo> orders, String qihao) {

//        Type listType = new TypeToken<ArrayList<OrderDataInfo>>() {}.getType();
//        list = new Gson().fromJson(json, listType);
        list = orders;
        menuAdapter = new ZhudangAdapter(mContext, list, R.layout.order_list_item);
        menuAdapter.setPeilvVersion(true);
        listView.setAdapter(menuAdapter);
//        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        if (list != null && !list.isEmpty()) {
                            if (position < list.size()) {
                                list.remove(position);
                                menuAdapter.notifyDataSetChanged();
                                //更新下注总金额
                                updateTotalMoney(list);
                            }
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        title.setText(String.format(mContext.getString(R.string.zhudang_detail_format),list.size()));
        qihaoTV.setText(String.format(mContext.getString(R.string.current_qihao_format),qihao));
        updateTotalMoney(list);

    }

    private void updateTotalMoney(List<OrderDataInfo> list) {
        if (list == null) {
            return;
        }
        double totalMoney = 0;
        if (list != null && !list.isEmpty()) {
            for (OrderDataInfo info : list) {
                totalMoney += info.getMoney();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0");
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

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(Utils.dip2px(mContext,90));
            // set a icon
            deleteItem.setIcon(R.drawable.ic_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

}
