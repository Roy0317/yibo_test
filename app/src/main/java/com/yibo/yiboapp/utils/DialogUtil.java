package com.yibo.yiboapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;

import com.simon.view.dialog.DefaultDialog;
import com.simon.view.dialog.DialogSelectListener;
import com.yibo.yiboapp.interfaces.OnPositiveButtonClickListener;


public class DialogUtil {

    /**
     * @param ctx
     * @param title
     * @param context
     * @param cancleText
     * @param okText
     * @param paramListener
     * @return
     */
    public static DefaultDialog getDefaultDialog(Context ctx, String title, String context, String cancleText, String okText, DialogSelectListener paramListener) {
        return getDefaultDialog(ctx, title, context, cancleText, okText, false, paramListener);
    }


    /**
     * @param ctx
     * @param title
     * @param context
     * @param cancleText
     * @param okText
     * @param cancel        点击屏幕是否取消对话框
     * @param paramListener
     * @return
     */
    public static DefaultDialog getDefaultDialog(Context ctx, String title, String context, String cancleText, String okText, boolean cancel, DialogSelectListener paramListener) {

        DefaultDialog dialog = new DefaultDialog(ctx);

        if (!TextUtils.isEmpty(title))
            dialog.setDialogTitle(title);

        dialog.setDescription(context);
        if (TextUtils.isEmpty(cancleText))
            dialog.HideCancleBtn();

        dialog.setCancelOnTouchOutside(cancel);
        dialog.setBtnCancle(cancleText);
        dialog.setBtnOk(okText);
        dialog.setDialogListener(paramListener);

        if (ctx instanceof Activity) {
            if (((Activity) ctx).isFinishing())
                return dialog;
        }
        dialog.show();
        return dialog;
    }


    public static AlertDialog.Builder alert(Context context, String message, OnPositiveButtonClickListener onPositiveButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (onPositiveButtonClickListener != null) {
                        onPositiveButtonClickListener.onPositiveClick(dialog, which);
                    }
                }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).create().show();
        return builder;
    }


}
