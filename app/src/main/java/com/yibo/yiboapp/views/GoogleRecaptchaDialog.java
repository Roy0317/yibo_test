package com.yibo.yiboapp.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GoogleRecaptchaDialog extends Dialog {

    private String TAG = "GoogleRecaptchaDialog";
    private String siteKey;
    private WebView webCaptcha;
    private GoogleCaptchaCallback callback;

    public GoogleRecaptchaDialog(Context context, String key){
        super(context);
        this.siteKey = key;

        View view = View.inflate(context, R.layout.dialog_google_recaptcha, null);
        setContentView(view);
        webCaptcha = view.findViewById(R.id.webCaptcha);
    }

    public void setCallback(GoogleCaptchaCallback callback) {
        this.callback = callback;
    }

    public void showRecaptcha(){
        String html;
        BufferedReader in = null;
        try {
            Scanner s = new Scanner(getContext().getAssets().open("captcha_file.html")).useDelimiter("\\A");
            html = s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            html = "";
            Log.e(TAG, "Error opening asset");
        }

        if("".equalsIgnoreCase(html)){
            if(callback != null) callback.onError("开启Google Recaptcha时发生错误");
        }else {
            html = html.replace("${siteKey}", siteKey);
            webCaptcha.setWebViewClient(new WebViewClient());
            webCaptcha.getSettings().setJavaScriptEnabled(true);
            webCaptcha.getSettings().setBuiltInZoomControls(false);
            webCaptcha.addJavascriptInterface(new BridgeWebViewClass(), "BridgeWebViewClass");
            webCaptcha.loadDataWithBaseURL(Urls.BASE_URL + Urls.PORT, html, "text/html", "UTF-8", null);
//        webCaptcha.loadData(html, "text/html", "UTF-8");
//        webCaptcha.loadUrl("file:///android_asset/captcha_file.html");
        }
    }

    private class BridgeWebViewClass {
        @JavascriptInterface
        public void hCaptchaCallbackInAndroid(String token){
            Utils.logAll(TAG, "token； " + token);
            if(callback != null)
                callback.onVerified(token);
        }
    }

    public interface GoogleCaptchaCallback{
        void onError(String message);
        void onVerified(String token);
    }
}
