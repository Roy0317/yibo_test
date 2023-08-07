package com.yibo.yiboapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.ActiveDetailActivity;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.ActivesResultWraper;
import com.yibo.yiboapp.entify.MessageResultWrapper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import java.util.List;


public class RecordAdapter extends LAdapter<ActivesResultWraper.ContentBean> {

    Context context;
    boolean showTitle;
    boolean showTime;

    public RecordAdapter(Context mContext, List<ActivesResultWraper.ContentBean> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        SysConfig config = UsualMethod.getConfigFromJson(context);
        if (config != null) {
            String active_title_switch = config.getActive_title_switch();
            if (!Utils.isEmptyString(active_title_switch) && active_title_switch.equalsIgnoreCase("on")) {
                showTitle = true;
            }
            String active_time_switch = config.getSwitch_active_deadline_time();
            if (!Utils.isEmptyString(active_time_switch) && active_time_switch.equalsIgnoreCase("on")) {
                showTime = true;
            }
        }
    }

    @Override
    public void convert(final int position, LViewHolder holder, ViewGroup parent, final ActivesResultWraper.ContentBean item) {
        LinearLayout topLayout = holder.getView(R.id.top);
        TextView titleTV = holder.getView(R.id.title);
        TextView timeTV = holder.getView(R.id.time);
        final SimpleDraweeView picture = holder.getView(R.id.picture);
        TextView contentTV = holder.getView(R.id.content);

        if (item.isShowContent()) {
            contentTV.setVisibility(View.VISIBLE);
        } else {
            contentTV.setVisibility(View.GONE);
        }

        RelativeLayout itemLayout = holder.getView(R.id.item);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 设置为已读
                 * 2021.04.08 确认接口失效,故停用下方代码
                 * */
//                if (item.getReadFlag() == 0) {
//                    syncRead(item.getId());
//                }

                //打开优惠活动
                ActiveDetailActivity.createIntent(context, item.getContent(), item.getTitle(), true);
            }
        });
        if (!showTime && !showTitle) {
            topLayout.setVisibility(View.GONE);
        }
        if (showTitle) {
            titleTV.setVisibility(View.VISIBLE);
            titleTV.setText(!Utils.isEmptyString(item.getTitle()) ? item.getTitle() : "暂无标题");
        } else {
            titleTV.setVisibility(View.GONE);
        }
        if (showTime) {
            timeTV.setVisibility(View.VISIBLE);
            String endTime = !Utils.isEmptyString(Utils.formatTime(item.getOverTime()))
                    ? Utils.formatTime(item.getOverTime(), "yyyy-MM-dd") : "暂无时间";
            timeTV.setText("截止时间:" + endTime);
        } else {
            timeTV.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(item.getTitleImgUrl().trim())) {
            picture.setVisibility(View.GONE);
        } else {
            picture.setVisibility(View.VISIBLE);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(item.getTitleImgUrl().trim()))
                    .setAutoPlayAnimations(true)//设置为true将循环播放Gif动画
                    .setControllerListener(new ControllerListener<ImageInfo>() {
                        @Override
                        public void onSubmit(String id, Object callerContext) {

                        }

                        @Override
                        public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo, @javax.annotation.Nullable Animatable animatable) {
                            adjustSdv(picture, imageInfo.getWidth(), imageInfo.getHeight());
                        }

                        @Override
                        public void onIntermediateImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo) {

                        }

                        @Override
                        public void onIntermediateImageFailed(String id, Throwable throwable) {

                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {

                        }

                        @Override
                        public void onRelease(String id) {

                        }
                    })
                    .build();
            picture.setController(controller);
        }

    }

    /**
     * 置已读
     */
    private void syncRead(long id) {
        ApiParams params = new ApiParams();
        params.put("id", id);
        HttpUtil.get(context, Urls.SET_ACTIVE_READ_URL, params, false, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result == null) {
                    Toast.makeText(context, R.string.setread_fail, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!result.isSuccess()) {
                    Toast.makeText(context, R.string.setread_fail, Toast.LENGTH_LONG).show();
                    return;
                }
                MessageResultWrapper reg = new Gson().fromJson(result.getContent(), MessageResultWrapper.class);
                if (!reg.isSuccess()) {
                    showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                            context.getString(R.string.setread_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (reg.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(context);
                    }
                    return;
                }
                YiboPreference.instance(context).setToken(reg.getAccessToken());
            }
        });

    }

    private void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    private void adjustSdv(SimpleDraweeView image, int width, int height) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image.getLayoutParams();
        params.width = Utils.screenInfo(context).widthPixels;
        params.height = (int) ((float) height / width * Utils.screenInfo(context).widthPixels);
        image.setLayoutParams(params);
    }

}