package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.ChoseBrowserAdapter;
import com.yibo.yiboapp.entify.BrowserInfo;

import java.util.ArrayList;


public class ChoseBrowserDialog extends Dialog implements View.OnClickListener {

    //在构造方法里提前加载了样式
    private Context context;//上下文
    private int layoutResID;//布局文件id
    private ChoseBrowserAdapter adapter;
    private TextView tv_only, tv_away, tv_cancel;
    private ListView mListview;
    private ArrayList<BrowserInfo> mlist;
    private  OnSelectBrowserLinsenner linsenner;

    public void setPosition(int position) {
        this.position = position;
    }

    private int position = 0;

    public ChoseBrowserDialog(Context context,OnSelectBrowserLinsenner linsenner) {
        super(context, R.style.ChoseBrowserDialog);//加载dialog的样式
        this.context = context;
        this.linsenner=linsenner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //提前设置Dialog的一些样式
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);//设置dialog显示居中
        setContentView(R.layout.dialog_chose_browser);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失
        tv_away = (TextView) findViewById(R.id.tv_chose_away);
        tv_only = (TextView) findViewById(R.id.tv_chose_only);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        mListview = (ListView) findViewById(R.id.mlistview);

        initdata();
        mListview.setAdapter(adapter);
        tv_away.setOnClickListener(this);
        tv_only.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPosition(position);
                for (int i = 0; i < mlist.size(); i++) {
                    mlist.get(i).setSelected(0);
                }
                mlist.get(position).setSelected(1);
                adapter.notifyDataSetChanged();
            }
        });


    }


    private void initdata() {
        mlist = new ArrayList<BrowserInfo>();
        mlist.add(new BrowserInfo("浏览器", context.getResources().getDrawable(R.drawable.androidbrowser), "0", 1));
        mlist.add(new BrowserInfo("UC浏览器", context.getResources().getDrawable(R.drawable.ucbrowser), "1", 0));
        mlist.add(new BrowserInfo("QQ浏览器", context.getResources().getDrawable(R.drawable.qqbrowser), "2", 0));
        mlist.add(new BrowserInfo("谷歌浏览器", context.getResources().getDrawable(R.drawable.googlebrowser), "3", 0));
        mlist.add(new BrowserInfo("火狐浏览器", context.getResources().getDrawable(R.drawable.firefoxbrowser), "4", 0));
        adapter = new ChoseBrowserAdapter(context, mlist);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_chose_away:
                SharedPreferences browserinfo = context.getSharedPreferences("browser", 0);
                SharedPreferences.Editor editor = browserinfo.edit();
                editor.putString("browser", mlist.get(position).getState());
                editor.commit();
                linsenner.ChoseAway(position);

                break;
            case R.id.tv_chose_only:
                linsenner.ChoseOnly(position);
                break;
            case R.id.tv_cancel:
                dismiss();
                break;


        }


    }


    public interface OnSelectBrowserLinsenner {

        void ChoseAway(int position);

        void ChoseOnly(int position);


    }


}
