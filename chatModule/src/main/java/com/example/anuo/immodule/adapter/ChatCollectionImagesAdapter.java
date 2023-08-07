package com.example.anuo.immodule.adapter;

import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ChatCollectionImagesSelectBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.GlideUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.ScreenUtil;

import java.util.ArrayList;

public class ChatCollectionImagesAdapter extends BaseQuickAdapter<ChatCollectionImagesSelectBean, BaseViewHolder> {


    public ChatCollectionImagesAdapter(int layoutResId, @Nullable ArrayList<ChatCollectionImagesSelectBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ChatCollectionImagesSelectBean item) {


        final StringBuilder urls = new StringBuilder();
        urls.append(ConfigCons.CHAT_FILE_BASE_URL).append(ConfigCons.PORT_CHAT).append(ConfigCons.READ_FILE_WAP);
//        urls.append("?fileId=" + item.getRecord());
//        urls.append("&contentType=image/jpeg");
        urls.append("/" + item.getRecord());
        LogUtils.e("CollectionImageUri:", urls.toString());
        RelativeLayout relativeLayout = helper.getView(R.id.rl_image_bg);
        int screenWidth = ScreenUtil.getScreenWidth(mContext);
        ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        layoutParams.height = screenWidth / 3 - 10;
        relativeLayout.setLayoutParams(layoutParams);
        Drawable drawableSelect = mContext.getResources().getDrawable(R.drawable.image_selected);
        Drawable drawableUnSelect = mContext.getResources().getDrawable(R.drawable.image_unselected);
        helper.getView(R.id.iv_select_bg).setBackground(item.isSelect() ? drawableSelect : drawableUnSelect);
        GlideUtils.loadChatImage(mContext, (ImageView) helper.getView(R.id.iv_collection_image), urls.toString(), R.mipmap.default_img_failed);


    }


}
