package com.yibo.yiboapp.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import crazy_wrapper.Crazy.Utils.Utils;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.views
 * @description: ${DESP}
 * @date: 2018/12/28
 * @time: 下午1:50
 */
public class MyWebView extends WebView {

    private static final String TAG = "MyWebView";

    public MyWebView(Context context) {
        super(context);
        initWebView(context);
    }


    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView(context);

    }

    private void initWebView(Context context) {
        setVerticalScrollBarEnabled(false);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setBlockNetworkImage(false);
        getSettings().setBlockNetworkLoads(false);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().supportMultipleWindows();
        getSettings().setAllowUniversalAccessFromFileURLs(true);//支持跨域访问
        getSettings().setAllowFileAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        getSettings().setAppCachePath(appCachePath);
        getSettings().setAllowFileAccess(true);
        getSettings().setAppCacheEnabled(true);
        setVerticalScrollBarEnabled(true);
        setHorizontalScrollBarEnabled(true);
    }


    public void loadHtml(String baseUrl, String content) {
        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
        html.append(content).append("\n");
        String style = "<script type=\"text/javascript\">\n" + "  var img = document.getElementsByTagName(\"img\");\n"
                + "  for(var i=0;i<img.length;i++){\n" + "\t\t\timg[i].setAttribute(\"style\",\"width:100%\");\n"
                + "  }\n" + "  </script>";
        html.append(style).append("\n");
        html.append("</html>");
        Utils.LOG(TAG, "the html ==== " + html.toString().trim());

        loadDataWithBaseURL(baseUrl, html.toString().trim(), "text/html", "utf-8", null);
    }


}


