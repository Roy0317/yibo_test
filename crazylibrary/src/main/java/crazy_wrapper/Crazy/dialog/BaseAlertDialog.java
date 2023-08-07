package crazy_wrapper.Crazy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import crazy_wrapper.Crazy.Utils.Utils;

public abstract class BaseAlertDialog<T extends BaseAlertDialog<T>> extends BaseDialog {
    /**
     * container
     */
    protected LinearLayout ll_container;
    //title
    /**
     * title
     */
    protected TextView tv_title;
    /**
     * title content(标题)
     */
    protected String title;
    /**
     * title textcolor(标题颜色)
     */
    protected int titleTextColor;
    /**
     * title textsize(标题字体大小,单位sp)
     */
    protected float titleTextSize_SP;
    /**
     * enable title show(是否显示标题)
     */
    protected boolean isTitleShow = true;

    //content
    /** content */
    /**
     * scrollview
     */
    protected ScrollView scrollView;
    protected TextView tv_content;
    protected WebView tv_webview;
    /**
     * content text
     */
    protected String content;
    /**
     * show gravity of content(正文内容显示位置)
     */
    protected int contentGravity = Gravity.CENTER_VERTICAL;
    /**
     * content textcolor(正文字体颜色)
     */
    protected int contentTextColor;
    protected int contentLodingTextColor;
    /**
     * content textsize(正文字体大小)
     */
    protected float contentTextSize_SP;
    /**
     * enable title show(是否显示正文)
     */
    protected boolean isContentShow = true;
    protected boolean needScroll = true;
    protected boolean isToastShow;
    protected boolean isHtmlContent;
    protected String baseUrl = "";

    //ProgressBar
    protected TextView progressText;//进度文字
    protected ProgressBar progressBar;
    protected ProgressWheel progressWheel;
    LoadingDialog.ProgressListener pListener;

    //spinner
    /**
     * num of spinner, [1,2]
     */
    protected int spinnerNum = 0;
    /**
     * spinner content text(spinner提示内容)
     */
    protected String spinnerUpContentText;
    protected String spinnerDownContentText;
    protected TextView tvSpinnerUpContent;
    protected TextView tvSpinnerUp;
    protected TextView tvSpinnerDownContent;
    protected TextView tvSpinnerDown;


    //btns
    /**
     * num of btns, [1,3]
     */
    protected int btnNum = 2;
    /**
     * btn container
     */
    protected LinearLayout ll_btns;
    protected CheckBox toast;
    /**
     * btns
     */
    protected TextView tv_btn_left;
    protected TextView tv_btn_right;
    protected TextView tv_btn_middle;
    /**
     * btn text(按钮内容)
     */
    protected String btnLeftText = "取消";
    protected String btnRightText = "确定";
    protected String btnMiddleText = "继续";
    protected String toastText = "不再提示";
    /**
     * btn textcolor(按钮字体颜色)
     */
    protected int leftBtnTextColor;
    protected int rightBtnTextColor;
    protected int middleBtnTextColor;
    protected int toastBtnTextColor;
    /**
     * btn textsize(按钮字体大小)
     */
    protected float leftBtnTextSize_SP = 15f;
    protected float rightBtnTextSize_SP = 15f;
    protected float middleBtnTextSize_SP = 15f;
    protected float smallBtnTextSize_SP = 12f;
    /**
     * btn press color(按钮点击颜色)
     */
    protected int btnPressColor = Color.parseColor("#E3E3E3");// #85D3EF,#ffcccccc,#E3E3E3
    /**
     * left btn click listener(左按钮接口)
     */
    protected OnBtnClickL onBtnLeftClickL;
    /**
     * right btn click listener(右按钮接口)
     */
    protected OnBtnClickL onBtnRightClickL;
    /**
     * middle btn click listener(右按钮接口)
     */
    protected OnBtnClickL onBtnMiddleClickL;
    protected OnBtnClickL onToastClickL;
    protected WebviewJumpListener webviewJumpListener;

    /**
     * corner radius,dp(圆角程度,单位dp)
     */
    protected float cornerRadius_DP = 3;
    /**
     * background color(背景颜色)
     */
    protected int bgColor = Color.parseColor("#ffffff");

    /**
     * method execute order:
     * show:constrouctor---show---oncreate---onStart---onAttachToWindow
     * dismiss:dismiss---onDetachedFromWindow---onStop
     *
     * @param context
     */
    public BaseAlertDialog(Context context) {
        super(context);
        widthScale(0.88f);

        ll_container = new LinearLayout(context);
        ll_container.setOrientation(LinearLayout.VERTICAL);

        /** title */
        tv_title = new TextView(context);

        /** content */
        tv_content = new TextView(context);
        tv_webview = new WebView(context);
        scrollView = new ScrollView(context);

        /** progress bar */

        progressBar = new ProgressBar(context);
        progressText = new TextView(context);

        /** spinner */
        tvSpinnerUpContent = new TextView(context);
        tvSpinnerUp = new TextView(context);
        tvSpinnerDownContent = new TextView(context);
        tvSpinnerDown = new TextView(context);

        /**btns*/
        ll_btns = new LinearLayout(context);
        ll_btns.setOrientation(LinearLayout.HORIZONTAL);
        /** toast view*/
        toast = new CheckBox(context);

        tv_btn_left = new TextView(context);
        tv_btn_left.setGravity(Gravity.CENTER);

        tv_btn_middle = new TextView(context);
        tv_btn_middle.setGravity(Gravity.CENTER);

        tv_btn_right = new TextView(context);
        tv_btn_right.setGravity(Gravity.CENTER);
    }

    /**
     更新框中的显示内容
     created zhangy on 下午9:13 17/1/16
     **/
    protected void updateContent(String content) {
        tv_content.setText(content);
    }


    private void initWebView() {
        tv_webview.setVerticalScrollBarEnabled(false);
//        tv_webview.setWebViewClient(new MyClient());
        tv_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        tv_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        tv_webview.getSettings().setJavaScriptEnabled(true);
        tv_webview.getSettings().setBlockNetworkImage(false);
        tv_webview.getSettings().setBlockNetworkLoads(false);
        tv_webview.getSettings().setSupportZoom(true);
        tv_webview.getSettings().setBuiltInZoomControls(true);
        tv_webview.getSettings().supportMultipleWindows();
//        webView.getSettings().setSupportMultipleWindows(true);
        tv_webview.getSettings().setAllowUniversalAccessFromFileURLs(true);//支持跨域访问
        tv_webview.getSettings().setAllowFileAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tv_webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        tv_webview.getSettings().setUseWideViewPort(true);
        tv_webview.getSettings().setLoadWithOverviewMode(true);
        tv_webview.getSettings().setDomStorageEnabled(true);
        tv_webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        tv_webview.getSettings().setAppCachePath(appCachePath);
        tv_webview.getSettings().setAllowFileAccess(true);
        tv_webview.getSettings().setAppCacheEnabled(true);
        tv_webview.setVerticalScrollBarEnabled(true);
        tv_webview.setHorizontalScrollBarEnabled(true);
    }


    @Override public void setUiBeforShow() {
        /** title */
        tv_title.setVisibility(isTitleShow ? View.VISIBLE : View.GONE);

        tv_title.setText(TextUtils.isEmpty(title) ? "温馨提示" : title);
        tv_title.setTextColor(titleTextColor);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize_SP);

        initWebView();

        /** content */
        if (isHtmlContent) {
            tv_webview.setVisibility(isContentShow ? View.VISIBLE : View.GONE);
        }else{
            tv_content.setVisibility(isContentShow ? View.VISIBLE : View.GONE);
        }
        tv_content.setGravity(contentGravity);
        if (isHtmlContent) {
            tv_webview.getSettings().setUseWideViewPort(false);
//            Utils.syncCookie(this, baseUrl);
            StringBuilder html = new StringBuilder();
            html.append("<html>\n");
            html.append(content).append("\n");
//            String style = "<script type=\"text/javascript\">\n" + "  var img = document.getElementsByTagName(\"img\");\n"
//                    + "  for(var i=0;i<img.length;i++){\n" + "\t\t\timg[i].setAttribute(\"style\",\"width:100%\");\n"
//                    + "  }\n" + "  </script>";
//            html.append(style).append("\n");
            html.append("</html>");
            Utils.LOG(TAG,"the html ==== "+html.toString().trim());
            tv_webview.loadDataWithBaseURL(baseUrl, html.toString().trim(),
                    "text/html", "utf-8", null);
//            tv_content.setText(Html.fromHtml(html, null, null));
        }else{
            tv_content.setText(content);
        }
        tv_content.setTextColor(contentTextColor);
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, contentTextSize_SP);
        tv_content.setLineSpacing(0, 1.3f);



        scrollView.setVisibility(needScroll ? View.VISIBLE : View.GONE);
        scrollView.setVerticalScrollBarEnabled(false);

        /** toast */
        toast.setVisibility(isToastShow ? View.VISIBLE : View.GONE);

        /** spinner */
        if (spinnerNum == 1) {
            tvSpinnerUpContent.setVisibility(View.VISIBLE);
            tvSpinnerUp.setVisibility(View.VISIBLE);
            tvSpinnerDownContent.setVisibility(View.GONE);
            tvSpinnerDown.setVisibility(View.GONE);
            tvSpinnerUpContent.setText(spinnerUpContentText);
        } else if (spinnerNum == 2) {
            tvSpinnerUpContent.setVisibility(View.VISIBLE);
            tvSpinnerUp.setVisibility(View.VISIBLE);
            tvSpinnerDownContent.setVisibility(View.VISIBLE);
            tvSpinnerDown.setVisibility(View.VISIBLE);
            tvSpinnerUpContent.setText(spinnerUpContentText);
            tvSpinnerDownContent.setText(spinnerDownContentText);
        } else {
            tvSpinnerUpContent.setVisibility(View.GONE);
            tvSpinnerUp.setVisibility(View.GONE);
            tvSpinnerDownContent.setVisibility(View.GONE);
            tvSpinnerDown.setVisibility(View.GONE);
        }

        /**btns*/
        tv_btn_left.setText(btnLeftText);
        tv_btn_right.setText(btnRightText);
        tv_btn_middle.setText(btnMiddleText);

        /**set toast view**/
        toast.setText(toastText);
        toast.setTextColor(toastBtnTextColor);
        toast.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallBtnTextSize_SP);

        tv_btn_left.setTextColor(leftBtnTextColor);
        tv_btn_right.setTextColor(rightBtnTextColor);
        tv_btn_middle.setTextColor(middleBtnTextColor);

        tv_btn_left.setTextSize(TypedValue.COMPLEX_UNIT_SP, leftBtnTextSize_SP);
        tv_btn_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightBtnTextSize_SP);
        tv_btn_middle.setTextSize(TypedValue.COMPLEX_UNIT_SP, middleBtnTextSize_SP);

        if (btnNum == 1) {
            tv_btn_left.setVisibility(View.GONE);
            tv_btn_right.setVisibility(View.GONE);
        } else if (btnNum == 2) {
            tv_btn_middle.setVisibility(View.GONE);
        }

        toast.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (onToastClickL != null) {
                    onToastClickL.onBtnClick();
                } else {
//                    dismiss();
                }
            }
        });

        tv_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onBtnRightClickL != null) {
                    onBtnRightClickL.onBtnClick();
                } else {
                    dismiss();
                }
            }
        });

        tv_btn_middle.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onBtnMiddleClickL != null) {
                    onBtnMiddleClickL.onBtnClick();
                } else {
                    dismiss();
                }
            }
        });

        tv_btn_left.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onBtnLeftClickL != null) {
                    onBtnLeftClickL.onBtnClick();
                } else {
                    dismiss();
                }
            }
        });

    }

    @Override
    public BaseDialog widthScale(float widthScale) {
        return super.widthScale(widthScale);
    }

    @Override
    public BaseDialog heightScale(float heightScale) {
        return super.heightScale(heightScale);
    }

    /**
     * set title text(设置标题内容) @return MaterialDialog
     */
    public T title(String title) {
        this.title = title;
        return (T) this;
    }

    /**
     * set title textcolor(设置标题字体颜色)
     */
    public T titleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return (T) this;
    }

    /**
     * set title textsize(设置标题字体大小)
     */
    public T titleTextSize(float titleTextSize_SP) {
        this.titleTextSize_SP = titleTextSize_SP;
        return (T) this;
    }

    /**
     * enable title show(设置标题是否显示)
     */
    public T isTitleShow(boolean isTitleShow) {
        this.isTitleShow = isTitleShow;
        return (T) this;
    }

    /**
     * enable content show(设置标题是否显示)
     */
    public T isContentShow(boolean isContentShow) {
        this.isContentShow = isContentShow;
        return (T) this;
    }

    /**
     * enable content show(设置提示view是否显示)
     */
    public T isToastShow(boolean isToastShow) {
        this.isToastShow = isToastShow;
        return (T) this;
    }

    /**
     * enable content show(设置提示view是否显示)
     */
    public T isHtmlContent(boolean isHtmlContent) {
        this.isHtmlContent = isHtmlContent;
        return (T) this;
    }


    /**
     * set content text(设置正文内容)
     */
    public T content(String content) {
        this.content = content;
        return (T) this;
    }

    /**
     * set base url text(设置网址域名)
     */
    public T baseUrl(String url) {
        this.baseUrl = url;
        return (T) this;
    }

    /**
     * set toast text(设置提示内容)
     */
    public T toast(String toast) {
        this.toastText = toast;
        return (T) this;
    }

    /**
     * set content gravity(设置正文内容,显示位置)
     */
    public T contentGravity(int contentGravity) {
        this.contentGravity = contentGravity;
        return (T) this;
    }

    /**
     * set content textcolor(设置正文字体颜色)
     */
    public T contentTextColor(int contentTextColor) {
        this.contentTextColor = contentTextColor;
        return (T) this;
    }

    /**
     * set content textsize(设置正文字体大小,单位sp)
     */
    public T contentTextSize(float contentTextSize_SP) {
        this.contentTextSize_SP = contentTextSize_SP;
        return (T) this;
    }

    /**
     * set btn text(设置按钮文字内容)
     * btnTexts size 1, middle
     * btnTexts size 2, left right
     * btnTexts size 3, left right middle
     */
    public T btnNum(int btnNum) {
        if (btnNum < 1 || btnNum > 3) {
            throw new IllegalStateException("btnNum is [1,3]!");
        }
        this.btnNum = btnNum;

        return (T) this;
    }

    /**
     * set btn text(设置按钮文字内容)
     * btnTexts size 1, middle
     * btnTexts size 2, left right
     * btnTexts size 3, left right middle
     */
    public T btnText(String... btnTexts) {
        if (btnTexts.length < 1 || btnTexts.length > 3) {
            throw new IllegalStateException(" range of param btnTexts length is [1,3]!");
        }

        if (btnTexts.length == 1) {
            this.btnMiddleText = btnTexts[0];
        } else if (btnTexts.length == 2) {
            this.btnLeftText = btnTexts[0];
            this.btnRightText = btnTexts[1];
        } else if (btnTexts.length == 3) {
            this.btnLeftText = btnTexts[0];
            this.btnRightText = btnTexts[1];
            this.btnMiddleText = btnTexts[2];
        }
        return (T) this;
    }

    /**
     * set btn textcolor(设置按钮字体颜色)
     * btnTextColors size 1, middle
     * btnTextColors size 2, left right
     * btnTextColors size 3, left right middle
     */
    public T btnTextColor(int... btnTextColors) {
        if (btnTextColors.length < 1 || btnTextColors.length > 3) {
            throw new IllegalStateException(" range of param textColors length is [1,3]!");
        }

        if (btnTextColors.length == 1) {
            this.middleBtnTextColor = btnTextColors[0];
        } else if (btnTextColors.length == 2) {
            this.leftBtnTextColor = btnTextColors[0];
            this.rightBtnTextColor = btnTextColors[1];
        } else if (btnTextColors.length == 3) {
            this.leftBtnTextColor = btnTextColors[0];
            this.rightBtnTextColor = btnTextColors[1];
            this.middleBtnTextColor = btnTextColors[2];
        }

        return (T) this;
    }

    /**
     * set btn textsize(设置字体大小,单位sp)
     * btnTextSizes size 1, middle
     * btnTextSizes size 2, left right
     * btnTextSizes size 3, left right middle
     */
    public T btnTextSize(float... btnTextSizes) {
        if (btnTextSizes.length < 1 || btnTextSizes.length > 3) {
            throw new IllegalStateException(" range of param btnTextSizes length is [1,3]!");
        }

        if (btnTextSizes.length == 1) {
            this.middleBtnTextSize_SP = btnTextSizes[0];
        } else if (btnTextSizes.length == 2) {
            this.leftBtnTextSize_SP = btnTextSizes[0];
            this.rightBtnTextSize_SP = btnTextSizes[1];
        } else if (btnTextSizes.length == 3) {
            this.leftBtnTextSize_SP = btnTextSizes[0];
            this.rightBtnTextSize_SP = btnTextSizes[1];
            this.middleBtnTextSize_SP = btnTextSizes[2];
        }

        return (T) this;
    }

    /**
     * set btn press color(设置按钮点击颜色)
     */
    public T btnPressColor(int btnPressColor) {
        this.btnPressColor = btnPressColor;
        return (T) this;
    }

    /**
     * set corner radius (设置圆角程度)
     */
    public T cornerRadius(float cornerRadius_DP) {
        this.cornerRadius_DP = cornerRadius_DP;
        return (T) this;
    }

    /**
     * set backgroud color(设置背景色)
     */
    public T bgColor(int bgColor) {
        this.bgColor = bgColor;
        return (T) this;
    }

    /**
     * set spinner content count(设置spinner提示文字数量)
     */
    public T spinnerNum(int spinnerNum) {
        if (spinnerNum < 1 || spinnerNum > 2) {
            throw new IllegalStateException("spinnerNum is [1,2]!");
        }
        this.spinnerNum = spinnerNum;

        return (T) this;
    }

    /**
     * set spinner content text(设置spinner提示文字内容)
     * spinnerContentTexts size 1, up
     * spinnerContentTexts size 2, up down
     */
    public T spinnerContentText(String... spinnerContentTexts) {
        if (spinnerContentTexts.length < 1 || spinnerContentTexts.length > 2) {
            throw new IllegalStateException(" range of param spinnerContentTexts length is [1,2]!");
        }
        if (spinnerContentTexts.length == 1) {
            this.spinnerUpContentText = spinnerContentTexts[0];
        } else if (spinnerContentTexts.length == 2) {
            this.spinnerUpContentText = spinnerContentTexts[0];
            this.spinnerDownContentText = spinnerContentTexts[1];
        }
        return (T) this;
    }

    /**
     * set btn click listener(设置按钮监听事件)
     * onBtnClickLs size 1, middle
     * onBtnClickLs size 2, left right
     * onBtnClickLs size 3, left right middle
     */
    public void setOnBtnClickL(OnBtnClickL... onBtnClickLs) {
        if (onBtnClickLs.length < 1 || onBtnClickLs.length > 3) {
            throw new IllegalStateException(" range of param onBtnClickLs length is [1,3]!");
        }

        if (onBtnClickLs.length == 1) {
            this.onBtnMiddleClickL = onBtnClickLs[0];
        } else if (onBtnClickLs.length == 2) {
            this.onBtnLeftClickL = onBtnClickLs[0];
            this.onBtnRightClickL = onBtnClickLs[1];
        } else if (onBtnClickLs.length == 3) {
            this.onBtnLeftClickL = onBtnClickLs[0];
            this.onBtnRightClickL = onBtnClickLs[1];
            this.onBtnMiddleClickL = onBtnClickLs[2];
        }
    }

    public void setToastClickL(OnBtnClickL onBtnClickL) {
        onToastClickL = onBtnClickL;
    }

    public void setWebJumpListener(WebviewJumpListener listener) {
        webviewJumpListener = listener;
    }


}
