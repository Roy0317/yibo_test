package com.example.anuo.immodule.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.CommonUtils;

import java.lang.reflect.Method;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :29/09/2019
 * Desc  :com.example.anuo.immodule.view
 */
public class RedPackageSendDialog extends Dialog {
    private final Context context;
    private String content;
    private final int width;
    private final View view;
    private EditText et_hb_amount;
    private EditText et_hb_num;
    private EditText et_hb_content;
    private ImageView iv_hb_close;
    private Button btn_hb_send;
    private LinearLayout ll_red_package_count;
    private View tv_limit;

    public RedPackageSendDialog(@NonNull Context context, boolean cancelable, @Nullable boolean canceledOnTouchOutside) {
        super(context, R.style.Dialog_Common);
        this.context = context;
        double deviceWidth = getScreenWidth(this.context);
        width = (int) (deviceWidth * 0.7);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        view = inflater.inflate(R.layout.red_package_send_dialog, null);
        initView();
    }

    private void initView() {
        ChatSysConfig chatSysConfig = ChatSpUtils.instance(context).getChatSysConfig();
        et_hb_amount = view.findViewById(R.id.et_hb_amount);
        et_hb_num = view.findViewById(R.id.et_hb_num);
        et_hb_content = view.findViewById(R.id.et_hb_content);
        ll_red_package_count = view.findViewById(R.id.ll_red_package_count);
        tv_limit = view.findViewById(R.id.tv_limit);

        if (chatSysConfig.getSwitch_red_bag_remark_show().equals("0")) {
            et_hb_content.setVisibility(View.GONE);
            tv_limit.setVisibility(View.INVISIBLE);
        } else {
            if (!TextUtils.isEmpty(chatSysConfig.getName_red_bag_remark_info()))
                et_hb_content.setHint(chatSysConfig.getName_red_bag_remark_info());
            else
                et_hb_content.setHint("恭喜发财，大吉大利");
        }

        iv_hb_close = view.findViewById(R.id.iv_hb_close);
        btn_hb_send = view.findViewById(R.id.btn_hb_send);
        iv_hb_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.hideSoftInput(context, et_hb_amount);
                CommonUtils.hideSoftInput(context, et_hb_num);
                CommonUtils.hideSoftInput(context, et_hb_content);
                dismiss();
            }
        });
        btn_hb_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSendListener != null) {
                    onSendListener.onSend(et_hb_amount.getText().toString(),
                            et_hb_num.getText().toString(),
                            et_hb_content.getText().toString());
                }
                CommonUtils.hideSoftInput(context, et_hb_amount);
                CommonUtils.hideSoftInput(context, et_hb_num);
                CommonUtils.hideSoftInput(context, et_hb_content);
                dismiss();
            }
        });

        //设置Input的类型两种都要
        et_hb_amount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        //设置字符过滤
        et_hb_amount.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if (length == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams
                .WRAP_CONTENT, 0);
        setContentView(view, params);
    }

    private OnSendListener onSendListener;

    public void setOnSendListener(OnSendListener onSendListener) {
        this.onSendListener = onSendListener;
    }

    public interface OnSendListener {
        void onSend(String amount, String num, String content);
    }

    private int getScreenWidth(Context context) {
        return getScreenSize(context)[0];
    }

    private int getScreenHeight(Context context) {
        return getScreenSize(context)[1];
    }

    @SuppressLint("WrongConstant")
    private int[] getScreenSize(Context context) {
        WindowManager windowManager;
        try {
            windowManager = (WindowManager) context.getSystemService("window");
        } catch (Throwable var6) {
            windowManager = null;
        }

        if (windowManager == null) {
            return new int[]{0, 0};
        } else {
            Display display = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT < 13) {
                DisplayMetrics t1 = new DisplayMetrics();
                display.getMetrics(t1);
                return new int[]{t1.widthPixels, t1.heightPixels};
            } else {
                try {
                    Point t = new Point();
                    Method method = display.getClass().getMethod("getRealSize", new Class[]{Point.class});
                    method.setAccessible(true);
                    method.invoke(display, new Object[]{t});
                    return new int[]{t.x, t.y};
                } catch (Throwable var5) {
                    return new int[]{0, 0};
                }
            }
        }
    }

    public void setLinearLayoutCountGone(boolean flag) {
        ll_red_package_count.setVisibility(flag ? View.GONE : View.VISIBLE);
    }
}
