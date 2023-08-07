package com.yibo.yiboapp.views;

import android.content.Context;

import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.route.LDNetActivity.RouteCheckingActivity;

import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;

public class NetErrorDialog {
    private static CustomConfirmDialog ccd;


    public static CustomConfirmDialog getInstance(Context mContext) {
        if (ccd == null)
            return showNetErrorDialog(mContext);
        return ccd;
    }


    /**
     * 打开检测路由的对话框
     *
     * @param mContext
     */
    private static CustomConfirmDialog showNetErrorDialog(final Context mContext) {
        ccd = new CustomConfirmDialog(mContext);
        ccd.setBtnNums(2);
        ccd.setLeftBtnText("去路由检测");
        ccd.setRightBtnText("取消");

        ccd.setLeftBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                RouteCheckingActivity.createIntent(mContext);
                ccd.dismiss();
            }
        });
        ccd.setRightBtnClickListener(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        ccd.setTitle("温馨提示");
        ccd.setContent("当前网络环境较差，请检测路由状况");
//        ccd.setToastShow(true);
        ccd.setHtmlContent(true);
        ccd.setBaseUrl(Urls.BASE_URL);
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();

        return ccd;
    }
}
