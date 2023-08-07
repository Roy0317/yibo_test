package crazy_wrapper.Crazy.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.meiya.cunnar.crazy.crazylibrary.R;

import java.io.BufferedInputStream;

import crazy_wrapper.Crazy.Utils.Utils;

/**
 * Dialog like Material Design Dialog
 */
public class MaterialDialog extends BaseAlertDialog<MaterialDialog> {

    Context context;

    public MaterialDialog(Context context) {
        super(context);

        this.context = context;
        /** default value*/
        titleTextColor = context.getResources().getColor(R.color.crazy_dialog_title_txt_color);
        titleTextSize_SP = context.getResources().getDimension(R.dimen.crazy_dialog_title_size);
        contentTextColor = context.getResources().getColor(R.color.crazy_dialog_content_txt_color);
        contentTextSize_SP = context.getResources().getDimension(R.dimen.crazy_dialog_content_size);
        leftBtnTextColor = context.getResources().getColor(R.color.crazy_colorPrimary);
            rightBtnTextColor = context.getResources().getColor(R.color.crazy_colorPrimary);
        middleBtnTextColor = context.getResources().getColor(R.color.crazy_dialog_middle_btn_color);
        toastBtnTextColor = context.getResources().getColor(R.color.crazy_dialog_toast_btn_color);

        /** default value*/
    }

    private void initWebView() {
        tv_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
                Utils.LOG(TAG, "the material dialog url ==== " + url);
                if (!Utils.isEmptyString(url) && url != (baseUrl + "/")) {
                    Utils.LOG(TAG, "the material dialog loading url = " + url);
                    view.loadUrl(url);
                    if (webviewJumpListener != null) {
                        webviewJumpListener.webJumpEvent(url);
                    }
                    return false;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
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

        tv_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        // JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)
        WebSettings webSettings = tv_webview.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        tv_webview.setVerticalScrollBarEnabled(false);
        tv_webview.setHorizontalScrollBarEnabled(false);
        tv_webview.setBackgroundColor(Color.TRANSPARENT);
        //mWebViewSummary.addJavascriptInterface(new Utils.JsInterface(this), "imagelistner");
        tv_webview.getSettings().setBlockNetworkImage(false);
//        webSettings.getUserAgentString().replace("Android", "android/" + Utils.getVersionName(this));
    }

//    private void initWebView() {
////        // JavaScript使能(如果要加载的页面中有JS代码，则必须使能JS)
////        WebSettings webSettings = detailWebview.getSettings();
////
////        webSettings.setJavaScriptEnabled(true);
////        webSettings.setBuiltInZoomControls(true);
////        webSettings.setSupportZoom(true);
////        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
////        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//
////        detailWebview.setVerticalScrollBarEnabled(false);
////        detailWebview.setHorizontalScrollBarEnabled(false);
////        detailWebview.setBackgroundColor(Color.TRANSPARENT);
////        //mWebViewSummary.addJavascriptInterface(new Utils.JsInterface(this), "imagelistner");
////        detailWebview.getSettings().setBlockNetworkImage(false);
//////        webSettings.getUserAgentString().replace("Android", "android/" + Utils.getVersionName(this));
//
//        tv_webview.setVerticalScrollBarEnabled(false);
//        tv_webview.setWebViewClient(new MyClient());
//        tv_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        tv_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        tv_webview.getSettings().setJavaScriptEnabled(true);
//        tv_webview.getSettings().setBlockNetworkImage(false);
//        tv_webview.getSettings().setBlockNetworkLoads(false);
//        tv_webview.getSettings().setSupportZoom(true);
//        tv_webview.getSettings().setBuiltInZoomControls(true);
//        tv_webview.getSettings().supportMultipleWindows();
//        tv_webview.getSettings().setAllowUniversalAccessFromFileURLs(true);//支持跨域访问
//        tv_webview.getSettings().setAllowFileAccessFromFileURLs(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            tv_webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//
//        tv_webview.getSettings().setUseWideViewPort(true);
//        tv_webview.getSettings().setLoadWithOverviewMode(true);
//        tv_webview.getSettings().setDomStorageEnabled(true);
//        tv_webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
//        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
//        tv_webview.getSettings().setAppCachePath(appCachePath);
//        tv_webview.getSettings().setAllowFileAccess(true);
//        tv_webview.getSettings().setAppCacheEnabled(true);
//        tv_webview.setVerticalScrollBarEnabled(true);
//        tv_webview.setHorizontalScrollBarEnabled(true);
//        tv_webview.setWebChromeClient(new WebChromeClient() {
//            // For Android 3.0+
//
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
//        });
//
//        Utils.LOG(TAG,"webview content in dialog = "+content);
////        if (!Utils.isEmptyString(content)) {
//////            String baseUrl = Urls.BASE_URL;
//////            Utils.syncCookie(this, baseUrl);
////
////            StringBuilder html = new StringBuilder();
////            html.append("<html>\n");
////            html.append(content);
////            String style = "<script type=\"text/javascript\">\n" + "  var img = document.getElementsByTagName(\"img\");\n"
////                    + "  for(var i=0;i<img.length;i++){\n" + "\t\t\timg[i].setAttribute(\"style\",\"width:100%\");\n"
////                    + "  }\n" + "  </script>";
////            html.append(style).append("\n");
////            html.append("</html>");
////            Utils.LOG(TAG,"the html ==== "+html.toString().trim());
////            tv_webview.loadDataWithBaseURL(baseUrl/*CoreRequest.ROOT_URL*/, content,
////                    "text/html", "utf-8", null);
////        }
//    }


    public class MyClient extends WebViewClient {

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            Utils.LOG(TAG, "onReceivedClientCertRequest = ");
            super.onReceivedClientCertRequest(view, request);
        }

//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        @Override public WebResourceResponse shouldInterceptRequest(WebView view,
//                                                                    WebResourceRequest request) {
//            if(!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico")) {
//                try {
//                    return new WebResourceResponse("image/png", null,
//                            new BufferedInputStream(view.getContext().getAssets().open("empty_favicon.ico")));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }

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
            tv_webview.setVisibility(View.VISIBLE);
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
            Utils.LOG(TAG, "shouldOverrideUrlLoading = " + view.getUrl());
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

            Utils.LOG(TAG, "the material dialog url ==== " + url);
            if (!Utils.isEmptyString(url) && url != (baseUrl + "/")) {
                Utils.LOG(TAG, "the material dialog loading url = " + url);
                view.loadUrl(url);
                if (webviewJumpListener != null) {
                    webviewJumpListener.webJumpEvent(url);
                }
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

    private void toggleBack() {
//        tvBackText.setVisibility(webView.canGoBack()?View.VISIBLE:View.GONE);
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


    @Override
    public View onCreateView() {

        /** title */
        tv_title.setGravity(Gravity.CENTER_VERTICAL);
        tv_title.getPaint().setFakeBoldText(true);
        tv_title.setPadding(dp2px(20), dp2px(20), dp2px(20), dp2px(20));
        tv_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_title.setBackgroundColor(context.getResources().getColor(R.color.crazy_colorPrimary));
        ll_container.addView(tv_title);

        /** content */
        tv_content.setPadding(dp2px(20), dp2px(40), dp2px(20), dp2px(40));
        tv_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        /** webview **/
        tv_webview.setPadding(dp2px(20), dp2px(40), dp2px(20), dp2px(40));
        tv_webview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        initWebView();

        //modify zhangy 20160727 for dialog show issue
        //ll_container.addView(tv_content);
        if (needScroll) {
            LinearLayout wrapper = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            wrapper.removeAllViews();
            wrapper.setLayoutParams(params);
            if (isHtmlContent) {
                wrapper.addView(tv_webview);
            } else {
                wrapper.addView(tv_content);
            }
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
            params1.weight = 1;
            scrollView.setLayoutParams(params1);    
            scrollView.addView(wrapper);
            ll_container.addView(scrollView);
        } else {
            if (isHtmlContent) {
                ll_container.addView(tv_webview);
            } else {
                ll_container.addView(tv_content);
            }
        }
        //end modify

        /**btns*/
        ll_btns.setGravity(Gravity.RIGHT);

        /**toast view*/
        LinearLayout.LayoutParams tparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tparams.weight = 1;
        toast.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        ll_btns.addView(toast, tparams);

        ll_btns.addView(tv_btn_left);
        ll_btns.addView(tv_btn_middle);
        ll_btns.addView(tv_btn_right);

        tv_btn_left.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        tv_btn_right.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        tv_btn_middle.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        toast.setPadding(dp2px(0), dp2px(0), dp2px(0), dp2px(0));
        ll_btns.setPadding(dp2px(20), dp2px(0), dp2px(10), dp2px(10));
        ll_container.addView(ll_btns);
        return ll_container;
    }

    @Override
    public void setUiBeforShow() {
        super.setUiBeforShow();
        /**set background color and corner radius */
        float radius = dp2px(cornerRadius_DP);
        ll_container.setBackgroundDrawable(CornerUtils.cornerDrawable(bgColor, radius));
        tv_btn_left.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
        tv_btn_right.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
        tv_btn_middle.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
        toast.setBackgroundDrawable(CornerUtils.btnSelector(radius, bgColor, btnPressColor, -2));
    }

    public void setTitleColor(int color) {
        tv_title.setBackgroundColor(color);
    }

    @Override
    public BaseDialog heightScale(float heightScale) {
        return super.heightScale(heightScale);
    }

    @Override
    public BaseDialog widthScale(float widthScale) {
        return super.widthScale(widthScale);
    }

    @Override
    public MaterialDialog bgColor(int bgColor) {
        return super.bgColor(bgColor);
    }
}
