package com.yibo.yiboapp.views.recycleanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;


public class ViewHolderAnimator {

    /**
     * 给view设置动画效果，并设置动画结束后回调
     *
     * @param v
     */
    public static void startAnimation(SpringSystem springSystem, final View v) {
        // 添加一个弹簧到系统
        Spring spring = springSystem.createSpring();
        //设置弹簧属性参数，如果不设置将使用默认值
        //两个参数分别是弹力系数和阻力系数
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 2));
        spring.setCurrentValue(-30);
        // 添加弹簧监听器
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                // value是一个符合弹力变化的一个数，我们根据value可以做出弹簧动画
                float value = (float) spring.getCurrentValue();
                //基于Y轴的弹簧阻尼动画
                v.setTranslationY(value);
            }
        });
        // 设置动画结束值
        spring.setEndValue(0);
    }


    public static class ViewHolderAnimatorListener extends AnimatorListenerAdapter {
        //holder对象
        private final RecyclerView.ViewHolder _holder;

        //设定在动画开始结束和取消状态下是否可以被回收
        public ViewHolderAnimatorListener(RecyclerView.ViewHolder holder) {
            _holder = holder;
        }

        //开始
        @Override
        public void onAnimationStart(Animator animation) {
            _holder.setIsRecyclable(false);
        }

        //结束
        @Override
        public void onAnimationEnd(Animator animation) {
            _holder.setIsRecyclable(true);
        }

        //取消
        @Override
        public void onAnimationCancel(Animator animation) {
            _holder.setIsRecyclable(true);
        }
    }


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


    //OpenHolder 中动画的具体操作方法
    public static Animator ofItemViewHeight(RecyclerView.ViewHolder holder) {
        View parent = (View) holder.itemView.getParent();
        if (parent == null)
            throw new IllegalStateException("Cannot animate the layout of a view that has no parent");
        //测量扩展动画的起始高度和结束高度
        int start = holder.itemView.getMeasuredHeight();
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int end = holder.itemView.getMeasuredHeight();

        //具体的展开动画
        final Animator animator = LayoutAnimator.ofHeight(holder, holder.itemView, start, end);
        //设定该item在动画开始结束和取消时能否被recycle
        animator.addListener(new ViewHolderAnimatorListener(holder));
        //设定结束时这个Item的宽高
        animator.addListener(new LayoutParamsAnimatorListener(holder.itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return animator;
    }

}
