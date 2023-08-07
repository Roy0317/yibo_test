package com.example.anuo.immodule.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.ImageSize;

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
 * Date  :2019/6/6
 * Desc  :com.example.anuo.immodule.utils
 */
public class GlideUtils {
    public static void loadChatImage(final Context mContext, final String imgUrl, final ImageView imageView) {


        final RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.default_img_failed)// 正在加载中的图片
                .error(R.mipmap.default_img_failed)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); // 加载失败的图片

        Glide.with(mContext)
                .load(imgUrl) // 图片地址
                .apply(options)
                .into(new SimpleTarget<Drawable>(150, 150) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ImageSize imageSize = BitmapUtil.getImageSize(drawableToBitmap(resource));
                        RelativeLayout.LayoutParams imageLP = (RelativeLayout.LayoutParams) (imageView.getLayoutParams());
                        imageLP.width = imageSize.getWidth();
                        imageLP.height = imageSize.getHeight();
                        imageView.setLayoutParams(imageLP);

                        Glide.with(mContext)
                                .load(imgUrl)
                                .apply(options) // 参数
                                .into(imageView);
                    }
                });
    }

    public static void loadHeaderPic(Context context, String imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_default_header)
                .error(R.drawable.ic_default_header);
        Glide.with(context)
                .load(imgUrl)
                .apply(options)
                .into(imageView);
    }

    public static void loadHeaderPic(Context context, int imgUrl, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_default_header)
                .error(R.drawable.ic_default_header);
        Glide.with(context)
                .load(imgUrl)
                .apply(options)
                .into(imageView);
    }

    public static void loadBackgroundImage(Context context, String imgUrl, final View view) {
        // 加载失败的图片
        Glide.with(context)
                .load(imgUrl)
                .into(new SimpleTarget<Drawable>(500, 800) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
    }

    public static void loadChatImage(Context context, ImageView imageView, String url, int default_lottery) {
        RequestOptions options = new RequestOptions();
        options.placeholder(default_lottery)
                .error(default_lottery);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void loadLevelImage(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    // drawable 转换成bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }
}
