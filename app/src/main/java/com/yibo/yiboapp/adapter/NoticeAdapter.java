package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.KefuActivity;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.NoticeBean;
import com.yibo.yiboapp.views.MyWebView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crazy_wrapper.Crazy.Utils.Utils;


/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.adapter
 * @description: ${DESP}
 * @date: 2018/12/27
 * @time: 下午9:54
 */
public class NoticeAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "NoticeAdapter";

    public static final int CHILDREN_COUNT = 1;

    public int childCount;
    public List<NoticeBean> list;

    public NoticeAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return CHILDREN_COUNT;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = View.inflate(parent.getContext(), R.layout.childview_dialog_notice, null);
            groupHolder.textView = (TextView) convertView.findViewById(R.id.tv_child_title);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.textView.setText(list.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder childHolder;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = View.inflate(parent.getContext(), R.layout.groupview_dialog_notice, null);
            childHolder.myWebView = (WebView) convertView.findViewById(R.id.my_webView);
            initWebView(childHolder.myWebView, parent.getContext());
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        loadHtml(childHolder.myWebView, Urls.BASE_URL, list.get(groupPosition).getList().get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class GroupHolder {
        TextView textView;

    }

    private static class ChildHolder {
        WebView myWebView;
    }

    public void loadHtml(WebView webView, String baseUrl, String content) {

        StringBuilder html = new StringBuilder();
        html.append("<html>\n");
//        html.append("<head>\n");
//        html.append("</head>\n");
//        html.append("<body>\n");
        //公告设置图片数量只有一张的话，强制将宽改成100%,两张以上则让租户自行设定宽度。
        if(content.split("<img").length <= 2){
            content = content.replace("<img","<img style=\"width:100%\"  class=\"hundred\"");
        }
        html.append(content).append("\n");
//        html.append("</body>\n");
//        String style = "<script type=\"text/javascript\">\n" + "  var img = document.getElementsByTagName(\"img\");\n"
//                + "  for(var i=0;i<img.length;i++){\n" + "\t\t\timg[i].setAttribute(\"style\",\"width:100%\");\n"
//                + "  }\n" + "  </script>";
//        html.append(style).append("\n");
        html.append("</html>");
        Utils.LOG(TAG, "the html ==== " + html.toString().trim());

        webView.loadDataWithBaseURL(baseUrl, html.toString().trim(), "text/html", "utf-8", null);
    }

    private void initWebView(WebView tv_webview, final Context context) {
        tv_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
                Utils.LOG(TAG, "the material dialog url ==== " + url);
                if (!Utils.isEmptyString(url) && url != (Urls.BASE_URL + "/")) {
                    Utils.LOG(TAG, "the material dialog loading url = " + url);

//                    view.loadUrl(url);
//                    Intent intent=new Intent(context,KefuActivity.class);
//                    intent.putExtra("title","公告内容");
//                    intent.putExtra("url",url);
//                    context.startActivity(intent);
                    if(url.contains("mailto:")) {
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
                //addImageClickListner();

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
        //mWebViewSummary.addJavascriptInterface(new Utils.JsInterface(this), "imagelistner");

//        webSettings.getUserAgentString().replace("Android", "android/" + Utils.getVersionName(this));
    }


}
