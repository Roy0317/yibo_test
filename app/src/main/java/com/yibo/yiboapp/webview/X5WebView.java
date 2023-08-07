package com.yibo.yiboapp.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;

/**
 * 封装些x5设置
 */
public class X5WebView extends WebView {
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        // 配置WebView的属性,包括启用JS等
        initWebViewSettings(this);
        this.getView().setClickable(true);
    }

    /**
     * 该方法中的WebView配置可能并不通用(例如:页面的缩放策略以及缓存加载策略)
     * 可自行根据需求修改
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings(WebView webView) {
        WebSettings webSetting = webView.getSettings();
        // 设置JS脚本是否允许自动打开弹窗
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置在WebView内部是否允许访问文件
        webSetting.setAllowFileAccess(true);
        // 设置在WebView宽度自适应(NARROW_COLUMNS表示:尽可能使所有列的宽度不超过屏幕宽度)
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        // 使WebView支持缩放
        webSetting.setSupportZoom(true);
        // 启用WebView内置缩放功能
        webSetting.setBuiltInZoomControls(true);
        // 使WebView支持可任意比例缩放
        webSetting.setUseWideViewPort(true);
        // 设置WebView支持打开多窗口
        webSetting.setSupportMultipleWindows(true);
        // 开启Application H5 Caches 功能
        webSetting.setAppCacheEnabled(true);
        // 设置最大缓存大小
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // 不使用缓存，每次都从网络上获取
        // 四种模式可选:LOAD_DEFAULT, LOAD_CACHE_ONLY, LOAD_NO_CACHE, LOAD_CACHE_ELSE_NETWORK
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 让WebView支持DOM storage API
        webSetting.setDomStorageEnabled(true);
        // 启用定位功能
        webSetting.setGeolocationEnabled(true);
        // 让WebView支持播放插件
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // 缩放至屏幕的大小
        webSetting.setLoadWithOverviewMode(true);
        // WebView启用javascript支持
        webSetting.setJavaScriptEnabled(true);

        // 设置混合加载,解决https页面嵌入了http的链接问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(0);
        }
        // 启用第三方cookie,解决iframe跨域问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            com.tencent.smtt.sdk.CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
    }

    public X5WebView(Context arg0) {
        super(arg0);
        setBackgroundColor(85621);
    }

}
