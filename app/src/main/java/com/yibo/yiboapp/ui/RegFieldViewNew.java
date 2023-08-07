package com.yibo.yiboapp.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.utils.Utils;

/**
 * Created by johnson on 2017/10/5.
 */

public class RegFieldViewNew extends LinearLayout {

    Context context;
    TextView regNameTV;
    XEditText regValue;
    ImageView vcode;
    int isRequire;
    int validate;
    String regex;
    String regName;
    String key;
    boolean showVCode;//是否显示验证码
    Drawable requireDrawable;

    public RegFieldViewNew(Context context) {
        super(context);
        this.context = context;
    }

    public RegFieldViewNew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RegFieldViewNew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setRegName(String regName, int isRequire) {
        this.regName = regName;
//        regNameTV.setText(regName);
        regNameTV.setCompoundDrawables(getRegDrawableFromName(regName), null, null, null);

        if ("邀请码".equals(regName) && isRequire == 1) {
            regValue.setHint("请输入" + regName + ",没有可不填");
        } else {
            regValue.setHint("请输入" + regName);
            if ("手机".equals(regName) && isRequire == 1) {
                regValue.setHint("请输入手机号,没有可不填");
            } else if ("微信号".equals(regName) && isRequire == 1) {
                regValue.setHint("请输入微信号,没有可不填");
            }
        }


//        if (isRequire == 1) {
//            regValue.setHint("请输入" + regName);
//        } else {
//            regValue.setHint("请输入" + regName + ",必填");
//        }
//
//        if ("邀请码".equals(regName) && isRequire == 1) {
//            regValue.setHint("请输入" + regName + ",没有可不填");
//        } else if ("手机".equals(regName) && isRequire == 1) {
//            regValue.setHint("请输入手机号,没有可不填");
//        } else if ("微信号".equals(regName) && isRequire == 1) {
//            regValue.setHint("请输入微信号,没有可不填");
//        }

    }

    public String getRegName() {
        return regName;
    }

    public void setRequire(int require) {
        isRequire = require;
        if (isRequire == 2) {
            regNameTV.setCompoundDrawables(requireDrawable, null, getRegDrawableFromName(getRegName()), null);
        }
    }

    public int getIsRequire() {
        return isRequire;
    }

    public int isValidate() {
        return validate;
    }

    public void setValidate(int validate) {
        this.validate = validate;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public boolean isShowVCode() {
        return showVCode;
    }

    public void setShowVCode(boolean showVCode) {
        this.showVCode = showVCode;
        vcode.setVisibility(showVCode ? VISIBLE : GONE);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Drawable getRegDrawableFromName(String regName) {
        Drawable drawable;
        switch (regName) {
            case "验证码":
            case "邮箱":
                drawable = getResources().getDrawable(R.drawable.new_page_verify_code_drawable);
                break;
            case "QQ":
                drawable = getResources().getDrawable(R.drawable.new_page_qq_drawable);
                break;
            case "手机":
                drawable = getResources().getDrawable(R.drawable.new_page_phone_number_drawable);
                break;
            case "提款密码":
                drawable = getResources().getDrawable(R.drawable.new_page_password_drawable);
                break;
            case "银行卡号":
                drawable = getResources().getDrawable(R.drawable.new_page_bank_card_drawable);
                break;
            case "微信号":
                drawable = getResources().getDrawable(R.drawable.new_page_wechat_drawable);
                break;
            case "邀请码":
                drawable = getResources().getDrawable(R.drawable.new_page_invite_code_drawable);
                break;
            case "真实姓名":
            default:
                drawable = getResources().getDrawable(R.drawable.new_page_account_drawable);
        }
        drawable.setBounds(0, 0, getResources().getDimensionPixelSize(R.dimen.icon_20dp_size), getResources().getDimensionPixelSize(R.dimen.icon_20dp_size));
        return drawable;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        regNameTV = (TextView) findViewById(R.id.name);
        regValue = (XEditText) findViewById(R.id.value);
        vcode = (ImageView) findViewById(R.id.vertify_code_img);
        vcode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowVCode()) {
                    return;
                }
                updateImage();
            }
        });
        requireDrawable = getResources().getDrawable(R.drawable.start_icon);
        requireDrawable.setBounds(0, 0, requireDrawable.getMinimumWidth(), requireDrawable.getMinimumHeight());
    }

    public String getValueString() {
        return regValue.getText().toString().trim();
    }

    public void updateImage() {
        StringBuilder urls = new StringBuilder();
        urls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.REGISTER_VCODE_IMAGE_URL);
        if (Utils.isEmptyString(urls.toString())) {
            return;
        }
        GlideUrl gu = UsualMethod.getGlide(context, urls.toString());
        RequestOptions options = new RequestOptions().skipMemoryCache(true).placeholder(R.drawable.default_placeholder_picture)
                .error(R.drawable.default_placeholder_picture).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getContext()).load(gu)
                .apply(options)
//                placeholder(R.drawable.default_placeholder_picture)
//                .error(R.drawable.default_placeholder_picture)
//                .diskCacheStrategy( DiskCacheStrategy.NONE )//禁用磁盘缓存
                .into(vcode);
    }

}
