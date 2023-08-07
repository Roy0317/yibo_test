package com.example.anuo.immodule.adapter;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ChatLongDragonBean;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.CommonUtils;

import java.util.List;

import crazy_wrapper.Crazy.Utils.RequestUtils;
import crazy_wrapper.Crazy.Utils.Utils;

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
 * Date  :2020-04-28
 * Desc  :com.example.anuo.immodule.adapter
 */
public class ChatLongDragonAdapter extends BaseQuickAdapter<ChatLongDragonBean.SourceBean.LongDragonBean, BaseViewHolder> {

    public ChatLongDragonAdapter(int layoutResId, @Nullable List<ChatLongDragonBean.SourceBean.LongDragonBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatLongDragonBean.SourceBean.LongDragonBean item) {
        if (!TextUtils.isEmpty(item.getLotteryIcon())) {
            updateLocImage((ImageView) helper.getView(R.id.iv_lottery_icon), item.getLotteryIcon());
        } else {
            String name = item.getLotteryCode();
            if (name.equalsIgnoreCase("ycp")) {
                name = "native_" + name;
            }
            String imgUrl = ConfigCons.YUNJI_BASE_URL + ConfigCons.PORT + "/native/resources/images/" + name + ".png";
            updateLocImage((ImageView) helper.getView(R.id.iv_lottery_icon), imgUrl);
        }
        ((TextView) helper.getView(R.id.tv_lottery_name)).setText(item.getLotteryName());
        ((TextView) helper.getView(R.id.tv_lot_qihao)).setText(item.getLotteryNum());
        ((TextView) helper.getView(R.id.tv_lot_content)).setText(item.getLotteryResult());
        ((TextView) helper.getView(R.id.tv_lot_play)).setText(item.getPlayName());
        ((TextView) helper.getView(R.id.tv_lot_qishu)).setText(item.getContinueStage() + "期");
    }

    private void updateLocImage(ImageView lotImageView, String imgUrl) {
        //彩种的图地址是根据彩种编码号为姓名构成的
//        String imgUrl = Urls.BASE_URL +Urls.PORT + "/native/resources/images/" + lotCode + ".png";
//        Utils.LOG(TAG, "the pic url = " + imgUrl);
        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }

        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("Cookie", "SESSION=" + ChatSpUtils.instance(mContext).getToken())
                .addHeader("User-Agent", "android/" + CommonUtils.getVersionName(mContext))
                .addHeader("Native-Flag", "1");
        if (!Utils.isEmptyString(ConfigCons.YUNJI_BASE_HOST_URL) && imgUrl.contains(ConfigCons.YUNJI_BASE_URL)) {//本域名的图片下载，如果有HOST，则需要带上host
            builder.addHeader(RequestUtils.NATIVE_HOST, ConfigCons.YUNJI_BASE_HOST_URL);
        }
        GlideUrl glideUrl = new GlideUrl(imgUrl.trim(), builder.build());
        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_lottery)
                .error(R.drawable.default_lottery);
        Glide.with(mContext).load(glideUrl).
                apply(options)
                .into(lotImageView);
    }
}
