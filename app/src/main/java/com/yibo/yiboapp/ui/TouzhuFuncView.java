package com.yibo.yiboapp.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.interfaces.PlayItemSelectListener;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author johnson
 * 万千百十个位数view
 */
public class TouzhuFuncView extends LinearLayout {


    public static final String TAG = TouzhuFuncView.class.getSimpleName();
    Context mContext;
    int viewSize;
    public static final int DEFAULT_BORDER_WIDTH = 30;
    public String ballonNums = "";//球的号码或功能文字或位数文字
    int viewType = Constant.BALLON_VIEW;
    int viewColorNormal = R.color.light_color_red;
    int viewColorSelect = R.color.colorPrimary;
    boolean subViewClickable = false;
    Drawable unSelectDrawable;//未选中状态下的背景
    Drawable selectDrawable;//选中状态下的背景
    boolean justBackgroudWithoutText;//是否只显示图片，不显示文字
    boolean animEffect;//是否需要点击动画
    PlayItemSelectListener playItemSelectListener;//投注球组合view的选择回调事件
    OnClickListener ballonClickListener;//子球的点击事件
    int posInArray;//球在球组中的位置
    Animation clickAnimation;

    public TouzhuFuncView(Context context) {
        super(context);
        mContext = context;
    }

    public TouzhuFuncView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouzhuFuncView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TouzhuFuncView, defStyleAttr, 0);
        viewSize = a.getDimensionPixelSize(R.styleable.TouzhuFuncView_view_size, DEFAULT_BORDER_WIDTH);
        String str = a.getString(R.styleable.TouzhuFuncView_view_string);
        viewType = a.getInteger(R.styleable.TouzhuFuncView_view_category,Constant.BALLON_VIEW);
        viewColorNormal = a.getColor(R.styleable.TouzhuFuncView_view_textcolor_normal,
                mContext.getResources().getColor(R.color.light_color_red));
        viewColorSelect = a.getColor(R.styleable.TouzhuFuncView_view_textcolor_select,
                mContext.getResources().getColor(R.color.colorPrimary));
        subViewClickable = a.getBoolean(R.styleable.TouzhuFuncView_view_clickable, false);
        unSelectDrawable = a.getDrawable(R.styleable.TouzhuFuncView_view_unselect_drawable);
        selectDrawable = a.getDrawable(R.styleable.TouzhuFuncView_view_select_drawable);
        animEffect = a.getBoolean(R.styleable.TouzhuFuncView_view_click_anim_effect, false);

        if (unSelectDrawable == null) {
            unSelectDrawable = mContext.getResources().getDrawable(R.drawable.red_border_normal);
        }
        if (selectDrawable == null) {
            selectDrawable = mContext.getResources().getDrawable(R.drawable.red_border_press);
        }
        if (!Utils.isEmptyString(str)) {
            ballonNums = str;
        }
        //加载动画
        if (clickAnimation == null) {
            clickAnimation = AnimationUtils.loadAnimation(mContext, R.anim.button_up);
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateBallons();
    }

    public void updateBallons() {
        List<String> numbers = Utils.splitString(ballonNums, ",");
        List<BallListItemInfo> infos = new ArrayList<BallListItemInfo>();
        for (String num : numbers) {
            BallListItemInfo info = new BallListItemInfo();
            info.setNum(num);
            info.setSelected(false);
            infos.add(info);
        }
        fillBallons(infos,true);
    }

    public void updateBallons(List<BallListItemInfo> infos) {
        fillBallons(infos,false);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        ballonClickListener = l;
        for (int i=0;i<this.getChildCount();i++){
            BallonView ballonView = (BallonView) this.getChildAt(i);
            ballonView.setOnClickListener(ballonClickListener);
        }
    }

    private Drawable getDrwableFromTextWhenLotteryIsCaiche(String ballonNum){
        if (ballonNum.equals("01")){
            return mContext.getResources().getDrawable(R.drawable.sc_01_title);
        }else if (ballonNum.equals("02")){
            return mContext.getResources().getDrawable(R.drawable.sc_02_title);
        }else if (ballonNum.equals("03")){
            return mContext.getResources().getDrawable(R.drawable.sc_03_title);
        }else if (ballonNum.equals("04")){
            return mContext.getResources().getDrawable(R.drawable.sc_04_title);
        }else if (ballonNum.equals("05")){
            return mContext.getResources().getDrawable(R.drawable.sc_05_title);
        }else if (ballonNum.equals("06")){
            return mContext.getResources().getDrawable(R.drawable.sc_06_title);
        }else if (ballonNum.equals("07")){
            return mContext.getResources().getDrawable(R.drawable.sc_07_title);
        }else if (ballonNum.equals("08")){
            return mContext.getResources().getDrawable(R.drawable.sc_08_title);
        }else if (ballonNum.equals("09")){
            return mContext.getResources().getDrawable(R.drawable.sc_09_title);
        }else if (ballonNum.equals("10")){
            return mContext.getResources().getDrawable(R.drawable.sc_10_title);
        }
        return mContext.getResources().getDrawable(R.drawable.sc_01_title);
    }

    /**
     * 更新球组下的所有球的状态
     * @param numList
     * @param cleanViews 是否先移除掉所有球再重新添加
     */
    public void fillBallons(List<BallListItemInfo> numList,boolean cleanViews) {
        if (numList == null || numList.isEmpty()) {
            return;
        }
        if (!cleanViews) {
            int count = this.getChildCount();
            int nums = numList.size();
            for (int i=0;i<count&&i<nums&&count==nums;i++) {
                BallListItemInfo result = numList.get(i);
                BallonView ballonView = (BallonView) this.getChildAt(i);
                if (!result.isSelected()) {
                    ballonView.setBackground(unSelectDrawable);
                    ballonView.setTextColor(viewColorNormal);
                }else{
                    ballonView.setBackground(selectDrawable);
                    ballonView.setTextColor(viewColorSelect);
                }
                if (result.isClickOn() && animEffect) {
                    //对点击的球进行动画效果
                    ballonView.startAnimation(clickAnimation);
                    result.setClickOn(false);//一定要将点击状态去掉，否则列表中多行情
                    // 况下会出现点击某行的球，同时会有其他行的球同时有动画效果
                }
                if (!justBackgroudWithoutText){
                    ballonView.setText(result.getNum());
                    ballonView.setUnSelectDrawable(unSelectDrawable);
                    ballonView.setSelectDrawable(selectDrawable);
                    ballonView.setViewColorNormal(viewColorNormal);
                    ballonView.setViewColorSelect(viewColorSelect);
                }else{
                    ballonView.setBackground(getDrwableFromTextWhenLotteryIsCaiche(result.getNum()));
                }
//                ballonView.setTextSize(8);
                ballonView.setOnClickListener(ballonClickListener);
                ballonView.setClickable(subViewClickable);//在设置完监听器之后再设置是否点击才会生效
                ballonView.setGravity(Gravity.CENTER);
                ballonView.setBallonType(viewType);
                ballonView.setViewIndex(i);
                ballonView.setIcChecked(result.isSelected());
                ballonView.setNumber(result.getNum());

                ballonView.setAnimEffect(animEffect);
                ballonView.setViewSize(viewSize);
                ballonView.setPosInArray(posInArray);
                ballonView.setNumberString(ballonNums);
            }
            return;
        }
        removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewSize,viewSize);
        //球类状态下，每个子球之间有一定的间隔
        for (int i=0;i<numList.size();i++) {
            if (viewType == Constant.BALLON_VIEW) {
                //如果FuncView只包含一个球时(投注界面列表中是这个情况)，
                // 则设置球的上下左右间距，方便点击球时动画的展现。
                int margin = 0;
                //if (singleBallonInList) {
                    margin = (int) mContext.getResources().getDimension(R.dimen.ballon_margin_when_touzhu);
                //}
                params.rightMargin = margin;
                params.leftMargin = margin;
                params.topMargin = margin;
                params.bottomMargin = margin;
            }else if (viewType == Constant.LETTER_VIEW || viewType == Constant.WEISHU_VIEW) {
                params.rightMargin = 0;
                params.leftMargin = /*(int) mContext.getResources().getDimension(R.dimen.mistake_margin)*/0;
            }/*else if (viewType == Constant.WEISHU_VIEW) {
                params.rightMargin = 2;
                params.leftMargin = 2;
            }*/
            BallListItemInfo result = numList.get(i);
            BallonView ballonView = new BallonView(mContext);
            ballonView.setOnClickListener(ballonClickListener);
            ballonView.setClickable(subViewClickable);//在设置完监听器之后再设置是否点击才会生效
            ballonView.setGravity(Gravity.CENTER);
            ballonView.setBallonType(viewType);
            ballonView.setViewIndex(i);
            ballonView.setNumberString(ballonNums);
            ballonView.setNumber(result.getNum());
            ballonView.setIcChecked(result.isSelected());
            ballonView.setUnSelectDrawable(unSelectDrawable);
            ballonView.setSelectDrawable(selectDrawable);
            ballonView.setViewColorNormal(viewColorNormal);
            ballonView.setViewColorSelect(viewColorSelect);
            ballonView.setAnimEffect(animEffect);
            ballonView.setViewSize(viewSize);
            ballonView.setPosInArray(posInArray);
            if (!justBackgroudWithoutText){
                ballonView.setText(result.getNum());
            }
            if (viewType == Constant.LETTER_VIEW) {
                StateListDrawable letterBg = new StateListDrawable();
                letterBg.addState(new int[]{android.R.attr.state_focused}, selectDrawable);
                letterBg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, selectDrawable);
                letterBg.addState(new int[0], unSelectDrawable);
                ballonView.setBackground(letterBg);
            }else{
                if (!justBackgroudWithoutText){
                    if (!result.isSelected()) {
                        ballonView.setBackground(unSelectDrawable);
                        ballonView.setTextColor(viewColorNormal);
                    }else{
                        ballonView.setBackground(selectDrawable);
                        ballonView.setTextColor(viewColorSelect);
                    }
                }else{
                    ballonView.setBackground(getDrwableFromTextWhenLotteryIsCaiche(result.getNum()));
                }
            }
            addView(ballonView,params);
        }
    }


    public void setPosInArray(int posInArray) {
        this.posInArray = posInArray;
    }

    public void setPlayItemSelectListener(PlayItemSelectListener playItemSelectListener) {
        this.playItemSelectListener = playItemSelectListener;
    }

    public void setBallonNum(String ballonNum) {
        this.ballonNums = ballonNum;
    }
}
