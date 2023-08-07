package crazy_wrapper.Crazy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.meiya.cunnar.crazy.crazylibrary.R;

import crazy_wrapper.Crazy.Utils.CustomToast;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.dialog.*;
import crazy_wrapper.Crazy.request.CrazyRequest;

/**
 * Created by zhangy on 2016/12/29.
 * 定义crazy请求或组合请求使用的前台框
 */
public class CrazyDialog {

    public static final String TAG = CrazyDialog.class.getSimpleName();
    String content;
    //LoadingDialog.DismissListener listener;

    /**
     * the proxy of crazy dialog who present inner dialog create dismiss perform .
     * created zhangy on 下午2:17 17/1/15
     **/
    public interface Proxy {
        Dialog create() throws CrazyException;

        void dismiss();

        void update(String content);
    }

    /**
     * crazy 框需要显示的标题,内容,按钮监听事件等
     * created zhangy on 下午6:12 17/1/15
     **/
    public final static class Entry {

        public String title;
        public String content;
        public OnBtnClickL[] clickLs;
        public int btnNums;
        public String[] btnTexts;
        public int loadMethod;
        public BaseAnimatorSet animIn;
        public BaseAnimatorSet animOut;
        public int bgColor = Color.parseColor("#ffffffff");
        public float heightScale;
        public float widthScale;
        public boolean toastShow;
        public String toastText;
        public OnBtnClickL toastL;
        public boolean keyCancel = true;
        public boolean outsideCancel;
        public boolean contentScroll;

    }


    /**
     * default crazy dialog proxy
     */
    public final static class DefaultDialogProxy implements Proxy {

        Entry entry;
        Context context;
        Dialog onlyDialog;

        public DefaultDialogProxy(Context context, Entry entry) {
            this.entry = entry;
            this.context = context;
        }

        @Override
        public Dialog create() throws CrazyException {
            if (entry == null) {
                throw new CrazyException("crazy dialog exception! we need to specific dialog entry first !");
            }
            int loadMethod = entry.loadMethod;
            if (loadMethod == CrazyRequest.LOAD_METHOD.NONE.ordinal()) {
                return null;
            }
            if (entry.btnNums == 0) {
                entry.btnNums = 2;
            }
            if (entry.btnTexts == null) {
                entry.btnTexts = new String[]{context.getString(R.string.cancel), context.getString(R.string.confirm)};
            }
            if (entry.clickLs == null || entry.clickLs.length == 0) {
                entry.clickLs = new OnBtnClickL[1];
            }
            float wScale = entry.widthScale == 0 ? 0.8f : entry.widthScale;
            if (loadMethod == CrazyRequest.LOAD_METHOD.DIALOG.ordinal()) {
                ConfirmDialogBuilder dialogBuilder = new ConfirmDialogBuilder(context)
                        .btnNum(entry.btnNums)
                        .btnTexts(entry.btnTexts)
                        .setContentScroll(entry.contentScroll)
                        .onBtnClicks(entry.clickLs);
                dialogBuilder.setBgColor(entry.bgColor);
                dialogBuilder.setKeyCancel(entry.keyCancel);
                dialogBuilder.setTouchOutCancel(entry.outsideCancel);
                dialogBuilder.setAnimIn(entry.animIn);
                dialogBuilder.setAnimOut(entry.animOut);
                dialogBuilder.setHeightScale(entry.heightScale);
                dialogBuilder.setWidthScale(wScale);
                dialogBuilder.setToastShow(entry.toastShow);
                dialogBuilder.setToastStr(entry.toastText);
                dialogBuilder.setOnToastBtnClick(entry.toastL);
                onlyDialog = dialogBuilder.createDialog();
            } else if (loadMethod == CrazyRequest.LOAD_METHOD.LOADING.ordinal()) {
                Builder dialogBuilder = new LoadingDialogBuilder(context);
                dialogBuilder.setContent(entry.content);
                dialogBuilder.setBgColor(entry.bgColor);
                dialogBuilder.setKeyCancel(entry.keyCancel);
                dialogBuilder.setTouchOutCancel(entry.outsideCancel);
                dialogBuilder.setAnimIn(entry.animIn);
                dialogBuilder.setAnimOut(entry.animOut);
                dialogBuilder.setHeightScale(entry.heightScale);
                dialogBuilder.setWidthScale(wScale);
                onlyDialog = dialogBuilder.createDialog();
            } else if (loadMethod == CrazyRequest.LOAD_METHOD.TOAST.ordinal()) {
                showToast(context, entry.content);
                return null;
            } else if (loadMethod == CrazyRequest.LOAD_METHOD.PROGRESS.ordinal()) {
                int btnNums = entry.btnNums;
                btnNums = btnNums == 0 ? 1 : btnNums;
                ProgressDialogBuilder dialogBuilder = new ProgressDialogBuilder(context)
                        .btnNum(btnNums)
                        .btnTexts(entry.btnTexts)
                        .onBtnClicks(entry.clickLs);
                dialogBuilder.setBgColor(entry.bgColor);
                dialogBuilder.setTitle(entry.title);
                dialogBuilder.setContent(entry.content);
                dialogBuilder.setKeyCancel(entry.keyCancel);
                dialogBuilder.setTouchOutCancel(entry.outsideCancel);
                dialogBuilder.setAnimIn(entry.animIn);
                dialogBuilder.setAnimOut(entry.animOut);
                dialogBuilder.setHeightScale(entry.heightScale);
                dialogBuilder.setWidthScale(wScale);
                dialogBuilder.setToastShow(entry.toastShow);
                dialogBuilder.setToastStr(entry.toastText);
                dialogBuilder.setOnToastBtnClick(entry.toastL);
                onlyDialog = dialogBuilder.createDialog();
            }
            return onlyDialog;
        }

        @Override
        public void dismiss() {
            if (onlyDialog != null && onlyDialog.isShowing()) {
                onlyDialog.dismiss();
                onlyDialog = null;
            }
        }

        @Override
        public void update(String content) {
            if (onlyDialog == null || !onlyDialog.isShowing()) {
                return;
            }
            if (onlyDialog instanceof LoadingDialog) {
                LoadingDialog loading = (LoadingDialog) onlyDialog;
                if (loading.isShowProgress()) {
                    if (Utils.isNumeric(content)) {
                        loading.setProgress(Integer.parseInt(content));
                    } else {
                        loading.updateDialogContent(content);
                    }
                } else {
                    loading.updateDialogContent(content);
                }
            }
        }
    }

    private static void showToast(Context context, String text) {
        CustomToast.showToast(context, text, Toast.LENGTH_LONG);
    }

    /**
     * crazy dialog base builder
     */
    public abstract static class Builder implements CustomDialogManager {

        public Builder(Context context) {
            this.context = context;
        }

        Context context;
        boolean touchOutCancel = false;
        boolean keyCancel = true;
        BaseAnimatorSet animIn = null;
        BaseAnimatorSet animOut = null;
        String title;
        String content;
        boolean toastShow;//是否显示toast提示按钮
        OnBtnClickL onToastBtnClick;//确认框中的toast提醒事件
        String toastStr;//toast文字
        float widthScale = 0.8f;//对话框宽度比例
        float heightScale;//对话框高度比例
        int bgColor;//对话框背景色

        public void setBgColor(int bgColor) {
            this.bgColor = bgColor;
        }

        public void setTouchOutCancel(boolean touchOutCancel) {
            this.touchOutCancel = touchOutCancel;
        }

        public void setKeyCancel(boolean keyCancel) {
            this.keyCancel = keyCancel;
        }

        public void setAnimIn(BaseAnimatorSet animIn) {
            this.animIn = animIn;
        }

        public void setAnimOut(BaseAnimatorSet animOut) {
            this.animOut = animOut;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setToastShow(boolean toastShow) {
            this.toastShow = toastShow;
        }

        public void setOnToastBtnClick(OnBtnClickL onToastBtnClick) {
            this.onToastBtnClick = onToastBtnClick;
        }

        public void setToastStr(String toastStr) {
            this.toastStr = toastStr;
        }

        public void setWidthScale(float widthScale) {
            this.widthScale = widthScale;
        }

        public void setHeightScale(float heightScale) {
            this.heightScale = heightScale;
        }

        @Override
        public Dialog createDialog() {
            return null;
        }
    }

    /**
     * custom confirm dialog builder
     */
    public static class ConfirmDialogBuilder extends Builder {

        OnBtnClickL[] onBtnClicks;
        String[] btnTexts;
        int btnNum = 2;
        boolean contentScroll;//内容是否可滚动查看

        public ConfirmDialogBuilder(Context context) {
            super(context);
        }

        public ConfirmDialogBuilder setContentScroll(boolean contentScroll) {
            this.contentScroll = contentScroll;
            return this;
        }

        public ConfirmDialogBuilder btnNum(int btnNum) {
            this.btnNum = btnNum;
            return this;
        }

        public ConfirmDialogBuilder onBtnClicks(OnBtnClickL[] onBtnClicks) {
            this.onBtnClicks = onBtnClicks;
            return this;
        }

        public ConfirmDialogBuilder btnTexts(String[] btnTexts) {
            this.btnTexts = btnTexts;
            return this;
        }

        @Override
        public Dialog createDialog() {

            MaterialDialog dialog = new MaterialDialog(context);
            dialog.title(title).content(content).btnNum(btnNum);
            dialog.setCanceledOnTouchOutside(touchOutCancel);
            dialog.setCancelable(keyCancel);
            dialog.isToastShow(toastShow);
            dialog.bgColor(bgColor);
            if (btnNum == 1 && btnTexts.length == 1) {
                if (Utils.isEmptyString(btnTexts[0])) {
                    btnTexts[0] = context.getString(R.string.confirm);
                }
            } else if (btnNum == 2 && btnTexts.length == 2) {
                if (Utils.isEmptyString(btnTexts[0])) {
                    btnTexts[0] = context.getString(R.string.cancel);
                }
                if (Utils.isEmptyString(btnTexts[1])) {
                    btnTexts[1] = context.getString(R.string.confirm);
                }
            } else if (btnNum == 3 && btnTexts.length == 3) {
                if (Utils.isEmptyString(btnTexts[0])) {
                    btnTexts[0] = context.getString(R.string.cancel);
                }
                if (Utils.isEmptyString(btnTexts[1])) {
                    btnTexts[1] = context.getString(R.string.continue_text);
                }
                if (Utils.isEmptyString(btnTexts[2])) {
                    btnTexts[2] = context.getString(R.string.confirm);
                }
            }
            dialog.widthScale(widthScale);
            dialog.heightScale(heightScale);
            dialog.toast(toastStr);
            dialog.btnText(btnTexts).showAnim(animIn).dismissAnim(animOut);
            dialog.setOnBtnClickL(onBtnClicks);//按钮点击事件是数组，按左右中顺序。
            dialog.setToastClickL(onToastBtnClick);
            dialog.show();
            return dialog;
        }

        @Override
        public Dialog createDialog(boolean isShow) {
            return null;
        }
    }

    /**
     * crazy loading dialog builder
     */
    public static class LoadingDialogBuilder extends Builder {

        LoadingDialog.DismissListener listener;

        public LoadingDialogBuilder(Context context) {
            super(context);
        }

        public LoadingDialogBuilder setListener(LoadingDialog.DismissListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public Dialog createDialog() {
            LoadingDialog dialog = new LoadingDialog(context, false);
            dialog.setOnDismissListener(listener);

            ProgressWheel progressWheel = (ProgressWheel) LayoutInflater.from(context).inflate(R.layout.wheel_progress_style, null);
            progressWheel.setProgress(1.0f);
            //progressWheel.setBarColor(Color.WHITE);
            progressWheel.setRimColor(context.getResources().getColor(R.color.crazy_progress_wheel_color));
            dialog.setCustomProgressBar(progressWheel);

            dialog.setTitle(context.getString(R.string.please_wait));
            dialog.content(content);
            dialog.bgColor(bgColor);
            dialog.setCanceledOnTouchOutside(touchOutCancel);
            dialog.setCancelable(keyCancel);
            dialog.showAnim(animIn);
            dialog.dismissAnim(animOut);
            dialog.widthScale(widthScale);
            dialog.heightScale(heightScale);
            try {
                if (!((Activity) context).isFinishing())
                    dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dialog;
        }

        @Override
        public Dialog createDialog(boolean isShow) {
            return null;
        }
    }

    public static class ProgressDialogBuilder extends Builder {

        OnBtnClickL[] onBtnClicks;
        String[] btnTexts;
        int btnNum = 2;


        public ProgressDialogBuilder(Context context) {
            super(context);
        }

        public ProgressDialogBuilder onBtnClicks(OnBtnClickL[] onBtnClicks) {
            this.onBtnClicks = onBtnClicks;
            return this;
        }

        public ProgressDialogBuilder btnTexts(String[] btnTexts) {
            this.btnTexts = btnTexts;
            return this;
        }

        public ProgressDialogBuilder btnNum(int btnNum) {
            this.btnNum = btnNum;
            return this;
        }

        @Override
        public Dialog createDialog() {
            LoadingDialog dialog = new LoadingDialog(context, true);
            //dialog.setOnDismissListener(listener);
            ProgressBar progressBar = (ProgressBar) LayoutInflater.from(context).inflate(
                    R.layout.custom_progressbar_progress, null);
            dialog.setProgressBar(progressBar);
            dialog.setTitle(title);
            dialog.content(content);
            dialog.btnNum(btnNum);
            dialog.setOnBtnClickL(onBtnClicks);
            dialog.btnText(btnTexts);
            dialog.bgColor(bgColor);
            dialog.setCanceledOnTouchOutside(touchOutCancel);
            dialog.setCancelable(keyCancel);
            dialog.showAnim(animIn);
            dialog.dismissAnim(animOut);
            dialog.widthScale(widthScale);
            dialog.heightScale(heightScale);
            dialog.show();
            return dialog;
        }

        @Override
        public Dialog createDialog(boolean isShow) {
            return null;
        }
    }

}
