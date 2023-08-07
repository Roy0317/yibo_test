package com.yibo.yiboapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.utils.Utils;

/*
 * 显示订单信息页面自定义view
 * 用于快速入款之后
 * blues
 * */
public class ShowPayInfoView extends LinearLayout {

    private TextView rightTxt;

    private TextView leftTxt;

    private Context context;

    public ShowPayInfoView(Context context) {
        this(context, null);

    }

    public ShowPayInfoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ShowPayInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ShowPayInfoView, defStyleAttr, 0);
        String left = arr.getString(R.styleable.ShowPayInfoView_spiView_left);
        arr.recycle();
        LayoutInflater.from(context).inflate(R.layout.item_show_pay_info, this);
        leftTxt = findViewById(R.id.item_confirm_pay_left_txt);
        leftTxt.setText(left);
        rightTxt = findViewById(R.id.item_confirm_pay_right_txt);
        findViewById(R.id.item_confirm_pay_copy_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionCopy(rightTxt.getText().toString());
            }
        });
    }

    public void setRightText(String value){
        rightTxt.setText(value);
    }
    public void setLeftText(String value){
        leftTxt.setText(value);
    }


    private void actionCopy(String content) {
        if (Utils.isEmptyString(content)) {
            Toast.makeText(context,  "没有内容，无法复制", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.LOG("aa", "content ==== " + content);
        UsualMethod.copy(content, context);
//        Toast.makeText(context,  "复制成功", Toast.LENGTH_SHORT).show();
        ToastUtils.showShort("复制成功");
    }

}