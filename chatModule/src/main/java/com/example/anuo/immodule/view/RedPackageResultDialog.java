package com.example.anuo.immodule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.adapter.RedPackageResultAdapter;
import com.example.anuo.immodule.bean.RedPackageDetailBean;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.GlideUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :29/09/2019
 * Desc  :com.example.anuo.immodule.view
 */
public class RedPackageResultDialog extends AlertDialog {
    private final Context context;
    private final int width;
    private final View view;
    private ImageView iv_hb_close;
    private CircleImageView iv_hb_user_img;
    private TextView tv_hb_user_name;
    private TextView tv_hb_content;
    private TextView tv_hb_charge;
    private TextView tv_hb_detail;
    private RecyclerView rcy_hb_result;
    private List<RedPackageDetailBean.SourceBean.OtherBean> datas = new ArrayList<>();
    private RedPackageResultAdapter resultAdapter;

    private ChatSysConfig chatSysConfig;
    private boolean grabed = false;

    public RedPackageResultDialog(@NonNull Context context, boolean cancelable, @Nullable boolean canceledOnTouchOutside) {
        super(context, R.style.Dialog_Common);
        this.context = context;
        double deviceWidth = getScreenWidth(this.context);
        width = (int) (deviceWidth * 0.7);
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        view = inflater.inflate(R.layout.red_package_result_dialog, null);
        chatSysConfig = ChatSpUtils.instance(context).getChatSysConfig();
        initView();
    }

    private void initView() {
        iv_hb_close = view.findViewById(R.id.iv_hb_close);
        iv_hb_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        iv_hb_user_img = view.findViewById(R.id.iv_hb_user_img);
        tv_hb_user_name = view.findViewById(R.id.tv_hb_user_name);
        tv_hb_content = view.findViewById(R.id.tv_hb_content);
        tv_hb_charge = view.findViewById(R.id.tv_hb_charge);
        tv_hb_detail = view.findViewById(R.id.tv_hb_detail);
        if (chatSysConfig.getSwitch_red_info().equals("1")) {
            rcy_hb_result = view.findViewById(R.id.rcy_hb_result);
            resultAdapter = new RedPackageResultAdapter(context, datas);
            rcy_hb_result.setLayoutManager(new LinearLayoutManager(context));
            rcy_hb_result.setAdapter(resultAdapter);
        } else {
            if (rcy_hb_result != null) {
                rcy_hb_result.setVisibility(View.GONE);
            }
        }

        if (chatSysConfig.getSwitch_red_bag_remark_show().equals("0")) {
            tv_hb_content.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams
                .WRAP_CONTENT, 0);
        setContentView(view, params);
    }

    public void setGrabMoney(String grabMoney) {
        String htmlStr = "已存入余额<br><big><big>" + grabMoney + "</big></big>元";
        tv_hb_charge.setText(Html.fromHtml(htmlStr));
    }

    public void setGrabed() {
        grabed = true;
    }

    public void setGrabOut() {
        grabed = false;
        tv_hb_charge.setText("红包已经抢完了！");
    }

    public void setRedPackageDetail(RedPackageDetailBean detailBean) {
        datas.clear();
        datas.addAll(detailBean.getSource().getOther());
        RedPackageDetailBean.SourceBean.CBean cBean = detailBean.getSource().getC();
        if (grabed) {
            String htmlStr = "已抢过此红包!<br><big><big>" + cBean.getMoney() + "</big></big>元";
            tv_hb_charge.setText(Html.fromHtml(htmlStr));
        }
        if (!TextUtils.isEmpty(cBean.getAvatar())) {
            GlideUtils.loadHeaderPic(context, cBean.getAvatar(), iv_hb_user_img);
        }
        tv_hb_user_name.setText(TextUtils.isEmpty(cBean.getNickName()) ? cBean.getAccount() : cBean.getNickName());
        RedPackageDetailBean.SourceBean.ParentBean parentBean = detailBean.getSource().getParent();
        tv_hb_content.setText(parentBean.getRemark());
        float countMoney = Float.parseFloat(parentBean.getCountMoney());
        float balanceMoney = Float.parseFloat(parentBean.getBalance());
        float remainMoney = (float) Math.round((countMoney - balanceMoney) * 100) / 100;
        if (chatSysConfig.getSwitch_red_info().equals("1")) {
            tv_hb_detail.setText("已领取" + parentBean.getHasNum() + "/" + parentBean.getCountNum() + "个,已抢"
                    + remainMoney + "元");
            resultAdapter.notifyDataSetChanged();
        }
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
}
