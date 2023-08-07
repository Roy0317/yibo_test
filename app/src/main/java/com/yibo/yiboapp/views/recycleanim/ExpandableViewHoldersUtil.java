package com.yibo.yiboapp.views.recycleanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.rebound.SpringSystem;


public class ExpandableViewHoldersUtil {

    //自定义处理列表中右侧图标，这里是一个旋转动画
    public static void rotateExpandIcon(final ImageView mImage, final float from, float to) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mImage.setRotation((Float) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }


    /**
     * 展开view动画 （当需要弹力动画时，扩展动画结束后透明动画开始，否则扩展动画和透明动画同时执行）
     *
     * @param holder       holder对象
     * @param expandView   展开部分的View，由holder.getExpandView()方法获取
     * @param animate      animate参数为true，则有动画效果
     * @param springSystem 弹力动画效果对象
     */
    public static void openH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate, final SpringSystem springSystem) {
        if (animate) {
            expandView.setVisibility(View.VISIBLE);
            //改变高度动画
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            if (springSystem != null) {
                //扩展的动画，结束后透明度动画开始
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
//                        EventBus.getDefault().post(new EventGams(holder.getPosition()));
                        final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(expandView, View.ALPHA, 1);
                        alphaAnimator.addListener(new ViewHolderAnimator.ViewHolderAnimatorListener(holder));
                        alphaAnimator.start();
                        //展开部分的view，给每个childView添加弹力动画
                        if (expandView instanceof LinearLayout) {
                            int childCount = ((LinearLayout) expandView).getChildCount();
                            for (int i = 0; i < childCount; i++) {
                                ViewHolderAnimator.startAnimation(springSystem, ((LinearLayout) expandView).getChildAt(i));
                            }
                        }
                    }
                });
                animator.start();
            } else {
                final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(expandView, View.ALPHA, 1);
                alphaAnimator.addListener(new ViewHolderAnimator.ViewHolderAnimatorListener(holder));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator, alphaAnimator);//同时执行高度，和透明动画
                animatorSet.start();
            }
        } else {  //为false时直接显示view
            expandView.setVisibility(View.VISIBLE);
            expandView.setAlpha(1);
        }
    }


    /**
     * 关闭view动画
     *
     * @param holder     holder对象
     * @param expandView 展开部分的View，由holder.getExpandView()方法获取
     * @param animate    animate参数为true，则有动画效果
     */
    public static void closeH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {
        if (animate) {
            expandView.setVisibility(View.GONE);
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            expandView.setVisibility(View.VISIBLE);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
//                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                }
            });
            animator.start();
        } else {
            expandView.setVisibility(View.GONE);
            expandView.setAlpha(0);
        }
    }


    /**
     * 获取展开部分的view
     */
    public  interface Expandable {
        View getExpandView();

        ImageView getExpandImageView();
    }


    /**
     * 根据业务逻辑将只能展开一个
     *
     * @param <VH>
     */
    public static class KeepOneH<VH extends RecyclerView.ViewHolder & Expandable> {
        //表示所有item是关闭状态 。 opend为pos值得表示pos位置的item为展开的状态
        private int _opened = -1;
        private VH holder;
        //size 为0表示所有item是关闭状态，
//        private ArrayList<String> openedList = new ArrayList<>();
        private SpringSystem springSystem;
        private boolean translateAn;

        public KeepOneH() {
            springSystem = SpringSystem.create();
        }

        /**
         * 此方法是Adapter的onBindViewHolder（）方法中调用
         *
         * @param holder
         * @param pos
         */
        public void bind(VH holder, int pos) {
            bind(holder, pos, false);
        }


        /**
         * 此方法是Adapter的onBindViewHolder（）方法中调用
         *
         * @param holder
         * @param pos
         * @param defaultPos 默认展开项
         */
        public void bind(VH holder, int pos, int defaultPos) {
            _opened = defaultPos;
            bind(holder, pos, false);
        }

        /**
         * 此方法是Adapter的onBindViewHolder（）方法中调用
         *
         * @param holder
         * @param pos
         */
        public void bind(VH holder, int pos, boolean translateAn) {
            this.translateAn = translateAn;
//            if (openedList.contains(String.valueOf(pos))) { //包含pos表示已经展开
//                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), false);
//            } else {
//                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), false);
//            }
            if (pos == _opened)   //展开ExpandView
                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), false, translateAn ? springSystem : null);
            else //关闭ExpandView
                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), false);
        }


        /**
         * 响应ViewHolder的点击事件
         *
         * @param holder
         * @param imageView 自己添加参数，为了处理图片旋转的动画，处理内部业务
         */
        @SuppressWarnings("unchecked")
        public void toggle(VH holder, ImageView imageView) {
//            int position = holder.getPosition();
//            if (openedList.contains(String.valueOf(position))) {
//                openedList.remove(String.valueOf(position));
//                if (imageView != null)
//                    ExpandableViewHoldersUtil.rotateExpandIcon(imageView, 90, 0);
//                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), true);
//            } else {
//                openedList.add(String.valueOf(position));
//                if (imageView != null)
//                    ExpandableViewHoldersUtil.rotateExpandIcon(imageView, 0, 90);
//                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), true);
//            }
            //点击item是已经展开的，则关闭item，并将opened 重置为-1
            if (_opened == holder.getPosition()) {
                _opened = -1;
                this.holder = null;
                if (imageView != null)
                    ExpandableViewHoldersUtil.rotateExpandIcon(imageView, 90, 0);
                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), true);
            } else { //点击的是本来关闭的item，则把opend值换成当前pos，把之前打开的item关闭
                int previous = _opened;
                this.holder = holder;
                _opened = holder.getPosition();
                if (imageView != null)
                    ExpandableViewHoldersUtil.rotateExpandIcon(imageView, 0, 90);
                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), true, translateAn ? springSystem : null);
                //动画关闭之前打开的item
                final VH oldHolder = (VH) ((RecyclerView) holder.itemView.getParent()).findViewHolderForPosition(previous);
                if (oldHolder != null) {
                    ExpandableViewHoldersUtil.closeH(oldHolder, oldHolder.getExpandView(), true);
                    ImageView expandImageView = oldHolder.getExpandImageView();
                    if (expandImageView != null)
                        ExpandableViewHoldersUtil.rotateExpandIcon(expandImageView, 90, 0);
                }
            }
        }


        public void resetExpend() {
            if (holder != null && _opened != -1) {
                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), false);
                holder = null;
                _opened = -1;
            }

        }

        public int get_opened() {
            return _opened;
        }

    }

}
