package crazy_wrapper.Crazy.dialog;

import android.app.Dialog;

/**
 * 自定义APP各种弹框抽象工厂接口
 * 身份：APP弹框总管理
 * @author zhangy
 *
 */
public interface CustomDialogManager {
	 Dialog createDialog();
	 Dialog createDialog(boolean isShow);
}
