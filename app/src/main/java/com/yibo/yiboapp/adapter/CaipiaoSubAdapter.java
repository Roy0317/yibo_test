package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;


public class CaipiaoSubAdapter extends LAdapter<LotteryData> {

    Context context;
    private static final String TAG = GameListAdapter.class.getSimpleName();
    UsualMethod.ChannelListener channelListener;

    public CaipiaoSubAdapter(Context mContext, List<LotteryData> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
    }

    @Override public void convert(int position, LViewHolder holder, ViewGroup parent, final LotteryData item) {
        LinearLayout view = holder.getView(R.id.item);
        final ImageView ivThumb = holder.getView(R.id.img);
        updateLocImage(ivThumb,item.getCode());
        final TextView tvName = holder.getView(R.id.name);
        if (item.getName().equals("---")) {
            tvName.setText("正在获取...");
        }else{
            tvName.setText(item.getName());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channelListener != null) {
                    channelListener.onCaiPiaoItemClick(item.getCode());
                }
            }
        });
    }

    private void updateLocImage(ImageView lotImageView,String lotCode) {
        //彩种的图地址是根据彩种编码号为姓名构成的
        String imgUrl = Urls.BASE_URL +Urls.PORT + "/native/resources/images/" + lotCode + ".png";
        GlideUrl glideUrl = UsualMethod.getGlide(context, imgUrl);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_lottery)
                .error(R.drawable.default_lottery);
        Glide.with(context).load(glideUrl)
                .apply(options)
                .into(lotImageView);
    }

    public void setChannelListener(UsualMethod.ChannelListener channelListener) {
        this.channelListener = channelListener;
    }
}
