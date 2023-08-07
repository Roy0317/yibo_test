package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.AdjustData;
import com.yibo.yiboapp.utils.Utils;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 2017/9/19.
 * 彩种列表弹出窗
 */

public class CaizhongWindow extends PopupWindow {

    private Context mContext;
    GridView content;
    CaizhongSelectListener caizhongSelectListener;

    public CaizhongWindow(final Context mContext) {

        this.mContext = mContext;
        content = (GridView) LayoutInflater.from(mContext).inflate(
                R.layout.selector_caizhong_layout, null);
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
        this.setContentView(content);
        DisplayMetrics dm = Utils.screenInfo(mContext);
        int height = dm.heightPixels;
        this.setHeight(height/2);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.caizhong_select_window_anim);
    }

    public void setData(String lotCode) {
        if (Utils.isEmptyString(lotCode)) {
            return;
        }
        Type listType = new TypeToken<ArrayList<LotteryData>>() {}.getType();
        List<LotteryData> datas = new Gson().fromJson(
                YiboPreference.instance(mContext).getLotterys(), listType);
        if (datas == null || datas.isEmpty()) {
            return;
        }
        int currentLotPos = -1;
        List<LotteryData> reals = new ArrayList<>();
        for (int i=0;i<datas.size();i++) {
            LotteryData data = datas.get(i);
            if (data.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
                reals.add(data);
            }
            if (!Utils.isEmptyString(data.getCode())) {
                if (data != null && data.getCode().equals(lotCode)) {
                    currentLotPos = i;
                }
            }
        }
        GridViewAdapter adapter = new GridViewAdapter(mContext, reals, R.layout.caizhong_select_list_item);
        adapter.setSelectPos(currentLotPos);
        content.setAdapter(adapter);
    }

    private final class GridViewAdapter extends LAdapter<LotteryData> {

        Context context;
        int selectPos = -1;

        public void setSelectPos(int selectPos) {
            this.selectPos = selectPos;
        }

        public GridViewAdapter(Context mContext, List<LotteryData> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final LotteryData item) {
            TextView czTV = holder.getView(R.id.cz);
            czTV.setText(item.getName());
            if (position == selectPos) {
                czTV.setBackgroundResource(R.drawable.caizhong_border_segment_highlight);
            }else{
                czTV.setBackgroundResource(R.drawable.caizhong_border_segment_normal);
            }
            czTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (caizhongSelectListener != null) {
                        caizhongSelectListener.onLotterySelect(item);
                    }
                    dismiss();
                }
            });
        }
    }

    public void showWindow(View attach) {
        if (!(mContext instanceof Activity)){
            throw new IllegalStateException("attach window is not in activity");
        }
        final Activity activity = (Activity) mContext;
        showAsDropDown(attach,0,5);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
    }


    public interface CaizhongSelectListener{
        void onLotterySelect(LotteryData data);
    }

    public void setCaizhongSelectListener(CaizhongSelectListener adjustListener) {
        this.caizhongSelectListener = adjustListener;
    }
}