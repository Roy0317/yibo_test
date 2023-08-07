package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.ShowPayInfoView;

import java.util.ArrayList;
import java.util.List;


/**
 * 显示支付信息界面，用于快速入款
 */
public class ShowPayInfoAvtivity extends BaseActivity {

    public static void createIntent(Context context, String[] keys, String[] values, String qrCode){
        Intent intent = new Intent(context, ShowPayInfoAvtivity.class);
        intent.putExtra("keys", keys);
        intent.putExtra("values", values);
        intent.putExtra("qrCode", qrCode);
        context.startActivity(intent);
    }


    private List<ShowPayInfoView> infoViewList;
    private ImageView qrImg;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_show_pay_info);
        initView();

        String[] keys = getIntent().getStringArrayExtra("keys");
        String[] values = getIntent().getStringArrayExtra("values");
        if(keys.length == values.length){
            for (int i = 0; i < keys.length; i++) {
                if(i < infoViewList.size()){
                    infoViewList.get(i).setVisibility(View.VISIBLE);
                    infoViewList.get(i).setLeftText(keys[i] + "： ");
                    infoViewList.get(i).setRightText(values[i]);
                }
            }
        }

        String qrcodeUrl = getIntent().getStringExtra("qrCode");
        if (!Utils.isEmptyString(qrcodeUrl)) {
            GlideUrl gu = UsualMethod.getGlide(this, qrcodeUrl);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.default_placeholder_picture)
                    .error(R.drawable.default_placeholder_picture);

            Glide.with(this).load(gu)
                    .apply(options)
                    .into(qrImg);
        } else {
            findViewById(R.id.act_show_pay_info_tips).setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("支付信息");

        infoViewList = new ArrayList<>();
        infoViewList.add(findViewById(R.id.info_view1));
        infoViewList.add(findViewById(R.id.info_view2));
        infoViewList.add(findViewById(R.id.info_view3));
        infoViewList.add(findViewById(R.id.info_view4));
        infoViewList.add(findViewById(R.id.info_view5));
        infoViewList.add(findViewById(R.id.info_view6));
        infoViewList.add(findViewById(R.id.info_view7));
        qrImg = findViewById(R.id.act_show_pay_info_qrcode);
    }
}
