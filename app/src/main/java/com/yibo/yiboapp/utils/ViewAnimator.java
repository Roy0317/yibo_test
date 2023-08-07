package com.yibo.yiboapp.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;



public class ViewAnimator {

    //设定在动画结束后view的宽度和高度分别为match_parent,warp_content
    public static class LayoutParamsAnimatorListener extends AnimatorListenerAdapter {
        private final View _view;
        private final int _paramsWidth;
        private final int _paramsHeight;

        public LayoutParamsAnimatorListener(View view, int paramsWidth, int paramsHeight) {
            _view = view;
            _paramsWidth = paramsWidth;
            _paramsHeight = paramsHeight;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            final ViewGroup.LayoutParams params = _view.getLayoutParams();
            params.width = _paramsWidth;
            params.height = _paramsHeight;
            _view.setLayoutParams(params);
        }
    }


    // 动画的具体操作方法
    public static Animator ofItemViewHeight(View animaView, boolean flag) {
        View parent = (View) animaView.getParent();
        if (parent == null)
            throw new IllegalStateException("Cannot animate the layout of a view that has no parent");
        //测量扩展动画的起始高度和结束高度
        int start = animaView.getMeasuredHeight();
        int end;
        if (flag) {
            animaView.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            end = animaView.getMeasuredHeight();
        } else {
            end = 0;
        }

        //具体的展开动画
        final Animator animator = LayoutAnimator.ofHeight(null,animaView, start, end);
        //设定结束时这个Item的宽高
        animator.addListener(new LayoutParamsAnimatorListener(animaView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return animator;
    }

}
