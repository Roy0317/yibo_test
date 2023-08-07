package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.event.CommonEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

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
 * Date  :01/08/2019
 * Desc  :com.yibo.yiboapp.adapter
 */
public class ChatLotteryChooseAdapter extends BaseRecyclerAdapter<LotteryData> {

//    private boolean isCircleSwitch
    private Context context ;

    public ChatLotteryChooseAdapter(Context ctx, List<LotteryData> list) {
        super(ctx, list);
//        isCircleSwitch = "on".equalsIgnoreCase(UsualMethod.getConfigFromJson(ctx).getGame_item_circle_little_badge_switch());
        this.context = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_chat_choose_lottery, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        LotteryData data = getList().get(position);
        ViewHolder vh = (ViewHolder) holder;
        vh.csl.setOnClickListener(v -> {
            if (!YiboPreference.instance(ctx).isLogin()) {
                UsualMethod.loginWhenSessionInvalid(ctx);
                return;
            }
            EventBus.getDefault().post(new CommonEvent(EventCons.CHOOSE_LOTTERY, data));

        });

//        UsualMethod.updateLocImage(ctx, vh.iv_img, data.getCode(), data.getLotteryIcon());

        if (!Utils.isEmptyString(data.getImgUrl())) {
            updateLocImage(vh.iv_img,data.getImgUrl());
        }else{
            String name = data.getCode();
            if (data.getCode().equalsIgnoreCase("ycp")) {
                name = "native_" + data.getCode();
            }
            String imgUrl = Urls.BASE_URL + Urls.PORT + "/native/resources/images/" + name + ".png";
            updateLocImage(vh.iv_img,imgUrl);
        }

        vh.tv_name.setText(data.getName());
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout csl;
        ImageView iv_img;
        ImageView iv_indictor;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            csl = itemView.findViewById(R.id.item_layout);
            iv_img = itemView.findViewById(R.id.img);
            iv_indictor = itemView.findViewById(R.id.indictor);
            tv_name = itemView.findViewById(R.id.name);
        }
    }



    private void updateLocImage(ImageView lotImageView,String imgUrl) {
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
}
