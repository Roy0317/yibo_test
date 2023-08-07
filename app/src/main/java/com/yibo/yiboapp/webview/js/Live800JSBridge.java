package com.yibo.yiboapp.webview.js;

import android.os.Build;
import androidx.annotation.NonNull;

import com.tencent.smtt.sdk.WebView;



public class Live800JSBridge {
    private static final Live800JSBridge INSTANCE = new Live800JSBridge();

    private Live800JSBridge() {
        // 单例模式构造方法
    }

    public static Live800JSBridge getInstance() {
        return INSTANCE;
    }

    /**
     * 对话转人工
     */
    public void switchToHuman(@NonNull WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:Live800AppConnector.switchToHuman()", null);
        } else {
            webView.loadUrl("javascript:Live800AppConnector.switchToHuman()");
        }
    }

    /**
     * 只有等待对话状态,或者对话接通状态,该方法才会生效
     * 弹出结束对话的Dialog
     */
    public void showCloseChatDialog(@NonNull WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:Live800AppConnector.confirmCloseChat()", null);
        } else {
            webView.loadUrl("javascript:Live800AppConnector.confirmCloseChat()");
        }
    }

    /**
     * 直接结束对话,不弹出结束对话的Dialog
     */
    @Deprecated
    public void closeChat(@NonNull WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:Live800AppConnector.closeChat()", null);
        } else {
            webView.loadUrl("javascript:Live800AppConnector.closeChat()");
        }
    }
}
