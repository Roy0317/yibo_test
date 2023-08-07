package com.yibo.yiboapp.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.webview.js.AndroidInterface;
import com.yibo.yiboapp.webview.js.JSCallback;
import com.yibo.yiboapp.webview.js.Live800JSBridge;
import com.yibo.yiboapp.webview.util.AndroidBug5497Workaround;


/**
 * 加载在线客服页面
 * 对话链接中的navHidden=1参数,用于决定是否显示H5对话页面是否显示导航栏
 * navEnable等于其他值或不添加,访客端将会显示导航栏
 *
 * @author Ray
 * @date 2019/10/05
 */
public class Live800ChattingActivity extends AppCompatActivity {
    private X5WebView webView;
    private com.tencent.smtt.sdk.ValueCallback<Uri> mUploadMessage;
    private com.tencent.smtt.sdk.ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;
    private TextView mWebTitleTv;
    private ImageView mSwitchHumanIv;
    private ProgressBar loading;
    /**
     * 是否启用了导航栏
     */
    private boolean mH5NavigationHidden;

    /**
     * 是否处于对话过程中
     */
    private boolean mIsChatting = false;

    private AndroidInterface mAndroidInterface = new AndroidInterface(new JSCallback() {
        @Override
        public void changeChatStatus(int chatStatus) {
            if (mSwitchHumanIv == null) {
                return;
            }
            switch (chatStatus) {
                case JSCallback.CHAT_STATUS_WAITING_HUMAN:
                    // 等待接通人工对话
                    mSwitchHumanIv.setVisibility(View.GONE);
                    mIsChatting = true;
                    break;
                case JSCallback.CHAT_STATUS_HUMAN:
                    // 已接通人工对话
                    mSwitchHumanIv.setVisibility(View.GONE);
                    mIsChatting = true;
                    break;
                case JSCallback.CHAT_STATUS_END_HUMAN:
                    // 人工对话结束
                    mSwitchHumanIv.setVisibility(View.GONE);
                    mIsChatting = false;
                    break;
                case JSCallback.CHAT_STATUS_ROBOT:
                    // 机器人状态
                    mIsChatting = false;
                    if (mH5NavigationHidden) {
                        mSwitchHumanIv.setVisibility(View.VISIBLE);
                    }
                    break;
                case JSCallback.CHAT_STATUS_LEAVE_MESSAGE:
                    // 留言状态
                    mSwitchHumanIv.setVisibility(View.GONE);
                    mIsChatting = false;
                default:
            }
        }

        @Override
        public void changeTitle(String title) {
            if (mWebTitleTv != null) {
                setTitleBarName();
            }
        }
    });

    private void setTitleBarName() {
        String basic_info_website_name = UsualMethod.getConfigFromJson(getApplicationContext()).getBasic_info_website_name();
        if (!TextUtils.isEmpty(basic_info_website_name)) {
            mWebTitleTv.setText(basic_info_website_name);
        } else {
            mWebTitleTv.setText("在线客服");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_live800_chatting);
//        StatusBarUtil.immersive(this);
        // 解决Activity全屏状态下,输入框被输入法遮挡的问题.
        AndroidBug5497Workaround.assistActivity(this);
        //如果有视频的时候可能出现闪烁，建议添加PixelFormat.TRANSLUCENT
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //防止输入法弹出后遮挡光标
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //采用add的方式,而非xml定义的方式，这样可以规避一部分内存泄露的问题。
        ConstraintLayout navigationCl = findViewById(R.id.navigation_layout_cl);
        //TODO
//        StatusBarUtil.setPaddingSmart(this, navigationCl);
        loading = findViewById(R.id.loading);
        webView = findViewById(R.id.web_view);
        // 设置WebChromeClient`,以便网页处理外链和文件选择发送的问题
        setWebClient(webView);

        // 向WebView中注入本地Java对象
        // 第二个参数是该对象的名字,方便被JavaScript识别(注意不要填写错了,否则Live800的JS无法识别)
        webView.addJavascriptInterface(mAndroidInterface, "Live800PageConnector");
        String url = getIntent().getStringExtra("url");
//        mH5NavigationHidden = getIntent().getBooleanExtra("h5_nav_hidden", false);
//        if (mH5NavigationHidden) {
        navigationCl.setVisibility(View.VISIBLE);
        initNavigationBar();
//        } else {
//            navigationCl.setVisibility(View.GONE);
//        }
        webView.loadUrl(url);
    }

    /**
     * 初始化导航栏
     */
    private void initNavigationBar() {
        ImageView webBackIv = findViewById(R.id.web_back_iv);
        mWebTitleTv = findViewById(R.id.web_title_tv);
        mSwitchHumanIv = findViewById(R.id.switch_human_manual_iv);

        webBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsChatting) {
                    Live800JSBridge.getInstance().showCloseChatDialog(webView);
                } else {
                    finish();
                }
            }
        });

        mSwitchHumanIv.setVisibility(View.VISIBLE);
        mSwitchHumanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Live800JSBridge.getInstance().switchToHuman(webView);
            }
        });
    }

    private void setWebClient(WebView webView) {
        /*
        设置WebViewClient,并重写shouldOverrideUrlLoading方法,防止重定向时调起系统的浏览器
        如果需要在加载网页前显示提示信息,可以重写WebViewClient的onPageStarted方法和onPageFinished
        WebViewClient中更多方法可以查阅android.webkit.WebViewClient的api
         */
        webView.setWebViewClient(new WebViewClient() {
//            /**
//             * 如果使用的是X5内核,不需要重写当前方法
//             */
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view,
//                                                    WebResourceRequest request) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    view.loadUrl(request.getUrl().toString());
//                }
//                return true;
//            }


            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                loading.setVisibility(View.VISIBLE);
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                loading.setVisibility(View.GONE);
                super.onPageFinished(webView, s);
            }

//            /**
//             * 防止重定向时调起系统浏览器
//             */
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return false;
//            }
        });
        /*
        如果要获取网页加载的进度,请重写onProgressChanged方法
        如果要监听JS Alert,请重写onJsAlert方法
        WebViewClient中更多方法可以查阅android.webkit.WebChromeClient的api
        */
        webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            //处理新窗口打开的外链
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // 在此处进行跳转URL的处理, 一般情况下需要重新打开一个页面, 这里直接让当前的Activity去加载url
                        Intent intent = new Intent(Live800ChattingActivity.this, Live800ChattingActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                        return true;
                    }

                });
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                loading.setProgress(i);
                super.onProgressChanged(webView, i);
            }

            // For Android 3.0+
            public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadMsg, String
                    acceptType) {
                Log.i("test", "openFileChooser 1");
                mUploadMessage = uploadMsg;
                openFileChooseProcess();
            }

            // For Android < 3.0
            public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadMsg) {
                Log.i("test", "openFileChooser 2");
                mUploadMessage = uploadMsg;
                openFileChooseProcess();
            }

            // For Android  > 4.1.1
            public void openFileChooser(com.tencent.smtt.sdk.ValueCallback<Uri> uploadMsg, String
                    acceptType, String capture) {
                Log.i("test", "openFileChooser 3");
                mUploadMessage = uploadMsg;
                openFileChooseProcess();
            }

            // For Android  >= 5.0
            public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
                mUploadCallbackAboveL = filePathCallback;
                openFileChooseProcess();
                return true;
            }

            @Override
            public void onReceivedTitle(WebView webView, String webTitle) {
                // 设置导航栏标题
                if (mWebTitleTv != null) {
                    setTitleBarName();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) {
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }

    }

    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onDestroy() {
        //从父控件中移除
        ViewParent parent = webView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(webView);
        }
        //及时销毁webView防止内存问题
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }

    @Override
    public void onBackPressed() {
        if (mIsChatting) {
            // 用户处于对话状态,或者等待接通人工的状态
            Live800JSBridge.getInstance().showCloseChatDialog(webView);
        } else {
            finish();
        }

//        不要使用WebView的goBack()方法
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            // 具体的返回逻辑
//        }

    }
}
