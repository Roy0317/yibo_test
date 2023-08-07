package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.anuo.immodule.BuildConfig;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.NoticeNewBean;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


public class AboutActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {
    private static final int INFO_REQUEST = 0x01;
    private WebView web_info;

    @Override
    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onCreate(bundle);
        setContentView(R.layout.activity_about);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.aboutus));
        initChildView();
        getAboutUs();
    }

    private void getAboutUs() {
        //最新公告
        // 类型 1:关于我们,2:提款帮助,3:存款帮助,4:合作伙伴->联盟方案,5:合作伙伴->联盟协议,6:联系我们,7:常见问题 ,
        // 8:玩法介绍,9:彩票公告,10:视讯公告,11:体育公告,12:电子公告,13:最新公告
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.NOTICE_URL_V2);
        urls.append("?code=1");
        CrazyRequest<CrazyResult<NoticeNewBean>> infoRequest = new AbstractCrazyRequest.Builder().
                url(urls.toString())
                .seqnumber(INFO_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<NoticeNewBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, infoRequest);
    }

    private void initChildView() {
        TextView versionText = (TextView) findViewById(R.id.version);
        web_info = findViewById(R.id.web_info);
        initWebView(web_info, this);
        versionText.setText( Utils.getVersionName(this) + "(" + BuildConfig.apk_code + ")");
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);

        if (this.isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == INFO_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ?
                        this.getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            NoticeNewBean reg = (NoticeNewBean) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            loadHtml(web_info,Urls.BASE_URL,reg.getContent().get(0).getContent());
        }
    }

    private void initWebView(WebView tv_webview, final Context context) {
        tv_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                crazy_wrapper.Crazy.Utils.Utils.LOG(TAG, "the material dialog url ==== " + url);
                if (!crazy_wrapper.Crazy.Utils.Utils.isEmptyString(url) && url != (Urls.BASE_URL + "/")) {
                    crazy_wrapper.Crazy.Utils.Utils.LOG(TAG, "the material dialog loading url = " + url);

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
                            context.startActivity(Intent
                                    .createChooser(emailIntent, "请选择邮件发送软件"));
                        }
                        return true;
                    }
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                    return true;

                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.getSettings().setJavaScriptEnabled(true);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (error.getPrimaryError() == android.net.http.SslError.SSL_INVALID) {// 校验过程遇到了bug
                    handler.proceed();
                } else {
                    handler.cancel();
                }
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
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(webSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        tv_webview.setVerticalScrollBarEnabled(false);
        tv_webview.setHorizontalScrollBarEnabled(false);
        tv_webview.setBackgroundColor(Color.TRANSPARENT);
    }

    public void loadHtml(WebView webView, String baseUrl, String content) {

        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append(content).append("\n");
        String style = "<script type=\"text/javascript\">\n" + "  var img = document.getElementsByTagName(\"img\");\n"
                + "  for(var i=0;i<img.length;i++){\n" + "\t\t\timg[i].setAttribute(\"style\",\"width:100%\");\n"
                + "  }\n" + "  </script>";
        html.append(style).append("\n");
        html.append("</html>");
        crazy_wrapper.Crazy.Utils.Utils.LOG(TAG, "the html ==== " + html.toString().trim());

        webView.loadDataWithBaseURL(baseUrl, html.toString().trim(), "text/html", "utf-8", null);
    }
}
