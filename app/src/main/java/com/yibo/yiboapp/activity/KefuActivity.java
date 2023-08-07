package com.yibo.yiboapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.utils.PaxWebChromeClient;

public class KefuActivity extends BaseActivity {

    private WebView webView;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int PHOTO_REQUEST = 100;
    PaxWebChromeClient chromeClient;
    private Uri imageUri;
    private ProgressBar loading;
    private LinearLayout ll_kefu;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_kefu);
        initView();
    }


    protected void initView() {
        super.initView();
        loading = findViewById(R.id.loading);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        ll_kefu = findViewById(R.id.ll_kefu);
        if (TextUtils.isEmpty(title)) {
            tvMiddleTitle.setText("在线客服");
        } else {
            tvMiddleTitle.setText(title);
        }
        if (!url.isEmpty()) {
            chromeClient = new PaxWebChromeClient(KefuActivity.this, loading);
            webView = (WebView) findViewById(R.id.kefu_webview);

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
                    startProgress();
                    loading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    loading.setVisibility(View.GONE);
                    stopProgress();
                    if (url.contains(Urls.INCOME_QUESTION_URL)) {
                        String js = "javascript:(function() {" +
                                "document.getElementsByClassName(\"header\")[0].style.display=\'none\';" + "" +
                                "document.getElementsByClassName(\"containter\")[0].style.marginTop=\'0\';" + "" +
                                "})()";
                        webView.loadUrl(js);

                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed(); // Ignore SSL certificate errors
                }

            });
            webView.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                    // url 你要访问的下载链接
                    // userAgent 是HTTP请求头部用来标识客户端信息的字符串
                    // contentDisposition 为保存文件提供一个默认的文件名
                    // mimetype 该资源的媒体类型
                    // contentLength 该资源的大小
                    // 这几个参数都是可以通过抓包获取的

                    // 用手机默认浏览器打开链接
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

        }
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
        if (!TextUtils.isEmpty(title) && title.equals("走势图"))
            settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        webView.loadUrl(url, Urls.getHeader(this, true));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        chromeClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ll_kefu.removeAllViews();
//        if (webView != null) {
//            webView.clearHistory();
//            webView.clearCache(true);
//            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
//            webView.freeMemory();
//            webView.pauseTimers();
//            webView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
//        }

        ((ViewGroup) webView.getParent()).removeView(webView);
        webView.clearCache(true);
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }
}
