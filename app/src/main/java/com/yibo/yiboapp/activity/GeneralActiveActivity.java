package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.simon.utils.LogUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.GeneralActiveWraper;
import com.yibo.yiboapp.utils.PaxWebChromeClient;
import com.yibo.yiboapp.utils.Utils;

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

import static com.yibo.yiboapp.data.Urls.BASE_URL;

/**
 * 优惠活动详情页
 */
public class GeneralActiveActivity extends BaseActivity implements SessionResponse.Listener
        <CrazyResult<Object>> {

    WebView webView;
    boolean isBigText = false;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_RESULTCODE_ANDROID5 = 2;

    public static final int GENERAL_URL_REQ = 1;

    String url = "";
    private PaxWebChromeClient chromeClient;
    private ProgressBar loading;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_detail);


        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");
        initView();
        setCookies();
//        String url = getUrl(code);
//        getUrl(true);
        webView.loadUrl(url, Urls.getHeader(this, true));

    }

    public void setCookies() {
        Log.i("Cookie", "SESSION=" + YiboPreference.instance(this).getToken());
        CookieManager.getInstance().setCookie(BASE_URL, "SESSION=" + YiboPreference.instance(this).getToken());// 设置 Cookie
    }

//    private String getUrl(String code) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(Urls.BASE_URL).append(Urls.PORT);
//        stringBuilder.append("/native/generalActivity.do");
//        stringBuilder.append("?code=").append(code);
//        stringBuilder.append("&userAgent=").append("android" + Utils.getVersionName(this));
//        return stringBuilder.toString().trim();
//    }


//    private void getUrl(boolean showDialog) {
//        if (this.isFinishing()) {
//            return;
//        }
//        StringBuilder configUrl = new StringBuilder();
//        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GENERAL_ACTIVITY_URL);
//
//        CrazyRequest<CrazyResult<GeneralActiveWraper>> request = new AbstractCrazyRequest.Builder().
//                url(configUrl.toString())
//                .seqnumber(GENERAL_URL_REQ)
//                .headers(Urls.getHeader(this))
//                .refreshAfterCacheHit(false)
//                .shouldCache(false).placeholderText(getString(R.string.get_recording))
//                .priority(CrazyRequest.Priority.HIGH.ordinal())
//                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(GsonConverterFactory.create(new TypeToken<GeneralActiveWraper>() {
//                }.getType()))
//                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
//                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
//                .create();
//        RequestManager.getInstance().startRequest(this, request);
//    }

    public static void createIntent(Context context, String url, String name) {
        Intent intent = new Intent(context, GeneralActiveActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        super.initView();
        loading = findViewById(R.id.loading);
        tvMiddleTitle.setText(!Utils.isEmptyString(name) ? name : "活动");
        webView = (WebView) findViewById(R.id.webview_summary);
//        // JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)

        chromeClient = new PaxWebChromeClient(this, loading);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUserAgentString("User-Agent");
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setBackgroundColor(Color.TRANSPARENT);
        //mWebViewSummary.addJavascriptInterface(new Utils.JsInterface(this), "imagelistner");
        webView.getSettings().setBlockNetworkImage(false);
//        webSettings.getUserAgentString().replace("Android", "android/" + Utils.getVersionName(this));

        webView.setVerticalScrollBarEnabled(false);
        webView.setWebChromeClient(chromeClient);
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
//        webView.setWebChromeClient(new WebChromeClient() {
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                startActivityForResult(Intent.createChooser(i, "File Chooser"),
//                        FILECHOOSER_RESULTCODE);
//            }
//
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                Log.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                startActivityForResult(Intent.createChooser(i, "File Browser"),
//                        FILECHOOSER_RESULTCODE);
//            }
//
//            // For Android 4.1
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
//
//            }
//
//            @Override
//            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
////                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
////                transport.setWebView(view);    //此webview可以是一般新创建的
////                resultMsg.sendToTarget();
//                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
//            }
//
//            // For Android 5.0+
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
//                                             WebChromeClient.FileChooserParams fileChooserParams) {
//                Log.d(TAG, "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
//                mUploadCallbackAboveL = filePathCallback;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE_ANDROID5);
//                return true;
//            }
//        });
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
            LogUtil.e("InterceptRequest1:", request.getUrl().getPath());
//            if (!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico")) {
//                try {
//                    return new WebResourceResponse("image/png", null,
//                            new BufferedInputStream(view.getContext().getAssets().open("empty_favicon.ico")));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (!request.getRequestHeaders().containsKey("webviewhost") && request.getUrl().getPath().endsWith(".do")) {
//                try {
//                    HttpURLConnection urlConnection = (HttpURLConnection) new URL(request.getUrl().toString()).openConnection();
//                    urlConnection.setRequestMethod(request.getMethod());
//                    for (String s : request.getRequestHeaders().keySet()) {
//                        urlConnection.addRequestProperty(s, request.getRequestHeaders().get(s));
//                    }
//                    if (!Utils.isEmptyString(BASE_HOST_URL)) {
//                        urlConnection.addRequestProperty(RequestUtils.NATIVE_WEBVIEW_HOST, BASE_HOST_URL);
//                    }
//                    urlConnection.addRequestProperty(RequestUtils.ROUTE_TYPE, String.valueOf(APP_ROUTE_TYPE));
//                    urlConnection.addRequestProperty(RequestUtils.NATIVE_DOMAIN, BASE_URL);
//                    urlConnection.addRequestProperty(RequestUtils.NATIVE_FLAG, "1");
//                    urlConnection.addRequestProperty("Cookie", "SESSION=" + YiboPreference.instance(GeneralActiveActivity.this).getToken());
//                    String contentType = urlConnection.getContentType();
//                    String charset = "";
//                    if (!TextUtils.isEmpty(contentType)) {
//                        StringTokenizer contentTypeTokenizer = new StringTokenizer(contentType, ";");
//                        String tokenizedContentType = contentTypeTokenizer.nextToken();
//                        String capturedCharset = urlConnection.getContentEncoding();
//                        if (TextUtils.isEmpty(capturedCharset)) {
//                            Pattern compile = Pattern.compile(".*?charset=(.*?)(;.*)?$");
//                            Matcher matcher = compile.matcher(contentType);
//                            if (matcher.find() && matcher.groupCount() > 0) {
//                                capturedCharset = matcher.group(1);
//                            }
//                        }
//                        if (TextUtils.isEmpty(capturedCharset) && !TextUtils.isEmpty(capturedCharset)) {
//                            charset = capturedCharset;
//                        }
//                        contentType = tokenizedContentType;
//                    }
//                    int status = urlConnection.getResponseCode();
//                    InputStream inputStream = null;
//                    if (status == HttpURLConnection.HTTP_OK) {
//                        inputStream = urlConnection.getInputStream();
//                    } else {
//                        inputStream = urlConnection.getErrorStream() == null ? urlConnection.getErrorStream() : urlConnection.getInputStream();
//                    }
//                    Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
//                    List<String> encondings = headerFields.get("Content-Encoding");
//                    if (encondings != null) {
//                        for (String enconding : encondings) {
//                            if (enconding.equals("gzip")) {
//                                inputStream = new GZIPInputStream(inputStream);
//                                break;
//                            }
//                        }
//                    }
//                    return new WebResourceResponse(contentType, charset, status, urlConnection.getResponseMessage(),
//                            convertConnectionResponseToSingleValueMap(urlConnection.getHeaderFields()), inputStream);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            return null;
        }

        private Map convertConnectionResponseToSingleValueMap(Map<String, List<String>> headerFields) {
            HashMap headers = new HashMap<String, String>();
            for (String s : headerFields.keySet()) {
                List<String> value = headerFields.get(s);
                if (value == null || value.isEmpty()) {
                    headers.put(s, "");
                } else if (value.size() == 1) {
                    headers.put(s, value.get(0));
                } else {
                    StringBuilder builder = new StringBuilder(value.get(0));
                    String separator = ";";
                    for (int i = 1; i <= value.size(); i++) {
                        builder.append(separator);
                        builder.append(value.get(i));
                    }
                    headers.put(s, builder.toString());
                }
            }
            return headers;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            LogUtil.e("InterceptRequest2:", url);
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
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

            Utils.LOG(TAG, "the url ==== " + url);
            // 如下方案可在非微信内部WebView的H5页面中调出微信支付
            if (url.startsWith("weixin:") || url.startsWith("alipays:")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
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
            loading.setVisibility(View.GONE);
            loadingState(false);
//            toggleBack();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Utils.LOG(TAG, "the start page url = " + url);
//            loadingState(true);
            loading.setVisibility(View.VISIBLE);
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
        chromeClient.onActivityResult(requestCode, resultCode, intent);
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

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == GENERAL_URL_REQ) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_fail);
                return;
            }
            Object regResult = result.result;
            GeneralActiveWraper reg = (GeneralActiveWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (!Utils.isEmptyString(reg.getContent().getUrl())) {
                webView.loadUrl(BASE_URL + Urls.PORT + reg.getContent().getUrl(), Urls.getHeader(this, true));
            }
        }
    }


}
