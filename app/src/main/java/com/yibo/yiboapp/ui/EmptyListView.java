package com.yibo.yiboapp.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.utils.Utils;


/**
 * Created by zhangy on 16-3-28.
 * 列表空白显示页
 */
public class EmptyListView extends LinearLayout {

    TextView emptyTxt;
    TextView clickRefresh;
    EmptyListviewListener mListener;
    String showText;
    ImageView iv_empty_image;

    public interface EmptyListviewListener{
        void onEmptyListviewClick();
    }
    public EmptyListView(Context context) {
        super(context);
    }

    public EmptyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(EmptyListviewListener mListener){
        this.mListener = mListener;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iv_empty_image = findViewById(R.id.iv_empty_image);
        emptyTxt = (TextView) findViewById(R.id.empty_text);
        clickRefresh = (TextView) findViewById(R.id.click_refresh);
        if (!TextUtils.isEmpty(showText)){
            clickRefresh.setText(showText);
        }
        clickRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onEmptyListviewClick();
                }
            }
        });
    }

    public void setShowText(String text){
        showText = text;
        if (!Utils.isEmptyString(text)){
            emptyTxt.setText(text);
        }
    }

    public void setButtonText(String btnTxt) {
        if (!Utils.isEmptyString(btnTxt)){
            clickRefresh.setText(btnTxt);
        }
    }


    public TextView getEmptyTxt() {
        return emptyTxt;
    }

    public TextView getClickRefresh() {
        return clickRefresh;
    }

    public ImageView getIv_empty_image() {
        return iv_empty_image;
    }
}
