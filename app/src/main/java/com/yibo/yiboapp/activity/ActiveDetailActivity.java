package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.anuo.immodule.utils.ScreenUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.utils.Utils;

import java.io.BufferedInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 优惠活动详情页
 */
public class ActiveDetailActivity extends BaseActivity {

    WebView webView;
    String activeDetailStr = "";
    String title = "";
    String url = "";
    boolean wrapper = true;
    boolean isBigText = false;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_RESULTCODE_ANDROID5 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_detail);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        wrapper = getIntent().getBooleanExtra("wrapper", false);
        activeDetailStr = getIntent().getStringExtra("detail");
        isBigText = getIntent().getBooleanExtra("isBigText", false);
        initView();


        //设置summary
        if (!Utils.isEmptyString(activeDetailStr)) {
//            activeDetailStr = activeDetailStr.replaceAll("\r|\n","");
            String baseUrl = Urls.BASE_URL;
            Utils.syncCookie(this, baseUrl);
            if (wrapper) {
                webView.loadDataWithBaseURL(baseUrl/*CoreRequest.ROOT_URL*/, activeDetailStr,
                        "text/html", "utf-8", null);
            } else {
                webView.loadDataWithBaseURL(baseUrl/*CoreRequest.ROOT_URL*/, activeDetailStr,
                        "text/html", "utf-8", null);
            }
        } else {
            if (!Utils.isEmptyString(url)) {
//                webView.loadUrl(url);
                webView.loadUrl(url,Urls.getHeader(this,true));
            }
        }
    }


    private String wrapHtml(String content) {

        String wraHtml = "<html>" +
                "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>" +
                "<body>" +
                content +
                "<script>\n" +
                "              function ResizeImages(){\n" +
                "                var myimg;\n" +
                "                for(var i=0;i <document.images.length;i++){\n" +
                "                  myimg = document.images[i];\n" +
                "                  myimg.width = " + ScreenUtil.getScreenWidth(this) + ";\n" +
                "                }\n" +
                "              }\n" +
                "              window.onload=function(){ \n" +
                "                ResizeImages()\n" +
                "                window.location.hash = '#' + document.body.clientHeight;\n" +
                "                document.title = document.body.clientHeight;\n" +
                "              }\n" +
                "              </script>"
                + "</body>" + "</html>";
        return wraHtml;
    }



    private String getTextStyle() {
        return "var meta = document.createElement('meta'); meta.setAttribute('name', 'viewport');" +
                " meta.setAttribute('content', 'width=device-width'); document.getElementsByTagName('head')[0].appendChild(meta);";
    }

    public static void createIntent(Context context, String content, String title, boolean wrapper) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        intent.putExtra("detail", content);
        intent.putExtra("title", title);
        intent.putExtra("wrapper", wrapper);
        context.startActivity(intent);
    }

    public static void createIntent(Context context, String content, String title, boolean wrapper, boolean isBigText) { //字体是否变大
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        intent.putExtra("detail", content);
        intent.putExtra("title", title);
        intent.putExtra("wrapper", wrapper);
        intent.putExtra("isBigText", isBigText);
        context.startActivity(intent);
    }

    public static void createIntent(Context context, String content, String title, String url) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        intent.putExtra("detail", content);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void createIntent(Context context, String content, String title,
                                    String url, boolean wrapper) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        intent.putExtra("detail", content);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("wrapper", wrapper);
        context.startActivity(intent);
    }

    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(!Utils.isEmptyString(title) ? title : "优惠活动");
        webView = (WebView) findViewById(R.id.webview_summary);
//        // JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)
//        WebSettings webSettings = detailWebview.getSettings();
//
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

//        detailWebview.setVerticalScrollBarEnabled(false);
//        detailWebview.setHorizontalScrollBarEnabled(false);
//        detailWebview.setBackgroundColor(Color.TRANSPARENT);
//        //mWebViewSummary.addJavascriptInterface(new Utils.JsInterface(this), "imagelistner");
//        detailWebview.getSettings().setBlockNetworkImage(false);
////        webSettings.getUserAgentString().replace("Android", "android/" + Utils.getVersionName(this));

        webView.setVerticalScrollBarEnabled(false);
        webView.setWebViewClient(new MyClient());
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().supportMultipleWindows();
        if (isBigText) {
            webView.getSettings().setTextZoom(150);
        }
//        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);//支持跨域访问
        webView.getSettings().setAllowFileAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.getSettings().setUseWideViewPort(true);

        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadByBrowser(url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"),
                        FILECHOOSER_RESULTCODE);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                Log.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);

            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//                transport.setWebView(view);    //此webview可以是一般新创建的
//                resultMsg.sendToTarget();
                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                Log.d(TAG, "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE_ANDROID5);
                return true;
            }

        });
    }

    public class MyClient extends WebViewClient {

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            Utils.LOG(TAG, "onReceivedClientCertRequest = ");
            super.onReceivedClientCertRequest(view, request);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          WebResourceRequest request) {
            if (!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico")) {
                try {
                    return new WebResourceResponse("image/png", null,
                            new BufferedInputStream(view.getContext().getAssets().open("empty_favicon.ico")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (url.toLowerCase().contains("/favicon.ico")) {
                try {
                    return new WebResourceResponse("image/png", null,
                            new BufferedInputStream(view.getContext().getAssets().open("empty_favicon.ico")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Utils.LOG(TAG, "onReceivedHttpError = " + view.getUrl());
            super.onReceivedHttpError(view, request, errorResponse);
//            if (!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico") ) {
//                Log.e(TAG,"favicon.ico 请求错误"+errorResponse.getStatusCode()+errorResponse.getReasonPhrase());
//            } else {
//                // TODO:  具体可根据返回状态码做相应处理
//                showToast(errorResponse.getReasonPhrase());
//            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  // 接受所有网站的证书
            super.onReceivedSslError(view, handler, error);
            Utils.LOG(TAG, "onReceivedSslError url = " + error.toString());
            webView.setVisibility(View.VISIBLE);
            toggleBack();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
//            Utils.LOG(TAG, "onLoadResource = "+url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Utils.LOG(TAG, "onReceivedSslError url = " + failingUrl);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = view.getUrl();
            Utils.LOG(TAG, "shouldOverrideUrlLoading(), url = " + url);
            if(!TextUtils.isEmpty(url) && url.contains(".apk")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

//            Utils.LOG(TAG, "shouldOverrideUrlLoading = " +view.getUrl());
//            if(url.startsWith("weixin:")||url.startsWith("alipays:")) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);
//                return true;
//            }
//            if (url.startsWith("http:") || url.startsWith("https:")){
//                Utils.LOG(TAG,"loading url = "+url);
//                view.loadUrl(url);
//                return false;
//            }


            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            Utils.LOG(TAG, "onFormResubmission = ");
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            Utils.LOG(TAG, "onReceivedHttpAuthRequest = ");
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            Utils.LOG(TAG, "onTooManyRedirects = ");
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Utils.LOG(TAG, "shouldOverrideUrlLoading(), url = " + url);
            if(!TextUtils.isEmpty(url) && url.contains(".apk")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            // 如下方案可在非微信内部WebView的H5页面中调出微信支付
            if (url.startsWith("weixin:") || url.startsWith("alipays:")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                try {
                    startActivity(intent);
                } catch (Exception e) {

                }
                return true;
            }

            if (url.contains("mailto:")) {
                url = url.replace("mailto:", "");
                // 邮箱正则表达式
                String check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(url);
                boolean isMatched = matcher.matches();
                if (isMatched) {

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text"); // emailIntent.setType("message/rfc822");
                    // //真机上使用

                    String[] emailReciver = new String[]{url};

                    // 设置邮件默认地址
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                            emailReciver);
                    // 设置邮件默认标题
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                            "");
                    // 设置要默认发送的内容
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                    // 调用系统的邮件系统
                    startActivity(Intent
                            .createChooser(emailIntent, "请选择邮件发送软件"));
                }
                return true;
            }
//           returnsuper.shouldOverrideUrlLoading(view, url);
            if (url.startsWith("http:") || url.startsWith("https:")) {
                Utils.LOG(TAG, "loading url = " + url);
                view.loadUrl(url);
                return false;
            }
            return true;
        }


        @Override
        public void onPageFinished(WebView view, final String url) {
            Utils.LOG(TAG, "the finished page url = " + url);
            loadingState(false);
//            toggleBack();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utils.LOG(TAG, "the start page url = " + url);
//            loadingState(true);
            toggleBack();
            super.onPageStarted(view, url, favicon);
        }

    }

    private void loadingState(boolean isLoading) {
//        if (isLoading) {
//            loadLayout.setVisibility(View.VISIBLE);
//            webView.setVisibility(View.GONE);
//        } else {
//            loadLayout.setVisibility(View.GONE);
//            webView.setVisibility(View.VISIBLE);
//        }
    }

    private void toggleBack() {
//        tvBackText.setVisibility(webView.canGoBack()?View.VISIBLE:View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_ANDROID5) {
            if (null == mUploadCallbackAboveL)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null
                    : intent.getData();
            if (result != null) {
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{result});
            } else {
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{});
            }
            mUploadCallbackAboveL = null;
        }
    }

}
