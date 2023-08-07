package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;

/**
 * Created by johnson on 2017/11/11.
 */

public class GameAdapter extends LAdapter<LotteryData> {

    Context context;
    private static final String TAG = GameAdapter.class.getSimpleName();
    GameEventDelegate delegate;
    private final SysConfig sysConfig;

    public GameAdapter(Context mContext, List<LotteryData> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        sysConfig = UsualMethod.getConfigFromJson(mContext);
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent,
                        final LotteryData item) {
        RelativeLayout view = holder.getView(R.id.item);
        final ImageView ivThumb = holder.getView(R.id.img);
        final TextView tvName = holder.getView(R.id.name);
        final ImageView ivDynamic = holder.getView(R.id.ic_dynamic);

        if (sysConfig.getOnoff_show_mode().equals("on")) {
            tvName.setVisibility(View.VISIBLE);
        } else {
            if (item.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
                tvName.setVisibility(View.GONE);
            } else {
                tvName.setVisibility(View.VISIBLE);
            }
        }

        if (!Utils.isEmptyString(item.getName()) && item.getName().equals("---")) {
            tvName.setText("暂无名称");
        } else {
            tvName.setText(item.getName());
        }

        if (!Utils.isEmptyString(item.getDynamicIcon())){
            // 存在动画效果
            Glide.with(context).asGif().load(item.getDynamicIcon())
                    .into(ivDynamic);
        }else{
            ivDynamic.setImageBitmap(null);
        }

        if (item.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
            if (!Utils.isEmptyString(item.getImgUrl())) {
                updateLocImage(ivThumb, item.getImgUrl());
            } else {
                String name = item.getCode();
                String imgUrl = "";
                // 判断图文显示开关是否打开
                if (sysConfig.getOnoff_show_mode().equals("on")) {
                    // 图文显示开关打开，加载不带彩种名称的图片
                    if (item.getCode().equalsIgnoreCase("ycp")) {
                        name = "native_" + item.getCode();
                    }
                    imgUrl = Urls.BASE_URL + Urls.PORT + "/native/resources/images/" + name + ".png";
                } else {
                    //图文显示开关关闭的，加载带有彩种名称的图片
                    imgUrl = Urls.BASE_URL + Urls.PORT + "/common/lotImg/" + name + ".png";
                }
                updateLocImage(ivThumb, imgUrl);
            }
        } else {
            if (!Utils.isEmptyString(item.getImgUrl())) {
                String imgUrl = Urls.BASE_URL + Urls.PORT + item.getImgUrl();
                updateLocImage(ivThumb, imgUrl);
            } else {
                if (item.getModuleCode() == LotteryData.DIANZI_MODULE) {
                    ivThumb.setBackgroundResource(R.drawable.icon_game);
                } else if (item.getModuleCode() == LotteryData.REALMAN_MODULE) {
                    ivThumb.setBackgroundResource(R.drawable.icon_real);
                } else if (item.getModuleCode() == LotteryData.SPORT_MODULE) {
                    ivThumb.setBackgroundResource(R.drawable.icon_ft);
                }
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate == null) {
                    return;
                }
                String code = "";
                if (item.getModuleCode() == LotteryData.CAIPIAO_MODULE) {
                    code = item.getCode();
                } else {
                    code = item.getCzCode();
                }
                delegate.onGameEvent(code, item.getModuleCode(), item.getName(), item);
            }
        });
    }

    private void updateLocImage(ImageView lotImageView, String imgUrl) {
        //彩种的图地址是根据彩种编码号为姓名构成的
//        String imgUrl = Urls.BASE_URL +Urls.PORT + "/native/resources/images/" + lotCode + ".png";
//        Utils.LOG(TAG, "the pic url = " + imgUrl);
        if (Utils.isEmptyString(imgUrl)) {
            return;
        }
        GlideUrl glideUrl = UsualMethod.getGlide(context, imgUrl);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_lottery)
                .error(R.drawable.default_lottery);
        Glide.with(context).load(glideUrl).
                apply(options)
                .into(lotImageView);
    }

    public interface GameEventDelegate {
        /**
         * 列表游戏项点击事件
         *
         * @param gameCode  游戏code
         * @param gameModue 游戏分类，彩票，真人，电子，体育
         */
        //void onGameEvent(String gameCode, int gameModue);

        void onGameEvent(String gameCode, int gameModue, String gameName, LotteryData data);
    }

    public void setDelegate(GameEventDelegate delegate) {
        this.delegate = delegate;
    }
}
