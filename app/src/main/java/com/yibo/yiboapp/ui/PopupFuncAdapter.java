package com.yibo.yiboapp.ui;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.FuncResult;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;

/**
 * Created by johnson on 2018/4/7.
 */


public class PopupFuncAdapter extends LAdapter<FuncResult> {

    public interface PopupCallback{
        void onItemClick(int pos, String title);
    }
    public void setPopupCallback(PopupCallback popupCallback) {
        this.popupCallback = popupCallback;
    }
    Context context;
    PopupCallback popupCallback;
    public PopupFuncAdapter(Context mContext, List<FuncResult> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
    }

    @Override public void convert(final int position, LViewHolder holder, ViewGroup parent, final FuncResult item) {

        final LinearLayout itemLayout = holder.getView(R.id.item);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupCallback != null) {
                    popupCallback.onItemClick(position, item.getTitle());
                }
            }
        });
        ImageView img = holder.getView(R.id.img);
        TextView titleTV = holder.getView(R.id.title);
        titleTV.setText(item.getTitle());
        if (!Utils.isEmptyString(item.getImgUrl())) {

            //todo 待测
            RequestOptions options = new RequestOptions().error(R.drawable.default_lottery)
                    .transform(new GlideCircleTransform(context));

            Glide.with(mContext)
                    .load(item.getImgUrl())
                    .apply(options)
                    .into(img);
//            UsualMethod.updateLocImageWithUrl(context, img, item.getImgUrl());
        }else{
            img.setBackgroundResource(item.getImgID());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.gridview_item_anim));
            }
        },30*(position+1));
    }

}