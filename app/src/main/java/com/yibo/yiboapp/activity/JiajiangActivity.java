package com.yibo.yiboapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.MemberDeficitRecord;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.PaxWebChromeClient;

import java.net.URLStreamHandler;

public class JiajiangActivity extends BaseActivity {

    private WebView webView;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int PHOTO_REQUEST = 100;
    PaxWebChromeClient chromeClient;
    private Uri imageUri;
    private ProgressBar loading;
    private TextView lingqu;
    String title;
    String url;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_jiajiang);
        initView();
    }

    protected void initView() {
        super.initView();
        loading = findViewById(R.id.loading);
        lingqu = findViewById(R.id.lingqu);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        lingqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "";
                if (title.contains("周周转运")) {
                    uri = Urls.LINGQU_ZHUANYUN;
                }
                if (title.contains("每日加奖")) {
                    uri = Urls.LINGQU_JIAJIANG;
                }
                HttpUtil.postForm(getApplicationContext(), uri, null, true, getString(R.string.get_recording), new HttpCallBack() {
                    @Override
                    public void receive(NetworkResult result) {

                        if (result.isSuccess()) {
                            showToast("领取成功");
                        } else {
                            showToast("领取失败");
                        }
                    }
                });
            }
        });
        if (TextUtils.isEmpty(title)) {
            title = "在线客服";
            tvMiddleTitle.setText("在线客服");
        } else {
            tvMiddleTitle.setText(title);
        }
        if (title.contains("周周转运")) {
            tvRightText.setText("记录");
            tvRightText.setVisibility(View.VISIBLE);
            tvRightText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(JiajiangActivity.this, NativeRecordActivity.class);
                    intent.putExtra("type", 1);
                    JiajiangActivity.this.startActivity(intent);
                }
            });
        } else if (title.contains("每日加奖")) {

            tvRightText.setText("记录");
            tvRightText.setVisibility(View.VISIBLE);
            tvRightText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(JiajiangActivity.this, NativeRecordActivity.class);
                    intent.putExtra("type", 2);
                    JiajiangActivity.this.startActivity(intent);
                }
            });
        } else {
            lingqu.setVisibility(View.GONE);
        }


        if (!url.isEmpty()) {
            chromeClient = new PaxWebChromeClient(JiajiangActivity.this, loading);
            webView = (WebView) findViewById(R.id.kefu_webview);
            webView.setWebChromeClient(chromeClient);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();// 接受所有网站的证书
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    CookieManager cm = CookieManager.getInstance();
                    String cookies = cm.getCookie(url);
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
                    if (url.contains(Urls.ZHOUZHOUZHUANYUN)) {//周周转运隐藏标题栏
                        String js = "javascript:(function() {" +
                                "document.getElementsByClassName(\"bar bar-nav\")[0].style.display=\'none\';" + "" +
                                "document.getElementsByClassName(\"content\")[0].style.marginTop=\'-50px\';" + "" +
                                "document.getElementById(\"bonusBtn\").style.display=\'none\';" + "" +
                                "})()";
                        webView.loadUrl(js);
                    } else if (url.contains(Urls.MEIRIJIAJIANG)) {//每日加奖隐藏标题栏
                        String js = "javascript:(function() {" +
                                "document.getElementsByClassName(\"bar bar-nav\")[0].style.display=\'none\';" + "" +
                                "document.getElementsByClassName(\"content\")[0].style.marginTop=\'-50px\';" + "" +
                                "document.getElementById(\"bonusBtn\").style.display=\'none\';" + "" +
                                "})()";
                        webView.loadUrl(js);
                    }
                }
            });
        }
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        settings.setBlockNetworkImage(false);//解决图片不显示
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true。。
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(true);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(true);

        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);



        //如果访问的页面中有Javascript，则webview必须设置支持Javascript

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);

        
        webView.loadUrl(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        chromeClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



}
