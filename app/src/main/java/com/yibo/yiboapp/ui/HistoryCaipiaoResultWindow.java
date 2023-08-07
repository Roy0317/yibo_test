package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.HistoryResult;
import com.yibo.yiboapp.utils.Utils;
import java.util.List;

/**
 * Created by johnson on 2017/9/21.
 */

public class HistoryCaipiaoResultWindow extends PopupWindow{

    private Context mContext;
    SwipeMenuListView listView;
    View content;
    HistoryResultAdapter menuAdapter;
    int height;
    int width;

    public HistoryCaipiaoResultWindow(final Context mContext) {

        this.mContext = mContext;
        content = LayoutInflater.from(mContext).inflate(R.layout.history_result_pop, null);
        listView = (SwipeMenuListView) content.findViewById(R.id.cListview);

        DisplayMetrics dm = Utils.screenInfo(mContext);
        height = dm.heightPixels;
        width = dm.widthPixels;

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
        this.setHeight(height/3);
        this.setWidth((width *3)/4);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
//        this.setAnimationStyle(R.style.adjust_window_anim);
    }

    public void setData(List<HistoryResult> results) {

        menuAdapter = new HistoryResultAdapter(mContext, results, R.layout.history_result_pop_item);
        listView.setAdapter(menuAdapter);
    }

    public void showWindow(View attach) {
        if (!(mContext instanceof Activity)){
            throw new IllegalStateException("attach window is not in activity");
        }
        final Activity activity = (Activity) mContext;
//        showAtLocation(attach, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
        showAsDropDown(attach,0,5);
    }


    public final class HistoryResultAdapter extends LAdapter<HistoryResult> {

        Context context;
        public HistoryResultAdapter(Context mContext, List<HistoryResult> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, HistoryResult item) {

            TextView qihao = holder.getView(R.id.qihao);
            TextView numbers = holder.getView(R.id.numbers);
            qihao.setText(String.format(context.getString(R.string.qihao_format_history),
                    item.getQihao()));
            numbers.setText(item.getResultNumbers());
        }
    }

}
