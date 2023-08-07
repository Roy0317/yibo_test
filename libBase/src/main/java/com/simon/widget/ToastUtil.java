package com.simon.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simon.R;


/**
 * Created by simon on 17/3/2.
 */

public class ToastUtil {

    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public static Toast showToast(Activity activity, String info) {
        if (activity == null) return null;
        Toast toast = Toast.makeText(activity, info, Toast.LENGTH_SHORT);
        if (!activity.isFinishing()) {
            toast.show();
        }
        return toast;
    }


    /**
     * 短时间显示Toast
     */
    public static Toast showToast(Context cxt, String info) {
        Toast toast = Toast.makeText(cxt, info, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }


    /**
     * 短时间显示Toast
     */
    public static Toast showToast(Context cxt, int resId) {
        if (cxt == null) return null;
        Toast toast = Toast.makeText(cxt, cxt.getString(resId), Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    /**
     * 长时间显示Toast
     */
    public static Toast showToastLong(Context ctx, int resId) {
        if (ctx == null) return null;

        Toast toast = Toast.makeText(ctx, resId, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }


    /**
     * 长时间显示Toast
     *
     * @param info 显示的内容
     */
    public static Toast showToastLong(Context ctx, String info) {
        if (ctx == null) return null;

        Toast toast = Toast.makeText(ctx, info, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }


    /**
     * toast显示在屏幕中间,长时间显示
     *
     * @param ctx
     * @param info
     * @return
     */
    public static Toast showToastCenter(Context ctx, String info, long time) {
        if (ctx == null) return null;

        final Toast toast = Toast.makeText(ctx, info, Toast.LENGTH_LONG);

        View view = View.inflate(ctx, R.layout.layout_toast, null);
        TextView txt_toast_info = view.findViewById(R.id.txt_toast_info);
        txt_toast_info.setText(info);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, time);

        toast.show();
        return toast;
    }

}
