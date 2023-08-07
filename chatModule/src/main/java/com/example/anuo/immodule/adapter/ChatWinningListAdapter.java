package com.example.anuo.immodule.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ChatWinningListBean;
import com.example.anuo.immodule.utils.GlideUtils;

import java.util.List;

public class ChatWinningListAdapter extends BaseQuickAdapter<ChatWinningListBean.SourceBean.WinningListBean, BaseViewHolder> {


    public ChatWinningListAdapter(int layoutResId, @Nullable List<ChatWinningListBean.SourceBean.WinningListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatWinningListBean.SourceBean.WinningListBean item) {

        //资料头像
        GlideUtils.loadHeaderPic(mContext, item.getImageUrl(), (ImageView) helper.getView(R.id.iv_profile_photo));

        //xxx 在 xxx
        SpannableString spannableString = new SpannableString(item.getUserName() + " 在" + item.getPrizeProject());
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#3289ff"));
        spannableString.setSpan(foregroundColorSpan, 0, item.getUserName().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tv_name, spannableString);

        //喜中 ¥***
        SpannableString spannableString2 = new SpannableString("喜中 ¥" + item.getPrizeMoney());
        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.parseColor("#e04c51"));
        spannableString2.setSpan(foregroundColorSpan2, 2, 4 + String.valueOf(item.getPrizeMoney()).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tv_money, spannableString2);


        if (item.getSignalLogo() != 0) {
            GlideUtils.loadHeaderPic(mContext, item.getSignalLogo(), (ImageView) helper.getView(R.id.iv_image_logo));
        }


    }
}
