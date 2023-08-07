package com.yibo.yiboapp.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.ActiveStationDetailData;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.views.ActiviteDetailDialog;

public class ActiveStationDetailActivity extends BaseActivity {


    int id;
    private TextView title;
    private TextView comfirm;
    private WebView webView;
    private ActiveStationDetailData data;

    public static void createIntent(Context context, int id) {
        Intent intent = new Intent(context, ActiveStationDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activtiy_activite_station_detail);
        id = getIntent().getExtras().getInt("id");
        initView();
        initData();
    }


    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("活动详情");
        title = findViewById(R.id.tv_title);
        comfirm = findViewById(R.id.confirm);
        webView = findViewById(R.id.my_webView);
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActiviteDetailDialog(ActiveStationDetailActivity.this,id,data.getAwards(),data.getActivity().getTitle()).show();

            }
        });

    }

    protected void initData() {
        getdata(true);
    }


    private void getdata(boolean showDialog) {
        ApiParams params = new ApiParams();
        params.put("activeId", id);
        HttpUtil.get(this, Urls.GET_ACTIVE_LOBBY_DETAIL, params, true, "正在获取信息", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    data = new Gson().fromJson(result.getContent(), ActiveStationDetailData.class);
                    title.setText(data.getActivity().getTitle());
                    loadwebview(data.getActivity().getContent());

                } else {
                    showToast(result.getMsg());
                }
            }
        });

    }

    void loadwebview(String content) {
        StringBuffer sb = new StringBuffer();
//添加html
        sb.append("<html><head><meta http-equiv='content-type' content='text/html; charset=utf-8'>");
        sb.append("<meta charset='utf-8'  content='1'></head><body style='background-color: #1e1e27'><p></p>");
//
//< meta http-equiv="refresh"content="time" url="url" >
//添加文件的内容
        sb.append(content);
//加载本地文件
// sb.append("<img src='file:///"+AContext.getFileUtil().getDownloadsPath()+"'>");
        sb.append("</body></html>");
// webView.loadData(data, mimeType, encoding);

//设置字符编码，避免乱码


        WebSettings webSetting = webView.getSettings();
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //此方法不支持4.4以后
        webSetting.setUseWideViewPort(true);
        webSetting.setJavaScriptEnabled(true);

        webSetting.setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        webView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
    }
}
