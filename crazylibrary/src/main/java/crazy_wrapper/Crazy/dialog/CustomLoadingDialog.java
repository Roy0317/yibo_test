package crazy_wrapper.Crazy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import com.meiya.cunnar.crazy.crazylibrary.R;
import crazy_wrapper.Crazy.Utils.Utils;

/**
 * 自定义加载框的工厂类
 * @author zhangy
 */
public class CustomLoadingDialog implements CustomDialogManager, LoadingDialog.ProgressListener {

    Context context;
    BaseAnimatorSet bas_in = null;
    BaseAnimatorSet bas_out = null;
    String content;
    boolean showProgress;
    OnBtnClickL onLeftBtnClicks;
    OnBtnClickL onRigtBtnClicks;
    OnBtnClickL onMiddleBtnClicks;
    LoadingDialog dialog = null;
    int btnNum = 2;

    boolean touchOutCancel = false;//zhangy modify 20161102 to disable cancel dialog when
    // touch outside of dialog view default.
    boolean normalCancel = true;

    String leftBtnText;
    String rightBtnText;
    String middleBtnText;

    LoadingDialog.DismissListener listener;


    public CustomLoadingDialog(Context context, boolean showProgress) {
        this.context = context;
        bas_in = new BounceRightEnter();
        bas_out = new SlideLeftExit();
        this.showProgress = showProgress;
    }

    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLeftBtnText(String text) {
        this.leftBtnText = text;
    }

    public void setRightBtnText(String text) {
        rightBtnText = text;
    }

    public void setMiddleBtnText(String text) {
        this.middleBtnText = text;
    }


    /**
     * 是否显示加载进度
     *
     * @param show
     */
    public void setShowProgress(boolean show) {
        this.showProgress = show;
    }

    public void setBtnNums(int btnNum) {
        this.btnNum = btnNum;
    }

    public boolean isShowing() {
        if (dialog == null) {
            return false;
        }
        return dialog.isShowing();
    }

    public void setLeftBtnClickListener(OnBtnClickL leftClickListener) {
        this.onLeftBtnClicks = leftClickListener;
    }

    public void setRightBtnClickListener(OnBtnClickL rightClickListener) {
        this.onRigtBtnClicks = rightClickListener;
    }

    public void setMiddleBtnClickListener(OnBtnClickL middleClickListener) {
        this.onMiddleBtnClicks = middleClickListener;
    }

    public void setOnDismissListener(LoadingDialog.DismissListener listener) {
        this.listener = listener;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        this.touchOutCancel = cancel;
    }

    public void setCancelable(boolean cancel) {
        this.normalCancel = cancel;
    }


    @Override public Dialog createDialog() {
        ProgressBar progressBar = null;
        dialog = new LoadingDialog(context, this.showProgress);
        dialog.setOnDismissListener(listener);
        if (this.showProgress) {
            progressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.custom_progressbar_progress, null);
            dialog.setProgressBar(progressBar);
        } else {
            //progressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.custom_progressbar, null);
            //dialog.setProgressBar(progressBar);
            ProgressWheel progressWheel = (ProgressWheel) LayoutInflater.from(context).inflate(R.layout.wheel_progress_style, null);
            progressWheel.setProgress(1.0f);
            progressWheel.setBarColor(context.getResources().getColor(R.color.crazy_dialog_loading_front_color));
            progressWheel.setRimColor(context.getResources().getColor(R.color.crazy_dialog_loading_content_txt_color));
            dialog.setCustomProgressBar(progressWheel);
        }
        dialog.setTitle(context.getString(R.string.please_wait));
        dialog.content(content);
        dialog.btnNum = btnNum;
        dialog.setCanceledOnTouchOutside(touchOutCancel);
        dialog.setCancelable(normalCancel);

        if (btnNum == 1) {
            if (Utils.isEmptyString(middleBtnText)) {
                middleBtnText = context.getString(R.string.confirm);
            }
            dialog.btnText(middleBtnText);
        } else if (btnNum == 2) {
            if (Utils.isEmptyString(leftBtnText)) {
                leftBtnText = context.getString(R.string.cancel);
            }
            if (Utils.isEmptyString(rightBtnText)) {
                rightBtnText = context.getString(R.string.confirm);
            }
            dialog.btnText(leftBtnText, rightBtnText);
        } else if (btnNum == 3) {
            if (Utils.isEmptyString(leftBtnText)) {
                leftBtnText = context.getString(R.string.cancel);
            }
            if (Utils.isEmptyString(rightBtnText)) {
                rightBtnText = context.getString(R.string.confirm);
            }
            if (Utils.isEmptyString(middleBtnText)) {
                middleBtnText = context.getString(R.string.continue_text);
            }
            dialog.btnText(leftBtnText, middleBtnText, rightBtnText);
        }

        //dialog.showAnim(bas_in)
        //.dismissAnim(bas_out);
        dialog.show();
        if (btnNum == 1) {
            dialog.setOnBtnClickL(onMiddleBtnClicks);//按钮点击事件是数组，按左右中顺序。
        } else if (btnNum == 2) {
            dialog.setOnBtnClickL(onLeftBtnClicks, onRigtBtnClicks);//按钮点击事件是数组，按左右中顺序。
        } else if (btnNum == 3) {
            dialog.setOnBtnClickL(onLeftBtnClicks, onRigtBtnClicks, onMiddleBtnClicks);//按钮点击事件是数组，按左右中顺序。
        }
        return dialog;
    }

    @Override
    public Dialog createDialog(boolean isShow) {
        return null;
    }

    //回调进度，更新UI
    @Override public void onProgress(int progress) {
        if (dialog == null) {
            return;
        }
        dialog.setProgress(progress);
    }

}
