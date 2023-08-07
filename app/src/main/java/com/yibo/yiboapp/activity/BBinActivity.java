package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.utils.Utils;

/**
 * bbin页
 */
public class BBinActivity extends BaseActivity {

    WebView detailWebview;
    String activeDetailStr = "";
    String title = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_detail);
        title = getIntent().getStringExtra("title");
        initView();
        activeDetailStr = getIntent().getStringExtra("detail");

        //设置summary
        if (!Utils.isEmptyString(activeDetailStr)) {
            String baseUrl = Urls.BASE_URL;
            Utils.syncCookie(this, baseUrl);

            StringBuilder html = new StringBuilder();
            html.append(activeDetailStr);
            detailWebview.loadDataWithBaseURL(baseUrl/*CoreRequest.ROOT_URL*/, html.toString().trim(),
                    "text/html", "utf-8", null);
        }

    }

    public static void createIntent(Context context, String content,String title) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        intent.putExtra("detail", content);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(!Utils.isEmptyString(title)?title:"优惠活动");
        detailWebview = (WebView) findViewById(R.id.webview_summary);
        detailWebview.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.getSettings().setJavaScriptEnabled(true);
                //addImageClickListner();

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                //handler.proceed();
            }

        });

        detailWebview.setWebChromeClient(new WebChromeClient() {
            @Override public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            @Override public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        // JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)
        WebSettings webSettings = detailWebview.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        detailWebview.setVerticalScrollBarEnabled(false);
        detailWebview.setHorizontalScrollBarEnabled(false);
        detailWebview.setBackgroundColor(Color.TRANSPARENT);
        //mWebViewSummary.addJavascriptInterface(new Utils.JsInterface(this), "imagelistner");
        detailWebview.getSettings().setBlockNetworkImage(false);
//        webSettings.getUserAgentString().replace("Android", "android/" + Utils.getVersionName(this));
    }
}
