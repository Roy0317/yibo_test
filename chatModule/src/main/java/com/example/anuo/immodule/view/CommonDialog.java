package com.example.anuo.immodule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
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
 * Date  :12/07/2019
 * Desc  :com.example.anuo.immodule.view
 */
public class CommonDialog extends AlertDialog {
    private static final String TAG = "CommonDialog";
    private View view;
    private Context context;
    private TextView title;
    private ImageView close;
    private TextView msg;
    private TextView confirm;
    private TextView cancel;
    private LinearLayout bottomLl;
    private RelativeLayout topRl;
    private View verticalLine;
    private int width;
    private EditText et_input;

    protected CommonDialog(Context context, boolean cancelable, boolean canceledOnTouchOutside) {
        super(context, R.style.Dialog_Common);
        this.context = context;
        double deviceWidth = getScreenWidth(this.context);
        width = (int) (deviceWidth * 0.7);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        view = inflater.inflate(R.layout.common_dialog, null);
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams
                .WRAP_CONTENT, 0);
        setContentView(view, params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void setDialogTitle(CharSequence title, boolean closeBtn) {
        if (title == null || "".equals(title)) {
            if (this.title != null) {
                this.title.setVisibility(View.GONE);
            }
        } else {
            if (this.title != null) {
                this.title.setText(title);
            }
        }
        if (closeBtn && this.close != null) {
            this.close.setVisibility(View.VISIBLE);
        }
    }

    public void setDialogMessage(CharSequence msg, boolean showContent) {
        if (msg == null || "".equals(msg) || !showContent) {
            if (this.msg != null) {
                this.msg.setVisibility(View.GONE);
            }
        } else {
            if (this.msg != null) {
                this.msg.setText(msg);
            }
        }
    }

    protected void setDialogButton(int whichButton, CharSequence text, final DialogClickListener listener, final boolean inputListener) {

        if (text == null || "".equals(text)) {
            switch (whichButton) {
                case DialogInterface.BUTTON_POSITIVE: {
                    if (this.confirm != null) {
                        this.confirm.setVisibility(View.GONE);
                    }
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE: {
                    if (this.cancel != null) {
                        this.cancel.setVisibility(View.GONE);
                    }
                    break;
                }
                default: {
                    Log.e(TAG, "Button can not be found. whichButton=" + whichButton);
                }
            }
        } else {
            switch (whichButton) {
                case DialogInterface.BUTTON_POSITIVE: {
                    if (this.confirm != null) {
                        this.confirm.setText(text);
                        this.confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listener != null) {
                                    if (inputListener) {
                                        String s = et_input.getText().toString();
                                        listener.onInputListener(v, s);
                                    } else {
                                        listener.onClick(v);
                                    }
                                }
                                if (et_input != null) {
                                    et_input.setText("");
                                    CommonUtils.hideSoftInput(context, et_input);
                                }
                                dismiss();
                            }
                        });
                    }
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE: {
                    if (this.cancel != null) {
                        this.cancel.setText(text);
                        this.cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listener != null) {
                                    if (inputListener) {
                                        String s = et_input.getText().toString();
                                        listener.onInputListener(v, s);
                                    } else {
                                        listener.onClick(v);
                                    }
                                }
                                if (et_input != null) {
                                    et_input.setText("");
                                    CommonUtils.hideSoftInput(context, et_input);
                                }
                                dismiss();
                            }
                        });
                    }
                    break;
                }
                default: {
                    Log.e(TAG, "Button can not be found. whichButton=" + whichButton);
                }
            }
        }
    }

    public void setDialogButton(String confirm, DialogClickListener positiveClickListener, String cancel,
                                DialogClickListener negativeClickListener, boolean inputListener) {
        if ((confirm == null || "".equals(confirm)) && (cancel == null || "".equals(cancel))) {
            if (this.bottomLl != null) {
                this.bottomLl.setVisibility(View.GONE);
            }
        } else if ((confirm != null && !"".equals(confirm)) && (cancel != null && !"".equals(cancel))) {
            setDialogButton(DialogInterface.BUTTON_POSITIVE, confirm, positiveClickListener, inputListener);
            setDialogButton(DialogInterface.BUTTON_NEGATIVE, cancel, negativeClickListener, inputListener);
        } else {
            // Hide vertical line
            this.verticalLine.setVisibility(View.GONE);
            // Hide positive button
            setDialogButton(DialogInterface.BUTTON_POSITIVE, null, null, inputListener);
            if (confirm == null || "".equals(confirm)) {
                setDialogButton(DialogInterface.BUTTON_NEGATIVE, cancel, negativeClickListener, inputListener);
            } else {
                // confirm is not null and cancel is null
                setDialogButton(DialogInterface.BUTTON_NEGATIVE, confirm, positiveClickListener, inputListener);
            }

        }
    }

    private void initView() {
        this.title = (TextView) view.findViewById(R.id.common_dialog_title_tv);
        this.close = (ImageView) view.findViewById(R.id.common_dialog_close_iv);
        this.msg = (TextView) view.findViewById(R.id.common_dialog_message_tv);
        // Set Scrollable
        this.msg.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.confirm = (TextView) view.findViewById(R.id.common_dialog_confirm_tv);
        this.cancel = (TextView) view.findViewById(R.id.common_dialog_cancel_tv);
        this.bottomLl = (LinearLayout) view.findViewById(R.id.common_dialog_bottom_ll);
        this.topRl = (RelativeLayout) view.findViewById(R.id.common_dialog_top_rl);
        this.verticalLine = view.findViewById(R.id.common_dialog_vertical_line);
        this.et_input = view.findViewById(R.id.common_dialog_message_et);
        if (this.close != null) {
            this.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et_input != null)
                        CommonUtils.hideSoftInput(context, et_input);
                    dismiss();
                }
            });
        }
        if (this.et_input != null) {
            this.et_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (TextUtils.isEmpty(s) || et_input.getInputType() == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    || et_input.getInputType() == InputType.TYPE_CLASS_TEXT) {
                        return;
                    }
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                            s = s.toString().subSequence(0,
                                    s.toString().indexOf(".") + 3);
                            et_input.setText(s);
                            et_input.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        et_input.setText(s);
                        et_input.setSelection(2);
                    }

                    if (s.toString().startsWith("0")
                            && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            et_input.setText(s.subSequence(0, 1));
                            et_input.setSelection(1);
                            return;
                        }
                    }
                    // 单注最大投注金额限制为500000
                    if (Float.parseFloat(s.toString()) > 500000) {
                        et_input.setText("500000");
                        et_input.setSelection(6);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    /**
     * Obtain a confirm dialog instance
     *
     * @param context                context
     * @param title                  title of the dialog, pass null or "" if no title is needed
     * @param message                message to show
     * @param confirm                name of confirm button, pass null or "" if no confirm button is needed
     * @param positiveClickListener  click listener of confirm button
     * @param cancel                 name of cancel button, pass null or "" if no confirm button is needed
     * @param negativeClickListener  click listener of cancel button
     * @param cancelable             cancelable when press back
     * @param canceledOnTouchOutside canceled on touch outside
     * @param closeBtn               whether to show close button
     * @return CommonDialog
     */
    public static CommonDialog create(Context context, String title, String message, String confirm, DialogClickListener
            positiveClickListener, String cancel, DialogClickListener negativeClickListener, boolean cancelable,
                                      boolean canceledOnTouchOutside, boolean closeBtn) {
        CommonDialog dialog = new CommonDialog(context, cancelable, canceledOnTouchOutside);
        dialog.setDialogTitle(title, closeBtn);
        dialog.setDialogMessage(message, true);
        dialog.setDialogButton(confirm, positiveClickListener, cancel, negativeClickListener, false);
        dialog.setInput(false, "", -1);
        return dialog;
    }

    public static CommonDialog create(Context context, String title, String message, String confirm, DialogClickListener
            positiveClickListener, String cancel, DialogClickListener negativeClickListener, boolean cancelable,
                                      boolean canceledOnTouchOutside, boolean closeBtn, boolean showContent, boolean showInput, int inputType, boolean inputListener) {
        CommonDialog dialog = new CommonDialog(context, cancelable, canceledOnTouchOutside);
        dialog.setDialogTitle(title, closeBtn);
        dialog.setDialogMessage(message, showContent);
        dialog.setDialogButton(confirm, positiveClickListener, cancel, negativeClickListener, inputListener);
        dialog.setInput(showInput, message, inputType);
        return dialog;
    }

    private void setInput(boolean showInput, String message, int inputType) {
        if (inputType != -1) {
            et_input.setInputType(inputType);
        }
        if (showInput) {
            et_input.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(message)) {
                et_input.setHint(message);
            }
        } else {
            et_input.setVisibility(View.GONE);
        }
    }

    /**
     * Obtain a confirm dialog instance
     *
     * @param context                context
     * @param titleRes               resource id of the dialog's title, pass 0 if no title is needed
     * @param messageRes             resource id of the dialog's message, pass 0 if no title is needed
     * @param confirmRes             resource id of the dialog's confirm button, pass 0 if no title is needed
     * @param positiveClickListener  click listener of confirm button
     * @param cancelRes              resource id of the dialog's cancel button, pass 0 if no title is needed
     * @param negativeClickListener  click listener of cancel button
     * @param cancelable             cancelable when press back
     * @param canceledOnTouchOutside canceled on touch outside
     * @param closeBtn               whether to show close button
     * @return CommonDialog
     */
    public static CommonDialog create(Context context, int titleRes, int messageRes, int confirmRes, DialogClickListener
            positiveClickListener, int cancelRes, DialogClickListener negativeClickListener, boolean cancelable,
                                      boolean canceledOnTouchOutside, boolean closeBtn) {
        return create(context, titleRes, messageRes, confirmRes, positiveClickListener, cancelRes, negativeClickListener,
                cancelable, canceledOnTouchOutside, closeBtn, null);
    }

    public static CommonDialog create(Context context, int titleRes, int messageRes, int confirmRes, DialogClickListener
            positiveClickListener, int cancelRes, DialogClickListener negativeClickListener, boolean cancelable,
                                      boolean canceledOnTouchOutside, boolean closeBtn, OnDismissListener listener) {
        CommonDialog dialog = new CommonDialog(context, cancelable, canceledOnTouchOutside);
        if (listener != null) {
            dialog.setOnDismissListener(listener);
        }
        String title = null;
        try {
            title = titleRes > 0 ? context.getResources().getString(titleRes) : null;
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Resource not found. resId=" + titleRes, e);
        }
        dialog.setDialogTitle(title, closeBtn);
        String msg = null;
        try {
            msg = messageRes > 0 ? context.getResources().getString(messageRes) : null;
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Resource not found. resId=" + messageRes, e);
        }
        dialog.setDialogMessage(msg, true);
        dialog.setInput(false, "", -1);
        String confirm = null;
        String cancel = null;
        try {
            confirm = confirmRes > 0 ? context.getResources().getString(confirmRes) : null;
            cancel = cancelRes > 0 ? context.getResources().getString(cancelRes) : null;
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Resource not found.", e);
        }
        dialog.setDialogButton(confirm, positiveClickListener, cancel, negativeClickListener, false);

        return dialog;
    }

    public void setCancel(int cancelRes) {
        cancel.setText(context.getResources().getString(cancelRes));
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

    public void showAndInput() {
        show();
        //弹出对话框后直接弹出键盘
        et_input.setFocusableInTouchMode(true);
        et_input.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.showSoftInput(context, et_input);
            }
        }, 100);
    }

    public interface DialogClickListener extends View.OnClickListener {
        void onInputListener(View view, String input);
    }
}
