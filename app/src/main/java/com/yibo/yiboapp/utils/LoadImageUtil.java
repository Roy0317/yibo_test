package com.yibo.yiboapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;

/**
 * * 1.with(Context context) - 需要上下文，图片的加载会和 Activity/Fragment 的生命周期保持一致，
 * 例如：onPaused 时暂停加载，onResume 时又会自动重新加载。所以在传参的时候建议使用 Activity/Fragment 对象，而不是 Context。
 * <p>
 * 2.控制图片显示时的方式CenterCrop() 和 FitCenter()
 * CenterCrop() 方法是将图片按比例缩放到足矣填充 ImageView 的尺寸，但是图片可能会显示不完整；
 * 而 FitCenter() 则是图片缩放到小于等于 ImageView 的尺寸，这样图片是显示完整了，但是 ImageView 就可能不会填满了。
 * <p>
 * 3.默认有图片缓存，skipMemoryCache(true) 告诉 Glide 跳过内存缓存
 * <p>
 * 4.设置图片加载优先级  priority()  Priority.LOW   Priority.NORMAL   Priority.HIGH   Priority.IMMEDIAT
 */
public class LoadImageUtil {


    /**
     * 加载图片
     *
     * @param ctx
     * @param lotImageView
     * @param imgUrl
     */
    public static void loadPicImage(Context ctx, ImageView lotImageView, String imgUrl) {
        loadPicImage(ctx, lotImageView, imgUrl, false);
    }


    /**
     * 加载图片
     *
     * @param ctx
     * @param lotImageView
     * @param imgUrl
     */
    public static void loadPicImage(Context ctx, ImageView lotImageView, String imgUrl, int errorRes, OnLoadImageListener loadImageListener) {
        loadPicImage(ctx, lotImageView, imgUrl, errorRes, false, null, loadImageListener);
    }


    /**
     * @param ctx
     * @param lotImageView
     * @param imgUrl
     */
    public static void loadPicImage(Context ctx, ImageView lotImageView, String imgUrl, int errorRes) {
        loadPicImage(ctx, lotImageView, imgUrl, errorRes, false);
    }


    /**
     * @param ctx
     * @param lotImageView
     * @param imgUrl
     * @param skipMemoryCache 默认false 有图片缓存  true 跳过图片缓存
     */
    public static void loadPicImage(Context ctx, ImageView lotImageView, String imgUrl, boolean skipMemoryCache) {
        loadPicImage(ctx, lotImageView, imgUrl, R.drawable.default_placeholder_picture, skipMemoryCache, null, null);
    }


    /**
     * @param ctx
     * @param lotImageView
     * @param imgUrl
     * @param skipMemoryCache 默认false 有图片缓存  true 跳过图片缓存
     */
    public static void loadPicImage(Context ctx, ImageView lotImageView, String imgUrl, int errorRes, boolean skipMemoryCache) {
        loadPicImage(ctx, lotImageView, imgUrl, errorRes, skipMemoryCache, null, null);
    }


    /**
     * 加载圆形头像
     *
     * @param ctx
     * @param imageView
     * @param imgUrl
     */
    public static void loadCircleHeaderImage(final Context ctx, final ImageView imageView, String imgUrl, final OnLoadImageListener loadImageListener) {
        //singature 做标识，否则请求会缓存不能正常请求出图片
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        //跳过磁盘缓存
        requestOptions.placeholder(R.drawable.icon_default_header)//加载成功之前占位图
                .error(R.drawable.icon_default_header)//加载错误之后的错误图
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(ctx).load(getGlideUrl(ctx, imgUrl))
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade()) // 动画渐变加载
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (loadImageListener != null)
                            loadImageListener.onLoadFailed(e, model, target, isFirstResource);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (loadImageListener != null)
                            loadImageListener.onResourceReady(resource, model, target, dataSource, isFirstResource);
                        return false;
                    }
                })
                .into(imageView);
    }


    /**
     * 加载欢迎图片
     *
     * @param ctx
     * @param imageView
     * @param imgId
     */
    public static void loadWlecomeImage(Context ctx, ImageView imageView, int imgId) {
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter();
        Glide.with(ctx).load(imgId)
                .apply(requestOptions)
                .into(imageView);
    }


    /**
     * @param ctx
     * @param imageView
     * @param imgUrl
     * @param errRes
     */
    public static void loadActiveImage(Context ctx, final ImageView imageView, String imgUrl, final int errRes) {
        loadActiveImage(ctx, imageView, imgUrl, errRes, null);
    }


    /**
     * 加载优惠活动图片，因为存在图片尺寸大小和默认尺寸不一样，不能开启动画渐变，否则加载时显示的图片不会消失
     *
     * @param ctx
     * @param imageView
     * @param imgUrl
     * @param errRes    -1不设置图片
     */
    public static void loadActiveImage(Context ctx, final ImageView imageView, String imgUrl, final int errRes,
                                       final OnLoadImageListener loadImageListener) {
        if (TextUtils.isEmpty(imgUrl) || ctx == null)
            return;

        if (ctx instanceof Activity) {
            if (((Activity) ctx).isFinishing())
                return;
        }

        Glide.with(ctx).load(getGlideUrl(ctx, imgUrl)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {


                if (loadImageListener != null)
                    loadImageListener.onResourceReady(resource, null, null, null, false);
                else {
                    try {
                        imageView.setImageDrawable(resource);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if (loadImageListener != null) {
                    loadImageListener.onLoadFailed(null, null, null, false);
                } else if (errRes != -1)
                    imageView.setImageResource(errRes);

            }
        });
    }

    /**
     * 给控件加载背景图片
     * @param context
     * @param view
     * @param imgUrl
     */
    public static void loadBackGroundImage(Context context, final View view ,String imgUrl){
        Glide.with(context).load(getGlideUrl(context, imgUrl)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                try {
                    view.setBackground(resource);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * 加载图片
     *
     * @param ctx
     * @param lotImageView
     * @param imgUrl
     * @param errorRes        加载失败显示的图片
     * @param skipMemoryCache 默认false 有图片缓存  true 跳过图片缓存
     */
    public static void loadPicImage(Context ctx, ImageView lotImageView, String imgUrl, int errorRes, boolean skipMemoryCache,
                                    RequestOptions requestOptions, final OnLoadImageListener loadImageListener) {
//        LogUtils.i("loadPicImage", "the pic url = " + imgUrl);


        if (TextUtils.isEmpty(imgUrl) || ctx == null)
            return;

        if (ctx instanceof Activity) {
            if (((Activity) ctx).isFinishing())
                return;
        }

        RequestOptions options;
        if (requestOptions == null) {
            options = new RequestOptions()
                    .placeholder(errorRes)//加载成功之前占位图
                    .error(errorRes)//加载错误之后的错误图
//                .override(400,400)//指定图片的尺寸
                    //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//                .fitCenter()
                    //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//                .centerCrop()
//                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                    .skipMemoryCache(skipMemoryCache)//跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//只缓存最终的图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
            ;
        } else {
            options = requestOptions;
        }

        Glide.with(ctx).load(getGlideUrl(ctx, imgUrl))
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade()) // 动画渐变加载
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (loadImageListener != null) {
                            loadImageListener.onLoadFailed(e, model, target, isFirstResource);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (loadImageListener != null)
                            loadImageListener.onResourceReady(resource, model, target, dataSource, isFirstResource);
                        return false;
                    }
                })
                .into(lotImageView);
    }


    private static GlideUrl getGlideUrl(Context ctx, String imgUrl) {
        GlideUrl glideUrl = UsualMethod.getGlide(ctx, imgUrl);
        return glideUrl;
    }


    private static String checkUrl(String imgUrl) {
        String checkUrl = imgUrl.trim();
        if (imgUrl.contains("http")) {
            checkUrl = imgUrl.substring(imgUrl.indexOf("http"), imgUrl.length());
            return checkUrl;
        }

        return checkUrl;
    }


    public static abstract class OnLoadImageListener {
        //加载完成
        public void onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

        }

        //加载图片失败
        public void onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

        }
    }


}
