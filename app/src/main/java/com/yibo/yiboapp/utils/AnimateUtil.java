package com.yibo.yiboapp.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;


public class AnimateUtil {


    /**
     * 筛选控件显示隐藏动画
     *
     * @param alpha
     * @param toY
     */
    public static void layoutAnimate(View tranView, final View alphaView, int alpha, int toY, long durationMillis, final int visible) {

        AnimateUtil.setTarnsAni(tranView, toY, 300);
        AnimateUtil.setAlphaAni(alphaView, alpha, durationMillis, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                alphaView.setVisibility(visible);
            }
        });
    }


    /**
     * 设置位移动画 Y 轴方向移动
     *
     * @param view
     * @param toY
     * @param durationMillis
     */
    public static void setTarnsAni(View view, int toY, long durationMillis) {
        view.animate().translationY(toY).setDuration(durationMillis).start();
    }


    /**
     * 设置位移动画效果，来回移动
     */
    public static void setTransAni(View view, int x0, int x1, int y0, int y1, long durationMillis) {
        TranslateAnimation transAni = new TranslateAnimation(x0, x1, y0, y1);
        transAni.setDuration(durationMillis);
        view.startAnimation(transAni);
    }


    /**
     * @param view
     * @param alpha
     * @param durationMillis
     */
    public static void setAlphaAni(View view, int alpha, long durationMillis, AnimatorListenerAdapter listenerAdapter) {
        view.animate().alpha(alpha).setDuration(durationMillis).setListener(listenerAdapter).start();
    }


    /**
     * 为View添加透明度变换效果，透明度从fromAlpha变化到toAlpha，变化持续时间durationMillis，重复模式repeatMode
     */
    public static void setAlphaAni(View view, float fromAlpha, float toAlpha, long durationMillis) {
        AlphaAnimation alphaAni = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAni.setDuration(durationMillis);   // 设置动画效果时间
        view.startAnimation(alphaAni);
    }


    /**
     * 展开view动画
     *
     * @param expandView 展开部分的View
     */
    public static void openView(final View expandView) {
        expandView.setVisibility(View.VISIBLE);
        //改变高度动画
        final Animator animator = ViewAnimator.ofItemViewHeight(expandView, true);
        final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(expandView, View.ALPHA, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, alphaAnimator);//同时执行动画
        animatorSet.start();

    }


    /**
     * 关闭view动画
     *
     * @param expandView 展开部分的View
     */
    public static void closeView(final View expandView) {

        expandView.setVisibility(View.GONE);
        final Animator animator = ViewAnimator.ofItemViewHeight(expandView, false);
        expandView.setVisibility(View.VISIBLE);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                expandView.setVisibility(View.GONE);
                expandView.setAlpha(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                expandView.setAlpha(0);
            }
        });
        animator.start();
    }



}
