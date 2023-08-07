package com.yibo.yiboapp.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;


public class PaxWebChromeClient extends WebChromeClient {
    private static final int CHOOSE_REQUEST_CODE = 0x9001;
    private Activity mActivity;
    private ValueCallback<Uri> uploadFile;//定义接受返回值
    private ValueCallback<Uri[]> uploadFiles;
    private ProgressBar loading;
//    private TextView mTitle;

    public PaxWebChromeClient(@NonNull Activity mActivity, ProgressBar loading) {
        this.mActivity = mActivity;
        this.loading = loading;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
//        if (newProgress == 100) {
//            bar.setVisibility(View.INVISIBLE);
//        } else {
//            if (View.INVISIBLE == bar.getVisibility()) {
//                bar.setVisibility(View.VISIBLE);
//            }
//            bar.setProgress(newProgress);
//        }
//        super.onProgressChanged(view, newProgress);
        loading.setProgress(newProgress);
    }

    @Override

    public void onReceivedTitle(WebView view, String title) {

        super.onReceivedTitle(view, title);




    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPermissionRequest(PermissionRequest request) {
        //                super.onPermissionRequest(request);//必须要注视掉
        request.grant(request.getResources());
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        this.uploadFile = uploadFile;
        openFileChooseProcess();
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
        this.uploadFile = uploadFile;
        openFileChooseProcess();
    }

    // For Android  > 4.1.1
//    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        this.uploadFile = uploadFile;
        openFileChooseProcess();
    }

    // For Android  >= 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        this.uploadFiles = filePathCallback;
        openFileChooseProcess();
        return true;
    }

    private void openFileChooseProcess() {
        //Intent i = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_OPEN_DOCUMENT
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT); //
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "Choose"), CHOOSE_REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_REQUEST_CODE:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    if (null != uploadFiles) {
                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null
                                : data.getData();
                        uploadFiles.onReceiveValue(new Uri[]{result});
                        uploadFiles = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }
            if (null != uploadFiles) {
                uploadFiles.onReceiveValue(null);
                uploadFiles = null;
            }
        }
    }



}
