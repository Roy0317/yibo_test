package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.ui.ProgressWheel;
import com.yibo.yiboapp.utils.Utils;

public class SportNewsWebActivity extends BaseActivity {

    private WebView webView;
    private TextView empty;
    private LinearLayout loadingData;
    ProgressWheel progressWheel;
    private boolean isShowLoading = false;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_news_web);
        initView();

        if (!Utils.isNetworkAvailable(this)) {
            empty.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            showToast(getString(R.string.network_invalid));
            return;
        }

        tvMiddleTitle.setText(!Utils.isEmptyString(getIntent().getStringExtra("title"))?
                getIntent().getStringExtra("title"):getString(R.string.sport_top_news));
        String URL = getIntent().getStringExtra("url");

        if (!Utils.isEmptyString(URL) ){

                URL = appendSchemeToUrl(URL);
                webView.loadUrl(URL);
                empty.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                return;

        }
    }

    private String appendSchemeToUrl(String url) {
        if (Utils.isEmptyString(url)) {
            return url;
        }
        if (url.indexOf("://") != -1) {
            if (!url.startsWith("https")) {
                String preffix = url.substring(0, url.indexOf("://"));
                String leftStr = url.substring(url.indexOf("://"), url.length());
                if (preffix.length() == 0 || preffix.equalsIgnoreCase("http")){
                    url = "http" + leftStr;
                }
            }
        }else{
            url = "http://" + url;
        }
        Utils.LOG(TAG, "the website url = " + url);
        return url;
    }

    public static void createIntent(Context context,String url,String title) {
        Intent intent = new Intent(context, SportNewsWebActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    @Override protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.sport_top_news));
        loadingData = (LinearLayout) findViewById(R.id.loading_data_text);
        progressWheel = (ProgressWheel) loadingData.findViewById(R.id.progress_wheel);
        empty = (TextView) findViewById(R.id.empty);
        webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

    }

    public class MyWebViewClient extends WebViewClient {
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override public void onPageFinished(WebView view, final String url) {
            loadingState(false);
            super.onPageFinished(view, url);
        }

        @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            showLoadingVisibility(false);
            webView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadingState(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private void loadingState(boolean isLoading) {
        if (isLoading) {
            showLoadingVisibility(true);
            webView.setVisibility(View.GONE);
        } else {
            showLoadingVisibility(false);
            webView.setVisibility(View.VISIBLE);
        }
    }

    private void showLoadingVisibility(boolean isShow) {
        if (isShow) {
            isShowLoading = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (isShowLoading) {
                        loadingData.setVisibility(View.VISIBLE);
                        progressWheel.spin();
                    }
                }
            }, 200);
        } else {
            isShowLoading = false;
            loadingData.setVisibility(View.GONE);
            progressWheel.stopSpinning();
        }
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.back_text) {
//            if (webView.canGoBack()) {
//                webView.goBack();
//            } else {
//                super.onClick(v);
//            }
            finish();
        }
    }


}