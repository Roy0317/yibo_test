package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.widget.TextView;

import com.yibo.yiboapp.R;


/**
 * Created by liweipeng on 2017/10/20.
 */

public class MyToast {

    public static final int LENGTH_SHORT = 1500;
    public static final int LENGTH_LONG = 3000;

    private Context mContext;
    private Handler mHandler;
    private TextView mTextView;
    private int mDuration;
    private Dialog dialog;

    public MyToast(Context context) {
        try {
            mContext = context;
            mHandler = new Handler();
            dialog = new Dialog(mContext, R.style.XToastDialogStyle);
            dialog.setContentView(R.layout.toast_image_layout);
            mTextView = (TextView) dialog.findViewById(R.id.mbMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyToast makeText(Context context, CharSequence message,
                                  int duration) {
        MyToast toastUtils = new MyToast(context);
        try {
            toastUtils.mDuration = duration;
            toastUtils.mTextView.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toastUtils;
    }

    public static MyToast makeText(Context context, int resId, int duration) {
        String mes = "";
        try {
            mes = context.getResources().getString(resId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return makeText(context, mes, duration);
    }


    public void show() {
        try {
            Activity activity = (Activity) mContext;
            if (activity!=null && !activity.isFinishing()){
                dialog.show();
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (dialog.isShowing() &&activity!=null&& !activity.isFinishing()){
                        dialog.dismiss();
                    }
                }
            }, mDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}