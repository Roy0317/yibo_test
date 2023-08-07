package com.yibo.yiboapp.views.recycleanim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.yibo.yiboapp.Event.EventGams;

import org.greenrobot.eventbus.EventBus;

public class LayoutAnimator {

    //监听动画的变化，不断设定view的高度值
    public static class LayoutHeightUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private RecyclerView.ViewHolder holder;
        private final View _view;

        public LayoutHeightUpdateListener(RecyclerView.ViewHolder holder, View view) {
            _view = view;
            this.holder = holder;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            final ViewGroup.LayoutParams lp = _view.getLayoutParams();
            lp.height = (int) animation.getAnimatedValue();
            _view.setLayoutParams(lp);
            if (holder != null)
                EventBus.getDefault().post(new EventGams(holder.getPosition()));
        }

    }

    //真正实现具体展开的方法，使用ValueAnimator.ofInt生成一系列高度值，然后添加上面的监听
    public static Animator ofHeight(RecyclerView.ViewHolder holder, View view, int start, int end) {
        final ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new LayoutHeightUpdateListener(holder, view));
        return animator;
    }

}
