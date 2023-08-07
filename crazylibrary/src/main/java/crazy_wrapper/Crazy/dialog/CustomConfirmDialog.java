package crazy_wrapper.Crazy.dialog;

import android.app.Dialog;
import android.content.Context;
import com.meiya.cunnar.crazy.crazylibrary.R;
import crazy_wrapper.Crazy.Utils.Utils;

/**
 * 自定义确认框和提示框的工厂类
 * @author zhangy
 *
 */
public class CustomConfirmDialog  implements CustomDialogManager{

    Context context;
    BaseAnimatorSet  bas_in =null;
    BaseAnimatorSet  bas_out = null;
    String title;
    String content;
    OnBtnClickL onLeftBtnClicks;
    OnBtnClickL onRigtBtnClicks;
    OnBtnClickL onMiddleBtnClicks;
    OnBtnClickL onToastBtnClick;

    WebviewJumpListener webviewJumpListener;
    MaterialDialog dialog = null;
    int btnNum = 2;
    String leftBtnText;
    String rightBtnText;
    String middleBtnText;
    String toastBtnText;

    boolean touchOutCancel =true;
    boolean normalCancel =true;
    boolean toastShow;
    boolean htmlContent;//显示内容是否是html文本
    String baseUrl = "";

    public CustomConfirmDialog(Context context){
        this.context = context;
        bas_in = new BounceRightEnter();
        bas_out = new SlideLeftExit();
    }
    public  MaterialDialog getDialog() {
        return dialog;
    }
    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setBtnNums(int btnNum){
        this.btnNum = btnNum;
    }

    public void setLeftBtnClickListener(OnBtnClickL leftClickListener){
        this.onLeftBtnClicks = leftClickListener;
    }
    public void setRightBtnClickListener(OnBtnClickL rightClickListener){
        this.onRigtBtnClicks = rightClickListener;
    }
    public void setMiddleBtnClickListener(OnBtnClickL middleClickListener){
        this.onMiddleBtnClicks = middleClickListener;
    }

    public void setOnToastBtnClick(OnBtnClickL toastBtnClick){
        this.onToastBtnClick = toastBtnClick;
    }

    public void setWebviewJumpListener(WebviewJumpListener webviewJumpListener) {
        this.webviewJumpListener = webviewJumpListener;
    }

    public void dismiss(){
        if(dialog == null){
            return;
        }
        dialog.dismiss();
    }

    public void setLeftBtnText(String text){
        this.leftBtnText = text;
    }
    public void setRightBtnText(String text){
        rightBtnText = text;
    }

    public void setMiddleBtnText(String text){
        this.middleBtnText = text;
    }

    public void setToastBtnText(String text){
        this.toastBtnText = text;
    }


    public boolean isTouchOutCancel() {
        return touchOutCancel;
    }

    public void setTouchOutCancel(boolean touchOutCancel) {
        this.touchOutCancel = touchOutCancel;
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        this.touchOutCancel = cancel;
    }

    public void setCancelable(boolean cancel){
        this.normalCancel = cancel;
    }

    public void setToastShow(boolean show){
        toastShow = show;
    }

    public void setHtmlContent(boolean htmlContent) {
        this.htmlContent = htmlContent;
    }

    public void setBaseUrl(String url) {
        this.baseUrl = url;
    }

    @Override
    public Dialog createDialog() {
        dialog = new MaterialDialog(context);
        dialog.title(title).content(content)
                .btnNum(btnNum);
        dialog.setCanceledOnTouchOutside(touchOutCancel);
        dialog.setCancelable(normalCancel);
        dialog.isToastShow(toastShow);
        dialog.baseUrl(baseUrl);
        dialog.isHtmlContent(htmlContent);

        if(btnNum == 1){
            if(Utils.isEmptyString(middleBtnText)){
                middleBtnText = context.getString(R.string.confirm);
            }
            dialog.btnText(middleBtnText);
        }else if(btnNum == 2){
            if(Utils.isEmptyString(leftBtnText)){
                leftBtnText = context.getString(R.string.cancel);
            }
            if(Utils.isEmptyString(rightBtnText)){
                rightBtnText = context.getString(R.string.confirm);
            }
            dialog.btnText(leftBtnText, rightBtnText);
        }else if(btnNum == 3){
            if(Utils.isEmptyString(leftBtnText)){
                leftBtnText = context.getString(R.string.cancel);
            }
            if(Utils.isEmptyString(rightBtnText)){
                rightBtnText = context.getString(R.string.confirm);
            }
            if(Utils.isEmptyString(middleBtnText)){
                middleBtnText = context.getString(R.string.continue_text);
            }
            dialog.btnText(leftBtnText,rightBtnText,middleBtnText);
        }
//	    dialog.showAnim(bas_in)
//	            .dismissAnim(bas_out);
        dialog.show();
        if(btnNum == 1){
            dialog.setOnBtnClickL(onMiddleBtnClicks);//按钮点击事件是数组，按左右中顺序。
        }else if(btnNum == 2){
            dialog.setOnBtnClickL(onLeftBtnClicks,onRigtBtnClicks);//按钮点击事件是数组，按左右中顺序。
        }else if(btnNum == 3){
            dialog.setOnBtnClickL(onLeftBtnClicks,onRigtBtnClicks,onMiddleBtnClicks);//按钮点击事件是数组，按左右中顺序。
        }
        dialog.setToastClickL(onToastBtnClick);
        dialog.setWebJumpListener(webviewJumpListener);
        return dialog;
    }

    @Override
    public Dialog createDialog(boolean isShow) {
        dialog = new MaterialDialog(context);
        dialog.title(title).content(content)
                .btnNum(btnNum);
        dialog.setCanceledOnTouchOutside(touchOutCancel);
        dialog.setCancelable(normalCancel);
        dialog.isToastShow(toastShow);
        dialog.baseUrl(baseUrl);
        dialog.isHtmlContent(htmlContent);

        if(btnNum == 1){
            if(Utils.isEmptyString(middleBtnText)){
                middleBtnText = context.getString(R.string.confirm);
            }
            dialog.btnText(middleBtnText);
        }else if(btnNum == 2){
            if(Utils.isEmptyString(leftBtnText)){
                leftBtnText = context.getString(R.string.cancel);
            }
            if(Utils.isEmptyString(rightBtnText)){
                rightBtnText = context.getString(R.string.confirm);
            }
            dialog.btnText(leftBtnText, rightBtnText);
        }else if(btnNum == 3){
            if(Utils.isEmptyString(leftBtnText)){
                leftBtnText = context.getString(R.string.cancel);
            }
            if(Utils.isEmptyString(rightBtnText)){
                rightBtnText = context.getString(R.string.confirm);
            }
            if(Utils.isEmptyString(middleBtnText)){
                middleBtnText = context.getString(R.string.continue_text);
            }
            dialog.btnText(leftBtnText,rightBtnText,middleBtnText);
        }
//	    dialog.showAnim(bas_in)
//	            .dismissAnim(bas_out);
        if(btnNum == 1){
            dialog.setOnBtnClickL(onMiddleBtnClicks);//按钮点击事件是数组，按左右中顺序。
        }else if(btnNum == 2){
            dialog.setOnBtnClickL(onLeftBtnClicks,onRigtBtnClicks);//按钮点击事件是数组，按左右中顺序。
        }else if(btnNum == 3){
            dialog.setOnBtnClickL(onLeftBtnClicks,onRigtBtnClicks,onMiddleBtnClicks);//按钮点击事件是数组，按左右中顺序。
        }
        dialog.setToastClickL(onToastBtnClick);
        dialog.setWebJumpListener(webviewJumpListener);
        return dialog;
    }

}
