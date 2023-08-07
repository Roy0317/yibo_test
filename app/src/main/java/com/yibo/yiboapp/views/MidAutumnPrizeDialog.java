package com.yibo.yiboapp.views;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.MidAutumnMyPrizeAdapter;
import com.yibo.yiboapp.adapter.MidAutumnPrizeAdapter;
import com.yibo.yiboapp.entify.FakeBean;
import com.yibo.yiboapp.entify.MyPrizeDataBean;

import java.util.List;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: game
 * @package: com.yibo.yiboapp.custom
 * @description: ${DESP}
 * @date: 2019/9/12
 * @time: 2:04 PM
 */
public class MidAutumnPrizeDialog extends Dialog {


    public static final int MY_PRIZE = 0;
    public static final int CHAMPION_LIST = 1;

    private int code;

    private View view;
    private RecyclerView recyclerView;
    private MidAutumnPrizeAdapter midAutumnPrizeAdapter;
    private MidAutumnMyPrizeAdapter midAutumnMyPrizeAdapter;
    private List<FakeBean> rows;
    private List<MyPrizeDataBean> myPrizeDataBeans;

    private String[] titles;

    public MidAutumnPrizeDialog(@NonNull Context context, List<MyPrizeDataBean> myPrizeDataBeans, String[] titles, int code, int dif) {
        super(context);
        this.myPrizeDataBeans = myPrizeDataBeans;
        this.titles = titles;
        this.code = code;
        initView(context);
    }


    public MidAutumnPrizeDialog(@NonNull Context context, List<FakeBean> rows, String[] titles, int code) {
        super(context);
        this.rows = rows;
        this.titles = titles;
        this.code = code;
        initView(context);
    }

    public MidAutumnPrizeDialog(@NonNull Context context, int themeResId) {
        super(context);
        initView(context,themeResId);

    }

    protected MidAutumnPrizeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }


    private void initView(Context context) {
        view = View.inflate(context, R.layout.dialog_mid_autumn_prize, null);
        Button btn=view.findViewById(R.id.btn_title);
        TextView tv1 = view.findViewById(R.id.tv_order);
        TextView tv2 = view.findViewById(R.id.tv_name);
        TextView tv3 = view.findViewById(R.id.tv_official_rank);
        TextView tv4 = view.findViewById(R.id.tv_times);

        TextView tv11 = view.findViewById(R.id.tv_order1);
        TextView tv22 = view.findViewById(R.id.tv_name1);
        TextView tv33 = view.findViewById(R.id.tv_official_rank1);
        TextView tv44 = view.findViewById(R.id.tv_times1);



        LinearLayout ll_title = view.findViewById(R.id.ll_title);
        LinearLayout ll_title1 = view.findViewById(R.id.ll_title1);
        Button btn_confirm = view.findViewById(R.id.btn_confirm);
        recyclerView = view.findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if (code==MY_PRIZE){
            ll_title1.setVisibility(View.VISIBLE);
            ll_title.setVisibility(View.GONE);
            tv11.setText(titles[0]);
            tv22.setText(titles[1]);
            tv33.setText(titles[2]);
            tv44.setText(titles[3]);
            btn.setText("我的奖品");
            midAutumnMyPrizeAdapter  = new MidAutumnMyPrizeAdapter(context,myPrizeDataBeans, R.layout.item_mid_autumn_my_prize);
            midAutumnMyPrizeAdapter.addAll(myPrizeDataBeans);
            recyclerView.setAdapter(midAutumnMyPrizeAdapter);
        }else{
            ll_title.setVisibility(View.VISIBLE);
            ll_title1.setVisibility(View.GONE);
            tv1.setText(titles[0]);
            tv2.setText(titles[1]);
            tv3.setText(titles[2]);
            tv4.setText(titles[3]);
            midAutumnPrizeAdapter = new MidAutumnPrizeAdapter(context, rows, R.layout.item_mid_autumn_prize);
            midAutumnPrizeAdapter.addAll(rows);
            recyclerView.setAdapter(midAutumnPrizeAdapter);
        }


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    private void initView(Context context, int layout){
        view = View.inflate(context, layout, null);
        LinearLayout ll_title = view.findViewById(R.id.ll_title);
        Button btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public MidAutumnPrizeDialog initDialog() {
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        layoutParams.height = d.getHeight() / 4 * 3;
        layoutParams.width = d.getWidth() / 5 * 4;
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        return this;
    }


}
