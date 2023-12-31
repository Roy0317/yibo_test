package com.simon.view.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.simon.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {

    private static final String TAG = "BridgeWebViewClient";
    private String mPrevUrl;

    private BridgeWebView webView;
    private WebViewClient mClient;
    private BridgeWebView.OnImageClickListener listener;

    public BridgeWebViewClient(BridgeWebView webView, WebViewClient client, BridgeWebView.OnImageClickListener mListener) {
        this.webView = webView;
        mClient = client;
        listener = mListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        boolean isDeal = false;
        if (mClient != null)
            isDeal = mClient.shouldOverrideUrlLoading(view, url);

        if (isDeal) return true;

        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        }

        return dealOurOwn(view, url);
    }

    /**
     * @param view
     * @param url
     * @return
     */
    private boolean dealOurOwn(WebView view, String url) {
        LogUtil.i(TAG, "the url ==== " + url);
        if (mPrevUrl != null && !mPrevUrl.equals(url)) {
            if (url.startsWith("http")) {
                LogUtil.i(TAG, "loading url = " + url);
                view.loadUrl(url);
                return false;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    view.getContext().startActivity(intent);
                } catch (Exception var6) {
                    var6.printStackTrace();
                    return super.shouldOverrideUrlLoading(view, url);
                }
                return true;
            }
        }
        mPrevUrl = url;

        if (mPrevUrl != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPrevUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                view.getContext().startActivity(intent);
            } catch (Exception var6) {
                var6.printStackTrace();
                return super.shouldOverrideUrlLoading(view, url);
            }
            return true;
        }

        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (mClient != null)
            mClient.onPageStarted(view, url, favicon);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mClient != null)
            mClient.onPageFinished(view, url);
        super.onPageFinished(view, url);

        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (mClient != null)
            mClient.onReceivedError(view, errorCode, description, failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }


    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();  // 接受所有网站的证书
    }


}