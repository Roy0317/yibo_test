package crazy_wrapper.Crazy.Utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class CustomToast {

    private static final int LONG_DELAY = 3000;
    private static final int SHORT_DELAY = 2000;
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {
        showToast(mContext,text,duration, Gravity.CENTER);
    }

    public static void showToast(Context mContext, String text, int duration,int gravity) {

        mHandler.removeCallbacks(r);
        if (mToast != null) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(mContext, text, duration);
        }
        int yOffset = 0;
        if (gravity == Gravity.BOTTOM){
            yOffset = 50;
        }
        mToast.setGravity(gravity, 0, yOffset);
        mHandler.postDelayed(r, duration == Toast.LENGTH_LONG ? LONG_DELAY : SHORT_DELAY);
        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }
}
