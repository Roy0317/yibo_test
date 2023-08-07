package com.yibo.yiboapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;

/**
 * Created by johnson on 2018/3/29.
 */

public class LogingDialog extends Dialog{

    TextView tv;
    String title = "正在加载...";

    public LogingDialog(Context context,String title) {
        super(context, R.style.loadingDialogStyle);
        this.title = title;
        setContentView(R.layout.loging_dialog);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(title);
    }

    public void updateTitle(String title) {
        this.title = title;
        tv.setText(title);
    }

    public void showLoading(String title){
        this.title = title;
        tv.setText(title);
        if(!isShowing())
            show();
    }
}
