package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.utils.Utils;


public class AppDownloadActivity extends BaseActivity {

    ImageView appStoreBtn;
    ImageView androidStoreBtn;

    SimpleDraweeView appStoreQrCode;
    SimpleDraweeView androidStoreQrCode;
    TextView tvInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_download);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("APP下载");
        appStoreBtn = (ImageView) findViewById(R.id.appstore_icon);
        androidStoreBtn = (ImageView) findViewById(R.id.android_store_icon);
        tvInfo = findViewById(R.id.tv_info);
        appStoreBtn.setOnClickListener(this);
        androidStoreBtn.setOnClickListener(this);
        appStoreQrCode = findViewById(R.id.appstore_qr_code);
        androidStoreQrCode = findViewById(R.id.android_store_qr_qode);
        SysConfig sys = UsualMethod.getConfigFromJson(this);
        appStoreQrCode.setImageURI(Uri.parse(sys.getApp_qr_code_link_ios().trim()));
        androidStoreQrCode.setImageURI(Uri.parse(sys.getApp_qr_code_link_android().trim()));
        if (!TextUtils.isEmpty(sys.getApp_download_info())) {
            tvInfo.setText("温馨提示:" + sys.getApp_download_info());
        }
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, AppDownloadActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.appstore_icon) {
            viewUrl(true);
        } else if (v.getId() == R.id.android_store_icon) {
            viewUrl(false);
        }
    }

    private void viewUrl(boolean isApple) {
        String sysConfig = YiboPreference.instance(this).getSysConfig();
        SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
        if (sc == null) {
            return;
        }
        if (isApple) {
            if (!Utils.isEmptyString(sc.getApp_download_link_ios())) {
                UsualMethod.viewLink(this, sc.getApp_download_link_ios().trim());
            } else {
                showToast("没有配置苹果APP下载地址");
            }
        } else {
            if (!Utils.isEmptyString(sc.getApp_download_link_android())) {
                UsualMethod.viewLink(this, sc.getApp_download_link_android().trim());
            } else {
                showToast("没有配置安卓APP下载地址");
            }
        }
    }
}
