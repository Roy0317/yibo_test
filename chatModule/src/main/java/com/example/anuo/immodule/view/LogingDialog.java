package com.example.anuo.immodule.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;

/**
 * Created by johnson on 2018/3/29.
 */

public class LogingDialog extends Dialog{

    Activity activity;
    TextView tv;
    String title = "正在加载...";

    public LogingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
        this.activity = (Activity) context;
    }

    public LogingDialog(Context context, String title) {
        super(context, R.style.loadingDialogStyle);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loging_dialog);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(title);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
//        linearLayout.getBackground().setAlpha(210);

    }

    public void updateTitle(final String title) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        this.title = title;
        if (!isShowing()) {
            show();
        }
        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.setText(title);
            }
        });
    }
}
