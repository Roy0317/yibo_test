package crazy_wrapper.Crazy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Dialog like Material Design Dialog,loading style
 */
public class BaseLoginDialog extends BaseAlertDialog<LoadingDialog> {

    Context context;
    View loginView;

    public interface DismissListener{
        void onDismiss();
    }

    public BaseLoginDialog(Context context) {
        super(context);
        this.context = context;
        /** default value*/
        titleTextColor = Color.parseColor("#DE000000");
        titleTextSize_SP = 20f;
        contentTextColor = Color.parseColor("#8a000000");
        contentTextSize_SP = 18f;
        leftBtnTextColor = Color.parseColor("#383838");
        rightBtnTextColor = Color.parseColor("#468ED0");
        middleBtnTextColor = Color.parseColor("#00796B");
        /** default value*/
    }

    DismissListener mListener;
    public void setOnDismissListener(DismissListener listener){
        this.mListener = listener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mListener != null){
            mListener.onDismiss();
        }
    }

    @Override
    public View onCreateView() {

        /** title */
        tv_title.setGravity(Gravity.CENTER_VERTICAL);
        tv_title.getPaint().setFakeBoldText(true);
        tv_title.setPadding(dp2px(20), dp2px(10), dp2px(20), dp2px(10));
        tv_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ll_container.addView(tv_title);

        if (this.loginView != null){
            ll_container.addView(this.loginView);
        }

        /**btns*/
        ll_btns.setGravity(Gravity.RIGHT);
        TextView spaceText = new TextView(context);
        spaceText.setTextSize(titleTextSize_SP);
        spaceText.setText("重新登录");
        spaceText.setVisibility(View.INVISIBLE);
        spaceText.setGravity(Gravity.CENTER_VERTICAL);
        spaceText.getPaint().setFakeBoldText(true);
        spaceText.setPadding(dp2px(20), dp2px(10), dp2px(20), dp2px(10));
        spaceText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ll_btns.addView(spaceText);
        //ll_btns.setPadding(dp2px(20), dp2px(20), dp2px(10), dp2px(20));
        ll_container.addView(ll_btns);
        return ll_container;
    }

    @Override
    public void setTitle(CharSequence customTitle) {
        super.setTitle(title);
        title = (String) customTitle;
    }

    public void setLoginView(View view){
        this.loginView = view;
    }

    @Override
    public void setUiBeforShow() {
        super.setUiBeforShow();
        /**set background color and corner radius */
        float radius = dp2px(cornerRadius_DP);
        ll_container.setBackgroundDrawable(CornerUtils.cornerDrawable(bgColor, radius));
        tv_btn_left.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
        tv_btn_right.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
        tv_btn_middle.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
    }


}
