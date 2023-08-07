package com.simon.view.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.simon.widget.ToastUtil;

import java.util.ArrayList;

public class WebUtil {

    /**
     * html模板
     */
    private static final String VOCALCONCER_DETAIL = "webview/vocalconcer.html";


    /**
     * 设置 html数据
     *
     * @param webView
     * @param content
     */
    public static void setVocalconcerDetail(BridgeWebView webView, String content) {
        String template = AssetsUtils.loadText(webView.getContext(), VOCALCONCER_DETAIL);
        template = template.replace("{content}", content);
        webView.setHtmlData(template);
    }

    public static String vertifyUrl(String url) {
        if (!url.equals("") && url.startsWith("HTTP://")) {
            url = url.replace("HTTP://", "http://");
        }else if (!url.equals("") && url.startsWith("HTTPS://")) {
            url = url.replace("HTTPS://", "https://");
        }
        return url;
    }

    /**
     * 打开外部浏览器
     *
     * @param ctx
     * @param url
     */
    public static boolean viewLink(Context ctx, String url) {
        try {
            url = vertifyUrl(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            ctx.startActivity(intent);
            return true;
        } catch (Exception e) {
            ToastUtil.showToast(ctx, "打开失败，网页地址有误");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 设置webview全屏
     */
    public static void addVideoFull(final BridgeWebView webView, final FrameLayout video, final WebChromeClient client) {
        // 播放视频
        webView.setWebChromeClient(new WebChromeClient() {

            // 进入全屏的时候
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // 赋值给callback
                if (client != null) {
                    client.onShowCustomView(view, callback);
                }
                video.addView(view);

            }

            // 退出全屏的时候
            @Override
            public void onHideCustomView() {
                if (client != null) {
                    client.onHideCustomView();
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (client != null) {
                    client.onProgressChanged(view, newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    /**
     * 实现普通的js交互
     *
     * @param ctx
     * @param webView
     */
    public static void addJavaScriptInterface(final Context ctx, BridgeWebView webView) {

        webView.setOnImageOpenListener(new BridgeWebView.OnImageClickListener() {

            //点击图片回调
            @Override
            public void onImageClick(int currentPosition, ArrayList<String> urls) {

            }

            //点击url链接回调
            @Override
            public void onUrlClick(String url) {

            }
        });
    }


}
