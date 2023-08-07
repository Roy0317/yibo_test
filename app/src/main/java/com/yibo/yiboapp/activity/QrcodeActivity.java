package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.utils.Utils;

/**
 * Created by johnson on 2018/1/26.
 */

public class QrcodeActivity extends BaseActivity {


    private ImageView ivQRCode;
    TextView clickDownloadApp;
    String appDownloadurl;

    @Override protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.qrcode_layout);
        initView();
        String sysConfig = YiboPreference.instance(this).getSysConfig();
        SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
        if (sc != null) {
            String url = sc.getApp_qr_code_link_android();
            appDownloadurl = sc.getApp_download_link_android();
            if (Utils.isEmptyString(url)){
                return;
            }
            GlideUrl glideUrl = UsualMethod.getGlide(getApplicationContext(), url);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.qrcode)
                    .error(R.drawable.qrcode);

            Glide.with(getApplicationContext()).load(glideUrl)
                    .apply(options)
                    .into(ivQRCode);
        }
    }

    @Override protected void initView() {
        super.initView();
        tvMiddleTitle.setText("APP二维码");
        ivQRCode = (ImageView) findViewById(R.id.img);
        clickDownloadApp = (TextView) findViewById(R.id.click_download);
        clickDownloadApp.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.click_download){
            if (Utils.isEmptyString(appDownloadurl)){
                showToast("没有安卓APP下载地址，请检查后台是否已经配置");
                return;
            }
            UsualMethod.viewLink(this,appDownloadurl);
        }
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, QrcodeActivity.class);
        context.startActivity(intent);
    }

}
