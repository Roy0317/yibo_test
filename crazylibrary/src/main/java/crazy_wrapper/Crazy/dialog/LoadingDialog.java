package crazy_wrapper.Crazy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.meiya.cunnar.crazy.crazylibrary.R;

/**
 * Dialog like Material Design Dialog,loading style
 */
public class LoadingDialog extends BaseAlertDialog<LoadingDialog> {


    Context context;
    boolean showProgress;
    int progress = 0;//进度,100为MAX

    public interface DismissListener {
        void onDismiss();
    }

    public interface ProgressListener {
        void onProgress(int progress);
    }

    public LoadingDialog(Context context, boolean showProgress) {
        super(context);

        this.context = context;
        this.showProgress = showProgress;
        /** default value*/
        titleTextColor = context.getResources().getColor(R.color.crazy_dialog_title_txt_color);
        titleTextSize_SP = context.getResources().getDimension(R.dimen.crazy_dialog_title_size);
        contentLodingTextColor = context.getResources().getColor(R.color.crazy_dialog_loading_content_txt_color);
        contentTextColor = context.getResources().getColor(R.color.crazy_dialog_content_txt_color);
        contentTextSize_SP = context.getResources().getDimension(R.dimen.crazy_dialog_content_size);
        leftBtnTextColor = context.getResources().getColor(R.color.crazy_colorPrimary);
        rightBtnTextColor = context.getResources().getColor(R.color.crazy_colorPrimary);
        middleBtnTextColor = context.getResources().getColor(R.color.crazy_dialog_middle_btn_color);
        /** default value*/
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    @Override public void dismiss() {
        super.dismiss();
        if (progressWheel != null) {
            if (progressWheel.isSpinning()) {
                progressWheel.stopSpinning();
            }
        }
        if (mListener != null) {
            mListener.onDismiss();
        }
    }

    DismissListener mListener;

    public void setOnDismissListener(DismissListener listener) {
        this.mListener = listener;
    }


    @Override public View onCreateView() {

        /** title */
        tv_title.setGravity(Gravity.CENTER_VERTICAL);
        tv_title.getPaint().setFakeBoldText(true);
        tv_title.setPadding(dp2px(20), dp2px(10), dp2px(20), dp2px(10));
        tv_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_title.setBackgroundColor(context.getResources().getColor(R.color.crazy_colorPrimary));

        LinearLayout loadingLayout = new LinearLayout(context);
        if (this.showProgress) {

            ll_container.addView(tv_title);//有进度条的加载框状态下，加入标题栏
            loadingLayout.setOrientation(LinearLayout.VERTICAL);

            /** content */
            tv_content.setPadding(dp2px(20), dp2px(20), dp2px(20), dp2px(20));
            tv_content.setTextSize(16);
            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            tv_content.setLayoutParams(params);
            loadingLayout.addView(tv_content);

            progressText.setText(progress + "/100");
            progressText.setPadding(dp2px(20), dp2px(10), dp2px(20), dp2px(0));
            loadingLayout.addView(progressText);
            progressBar.setPadding(dp2px(20), dp2px(10), dp2px(20), dp2px(10));
            loadingLayout.addView(progressBar);
            ll_container.addView(loadingLayout);

            if (btnNum == 1) {
                tv_btn_left.setVisibility(View.GONE);
                tv_btn_right.setVisibility(View.GONE);
            } else if (btnNum == 2) {
                tv_btn_middle.setVisibility(View.GONE);
            } else if (btnNum == 0) {
                tv_btn_left.setVisibility(View.GONE);
                tv_btn_right.setVisibility(View.GONE);
                tv_btn_middle.setVisibility(View.GONE);
            }

            /**btns*/
            ll_btns.setGravity(Gravity.RIGHT);
            ll_btns.addView(tv_btn_left);
            ll_btns.addView(tv_btn_middle);
            ll_btns.addView(tv_btn_right);
            tv_btn_left.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
            tv_btn_right.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
            tv_btn_middle.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
            ll_btns.setPadding(dp2px(20), dp2px(0), dp2px(10), dp2px(10));
            ll_container.addView(ll_btns);

        } else {

            widthScale(0.5f);
            tv_title.setVisibility(View.GONE);
            loadingLayout.setOrientation(LinearLayout.VERTICAL);
            progressWheel.setPadding(dp2px(0), dp2px(20), dp2px(0), dp2px(15));
            LinearLayout.LayoutParams proParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            proParams.gravity = Gravity.CENTER;
            loadingLayout.addView(progressWheel,proParams);

            /** content */
            tv_content.setPadding(dp2px(0), dp2px(0), dp2px(0), dp2px(20));
            LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            tv_content.setLayoutParams(params);
            loadingLayout.addView(tv_content);
            ll_container.addView(loadingLayout);
        }
        return ll_container;
    }

    @Override
    public LoadingDialog contentTextSize(float contentTextSize_SP) {
        return super.contentTextSize(contentTextSize_SP);
    }

    @Override public void setTitle(CharSequence customTitle) {
        super.setTitle(title);
        title = (String) customTitle;
    }


    public void setProgressBar(ProgressBar bar) {
        progressBar = bar;
    }

    public void setProgressListener(ProgressListener listener){
        pListener = listener;
    }

    public void setCustomProgressBar(ProgressWheel bar) {
        progressWheel = bar;
        progressWheel.spin();
    }

    public void setProgress(int progress) {
        if (!this.showProgress) {
            return;
        }
        this.progress = progress;
        updateProgress(progress);
    }

    private void updateProgress(int progress) {
        progressText.setText(progress + "/100");
        progressBar.setProgress(progress);
    }

    public void updateDialogContent(String content) {
        updateContent(content);
    }

    @Override public void setUiBeforShow() {
        super.setUiBeforShow();
        /**set background color and corner radius */
        float radius = dp2px(cornerRadius_DP);
        if (!showProgress) {
            tv_content.setTextColor(contentLodingTextColor);
        }
        ll_container.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#6e000000"), radius));
        tv_btn_left.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
        tv_btn_right.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
        tv_btn_middle.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
    }

    @Override
    public LoadingDialog bgColor(int bgColor) {
        return super.bgColor(bgColor);
    }
}
