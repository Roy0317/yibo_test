package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;

/**
 * Created by johnson on 2017/9/21.
 */

public class FeeConvertWindow extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    TextView title;
    TextView convertPath;
    XEditText convertMoney;
    Button cancelBtn;
    Button convertBtn;
    ConvertListener convertListener;

    String gameCode;
    boolean fromSys;


    public FeeConvertWindow(final Context mContext) {

        this.mContext = mContext;
        View content = LayoutInflater.from(mContext).inflate(R.layout.fee_convert_window, null);
        title = (TextView) content.findViewById(R.id.title);
        convertPath = (TextView) content.findViewById(R.id.convert_path);
        convertMoney = (XEditText) content.findViewById(R.id.input_money);
        cancelBtn = (Button) content.findViewById(R.id.cancel);
        convertBtn = (Button) content.findViewById(R.id.convert);

        cancelBtn.setOnClickListener(this);
        convertBtn.setOnClickListener(this);


        content.isFocusableInTouchMode();
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
        setOutsideTouchable(true);
        this.setContentView(content);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.adjust_window_anim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.convert:
                String money = convertMoney.getText().toString().trim();
                if (convertListener != null) {
                    convertListener.onConvert(money, gameCode, fromSys);
                }
                dismiss();
                break;
        }
    }

    public interface ConvertListener {
        void onConvert(String money, String gameCode, boolean fromSys);
    }

    public void setConvertListener(ConvertListener convertListener) {
        this.convertListener = convertListener;
    }

    public void showWindow(View attach, String gameCode, String gameName, boolean convertFromSysToPlatform) {
        if (!(mContext instanceof Activity)) {
            throw new IllegalStateException("attach window is not in activity");
        }
        title.setText(gameName + "额度转换");
        if (convertFromSysToPlatform) {
            convertPath.setText("从 系统 转到 " + gameName);
        } else {
            convertPath.setText("从 " + gameName + " 转到 系统");
        }
        this.gameCode = gameCode;
        this.fromSys = convertFromSysToPlatform;

        final Activity activity = (Activity) mContext;
        showAtLocation(attach, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.7f;
        activity.getWindow().setAttributes(params);
    }

}
