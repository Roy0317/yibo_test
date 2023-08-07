package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.MiningDialogAdapter;
import com.yibo.yiboapp.entify.WakuangListBean;

import java.util.List;


public class MiningDialog extends Dialog {


    private Context context;//上下文
    private MiningDialogAdapter adapter;
    private List<WakuangListBean> listBeans;


    public MiningDialog(Context context, List<WakuangListBean> listBeans) {
        super(context, R.style.ChoseBrowserDialog);//加载dialog的样式
        this.context = context;
        this.listBeans = listBeans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //提前设置Dialog的一些样式
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);//设置dialog显示居中
        setContentView(R.layout.mining_history_dialog);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 5/ 6;
        lp.height = display.getHeight() * 3 / 5;
        getWindow().setAttributes(lp);
        RecyclerView recyclerView = findViewById(R.id.rcv);
        ImageView iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(v -> dismiss());
        View header = View.inflate(context, R.layout.mining_history_dialog_header, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MiningDialogAdapter(R.layout.mining_history_dialog_item);
        if (listBeans != null && listBeans.size() != 0) {
            adapter.addData(listBeans);
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        adapter.addHeaderView(header);
        TextView textView = new TextView(context);
        textView.setText("暂无数据");
        textView.setGravity(Gravity.CENTER);
        adapter.setEmptyView(textView);
        recyclerView.setAdapter(adapter);


    }


}
