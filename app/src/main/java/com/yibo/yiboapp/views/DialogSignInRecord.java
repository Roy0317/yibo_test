package com.yibo.yiboapp.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.Meminfo;

import com.yibo.yiboapp.ui.SignInBean;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.loadmore.LoadMoreRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 签到记录
 */
public class DialogSignInRecord extends Dialog {

    private Context ctx;
    private View contentView;
    private TextView txtUserName;
    private TextView txtBalance;
    private TextView txtScore;
    private ImageView imageClose;
    private RecyclerView recyclerView;
    private SignInAdapter adapter;
    private LoadMoreRecyclerAdapter loadMoreAdapter;

    private List<SignInBean> beanList = new ArrayList<>();
    private Meminfo meminfo;

    public DialogSignInRecord(Context context) {
        super(context, R.style.DialogTheme);
        this.ctx = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        contentView = View.inflate(ctx, R.layout.layout_dialog_sign_in_record, null);
        setContentView(contentView);
        initViews();
        setListener();
        windowDeploy();
    }

    private void initViews() {
        txtUserName = findViewById(R.id.txtUserName);
        txtBalance = findViewById(R.id.txtBalance);
        txtScore = findViewById(R.id.txtScore);
        imageClose = findViewById(R.id.imageClose);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));

        adapter = new SignInAdapter(ctx, beanList);
        loadMoreAdapter = new LoadMoreRecyclerAdapter(adapter);
        recyclerView.setAdapter(loadMoreAdapter);

        if (meminfo != null) {
            txtUserName.setText(meminfo.getAccount());
            txtBalance.setText(Utils.getMoney(meminfo.getBalance(), true));
            txtScore.setText(meminfo.getScore() + "分");
        }

    }


    private void setListener() {
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return false;
            }
        });
        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void setMeminfo(Meminfo meminfo) {
        this.meminfo = meminfo;
    }

    public void setUserInfo(Meminfo meminfo) {
        if (meminfo != null) {
            txtUserName.setText(meminfo.getAccount());
            txtBalance.setText(Utils.getMoney(meminfo.getBalance(), true));
            txtScore.setText(meminfo.getScore() + "分");
        }
    }

    public void setBeanList(List<SignInBean> beanList) {
        if (beanList == null)
            return;
        this.beanList.clear();
        this.beanList.addAll(beanList);
        if (loadMoreAdapter != null)
            loadMoreAdapter.notifyDataSetChangedHF();
    }


    public void setOnItemClickLitener(LoadMoreRecyclerAdapter.OnItemClickListener onItemClickLitener) {
        loadMoreAdapter.setOnItemClickListener(onItemClickLitener);
    }


    /**
     * 设置窗口显示
     */
    public void windowDeploy() {
        Window window = getWindow();
        //出现动画
//        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        //设置显示位置
//        layoutParams.x = 0;
//        layoutParams.y = d.getHeight();
        //设置显示宽高
        layoutParams.height = d.getHeight() / 3 * 2;
        layoutParams.width = d.getWidth() / 6 * 5;
        window.setAttributes(layoutParams);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

    }
}
