package com.yibo.yiboapp.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import android.view.Display;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ProgressBar;


import com.yibo.yiboapp.Event.WebChatPhotoEvent;
import com.yibo.yiboapp.R;



import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class WebChatRoomDialog extends AlertDialog {

    public static final int CHOOSE_REQUEST_CODE = 0x9001;

    private WebView webView;
    PaxWebChromeClient chromeClient;
    private ProgressBar loading;
    private View view;
    private Activity mContext = null;
    private String url;

    public WebChatRoomDialog(Activity context, String url) {
        super(context,R.style.Transparent);
        this.mContext = context;
        this.url = url;
        initView();
        EventBus.getDefault().register(this);
    }


    protected void initView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = View.inflate(mContext, R.layout.webchat_dialog, null);
        loading = view.findViewById(R.id.loading);
        chromeClient = new PaxWebChromeClient(loading);
        webView = (WebView) view.findViewById(R.id.kefu_webview);
        webView.addJavascriptInterface(new JavascriptCloseInterface(), "close");
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loading.setVisibility(View.GONE);
                show();
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true。。
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.loadUrl(url);
    }


    public class JavascriptCloseInterface {
        /**
         * 注意： 在Android4.2极其以上系统需要给提供js调用的方法前加入一个注视：@JavaScriptInterface;
         * 在虚拟机当中 Javascript调用Java方法会检测这个anotation，
         * 如果方法被标识@JavaScriptInterface则Javascript可以成功调用这个Java方法，否则调用不成功。
         * 注意方法名要同后台约定的相同
         */
        @JavascriptInterface
        public void over() {
            dismiss();
        }
    }


    public WebChatRoomDialog initDialog() {
        show();
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        hide();
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.white);
        layoutParams.width = d.getWidth();
        layoutParams.height = d.getHeight();
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        return this;
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //表示消息接收函数运行在ui线程，即可以直接操作界面显示
    public void onMessageEvent(WebChatPhotoEvent event) {
        chromeClient.onMessageEvent(event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    class PaxWebChromeClient extends WebChromeClient {

       // private Activity mActivity;
        private ValueCallback<Uri> uploadFile;//定义接受返回值
        private ValueCallback<Uri[]> uploadFiles;
        private ProgressBar loading;


        public PaxWebChromeClient(ProgressBar loading) {
           // this.mActivity = mActivity;
            this.loading = loading;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
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
            Intent i = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_OPEN_DOCUMENT
           // Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT); //
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            mContext.startActivityForResult(Intent.createChooser(i, "Choose"), CHOOSE_REQUEST_CODE);
        }


        public void onMessageEvent(WebChatPhotoEvent event) {
            int resultCode = event.getResultCode();
            int requestCode = event.getRequestCode();
            Intent data = event.getIntent();
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
}