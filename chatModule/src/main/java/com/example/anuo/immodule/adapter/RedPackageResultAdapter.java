package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.RedPackageDetailBean;
import com.example.anuo.immodule.utils.GlideUtils;
import com.example.anuo.immodule.view.AlwaysMarqueeTextView;

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
 * Date  :01/10/2019
 * Desc  :com.example.anuo.immodule.adapter
 */
public class RedPackageResultAdapter extends RecyclerView.Adapter {
    private List<RedPackageDetailBean.SourceBean.OtherBean> datas;
    private Context context;

    public RedPackageResultAdapter(Context context, List<RedPackageDetailBean.SourceBean.OtherBean> datas) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RedPackageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_red_package_result, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RedPackageDetailBean.SourceBean.OtherBean otherBean = datas.get(i);
        ((RedPackageViewHolder) viewHolder).tv_user_name.setText(TextUtils.isEmpty(otherBean.getNickName()) ?
                otherBean.getNativeAccount() : otherBean.getNickName());
        ((RedPackageViewHolder) viewHolder).tv_user_money.setText(otherBean.getMoney());
        if (!TextUtils.isEmpty(otherBean.getAvatar())) {
            GlideUtils.loadHeaderPic(context, otherBean.getAvatar(), ((RedPackageViewHolder) viewHolder).iv_user_img);
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    private class RedPackageViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_user_img;
        AlwaysMarqueeTextView tv_user_name;
        TextView tv_user_money;

        public RedPackageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_user_img = itemView.findViewById(R.id.iv_hb_result_img);
            tv_user_name = itemView.findViewById(R.id.tv_hb_result_name);
            tv_user_money = itemView.findViewById(R.id.tv_hb_result_money);
        }
    }
}
