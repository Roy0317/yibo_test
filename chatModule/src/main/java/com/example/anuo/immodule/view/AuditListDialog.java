package com.example.anuo.immodule.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.adapter.AuditListAdapter;
import com.example.anuo.immodule.bean.GetAuditListBean;

import java.lang.reflect.Method;
import java.util.List;

public class AuditListDialog extends AlertDialog {

    private final int width;
    private final int height;
    private RecyclerView recyclerView;
    private TextView emptyView;

    private List<GetAuditListBean.UserAuditItem> auditItems;
    private AuditListAdapter.AuditActionListener actionListener;
    private AuditListAdapter adapter;

    public void setAuditItems(List<GetAuditListBean.UserAuditItem> auditItems) {
        emptyView.setVisibility(View.GONE);
        this.auditItems.clear();
        this.auditItems.addAll(auditItems);
        adapter.notifyDataSetChanged();
    }

    public AuditListDialog(Context context, List<GetAuditListBean.UserAuditItem> auditItems, AuditListAdapter.AuditActionListener l){
        super(context, R.style.Dialog_Common);
        this.auditItems = auditItems;
        this.actionListener = l;
        width = (int) (getScreenWidth(context) * 0.9);
        height = (int) (getScreenHeight(context) * 0.7);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_audit_list, null);
        initView(view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height, 0);
        setContentView(view, params);

        view.findViewById(R.id.image_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dismiss();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.tv_empty);
        if (auditItems.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
        }
        DividerItemDecoration dec = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.drawable_divider_item));
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new AuditListAdapter(auditItems);
        adapter.setActionListener(actionListener);
        recyclerView.setAdapter(adapter);
    }

    private int getScreenWidth(Context context) {
        return getScreenSize(context)[0];
    }

    private int getScreenHeight(Context context) {
        return getScreenSize(context)[1];
    }

    @SuppressLint("WrongConstant")
    private int[] getScreenSize(Context context) {
        WindowManager windowManager;
        try {
            windowManager = (WindowManager) context.getSystemService("window");
        } catch (Throwable var6) {
            windowManager = null;
        }

        if (windowManager == null) {
            return new int[]{0, 0};
        } else {
            Display display = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT < 13) {
                DisplayMetrics t1 = new DisplayMetrics();
                display.getMetrics(t1);
                return new int[]{t1.widthPixels, t1.heightPixels};
            } else {
                try {
                    Point t = new Point();
                    Method method = display.getClass().getMethod("getRealSize", new Class[]{Point.class});
                    method.setAccessible(true);
                    method.invoke(display, new Object[]{t});
                    return new int[]{t.x, t.y};
                } catch (Throwable var5) {
                    return new int[]{0, 0};
                }
            }
        }
    }

    public void showEmpty() {
        auditItems.clear();
        adapter.notifyDataSetChanged();
        emptyView.setVisibility(View.VISIBLE);
    }
}
