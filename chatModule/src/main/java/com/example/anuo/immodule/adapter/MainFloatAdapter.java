package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.FloatBallBean;

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
 * Date  :09/10/2019
 * Desc  :com.example.anuo.immodule.adapter
 */
public class MainFloatAdapter extends RecyclerView.Adapter<MainFloatAdapter.FloatButtonHolder> {
    private Context context;
    private List<FloatBallBean> mData;
    private OnItemAitClickListener listener;

    public MainFloatAdapter(Context context, List<FloatBallBean> beanList) {
        this.context = context;
        this.mData = beanList;
    }

    public void setOnItemClickListencer(OnItemAitClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public FloatButtonHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FloatButtonHolder(LayoutInflater.from(context).inflate(R.layout.item_main_float, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FloatButtonHolder floatButtonHolder, int i) {
        final String msgBean = mData.get(i).getTitle();
        floatButtonHolder.imageView.setBackgroundResource(mData.get(i).getImg());
        floatButtonHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemAitClick(msgBean);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class FloatButtonHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public FloatButtonHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_float_item);
        }
    }


    public interface OnItemAitClickListener {
        void onItemAitClick(String item);
    }
}
