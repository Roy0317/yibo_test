package com.yibo.yiboapp.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.anuo.immodule.utils.ScreenUtil;

import java.util.ArrayList;
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
 * Date  :2021-02-25
 * Desc  :com.yibo.yiboapp.views
 */
public class LuckPanView extends View {
    public int minCircleNum = 9; // 圈数
    public int maxCircleNum = 15;

    public long minOneCircleMillis = 400; // 平均一圈用时
    public long maxOneCircleMillis = 600;

    private List<String> mPrizeVoList;
    private RectF mRectF;

    private Paint dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Context mContext;

    public LuckPanView(Context context) {
        this(context, null);

    }

    public LuckPanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LuckPanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        dPaint.setColor(Color.rgb(82, 182, 197));
        sPaint.setColor(Color.rgb(186, 226, 232));
        textPaint.setColor(Color.YELLOW);
        textPaint.setLetterSpacing(0.2f);
        mRectF = new RectF();
        mPrizeVoList = new ArrayList<>();
//        PrizeVo prizeVo = new PrizeVo();
//        prizeVo.id = "";
//        prizeVo.rate = "";
//        prizeVo.title = "";
        for (int i = 0; i < 6; i++) {
            mPrizeVoList.add("暂无");
        }
    }

    /**
     * 设置奖项实体集合
     *
     * @param prizeVoList 奖项实体集合
     */
    public void setPrizeVoList(List<String> prizeVoList) {
        mPrizeVoList = prizeVoList;
        invalidate();
    }

    /**
     * 设置转盘交替的深色
     *
     * @param darkColor 深色 默认：Color.rgb(82, 182, 197)
     */
    public void setDarkColor(int darkColor) {
        dPaint.setColor(darkColor);
    }

    /**
     * 设置转盘交替的浅色
     *
     * @param shallowColor 浅色 默认：Color.rgb(186, 226, 232)
     */
    public void setShallowColor(int shallowColor) {
        sPaint.setColor(shallowColor);
    }

    /**
     * 设置转动圈数的范围
     *
     * @param minCircleNum 最小转动圈数
     * @param maxCircleNum 最大转动圈数
     */
    public void setCircleNumRange(int minCircleNum, int maxCircleNum) {
        if (minCircleNum > maxCircleNum) {
            return;
        }
        this.minCircleNum = minCircleNum;
        this.maxCircleNum = maxCircleNum;
    }

    /**
     * 设置平均转动一圈用时
     *
     * @param minOneCircleMillis 最小转动圈数
     * @param maxOneCircleMillis 最大转动圈数
     */
    public void setOneCircleMillisRange(long minOneCircleMillis, long maxOneCircleMillis) {
        if (minOneCircleMillis > maxOneCircleMillis)
            this.minOneCircleMillis = minOneCircleMillis;
        this.maxOneCircleMillis = maxOneCircleMillis;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //wrap_content value
        int mHeight = ScreenUtil.dip2px(mContext, 300);
        int mWidth = ScreenUtil.dip2px(mContext, 300);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int MinValue = Math.min(width, height);

        int radius = MinValue / 2;
        mRectF.set(getPaddingLeft(), getPaddingTop(), MinValue, MinValue);
        float sweepAngle = (float) (360.0 / mPrizeVoList.size());
        float angle = -90 - sweepAngle / 2;

        for (int i = 0; i < mPrizeVoList.size(); i++) {
            if (i % 2 == 0) {
                canvas.drawArc(mRectF, angle, sweepAngle, true, dPaint);
            } else {
                canvas.drawArc(mRectF, angle, sweepAngle, true, sPaint);
            }
            angle += sweepAngle;
        }
        angle = -90 - sweepAngle / 2;

        for (String prizeVo : mPrizeVoList) {
            Path mPath = new Path();
            mPath.addArc(mRectF, angle, sweepAngle);

            List<String> lines = new ArrayList<>();
            if(prizeVo.length() <= 5){
                textPaint.setTextSize(ScreenUtil.dip2px(getContext(), 16));
                lines.add(prizeVo);
            }else if(prizeVo.length() <= 12){
                textPaint.setTextSize(ScreenUtil.dip2px(getContext(), 15));
                lines.add(prizeVo.substring(0, prizeVo.length()/2));
                lines.add(prizeVo.substring(prizeVo.length()/2));
            }else{
                textPaint.setTextSize(ScreenUtil.dip2px(getContext(), 13));
                lines.add(prizeVo.substring(0, prizeVo.length()/3));
                lines.add(prizeVo.substring(prizeVo.length()/3, prizeVo.length()*2/3));
                lines.add(prizeVo.substring(prizeVo.length()*2/3));
            }

            for(int i = 0; i < lines.size(); i++){
                String line = lines.get(i);
                float textWidth = textPaint.measureText(line);
                float hOffset = (float) (2 * radius * Math.PI / mPrizeVoList.size() / 2 - textWidth / 2);
                float vOffset = radius*(0.15f + i*0.1f);
                canvas.drawTextOnPath(line, mPath, hOffset, vOffset, textPaint);
            }

            angle += sweepAngle;
        }
    }

    private float startAngle; // 每次开始转的起始角度

    private OnLuckPanAnimatorEndListener mOnLuckPanAnimatorEndListener;

    public void setOnLuckPanAnimatorEndListener(OnLuckPanAnimatorEndListener listener) {
        this.mOnLuckPanAnimatorEndListener = listener;
    }

    public interface OnLuckPanAnimatorEndListener {
        void onLuckPanAnimatorEnd(String choicePrizeVo);
    }

    /**
     * 开始转动 抽奖
     *
     * @param id 要停到对应奖项的 PrizeVo 实体的 id
     */
    public void start(final int id) {
        int choiceIndex1 = 0;
        // 获取选择的id对应 在转盘中的位置 从0开始
//        for (int i = 0; i < mPrizeVoList.size(); i++) {
//            if (id == Integer.parseInt(mPrizeVoList.get(i).id)) {
//                choiceIndex1 = i;
//            }
//        }
        final int choiceIndex = id - 1;
        // minOneCircleMillis < 平均一圈用时 < maxOneCircleMillis
        long oneCircleTime = (long) (minOneCircleMillis + Math.random() * (maxOneCircleMillis - minOneCircleMillis + 1));
        // minCircleNum < 圈数 < maxCircleNum
        int ringNumber = (int) (minCircleNum + Math.random() * (maxCircleNum - minCircleNum + 1));

        float allAngle = 360 * ringNumber + (360f / mPrizeVoList.size()) * -choiceIndex;

        ObjectAnimator animator = ObjectAnimator.ofFloat(LuckPanView.this, "rotation", -startAngle, allAngle);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(oneCircleTime * ringNumber);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startAngle = (360f / mPrizeVoList.size()) * choiceIndex;
                if (mOnLuckPanAnimatorEndListener != null) {
                    mOnLuckPanAnimatorEndListener.onLuckPanAnimatorEnd(mPrizeVoList.get(choiceIndex));
                }

            }
        });
        animator.start();

    }
}
