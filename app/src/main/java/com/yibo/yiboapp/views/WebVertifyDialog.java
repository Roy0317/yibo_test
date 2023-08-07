package com.yibo.yiboapp.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.interfaces.OnVertifyResultLinsenter;
import com.yibo.yiboapp.utils.PaxWebChromeClient;



import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;


public class WebVertifyDialog extends Dialog {

    private Context ctx;
    private View contentView;
    private WebView webView;
    private String content;
    PaxWebChromeClient chromeClient;
    private ProgressBar loading;
    OnVertifyResultLinsenter linsenter;
    boolean isFirst = true;
    private int type;//0登录 1注册
    private int inch = 3;//萤幕尺寸(英寸)


    public WebVertifyDialog(Context context, OnVertifyResultLinsenter linsenter, int type) {
        super(context);
        this.ctx = context;
        this.linsenter = linsenter;
        this.type = type;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = View.inflate(ctx, R.layout.dialog_picture_vertify, null);
        setContentView(contentView);
        initViews();
        setListener();
        windowDeploy();
    }

    private void initViews() {
        webView = contentView.findViewById(R.id.webView);
        loading = contentView.findViewById(R.id.loading);
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();

        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.floor(Math.sqrt(x + y));
        inch = ((int) Math.floor(screenInches));
        chromeClient = new PaxWebChromeClient((Activity) ctx, loading);
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

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
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
        webView.addJavascriptInterface(new JavascriptCloseInterface(), "vertifyAction");
        if (type == 0) {
            webView.loadUrl(Urls.BASE_URL + "/native/verify.do?verifyType=login", Urls.getHeader(ctx, true));
        } else {
            webView.loadUrl(Urls.BASE_URL + "/native/verify.do?verifyType=register", Urls.getHeader(ctx, true));
        }
    }

    private void setListener() {

    }


    public void setContent(String content) {
        this.content = content;
    }


    /**
     * 设置窗口显示
     */
    public void windowDeploy() {
        Window window = getWindow();
        //出现动画
//        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //设置显示位置
//        layoutParams.x = 0;
//        layoutParams.y = d.getHeight();
        //设置显示宽高
        layoutParams.height = d.getHeight() / 6 * 2;
        layoutParams.width = d.getWidth() / 10 * 9;
        //4寸以下调整视窗大小
        switch (inch){
            case 3:
                layoutParams.height = (d.getHeight() / 6 * 2) + 80;
                layoutParams.width = (d.getWidth() / 10 * 9) + 50;
                break;
            case 4:
                layoutParams.height = (d.getHeight() / 6 * 2) + 60;
                break;
        }
        window.setAttributes(layoutParams);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

    }

    @Override
    public void show() {
        super.show();
    }


    public class JavascriptCloseInterface {
        /**
         * 注意： 在Android4.2极其以上系统需要给提供js调用的方法前加入一个注视：@JavaScriptInterface;
         * 在虚拟机当中 Javascript调用Java方法会检测这个anotation，
         * 如果方法被标识@JavaScriptInterface则Javascript可以成功调用这个Java方法，否则调用不成功。
         * 注意方法名要同后台约定的相同
         */
        @JavascriptInterface
        public void nativeVertifyLogin(String Result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                    linsenter.onAccess();
                }
            });
        }

        @JavascriptInterface
        public void nativeVertifyRegister(String Result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                    linsenter.onAccess();
                }
            });
        }
    }
}
